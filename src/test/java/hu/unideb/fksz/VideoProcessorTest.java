package hu.unideb.fksz;

import org.junit.Test;
import org.opencv.core.Core;

import static org.junit.Assert.*;

public class VideoProcessorTest {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	VideoProcessor testProcessor = new VideoProcessor();

	@Test
	public void testInitVideo() {

		assertEquals(
				0,
				testProcessor.initVideo(VideoProcessorTest.class.getResource(
						"/video/Debrecen_Egyetemsgt.mp4").getPath()));

		System.out.println(VideoProcessorTest.class.getResource(
				"/video/Debrecen_Egyetemsgt.mp4").getPath());
	}

	@Test
	public void testGetFPS() {
		assertNotEquals(0, testProcessor.getFPS());
	}

	@Test
	public void testGetVideoCap() {
		assertNotNull("Not null!", testProcessor.getVideoCap());
	}

	@Test
	public void testIsOpened() {
		testProcessor.initVideo(VideoProcessorTest.class.getResource(
				"/video/Debrecen_Egyetemsgt.mp4").getPath());
		assertTrue(testProcessor.isOpened());
	}

	@Test
	public void testGetControlPointsHeight() {
		assertNotEquals(0, testProcessor.getControlPointsHeight());
	}

	@Test
	public void testGetHeightOfAControlPoint() {
		assertNotEquals(0, testProcessor.getHeightOfAControlPoint());

	}

	@Test
	public void testVideoProcessor() {
		testProcessor.initVideo(VideoProcessorTest.class.getResource(
				"/video/Debrecen_Egyetemsgt.mp4").getPath());

		while (!testProcessor.isFinished()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			testProcessor.processVideo();
		}
		
		assertTrue(!testProcessor.getFrame().empty());
		
		if (testProcessor.isFinished()) {
			assertNotEquals(0, testProcessor.getDetectedCarsCount());
		}

	}
	@Test
	public void testGetLengthFormatted() {
		assertNotEquals("", testProcessor.getLengthFormatted());
	}
	
	@Test
	public void testSetHeightOfTheControlPoints()
	{
		testProcessor.setHeightOfTheControlPoints(100);
	}
	@Test
	public void testWriteOnFrame()
	{
		testProcessor.writeOnFrame("Grumpy text");
	}
	
	@Test
	public void testSetFramePos()
	{
		testProcessor.setFramePos(-1);
	}
	
	@Test
	public void testConverCvMatToImage()
	{
		testProcessor.initVideo(VideoProcessorTest.class.getResource(
				"/video/Debrecen_Egyetemsgt.mp4").getPath());
		
		testProcessor.getVideoCap().read(testProcessor.getFrame());
		assertNotNull(testProcessor.convertCvMatToImage());
	}
	
	@Test
	public void testGetImageAtPos()
	{
		testProcessor.initVideo(VideoProcessorTest.class.getResource(
				"/video/Debrecen_Egyetemsgt.mp4").getPath());
		
		assertNotNull(testProcessor.getImageAtPos(2));
		assertNotNull(testProcessor.getImageAtPos(-2));
		
	}
	
	@Test
	public void testSetDetectedCarsCount()
	{
		testProcessor.setDetectedCarsCount(1);
	}
	
	@Test
	public void testSetFinished()
	{
		testProcessor.setFinished(false);
	}
	@Test
	public void testGetFrameCount()
	{
		testProcessor.initVideo(VideoProcessorTest.class.getResource(
				"/video/Debrecen_Egyetemsgt.mp4").getPath());
		testProcessor.getFrameCount();
	}
	@Test
	public void testSetCarsPerMinute()
	{
		testProcessor.setCarsPerMinute(2);
	}
	@Test
	public void testGetCarsPerMinute()
	{
		testProcessor.getCarsPerMinute();
	}
	@Test
	public void testGetPreviousControlPointsHeight()
	{
		testProcessor.getPreviousControlPointsHeight();
	}
	@Test
	public void testSetPreviousControlPointsHeight()
	{
		testProcessor.setPreviousControlPointsHeight(100);
	}
	@Test
	public void testGetImageArea()
	{
		assertNotNull(testProcessor.getImageArea());
	}
	
	@Test
	public void testGetFileName()
	{
		testProcessor.initVideo(VideoProcessorTest.class.getResource(
				"/video/Debrecen_Egyetemsgt.mp4").getPath());
		
		assertNotEquals("", testProcessor.getFileName());
	}
	
	@Test
	public void testGetFramePos()
	{
		testProcessor.initVideo(VideoProcessorTest.class.getResource(
				"/video/Debrecen_Egyetemsgt.mp4").getPath());
		testProcessor.setFramePos(2);
		assertNotEquals(0, testProcessor.getFramePos());
	}
	
	
}
