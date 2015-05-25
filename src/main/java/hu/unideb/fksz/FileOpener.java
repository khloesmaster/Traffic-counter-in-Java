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

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileOpener 
{
	
	private FileChooser file_chooser = new FileChooser();

	private void configureFileChooser()
	{
		this.file_chooser.setTitle("Select your video");
		this.file_chooser.setInitialDirectory(new File(System.getProperty("user.home")));
		
		this.file_chooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("MP4","*.mp4", "*.MP4"),
					new FileChooser.ExtensionFilter("AVI", "*.avi", "*.AVI"),
					new FileChooser.ExtensionFilter("PNG", "*.png")
					);
	}
	
	public String getFileName(Stage stage)
	{
		configureFileChooser();	
		File file = file_chooser.showOpenDialog(stage);
		if (file != null)
		{
			TrafficCounterLogger.traceMessage("File loaded successfully!");
			return file.toString();
		}
		else
		{
			TrafficCounterLogger.warnMessage("No file was selected!");
			return null;
		}
	}

}
