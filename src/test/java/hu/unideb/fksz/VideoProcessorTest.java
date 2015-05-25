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
						"/Debrecen_Egyetemsgt.mp4").getPath()));

	}

	@Test
	public void testGetFPS() {
		assertNotEquals(0, testProcessor.getFPS());
	}

	@Test
	public void getVideoCap() {
		assertNotNull("Not null!", testProcessor.getVideoCap());
	}

	@Test
	public void testGetControlPointsHeight() {
		assertNotEquals(0, testProcessor.getControlPointsHeight());
	}

	@Test
	public void testGetHeightOfAControlPoint() {
		assertNotEquals(0, testProcessor.getHeightOfAControlPoint());

	}


	
}
