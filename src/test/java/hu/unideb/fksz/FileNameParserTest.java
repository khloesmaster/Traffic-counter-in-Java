package hu.unideb.fksz;

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
