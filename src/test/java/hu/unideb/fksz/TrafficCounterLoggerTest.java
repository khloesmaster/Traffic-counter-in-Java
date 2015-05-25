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

import org.junit.Test;

public class TrafficCounterLoggerTest {

	@Test
	public void testError() {

		TrafficCounterLogger.errorMessage("Error message");
	}
	@Test
	public void testWarn() {

		TrafficCounterLogger.warnMessage("Warn message");
	}
	@Test
	public void testDebug() {

		TrafficCounterLogger.debugMessage("Debug message");
	}
	@Test
	public void testInfo() {

		TrafficCounterLogger.infoMessage("Info message");
	}
	@Test
	public void testTrace() {

		TrafficCounterLogger.traceMessage("Trace message");
	}
	@Test
	public void testConstructor()
	{
		TrafficCounterLogger logger = new TrafficCounterLogger();
	}

}
