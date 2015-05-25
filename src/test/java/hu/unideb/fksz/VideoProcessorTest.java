package hu.unideb.fksz;

import org.junit.Test;
import org.opencv.core.Core;


public class VideoProcessorTest {
	static
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	VideoProcessor testProcessor = new VideoProcessor();
	
	@Test
	public void testInitVideo()
	{
		//testProcessor.initVideo(VideoProcessorTest.class.getResource("/video/Debrecen_Egyetemsgt.mp4").getPath());
	}

}
