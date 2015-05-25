package hu.unideb.fksz;

import static org.junit.Assert.*;
import hu.unideb.fksz.view.TrafficCounterController;

import org.junit.Test;

public class TrafficCounterControllerTest {

	TrafficCounterController testController = new TrafficCounterController();

	@Test
	public void testIsNull()
	{
		assertNotNull(testController);
	}
	@Test
	public void testInit()
	{
		testController.init();
	}
	/*
	@Test
	public void testLoadVideo() {
		testController.init();
		assertEquals(0, testController.loadVideo(
				TrafficCounterControllerTest.class.getResource("/video/Debrecen_Egyetemsgt.mp4").getPath()));
	
	}*/
}
