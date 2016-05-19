package hu.unideb.fksz;

/*
 * #%L
 * Traffic-counter
 * %%
 * Copyright (C) 2016 FKSZSoft
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

import org.junit.Test;

public class FileNameParserTest {

	@Test
	public void testGetCity() {

		assertNotEquals(
				"",
				FileNameParser.getCity(FileNameParserTest.class.getResource(
						"/video/Debrecen_Egyetemsgt.mp4").getPath()));
		assertNotEquals("", FileNameParser.getCity("afilename.something"));

	}

	@Test
	public void testGetStreet() {
		assertNotEquals(
				"",
				FileNameParser.getStreet(FileNameParserTest.class.getResource(
						"/video/Debrecen_Egyetemsgt.mp4").getPath()));
		assertNotEquals("", FileNameParser.getStreet(("afilename.something")));

	}

	@Test
	public void testGetExtension() {
		assertNotEquals("",
				FileNameParser.getExtension((FileNameParserTest.class
						.getResource("/video/Debrecen_Egyetemsgt.mp4")
						.getPath())));
	}

	@Test
	public void testGetFileName() {
		assertNotEquals("",
				FileNameParser.getFileName((FileNameParserTest.class
						.getResource("/video/Debrecen_Egyetemsgt.mp4")
						.getPath())));
		assertNull(FileNameParser.getFileName(null));
	}

	@Test
	public void testConstructor() {
		FileNameParser fileNameParser = new FileNameParser();
		assertNotNull(fileNameParser);

	}

}
