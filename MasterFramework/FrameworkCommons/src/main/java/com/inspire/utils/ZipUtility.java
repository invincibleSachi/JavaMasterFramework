package com.inspire.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import com.inspire.abstestbase.MasterLogger;

/**
 * @author sachi
 *
 */
public class ZipUtility {
	public static Logger log = MasterLogger.getInstance();

	public static void unzipFile(String zipFilePath, String destDir) {
		File dir = new File(destDir);
		// create output directory if it doesn't exist
		if (!dir.exists())
			dir.mkdirs();
		FileInputStream fis;
		// buffer for read and write data to file
		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(zipFilePath);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(destDir + File.separator + fileName);
				log.info("Unzipping to " + newFile.getAbsolutePath());
				// create directories for sub directories in zip
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				// close this ZipEntry
				zis.closeEntry();
				ze = zis.getNextEntry();
			}
			// close last ZipEntry
			zis.closeEntry();
			zis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static List<String> readZipFileEntries(String zipFileName) {
		if (!FileIOUtility.isFileExists(zipFileName)) {
			log.info("Zip file is blank or does not exists");
			return null;
		}
		ZipFile zipFile;
		List<String> zippedFileNames = new ArrayList<String>();
		try {
			zipFile = new ZipFile(zipFileName);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				zippedFileNames.add(entry.getName().trim());
			}
		} catch (IOException e) {
			log.info("IO Exception occurred while reading zip file " + zipFileName + " " + e.getMessage());
		}
		log.info(Arrays.toString(zippedFileNames.toArray()));
		return zippedFileNames;
	}
	
	public static void zipFile(String fileName, String toBeZippedFolderName){
		 
		FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(toBeZippedFolderName);
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            File input = new File(fileName);
            fis = new FileInputStream(input);
            ZipEntry ze = new ZipEntry(input.getName());
            System.out.println("Zipping the file: "+input.getName());
            zipOut.putNextEntry(ze);
            byte[] tmp = new byte[4*1024];
            int size = 0;
            while((size = fis.read(tmp)) != -1){
                zipOut.write(tmp, 0, size);
            }
            zipOut.flush();
            zipOut.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            try{
                if(fos != null) fos.close();
                if(fis != null) fis.close();
            } catch(Exception ex){
                 
            }
        }
	    }
	
}
