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

import static org.junit.Assert.*;
import hu.unideb.fksz.view.TrafficCounterController;

import org.junit.Test;
import org.opencv.core.Core;

public class TrafficCounterControllerTest {

	static
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	TrafficCounterController testController = new TrafficCounterController();

	@Test
	public void testIsNull()
	{
		assertNotNull(testController);
	}
	/*@Test
	public void testInit()
	{
		testController.init();
	}*/
	/*
	@Test
	public void testLoadVideo() {
		testController.init();
		assertEquals(0, testController.loadVideo(
				TrafficCounterControllerTest.class.getResource("/video/Debrecen_Egyetemsgt.mp4").getPath()));
	
	}*/
}
