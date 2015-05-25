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



import java.io.File;

/**
 * Class for parsing the input file names.
 * 
 * 
 * @author krajsz
 *
 */



public class FileNameParser 
{
	/**
	 * Returns a {@code String} which represents a city name if it was
	 * specified in the file name.
	 * 
	 * @param fileName	The {@code String} representing the name of the file.
	 * @return a {@code String} which represents a city name if it was specified.
	 */
	public static String getCity(String fileName) 
	{
		String filename = new File(fileName).getName();

		if (filename.indexOf("_") == -1)
		{
			TrafficCounterLogger.warnMessage("City was not specified!");
			return "City not specified";
		}
		return filename.substring(0, filename.indexOf("_"));
	}
	
	/**
	 * Returns a {@code String} which represents a street name if it was
	 * specified in the file name.
	 * 
	 * @param fileName		The {@code String} representing the name of the file.
	 * @return a {@code String} which represents a street name if it was specified.
	 */
	public static String getStreet(String fileName) 
	{
		String filename = new File(fileName).getName();

		if (filename.indexOf("_") == -1 || ((filename.indexOf("_") +1) > filename.lastIndexOf(".")) )
		{
			TrafficCounterLogger.warnMessage("Street was not specified!");
			return "Street not specified";
		}
		return filename.substring(filename.indexOf("_")+1, filename.lastIndexOf("."));
	}
	
	/**
	 * Returns the name of the file denoted by this abstract pathname,
	 * or null if the {@code fileName} is null
	 * 
	 * @param fileName 		The {@code String} representing the name of the file.
	 * @return The name of the file denoted by this abstract pathname,
	 * or null if the {@code fileName} is null.
	 */
	public static String getFileName(String fileName)
	{
		return fileName == null ? null : new File(fileName).getName();
	}
	
	/***
	 * Returns the extension of the file denoted by this abstract {@code fileName}.
	 * 
	 * @param fileName		The {@code String} representing the name of the file.
	 * @return The extension of the file denoted by this abstract {@code fileName}.
	 */
	public static String getExtension(String fileName)
	{
		String filename = new File(fileName).getName();
		return filename.substring(filename.lastIndexOf(".")+1);
	}
	
}
