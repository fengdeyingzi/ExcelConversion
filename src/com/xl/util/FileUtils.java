package com.xl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
	
	  //获取文件后缀 包含.
	  public static String getEndName(String path){
	    if(path == null)return null;
	    for(int i=path.length()-1; i>=0; i--){
	      if(path.charAt(i) == '.'){
	        return path.substring(i);
	      }
	      else if(path.charAt(i) == '/' || path.charAt(i) == '\\'){
	        return "";
	      }
	    }
	    return "";
	  }

	  //获取文件名
	  public static String getName(String path){
	    if(path == null)return null;
	    for(int i=path.length()-1; i>=0; i--){
	      if(path.charAt(i)=='/' || path.charAt(i)=='\\'){
	        return path.substring(i+1);
	      }
	    }
	    return "";
	  }

	  //获取文件所在文件夹
	  public static String getDir(String path){
	    if(path == null)return null;
	    for(int i=path.length()-1; i>=0; i--){
	      if(path.charAt(i) == '/' || path.charAt(i) == '\\'){
	        return path.substring(0, i);
	      }
	    }
		return path;
	  }
	  
	  public static String readText(File file,String encoding) throws IOException
		{
			String content = "";
			//	File file = new File(path);

			if(file.isFile())
			{
				FileInputStream input= new FileInputStream(file);

				byte [] buf=new byte[input.available()];
				input.read(buf);
				input.close();
				content = new String(buf,encoding);
			}
			return content;
		}
	  
	  public static String writeText(File file,String encoding,String text) throws IOException
		{
			String content = "";
			//	File file = new File(path);

			
				FileOutputStream input= new FileOutputStream(file);

				
				input.write(text.getBytes(encoding));
				input.close();
			
			return text;
		}

}
