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

import static hu.unideb.fksz.TrafficCounterLogger.logger;

/**
 * Class for processing video files.
 * The main purpose of this class is to open video files, and read two
 * frames in a loop, then process the frames, and search for differences,
 * then for contours.
 * @author krajsz
 *
 */



public class VideoProcessor 
{
	static 
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
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
	
	private String fileName;
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
		
	/**
	 * Constructor for creating a {@code VideoProcessor} object,
	 * Initializes the {@code controlPoints}.
	 */
	public VideoProcessor()
	{
		initControlPoints();
	}
	
	public boolean isOpened()
	{
		return video.isOpened();
	}
	/**
	 * Returns an {@code int} , the {@code detectedCarsCount}.
	 * 
	 * @return the {@code detectedCarsCount}
	 */
	public int getDetectedCarsCount()
	{
		return detectedCarsCount;
	}
	
	/**
	 * Returns a {@code double}, the frame count of the current video.
	 * 
	 * @return the frame count of the current video.
	 */
	public double getFrameCount()
	{
		return (getVideoCap().isOpened() ? getVideoCap().get(7) : 0);
	}
	
	/**
	 * Returns an {@code int}, the position of the current frame.
	 * 
	 * @return the position of the current frame.
	 */
	public int getFramePos()
	{
		return (getVideoCap().isOpened() ? (int) getVideoCap().get(1) : 0);
	}
	
	/**
	 * Returns a {@code Mat}, the {@code frame}.
	 * 
	 * @return the {@code frame}
	 */
	public Mat getFrame()
	{
		return this.frame;
	}
	
	/**
	 * Returns a {@code double}, the frame per second value of the {@code video}.
	 * 
	 * @return the frame per second value of the {@code video}
	 */
	public double getFPS()
	{
		return (getVideoCap().isOpened() ?  getVideoCap().get(5) : 1);
	}
	
	/**
	 * Returns a {@code VideoCapture}, an OpenCV class for handling videos.
	 * 
	 * @return {@code video}, an OpenCV class for handling videos
	 */
	public VideoCapture getVideoCap()
	{
		return this.video;
	}
	
