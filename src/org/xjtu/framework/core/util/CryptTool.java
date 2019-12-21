package org.xjtu.framework.core.util;

public class CryptTool {
	 public static String getPassword(int count, boolean letters, boolean numbers) {
	        return org.apache.commons.lang.RandomStringUtils.random(count, letters, numbers);
	    }
}
