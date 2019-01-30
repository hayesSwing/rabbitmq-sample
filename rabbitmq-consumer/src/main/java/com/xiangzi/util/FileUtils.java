package com.xiangzi.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

public class FileUtils {
	
	public static String charset = "UTF-8";
	public static final byte[] bom = { -17, -69, -65 };
	
	public static boolean writeText(String fileName, String content) {
		return writeText(fileName, content, charset);
	}

	public static boolean writeText(String fileName, String content,
			String encoding) {
		return writeText(fileName, content, encoding, false);
	}

	public static boolean writeText(String fileName, String content,
			String encoding, boolean bomFlag) {
		try {
			byte[] bs = content.getBytes(encoding);
			if ((encoding.equalsIgnoreCase("UTF-8")) && (bomFlag)) {
				bs = ArrayUtils.addAll(bom, bs);
			}
			writeByte(fileName, bs);
		} catch (Exception e) {
			return false;
		}
		return true;
	}


	public static byte[] readByte(String fileName) {
		try {
			FileInputStream fis = new FileInputStream(fileName);
			byte[] r = new byte[fis.available()];
			fis.read(r);
			fis.close();
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] readByte(File f) {
		try {
			FileInputStream fis = new FileInputStream(f);
			byte[] r = readByte(fis);
			fis.close();
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] readByte(InputStream is) {
		try {
			byte[] r = new byte[is.available()];
			is.read(r);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean writeByte(String fileName, byte[] b) {
		try {
			BufferedOutputStream fos = new BufferedOutputStream(
					new FileOutputStream(fileName));
			fos.write(b);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean writeByte(File f, byte[] b) {
		try {
			BufferedOutputStream fos = new BufferedOutputStream(
					new FileOutputStream(f));
			fos.write(b);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String readText(File f) {
		return readText(f, charset);
	}

	public static String readText(File f, String encoding) {
		try {
			InputStream is = new FileInputStream(f);
			String str = readText(is, encoding);
			is.close();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String readText(InputStream is, String encoding) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					encoding));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			br.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String readText(String fileName) {
		return readText(fileName, charset);
	}

	public static String readText(String fileName, String encoding) {
		try {
			InputStream is = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					encoding));
			StringBuffer sb = new StringBuffer();
			String line;
			int c = br.read();
			if (!encoding.equalsIgnoreCase(charset) || c != 65279) {
				sb.append((char) c);
			}
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			br.close();
			is.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String readURLText(String urlPath) {
		return readURLText(urlPath, charset);
	}

	/**
	 * 根据urlPath读取网页数据
	 * 
	 * @param urlPath
	 * @param encoding
	 * @return
	 */
	public static String readURLText(String urlPath, String encoding) {
		try {
			URL url = new URL(urlPath);
			BufferedReader in = new BufferedReader(new InputStreamReader(url
					.openStream(), encoding));
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}
			in.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 不管路径是文件还是文件夹，都删掉
	 * 
	 * @param path
	 * @return
	 */
	public static boolean delete(String path) {
		File file = new File(path);
		return delete(file);
	}

	/**
	 * 不管路径是文件还是文件夹，都删掉
	 * 
	 * @param file
	 * @return
	 */
	public static boolean delete(File file) {
		if (!file.exists()) {
			return false;
		}
		if (file.isFile()) {
			return file.delete();
		} else {
			return FileUtils.deleteDir(file);
		}
	}

	/**
	 * 删除文件夹，且删除自己本身
	 * 
	 * @param path
	 * @return boolean
	 */
	private static boolean deleteDir(File dir) {
		try {
			return deleteFromDir(dir) && dir.delete(); // 先删除完里面所有内容再删除空文件夹
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static boolean mkdir(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return true;
	}

	/**
	 * 文件名支持使用正则表达式（文件路径不支持正则表达式）
	 */
	public static boolean deleteEx(String fileName) {
		int index1 = fileName.lastIndexOf("\\");
		int index2 = fileName.lastIndexOf("/");
		index1 = index1 > index2 ? index1 : index2;
		String path = fileName.substring(0, index1);
		String name = fileName.substring(index1 + 1);
		File f = new File(path);
		if (f.exists() && f.isDirectory()) {
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (Pattern.matches(name, files[i].getName())) {
					files[i].delete();
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 删除文件夹里面的所有文件,但不删除自己本身
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteFromDir(String dirPath) {
		File file = new File(dirPath);
		return deleteFromDir(file);
	}

	/**
	 * 删除文件夹里面的所有文件,但不删除自己本身
	 * 
	 * @param file
	 * @return
	 */
	public static boolean deleteFromDir(File dir) {
		if (!dir.exists()) {
			return false;
		}
		if (!dir.isDirectory()) {
			return false;
		}
		File[] tempList = dir.listFiles();
		for (int i = 0; i < tempList.length; i++) {
			if (!delete(tempList[i])) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean copy(String oldPath, String newPath, FileFilter filter) {
		File oldFile = new File(oldPath);
		File[] oldFiles = oldFile.listFiles(filter);
		boolean flag = true;
		if (oldFiles != null) {
			for (int i = 0; i < oldFiles.length; i++) {
				if (!copy(oldFiles[i], newPath + "/" + oldFiles[i].getName())) {
					flag = false;
				}
			}
		}
		return flag;
	}

	public static boolean copy(String oldPath, String newPath) {
		File oldFile = new File(oldPath);
		return copy(oldFile, newPath);
	}

	public static boolean copy(File oldFile, String newPath) {
		if (!oldFile.exists()) {
			return false;
		}
		if (oldFile.isFile()) {
			return copyFile(oldFile, newPath);
		} else {
			return copyDir(oldFile, newPath);
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldFile
	 * @param newPath
	 * @return boolean
	 */
	private static boolean copyFile(File oldFile, String newPath) {
		if (!oldFile.exists()) { // 文件存在时
			return false;
		}
		if (!oldFile.isFile()) { // 文件存在时
			return false;
		}
		try {
			int byteread = 0;
			InputStream inStream = new FileInputStream(oldFile); // 读入原文件
			FileOutputStream fs = new FileOutputStream(newPath);
			byte[] buffer = new byte[1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}
			fs.close();
			inStream.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldDir
	 * @param newPath
	 * @return boolean
	 */
	private static boolean copyDir(File oldDir, String newPath) {
		if (!oldDir.exists()) { // 文件存在时
			return false;
		}
		if (!oldDir.isDirectory()) { // 文件存在时
			return false;
		}
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File[] files = oldDir.listFiles();
			File temp = null;
			for (int i = 0; i < files.length; i++) {
				temp = files[i];
				if (temp.isFile()) {
					if (!FileUtils
							.copyFile(temp, newPath + "/" + temp.getName())) {
						return false;
					}
				} else if (temp.isDirectory()) {// 如果是子文件夹
					if (!FileUtils.copyDir(temp, newPath + "/" + temp.getName())) {
						return false;
					}
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	public static boolean move(String oldPath, String newPath) {
		return copy(oldPath, newPath) && delete(oldPath);
	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldFile
	 * @param newPath
	 */
	public static boolean move(File oldFile, String newPath) {
		return copy(oldFile, newPath) && delete(oldFile);
	}

	public static void serialize(Serializable obj, String fileName) {
		try {
			FileOutputStream f = new FileOutputStream(fileName);
			ObjectOutputStream s = new ObjectOutputStream(f);
			s.writeObject(obj);
			s.flush();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] serialize(Serializable obj) {
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ObjectOutputStream s = new ObjectOutputStream(b);
			s.writeObject(obj);
			s.flush();
			s.close();
			return b.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object unserialize(String fileName) {
		try {
			FileInputStream in = new FileInputStream(fileName);
			ObjectInputStream s = new ObjectInputStream(in);
			Object o = s.readObject();
			s.close();
			return o;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object unserialize(byte[] bs) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(bs);
			ObjectInputStream s = new ObjectInputStream(in);
			Object o = s.readObject();
			s.close();
			return o;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
