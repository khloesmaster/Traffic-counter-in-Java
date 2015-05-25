package hu.unideb.fksz;

import org.junit.Test;

public class FileSaverTest {

	FileSaver testFileSaver = new FileSaver();
	@Test
	public void testConfigureFileChooser() {
		testFileSaver.configureFileChooser();
	}

}
