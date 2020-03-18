package com.xl.util;

public class ParamTojson {
	
	//检测是否为字母
	static boolean checkLetter(char c){
		return (c>='a' && c<='z' && c>='A' && c<='Z');
	}
	
	//检测是否为空格
	static boolean checkSpace(char c){
		return (c==' ');
	}
	
	//检测是否为换行
	static boolean checkLine(char c){
		return (c=='\n');
	}
	
	
	//将参数转json 按照string的方式进行转换
	
	public static String toJson(String text){
		int type = 0;
		StringBuffer buffer= new StringBuffer();
		char c=0;
		int start = 0;
		buffer.append("{\n");
		String items[]= text.split("\n");
		String item=null;
		for(int i=0;i<items.length;i++){
			item = items[i];
			if(item.equals("string")){
				if(i>0){
					buffer.append("    \""+items[i-1]+"\" : "+items[i-1]+",");
					if(i+2 < items.length){
						buffer.append("    //"+items[i+2]);
						buffer.append("\n");
					}
					else{
						buffer.append("\n");
					}
				}
			}
		}
		/*
		for(int i=0;i<text.length();i++){
			c = text.charAt(i);
			switch(type){
			case 0:
				buffer.append(c);
				if(checkLetter(c)){
					type = 1;
					start = i;
					buffer.append("\"");
				}
				else if(checkLine(c)){
					type = 0;
				}
				else if(checkSpace(c)){
					type = 0;
				}
				break;
			case 1:
				if(checkSpace(c)){
					buffer.append("\"");
					buffer.append(c);
					item = text.substring(start,i);
					buffer.append(":"+item+",");
					buffer.append("//");
					type = 2;
				}
				else{
					buffer.append(c);
				}
				break;
			case 2:
				if(checkLine(c)){
					buffer.append(c);
					type = 0;
				}
				else 
				{
					buffer.append(c);
				}
				break;
			}
		}*/
		buffer.append("}");
		
		return buffer.toString();
		
	}
	
	

}
