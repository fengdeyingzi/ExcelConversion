package com.xl.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class JsonToCode {
	String textString;
	String codeString;
	String objectNameString;
	public static final int JSON_ARRAY=1,
	JSON_OBJECT=2,
	JSON_INT=3,
	JSON_STRING=5,
	JSON_DOUBLE=4;
	
	public JsonToCode(){
		this.objectNameString="jsonObject";
	}
	//设置代码风格
	
	//设置传入的json类名
	public void setJsonObjectName(String name){
		this.objectNameString= name;
	}
	//设置空格数
	
	//设置json
	public void setJson(String text) {
		this.textString= text;
	}
	
	
	private int getJSONType(String value){
		if(value.startsWith("\"")){
			return JSON_STRING;
		}
		else if(value.equals("null")){
			return JSON_STRING;
		}
		else if(value.startsWith("[")){
			return JSON_ARRAY;
		}
		else if(value.startsWith("{")){
			return JSON_OBJECT;
		}
		else if(value.indexOf('.')>0){
			return JSON_DOUBLE;
		}
		else {
			return JSON_INT;
		}
		
		
	}
	
	  /** 
     * 将字符串复制到剪切板。 
     */  
    public static void setSysClipboardText(String writeMe) {  
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();  
        Transferable tText = new StringSelection(writeMe);  
        clip.setContents(tText, null);  
    } 

	//去除换行符
    private String deleteEnter(String text) {
    	StringBuffer buffer= new StringBuffer();
    	for(int i=0;i<text.length();i++){
    		char c=text.charAt(i);
    		if(c!='\n' && c!='\r'){
    			buffer.append(c);
    		}
    	}
		return buffer.toString();
	}
    
    public String getCode(){
    	return getCode(objectNameString,textString);
    }
    
    //获取一段代码的结束位置
    private int getEndIndex(String text,int start){
    	int leve = 0;
    	int index=start;
    	char c= 0;
    	int type=0;
    	for(int i=start;i<text.length();i++){
    		c= text.charAt(i);
    		switch (type) {
			case 0:
				if(c=='{'){
					leve++;
					type=1;
				}
				else if(c=='}'){
					leve--;
				}
				
				break;
case 1:
	if(c=='{'){
		leve++;
	}
	if(c=='}'){
		leve--;
		if(leve==0){
			return i;
		}
	}
	
			default:
				break;
			}
    	}
    	
    	return index;
    }
	
	//开始转换
	public String getCode(String objectNameString,String textString) {
		StringBuffer buffer= new StringBuffer();
		int start=0;
		String name=null;
		String value=null;
		char c=0;
		int type=0;
		int end=0;
		for(int i=0;i<textString.length();i++){
			c=textString.charAt(i);
	//System.out.print("type="+type+" "+c+"\n");
		switch (type) {
		
		case 0:  //{
			if(c=='{'){
				type=1;
			}
			else if(c==','){
				type=1;
			}
			else if(c=='}'){
				type=0;
			}
			else if(c=='\"'){
				type=2;
				start= i+1;
			}
			break;
		case 1://"
			if(c=='\"'){
				type=2;
				start=i+1;
			}
			break;
		case 2:
			if(c=='\"'){
				name= textString.substring(start,i);
				type=3;
			}
			break;
		case 3://:
			if(c==':'){
				type=4;
				start=i;
				//name= textString.substring(start,i);
			}
			break;
		case 4://,
			if((c>='0' && c<='9')|| (c=='\"') || (c>='a'&&c<='z') || (c>='A'&& c<='Z') ){
				start= i;
				type=5;
			}
			else if(c=='['){
				type=6;
				start=i;
			}
			else if(c=='{'){
				type=0;
				end= getEndIndex(textString, i);
				buffer.append("JSONObject "+name+"="+objectNameString+".getJSONObject(\""+name+"\"); \n" );
				buffer.append(getCode(name, textString.substring(i,end+1)));
				i=end;
				
			}
		case 5:
			if(c==',' || c=='}'){
				value= textString.substring(start,i);
				int jsontype= getJSONType(value);
				switch (jsontype) {
				case JSON_INT:
					buffer.append("int "+name+"="+objectNameString+".getInt(\""+name+"\"); //"+value);
					buffer.append("\n");
					break;
				case JSON_DOUBLE:
					buffer.append("double "+name+"="+objectNameString+".getDouble(\""+name+"\"); //"+value);
					buffer.append("\n");
					break;
				case JSON_STRING:
					buffer.append("String "+name+"="+objectNameString+".getString(\""+name+"\"); //"+value);
					buffer.append("\n");
					break;

				default:
					break;
				}
				
				type=0;
				
			}
			break;
		case 6://]
			if(c==']'){
				value= textString.substring(start,i);
				type=0;
				buffer.append("JSONArray "+name+"="+objectNameString+".getJSONArray(\""+name+"\"); //"+deleteEnter(value) );
				buffer.append("\n");
			}
			
			break;
		case 7:
			break;
		default:
			break;
		}
		}
		setSysClipboardText(buffer.toString());
		return buffer.toString();
	}
	

}
