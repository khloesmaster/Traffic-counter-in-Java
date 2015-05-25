package hu.unideb.fksz;

/*
 * #%L
 * Traffic-counter
 * %%
 * Copyright (C) 2015 FKSZSoft
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */




import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class VideoProcessor 
{
	private VideoCapture video = new VideoCapture();

	private Mat frame = new Mat();
	private Image fxImage = null;
	private Mat firstGrayImage = new Mat();
	private Mat secondGrayImage = new Mat();
	private Mat secondFrame = new Mat();
	private Mat differenceOfImages = new Mat();
	private Mat thresholdImage = new Mat();
	private Mat hierarchy = new Mat();
	private Rect boundingRectangle = new Rect();
	private List<Point> controlPoints = new ArrayList<Point>();
	private int controlPointsHeight = 300;
	private int previousControlPointsHeight;
	private boolean wasAtCenterPoint = false;
	private boolean wasAtLeftPoint = false;
	private boolean wasAtRightPoint = false;
	
	private boolean finished = false;
	
	private int carsPerMinute;
	private int minutes;
	private int seconds; 
	private int hours;
	
	private final Point textPosition = new Point(10, 15);
	private final Scalar fontColor =   new Scalar(0, 50, 255);
	
	private MatOfByte buffer = new MatOfByte();
	private final MatOfInt params = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 20);
	
	
	private final Size frameSize = new Size(640, 480);
	private List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	private List<MatOfInt> hullPoints = new ArrayList<MatOfInt>();
	
	private int frameCounter = 0;
	private int detectedCarsCount = 0;
	
	private final Rect imageArea = new Rect(15, 15, 640, 480); 
		
	
	public VideoProcessor()
	{
		initControlPoints();
	}
	
	public int getDetectedCarsCount()
	{
		return detectedCarsCount;
	}
	
	public double getFrameCount()
	{
		return (getVideoCap().isOpened() ? getVideoCap().get(7) : 0);
	}
	public int getFramePos()
	{
		return (getVideoCap().isOpened() ? (int) getVideoCap().get(1) : 0);
	}
	
	public Mat getFrame()
	{
		return this.frame;
	}
	
	public double getFPS()
	{
		return (getVideoCap().isOpened() ?  getVideoCap().get(5) : 1);
	}
	
	public VideoCapture getVideoCap()
	{
		return this.video;
	}
	public void setFramePos(double pos)
	{
		if (pos >= 0 && pos <= getFrameCount())
		{
			try
			{
				getVideoCap().set(1, (int)pos);
			}
			catch(Exception e)
			{
				TrafficCounterLogger.errorMessage(e.getMessage());
			}
		}
		else
		{
			TrafficCounterLogger.errorMessage("Invalid positioning number!");
		}
	}
	
	public void writeOnFrame(String text)
	{
		Imgproc.putText(getFrame(), text, textPosition , Core.FONT_HERSHEY_SIMPLEX , 0.7, fontColor, 2);
	}
	
	public String getLengthFormatted()
	{
		seconds = (int)(getFrameCount() / getFPS());
	    minutes = (int)(seconds / 60);
	    hours = (int)(minutes / 60);

		minutes%= 60;
		seconds%= 60;
			
		return  (hours > 9 ? hours : "0" + hours)      + ":" +  
				(minutes > 9 ? minutes : "0"+ minutes) + ":" + 
				(seconds > 9 ? seconds : "0"+ seconds);
	}
	
	private void processFrame(Mat firstFrame)
	{
		double contourArea = 0;
		int position = 0;
		try
		{
			Imgproc.resize(firstFrame, firstFrame, frameSize);
		
			Imgproc.cvtColor(firstFrame, firstGrayImage, Imgproc.COLOR_BGR2GRAY);
			
			video.read(secondFrame);
		
			Imgproc.resize(secondFrame, secondFrame, frameSize);
	
			Imgproc.cvtColor(secondFrame, secondGrayImage, Imgproc.COLOR_BGR2GRAY);
	
			Core.absdiff(firstGrayImage, secondGrayImage, differenceOfImages);
			Imgproc.threshold(differenceOfImages, thresholdImage, 25, 255, Imgproc.THRESH_BINARY);
			Imgproc.blur(thresholdImage, thresholdImage, new Size(12, 12));
			Imgproc.threshold(thresholdImage, thresholdImage, 25, 255, Imgproc.THRESH_BINARY);
			contours.clear();
	
			Imgproc.line(firstFrame, controlPoints.get(6), controlPoints.get(7), new Scalar(255,0,0), Imgproc.LINE_4);
			Imgproc.findContours(thresholdImage, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
			
			hullPoints.clear();
			
			for (int i = 0; i < contours.size(); i++)
			{
				MatOfInt tmp = new MatOfInt();
				Imgproc.convexHull(contours.get(i), tmp, false);
				hullPoints.add(tmp);
			}
			
	
			if (contours.size() > 0)
			{
				for ( int i = 0; i < contours.size(); i++)
				{
					if (Imgproc.contourArea(contours.get(i)) > contourArea)
					{
						contourArea = Imgproc.contourArea(contours.get(i));
						position = i;
						boundingRectangle = Imgproc.boundingRect(contours.get(i));
					}
							
				}
			}
		
		}
		catch(Exception e)
		{
			TrafficCounterLogger.errorMessage(e.getMessage());
		}

		if (controlPoints.get(6).inside(boundingRectangle))
		{
			Imgproc.line(frame, controlPoints.get(0), controlPoints.get(1), new Scalar(0, 0, 255), 2);
			wasAtLeftPoint = true;
		}
		else if (!controlPoints.get(6).inside(boundingRectangle))
		{
			Imgproc.line(frame, controlPoints.get(0), controlPoints.get(1), new Scalar(0, 255, 0), 2);
		}
		
		if (controlPoints.get(8).inside(boundingRectangle))
		{
			Imgproc.line(frame, controlPoints.get(2), controlPoints.get(3), new Scalar(0, 0, 255), 2);
			wasAtCenterPoint = true;
		}
		else if (!controlPoints.get(8).inside(boundingRectangle))
		{
			Imgproc.line(frame, controlPoints.get(2), controlPoints.get(3), new Scalar(0, 255, 0), 2);
		}
		
		if (controlPoints.get(7).inside(boundingRectangle))
		{
			Imgproc.line(frame, controlPoints.get(4), controlPoints.get(5), new Scalar(0, 0, 255), 2);
			wasAtRightPoint = true;
		}
		else if (!controlPoints.get(7).inside(boundingRectangle))
		{
			Imgproc.line(frame, controlPoints.get(4), controlPoints.get(5), new Scalar(0, 255, 0), 2);
		}
		
		if (wasAtCenterPoint && wasAtLeftPoint && wasAtRightPoint)
		{
			detectedCarsCount++;
			
			wasAtCenterPoint = false;
			wasAtLeftPoint = false;
			wasAtRightPoint = false;
			TrafficCounterLogger.infoMessage("Detected " + detectedCarsCount + " car(s)");
		}
		
		if (contourArea > 3000)
		{
			Imgproc.drawContours(frame, contours, position, new Scalar(255,255,255));
		}
	}
	
	public void processVideo()
	{
		do 
		{
			video.read(this.frame);
			if (!this.frame.empty())
			{
				if (frameCounter < (getFrameCount()/2) -1 )
				{
					
					frameCounter++;
					if (getMinutes() > 0)
					{
						carsPerMinute = getDetectedCarsCount() / getMinutes();
					}
					processFrame(getFrame());
				}
				else
				{
					frameCounter = 0;
					finished = true;
					//detectedCarsCount = 0;

					TrafficCounterLogger.traceMessage("Restarting..");
					setFramePos(1);
				}
			}
			else
			{
				TrafficCounterLogger.warnMessage("Empty image!");
				
			}
		} while (frameCounter > (getFrameCount()/2) -2);
	}
	
	public Image convertCvMatToImage(Mat frameToConvert)
	{
		if (!buffer.empty())
		{
			buffer.release();
		}
		try
		{
			Imgproc.resize(frameToConvert, frameToConvert, frameSize);
			Imgcodecs.imencode(".jpg", frameToConvert, buffer, params);
		}
		catch(Exception e)
		{
			TrafficCounterLogger.errorMessage(e.getMessage());
		}
		fxImage = new Image( new ByteArrayInputStream(buffer.toArray()));
		return fxImage;
	}
	
	public Image convertCvMatToImage()
	{
		return convertCvMatToImage(getFrame());
	}
	
	public Image getImageAtPos(int pos)
	{
		if (video.isOpened())
		{
			if (pos < getFrameCount() && pos > 0)
			{
				setFramePos(pos);
				Mat tmp = new Mat();
				video.retrieve(tmp);
				setFramePos(0);
				try
				{
					Imgproc.resize(tmp, tmp, frameSize);
				}
				catch(Exception e)
				{
					TrafficCounterLogger.errorMessage(e.getMessage());
				}
				return convertCvMatToImage(tmp);
			}
			else
			{
				return getImageAtPos(1);
			}
		}
		else
		{
			TrafficCounterLogger.errorMessage("VideoCapture not opened!");
			return null;
		}
	}
	private void resetCheckPoints()
	{
		wasAtCenterPoint = false;
		wasAtLeftPoint = false;
		wasAtRightPoint = false;
	}
	
	private void initControlPoints()
	{
		try
		{
			controlPoints.add(new Point(80,100));
			controlPoints.add(new Point(80,frameSize.height - 100));
		
			controlPoints.add(new Point(frameSize.width / 2,100));
			controlPoints.add(new Point(frameSize.width / 2,frameSize.height - 100));

			controlPoints.add(new Point(frameSize.width - 80,100));
			controlPoints.add(new Point(frameSize.width - 80,frameSize.height - 100));
		
		
			controlPoints.add(new Point(80, controlPointsHeight));
			controlPoints.add(new Point(frameSize.width - 80,controlPointsHeight));
		
			controlPoints.add(new Point(frameSize.width / 2 ,controlPointsHeight));
			
			TrafficCounterLogger.traceMessage("Control points initialised successfully!");

		}
		catch (Exception e)
		{
			TrafficCounterLogger.errorMessage(e.getMessage());
		}
	}
	
	public int initVideo(String filename)
	{
		if (filename != null)
		{
			try
			{
				video.open(filename);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			if (video.isOpened())
			{
				resetCheckPoints();
				finished = false;
				frameCounter = 0;
				TrafficCounterLogger.traceMessage("VideoCapture opened successfully!");
				return 0;
			}
			TrafficCounterLogger.errorMessage("VideoCapture not opened!");
			return 1;
		}
		else
		{
			TrafficCounterLogger.warnMessage("File name is null!");
			return 1;
		}
	}
	public void setDetectedCarsCount(int detectedCarsCount) 
	{
		this.detectedCarsCount = detectedCarsCount;
	}

	public int getMinutes() 
	{
		return minutes;
	}

	public boolean isFinished() 
	{
		return finished;
	}
	
	public void setFinished(boolean finished)
	{
		this.finished = finished;
	}

	public int getControlPointsHeight() 
	{
		return controlPointsHeight;
	}

	public int getCarsPerMinute()
	{
		return carsPerMinute;
	}

	public void setCarsPerMinute(int carsPerMinute) 
	{
		this.carsPerMinute = carsPerMinute;
	}

	public int getPreviousControlPointsHeight() 
	{
		return previousControlPointsHeight;
	}

	public void setPreviousControlPointsHeight(int previousControlPointsHeight) 
	{
		this.previousControlPointsHeight = previousControlPointsHeight;
	}

	public Rect getImageArea() 
	{
		return imageArea;
	}
	
	public double getHeightOfAControlPoint()
	{
		return controlPoints.get(6).y;
	}
	
	public void setHeightOfTheControlPoints(double height)
	{
		controlPoints.get(6).y = height;
		controlPoints.get(7).y = height;
		controlPoints.get(8).y = height;
	}
}

