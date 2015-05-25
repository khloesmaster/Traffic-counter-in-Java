
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
import javafx.stage.FileChooser.ExtensionFilter;
/**
 * Class for saving files with a specific extension.
 * 
 * @author krajsz
 *
 */
public class FileSaver 
{

	private FileChooser file_chooser = new FileChooser();

	/**
	 * Configures the {@code FileChooser}.
	 * Sets the title of the {@code FileChooser}.
	 * Sets the initial directory of the {@code FileChooser}.
	 * Adds {@link ExtensionFilter}s to the {@code FileChooser}.
	 */
	private void configureFileChooser()
	{
		this.file_chooser.setTitle("Save your image");
		this.file_chooser.setInitialDirectory(new File(System.getProperty("user.home")));
		this.file_chooser.setInitialFileName("image");	

		this.file_chooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("JPG","*.jpg", "*.JPG"),
					new FileChooser.ExtensionFilter("JPEG", "*.jpeg", "*.JPEG"),
					new FileChooser.ExtensionFilter("PNG", "*.png", "*.PNG")
					);
	}
	
	/**
	 * Returns a {@code String} which represents the absolute path of the
	 * specified file name or {@code null} if no file name was specified.
	 * 
	 * @param stage		the owner window of the displayed file dialog.
	 * @return A {@code String} which represents the absolute path of the
	 * selected file or {@code null} if no file was selected.
	 */
	public String getFileName(Stage stage)
	{
		configureFileChooser();	
		File file = file_chooser.showSaveDialog(stage);
		if (file != null)
		{
			TrafficCounterLogger.traceMessage("File saved successfully!");
			return file.toString() + "." + file_chooser.getSelectedExtensionFilter().getDescription();
		}
		else
		{
			TrafficCounterLogger.warnMessage("No filename was specified!");
			return null;
		}
	}
}
