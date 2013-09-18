package com.test;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * 文件操作类 
 * @author wangYu
 *
 */
public class FileUtils {
	
	
	private static String rootPath; 
	
	/**
	 * 得到根路径
	 * @return 根路径
	 */
	
	private FileUtils(){}
	
	public static String getRootPath() {
		return rootPath;
	}

	/**
	 * 设置文件操作的根路径 
	 * @param rootPath  
	 */
	public static void setRootPath(String rootPath) {
		FileUtils.rootPath = rootPath;
	}

	/**
	 * 在磁盘上rootPath下创建文件
	 * @param filename 文件名
	 * @param dir 目录名
	 * @return file 创建的文件
	 */
	private static File createFile(String filename,String dir){
		File file = new File(rootPath + dir + File.separator + filename);
		if(!isFileExist(file))
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return file;
	}
	
	/**
	 * 在磁盘上rootPath下创建目录
	 * @param dir 目录名
	 * @return file 创建的目录
	 */
	private static File createDir(String dir){
		File dirFile = new File(rootPath + dir + File.separator);
		if(!isFileExist(dirFile))	
			dirFile.mkdir();
		return dirFile;
	}
		
	/**
	 * 判断文件是否存在
	 * @param file 文件
	 * @return true if the file exists, false otherwise
	 */
	private static boolean isFileExist(File file){
		if(file != null){
			return file.exists();
		}
		return false;
	}
	/**
	 * rootPath下指定目录名下文件是否存在
	 * @param dir 目录名
	 * @param filename 文件名
	 * @return true if the file exists, false otherwise
	 */
	private static boolean isFileExist(String dir,String filename) {
		File file = new File(rootPath + dir + File.separator + filename);
		return isFileExist(file);
	}
	/**
	 * rootPath下指定目录名是否存在 
	 * @param dir 目录名
	 * @return true if the file exists, false otherwise
	 */
	private static boolean isFileExist(String dir) {
		File file = new File(rootPath + dir + File.separator);
		return isFileExist(file);
	}
	
	/**
	 * 从输入流中读出数据写入到 rootPath下磁盘文件中 
	 * 一次写的内容固定大小 4 * 1024bytes
	 * @param dir 目录名
	 * @param filename 文件名
	 * @param input 输入流
	 * @return file 已写入数据的文件
	 */
	public static File writeFromInput(String dir,String filename,InputStream input){
		return writeFromInput(dir, filename, input, false);
	}
	/**
	 * 从输入流中读出数据写入到 rootPath下磁盘文件中 
	 * 一次写的内容固定大小 4 * 1024bytes
	 * @param dir 目录名
	 * @param filename 文件名
	 * @param input 输入流
	 * @param append 是否接着写
	 * @return file 已写入数据的文件
	 */
	public static File writeFromInput(String dir,String filename,InputStream input,boolean append){
		File file = null;
		OutputStream output = null;
		try{
			createDir(dir);
			file = createFile(filename,dir);
			output = new FileOutputStream(file,append);
			byte [] buffer = new byte[4 * 1024];
			int temp;
			while( ( temp = input.read(buffer))!=-1 ){
				output.write(buffer,0,temp);
			}
			output.flush();			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(output != null) {
					output.close();
					output = null;
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}
	
	/**
	 * 从字符串数组中读出数据写入到 rootPath下磁盘文件中
	 * 一次写一个data中的一个分量并且换行（"\r\n"）
	 * @param dir 目录名
	 * @param filename 文件名
	 * @param data 输入流
	 * @param append 是否接着写
	 * @return file 已写入数据的文件
	 */
	public static File writeFromStringArray(String dir,String filename,String[] data,boolean append){
		
		File file = null;
		DataOutputStream dos = null;
		try{
			createDir(dir);
			file = createFile(filename,dir);
			dos = new DataOutputStream(new FileOutputStream(file,append)); 
			for(int i = 0; i<data.length;i++) {
				dos.write(data[i].getBytes());
				dos.writeBytes("\r\n");
			}
				
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(dos != null) {
					dos.close();
					dos = null;
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}
	/**
	 * 从字符串数组中读出数据写入到 rootPath下指定目录名下的文件中
	 * 一次读的内容固定大小 4 * 1024bytes拼成一个字符串
	 * @param dir 目录名
	 * @param filename 文件名
	 * @param data 输入流
	 * @return file 已写入数据的文件
	 */
	public static File writeFromStringArray(String dir,String filename,String[] data){
		return writeFromStringArray(dir, filename, data,false);
	}
	
		
	
	/**
	 * 从磁盘 rootPath下指定目录名下的文件读出数据
	 * 一次读的内容固定大小 4 * 1024bytes拼成一个字符串
	 * @param dir 目录名
	 * @param filename 文件名
	 * @return 文件中的数据
	 */
	public static String readData(String dir,String filename) {
		
		String contant = "";
		File file = null;
		DataInputStream dis = null;
		byte [] buffer = new byte[4 * 1024];
		int temp;
		try {	
			if(!isFileExist(dir)) return null;
			if(!isFileExist(dir,filename)) return null;
			file = new File(rootPath + dir + File.separator + filename);
			dis = new DataInputStream(new FileInputStream(file));			
			while( (temp = dis.read(buffer) )!=-1 ){
				contant += new String(buffer,0,temp);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try{
				if(dis != null) {
					dis.close();
					dis = null;
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return contant;
	}
	
	/**
	 * 从磁盘rootPath下指定目录名下的文件中读出数据
	 * 一行一行读 
	 * @param dir 目录名
	 * @param filename
	 * @return 字符串list
	 */
	public static List<String> readLines(String dir,String filename) {
		String line = "";
		List<String> lines = new ArrayList<String>();
		BufferedReader bufferedReader = null;
		File file = null;
		try {
			if(!isFileExist(dir)) return null;
			if(!isFileExist(dir,filename)) return null;
			file = new File(rootPath + dir + File.separator + filename);
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
	        while ( (line = bufferedReader.readLine() ) != null) {
	        	lines.add(line);
	        }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try{
				if(bufferedReader != null) {
					bufferedReader.close();
					bufferedReader = null;
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return lines;
		
	}
	
	/**
	 * 删除rootPath下指定目录下的文件
	 * @param dir 目录名
	 * @param filename 文件名
	 * @return 不存在该文件或删除失败 返回false 删除成功返回true;
	 */
			
	public static boolean deleteFile(String dir,String filename) {
		if(!isFileExist(dir)) return false;
		if(!isFileExist(dir,filename)) return false;
		File file = new File(rootPath + dir + File.separator + filename);
		return file.delete();
	}
	
	/**
	 * 删除rootPath下指定路径下的所有文件
	 * @param dir
	 * @return
	 */
	public static void deleteALLFiles(String path,boolean flag) {
		if(!isFileExist(path)) return ;
		File file = new File(rootPath + path);
		File[] files = null;
		int length = 0;
		if(file.isDirectory()) {
			files = file.listFiles();
			length = files.length;
		}
		for (int i = 0; i < length; i++) {
			deleteALLFiles(path + File.separator + files[i].getName(),true);
		}
		if(flag)
			file.delete();
	}
	
	
	/**
	 * 列出rootPath下指定目录名下的所有文件名称
	 * @param dir 指定目录名
	 * @return 文件名数组
	 */
	public static String[] listFileNames(String dir) {
		File file = new File(rootPath + dir);
		if(!isFileExist(file)) {
			return null;
		} else {
			return file.list();
		}
	}
	
}
