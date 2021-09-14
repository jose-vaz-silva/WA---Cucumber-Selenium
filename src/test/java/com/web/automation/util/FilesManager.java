package com.web.automation.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;

public class FilesManager {
	public static File[] getFileList(String directory) {
		return new File(directory).listFiles();
	}

	public static File[] getFileListWithFilter(String directory, String regexFilter) {
		FileFilter fileFilter = new RegexFileFilter(regexFilter);
		return new File(directory).listFiles(fileFilter);
	}

	public static void deleteListFiles(File[] fileList) {
		Arrays.asList(fileList).forEach((file) -> {
			if (file.isDirectory()) {
				try {
					FileUtils.deleteDirectory(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				file.delete();
			}
		});
	}

	public static void deleteFile(File file) {
		if (file.isDirectory()) {
			try {
				FileUtils.deleteDirectory(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (file.exists())
					FileUtils.forceDelete(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getFileAsBase64Binary(File file) throws IOException {
		byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
		return new String(encoded, StandardCharsets.US_ASCII);
	}

	public static String getContentFileAsString(File file) {
		String content = new String();
		try {
			content = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public static void writeFile(File file, String value, boolean appendValue) {
		try {
			FileWriter fw = new FileWriter(file);
			if (appendValue)
				fw.append(value);
			else {
				fw.write(value);
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