	/**
	 * Sets the frame position of the {@code VideoCapture} if the value
	 * is valid.
	 * 
	 * @param pos	the position to be set.
	 */
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
			logger.error("Invalid positioning number!");
		}
	}
	
	/**
	 * Writes the specified {@code text} on the {@code frame}.
	 * @param text	the text to be written on the {@code frame}.
	 */
	public void writeOnFrame(String text)
	{
		Imgproc.putText(getFrame(), text, textPosition , Core.FONT_HERSHEY_SIMPLEX , 0.7, fontColor, 2);
	}
	
	/**
	 * Calculates the length of the loaded video, and returns it as a {@code String}.
	 * 
	 * @return the {@code String} representing the length of the video.
	 */
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
	
	
	/**
	 * Processes {@code firstFrame} and {@code secondFrame}.
	 * @param firstFrame 	the first frame of a cycle.
	 */
	private void processFrame(Mat firstFrame)
	{
		double contourArea = 0;
		int position = 0;
		try
		{
			/**
			 * Resizes the {@code firstFrame} to {@code frameSize}. 
			 *
			 */
			Imgproc.resize(firstFrame, firstFrame, frameSize);
		
			/**
			 * Convert the frame in grayscale color space.
			 */
			Imgproc.cvtColor(firstFrame, firstGrayImage, Imgproc.COLOR_BGR2GRAY);
			
			/**
			 * {@code video} reads the second frame.
			 */
			video.read(secondFrame);
		
			Imgproc.resize(secondFrame, secondFrame, frameSize);
	
			Imgproc.cvtColor(secondFrame, secondGrayImage, Imgproc.COLOR_BGR2GRAY);
	
			/**
			 * Getting the absolute per-pixel difference of the two frames into {@code differenceOfImages}.
			 */
			Core.absdiff(firstGrayImage, secondGrayImage, differenceOfImages);
			Imgproc.threshold(differenceOfImages, thresholdImage, 25, 255, Imgproc.THRESH_BINARY);
			Imgproc.blur(thresholdImage, thresholdImage, new Size(12, 12));
			Imgproc.threshold(thresholdImage, thresholdImage, 20, 255, Imgproc.THRESH_BINARY);
			contours.clear();
	
			/**
			 * The horizontal line.
			 */
			Imgproc.line(firstFrame, controlPoints.get(6), controlPoints.get(7), new Scalar(255,0,0), Imgproc.LINE_4);
			Imgproc.findContours(thresholdImage, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
			
			hullPoints.clear();
			
			for (int i = 0; i < contours.size(); i++)
			{
				MatOfInt tmp = new MatOfInt();
				Imgproc.convexHull(contours.get(i), tmp, false);
				hullPoints.add(tmp);
			}
			
			/**
			 * Searches for the contour with the greatest area.
			 */
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
			logger.error(e.getMessage());
		}

		/**
		 * Checking whether the control point on the left is 
		 * inside of {@code boundingRectangle}, which is a {@code Rect},
		 * bounding the greatest contour.
		 */
		if (controlPoints.get(6).inside(boundingRectangle))
		{
			Imgproc.line(frame, controlPoints.get(0), controlPoints.get(1), new Scalar(0, 0, 255), 2);
			wasAtLeftPoint = true;
		}
		else if (!controlPoints.get(6).inside(boundingRectangle))
		{
			Imgproc.line(frame, controlPoints.get(0), controlPoints.get(1), new Scalar(0, 255, 0), 2);
		}
		/**
		 * Checking whether the control point on the middle is 
		 * inside of {@code boundingRectangle}, which is a {@code Rect},
		 * bounding the greatest contour.
		 */
		if (controlPoints.get(8).inside(boundingRectangle))
		{
			Imgproc.line(frame, controlPoints.get(2), controlPoints.get(3), new Scalar(0, 0, 255), 2);
			wasAtCenterPoint = true;
		}
		else if (!controlPoints.get(8).inside(boundingRectangle))
		{
			Imgproc.line(frame, controlPoints.get(2), controlPoints.get(3), new Scalar(0, 255, 0), 2);
		}
		/**
		 * Checking whether the control point on the right is 
		 * inside of {@code boundingRectangle}, which is a {@code Rect},
		 * bounding the greatest contour.
		 */
		if (controlPoints.get(7).inside(boundingRectangle))
		{
			Imgproc.line(frame, controlPoints.get(4), controlPoints.get(5), new Scalar(0, 0, 255), 2);
			wasAtRightPoint = true;
		}
		else if (!controlPoints.get(7).inside(boundingRectangle))
		{
			Imgproc.line(frame, controlPoints.get(4), controlPoints.get(5), new Scalar(0, 255, 0), 2);
		}
		
		/**
		 * If the three control points have were inside the {@code boundingRectangle},
		 * it means that a "car" has passed.
		 */
		if (wasAtCenterPoint && wasAtLeftPoint && wasAtRightPoint)
		{
			detectedCarsCount++;
			
			wasAtCenterPoint = false;
			wasAtLeftPoint = false;
			wasAtRightPoint = false;
			logger.info("Detected " + detectedCarsCount + " car(s)");
		}
		/**
		 * If the contour is big enough, draw it.
		 */
		if (contourArea > 3000)
		{
			Imgproc.drawContours(frame, contours, position, new Scalar(255,255,255));
		}
	}
	
	/**
	 * Does the main loop, if we reach the penultimate frame, 
	 * it means we have reached the end of the end of the video.
	 */
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

					logger.trace("Restarting..");
					setFramePos(1);
				}
			}
			else
			{
				logger.warn("Empty image!");
				
			}
		} while (frameCounter > (getFrameCount()/2) -2);
	}
	
	/**
	 * Returns an {@code Image}, converted from a {@code Mat}.
	 * 
	 * @param frameToConvert	The frame to be converted to a {@code Image}
	 * @return	The {@code Image}, converted from a {@code Mat}
	 */
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
			logger.error(e.getMessage());
		}
		fxImage = new Image( new ByteArrayInputStream(buffer.toArray()));
		return fxImage;
	}
	
	/**
	 * Returns an {@code Image}, converted from a {@code Mat}, {@code frame}.
	 * 
	 * @return an {@code Image}, converted from a {@code Mat}, {@code frame}.
	 */
	public Image convertCvMatToImage()
	{
		return convertCvMatToImage(getFrame());
	}
	
	/**
	 * Gets an {@code Image}, converted from the specified index
	 * of the {@code video}.
	 * 
	 * @param pos	the position from which the frame is retrieved.
	 * @return	an {@code Image}, converted from the specified index 
	 * of the {@code video}.
	 */
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
			logger.error("VideoCapture not opened!");
			return null;
		}
	}
	
	/**
	 * Resets the value of the control points to {@code false}.
	 */
	private void resetCheckPoints()
	{
		wasAtCenterPoint = false;
		wasAtLeftPoint = false;
		wasAtRightPoint = false;
	}
	
	/**
	 * Initializes the {@code controlPoints}, 
	 * {@link VideoProcessor#VideoProcessor() in the constructor}.
	 */
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
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Returns an {@code int}, which represents the failure, or the success
	 * of the opening of the video specified by the {@code filename}.
	 * 
	 * @param filename	The absolute path of the video file to be opened.
	 * @return an {@code int}, which represents the failure, or the success
	 * of the opening of the video specified by the {@code filename}
	 */
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
				
				fileName = filename;
				finished = false;
				frameCounter = 0;
				logger.trace("VideoCapture opened successfully!");
				return 0;
			}
			logger.error("VideoCapture not opened!");
			return 1;
		}
		else
		{
			logger.warn("File name is null!");
			return 1;
		}
	}
	/**
	 * Sets detectedCarsCount.
	 * 
	 * @param detectedCarsCount to be set.
	 */
	public void setDetectedCarsCount(int detectedCarsCount) 
	{
		this.detectedCarsCount = detectedCarsCount;
	}

	/**
	 * Returns an {@code int}, {@code minutes}, the length of the loaded video in minutes.
	 * 
	 * @return the length of the loaded video in minutes.
	 */
	public int getMinutes() 
	{
		return minutes;
	}

	/**
	 * Returns a {@code boolean}, whether the video has finished or not.
	 * 
	 * @return whether the video has finished or not.
	 */
	public boolean isFinished() 
	{
		return finished;
	}
	
	/**
	 * Sets {@code finished}.
	 * 
	 * @param finished to be set.
	 */
	public void setFinished(boolean finished)
	{
		this.finished = finished;
	}

	/**
	 * Returns an {@code int}, the height of the {@code controlPoints}.
	 * 
	 * @return the height of the {@code controlPoints}.
	 */
	public int getControlPointsHeight() 
	{
		return controlPointsHeight;
	}

	/**
	 * Returns an {@code int}, the cars per minute value of the video.
	 * 
	 * @return the cars per minute value of the video.
	 */
	public int getCarsPerMinute()
	{
		return carsPerMinute;
	}

	/**
	 * Sets {@code carsPerMinute}.
	 * 
	 * @param carsPerMinute to be set.
	 */
	public void setCarsPerMinute(int carsPerMinute) 
	{
		this.carsPerMinute = carsPerMinute;
	}

	/**
	 * Returns {@code previousControlPointsHeight}, the previous height of the {@code controlPoints}.
	 * 
	 * @return the previous height of the {@code controlPoints}.
	 */
	public int getPreviousControlPointsHeight() 
	{
		return previousControlPointsHeight;
	}

	/**
	 * Sets {@code previousControlPointsHeight}.
	 * 
	 * @param previousControlPointsHeight to be set.
	 */
	public void setPreviousControlPointsHeight(int previousControlPointsHeight) 
	{
		this.previousControlPointsHeight = previousControlPointsHeight;
	}

	/**
	 * Returns a {@code Rect}, the designated image area represented by a rectangle.
	 * 
	 * @return the designated image area represented by a rectangle.
	 */
	public Rect getImageArea() 
	{
		return imageArea;
	}
	
	/**
	 * Returns the height of a control point.
	 * 
	 * @return the height of a control point.
	 */
	public double getHeightOfAControlPoint()
	{
		return controlPoints.get(6).y;
	}
	
	/**
	 * Sets height of the three control points.
	 * 
	 * @param height to be set.
	 */
	public void setHeightOfTheControlPoints(double height)
	{
		controlPoints.get(6).y = height;
		controlPoints.get(7).y = height;
		controlPoints.get(8).y = height;
	}
	/**
	 * Returns the file name of the opened video.
	 * 
	 * @return {@code filename}, the file name of the opened video.
	 */
	public String getFileName()
	{
		return fileName;
	}
}

