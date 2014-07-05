package amidst.gui.menu;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class LocationsFileFilter extends FileFilter {
	@Override
	public boolean accept(File file) {
		if (file.isDirectory())
			return true;
		String[] st = file.getName().split("\\.");
		String ext = st[st.length - 1];
		return ext.equalsIgnoreCase("txt") || ext.equalsIgnoreCase("csv");
	}
	
	@Override
	public String getDescription() {
		return "Overworld Map locations file (*.TXT or *.CSV)";
	}
}
