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



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for logging.
 * @author krajsz
 *
 */
public class TrafficCounterLogger {
	
	private static Logger logger = LoggerFactory.getLogger(TrafficCounterLogger.class);

	/**
	 * Sends an error log message.
	 * 
	 * @param message	The message to be logged.
	 */
	public static void errorMessage(String message)
	{
		logger.error(message);		
	}
	
	/**
	 * Sends a warn log message.
	 * 
	 * @param message	The message to be logged.
	 */
	public static void warnMessage(String message)
	{
		logger.warn(message);		
	}	
	
	/**
	 * Sends an info log message.
	 * 
	 * @param message	The message to be logged.
	 */
	public static void infoMessage(String message)
	{
		logger.info(message);		
	}
	
	/**
	 * Sends a debug log message.
	 * 
	 * @param message	The message to be logged.
	 */
	public static void debugMessage(String message)
	{
		logger.debug(message);		
	}	
	
	/**
	 * Sends a trace log message.
	 * 
	 * @param message	The message to be logged.
	 */
	public static void traceMessage(String message)
	{
		logger.trace(message);		
	}	
}
