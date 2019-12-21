package org.xjtu.framework.core.util;

import java.io.File;
import java.io.FilenameFilter;

public class RibFilenameFilter implements FilenameFilter {

	private String suffix;

	 public RibFilenameFilter(String suffix){ this.suffix = suffix; }
	 
	public boolean accept(File dir, String name) {
		if (name.endsWith(suffix)) {
			return true;
		}
		return false;
	}
}
