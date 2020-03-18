package com.xl.util;

/**
 * 针对apizza网站的参数转代码的功能 将参数复制下来 一键生成代码
 * @author Administrator
 *
 */
public class ParameterToCode {
	
	public static final int _POST=2,
			_GET = 1,
			_HEADER = 0,
			_FILE = 3;
	

	//检测是否为中文
		public static boolean checkCh(String text)
		{
			if(text.getBytes().length==text.length())//这句就是来判断 String是否含有中文字符。
			{
			 return false;
			}
			else
			{
				return true;
			}
		}
	
	public static String toCode(String name,String text) {
		StringBuffer buffer= new StringBuffer();
		String url = null;
		int type = _GET;
		System.out.println(text);
		String function_name= "addPostParmeter";
		if(text.indexOf("GET")>=0){
			function_name= "addGetParmeter";
		}
		String item_name=name;
		if(item_name.length()==0){
			item_name= "connect";
		}
		char c=0;
		String info=null;
		String items[]= text.split("\n");
		String item=null;
		String temp=null;
		for(int i=0;i<items.length;i++){
			item= items[i];
			if(item.equals("POST")){
				if(i+1<items.length){
					url = items[i+1];
					buffer.append("//POST请求\n");
					buffer.append("String url = \""+url+"\";\n\n");
				}
			}
			if(item.equals("GET")){
				if(i+1<items.length){
					url = items[i+1];
					buffer.append("//GET请求\n");
					buffer.append("String url = \""+url+"\";\n\n");
				}
			}
			if(item.equals("Header")){
				type = _HEADER;
				function_name = "addHeader";
			}
			if(item.equals("Body")){
				if(text.indexOf("GET")>=0){
					type = _GET;
					function_name = "addGetParmeter";
				}
				else{
					type = _POST;
					function_name = "addPostParmeter";
				}
			}
			System.out.println(item);
			if(item.indexOf("string")>=0){
				if(i>=1)
				temp= items[i-1];
				if(i+2<items.length)
				info= items[i+2];
				else{
					info= "";
				}
				if(!checkCh(temp)){
					buffer.append(""+item_name+"."+function_name+"(\""+temp+"\","+temp+"); //"+info+"\n");
					
				}
			}
			
		}
		
		return buffer.toString();
	}

}
