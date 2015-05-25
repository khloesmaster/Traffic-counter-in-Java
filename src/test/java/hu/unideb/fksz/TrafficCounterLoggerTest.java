package hu.unideb.fksz;

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
