import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.UIManager;

import org.apache.commons.compress.archivers.dump.InvalidFormatException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONException;
import org.json.JSONObject;

import com.xl.util.ClipBoard;
import com.xl.util.ExcelUtil;
import com.xl.util.UIUtil;
import com.xl.util.XmlToJson;
import com.xl.window.JSONToCodeWindow;


public class ExcelConversionMain {
static String app_help = "控制台支持以下命令\n  -t 判断输入的类型 取值如下\n"+
		 "    xmlToJson 将xml转json\n"+
		 "    xml2xls 将xml转xls\n"+
		 "    xls2xml 将xls转xml\n"+
		 " -f 输入的文件\n"+
		 " -o 输出的文件\n"+
		 " -coding 文件的文本编码 默认utf-8";
	
	/*
	 * -t 判断输入的类型 取值如下
	 *    xmlToJson 将xml转json
	 *    xml2xls 将xml转xls
	 *    xls2xml 将xls转xml
	 * -f 输入的文件
	 * -o 输出的文件
	 * -coding 文件的文本编码 默认utf-8
	 */
	public static void main(String[] args) {
		UIUtil.setWindowsStyle();//设置windows风格

		/*
		if(args.length==0){
			System.out.println("输入内容为空");
			String test = "-t xml2xls -i D:\\strings.xml D:\\strings_en.xml -o D:\\test.xls -coding UTF-8";
			args = test.split(" ");
		}
		*/
		File file = null;
		String type=null;
		File input=null;
		File output=null;
		File input2 = null;
		String coding = "UTF-8";
		int type_index = 0;
		
		if(args.length>0){
			//获取类型
			
			//获取输入文件
			
			//获取输出文件
			for(int i=0;i<args.length-1;i++){
				String item = args[i];
				switch (type_index) {
				case 0:
					if(item.equals("-t")){
						type = args[i+1];
					}
					else if(item.equals("-f")){
						file = new File(args[i+1]);
					}
					else if(item.equals("-i")){
						input = (new File(args[i+1]));
						type_index = 1;
					}
					else if(item.equals("-o")){
						output = new File(args[i+1]);
						
						
					}
					else if(item.equals("-h")){
						System.out.println(app_help);
					}
					else if(item.equals("-coding")){
						coding = args[i+1];
					}
					break;
				case 1:
				
					input = new File(args[i]);
						if(i<args.length-1){
							if(!args[i+1].startsWith("-")){
								input2 = new File(args[i+1]);
								type_index = 0;
							}
							else{
								type_index = 0;
							}
						}
						else{
							type_index = 0;
							
						}
					
					
					
					break;

				default:
					break;
				}
				
				
				/*
				switch (item) {
				case "-t":
					type = args[i+1];
					break;
				case "-f":
					file = new File(args[i+1]);
					break;
				case "-i":
					
						input = (new File(args[i+1]));
					    
					break;
				case "-o":
					
						output = new File(args[i+1]);
					
					
					break;
				case "-h":
					System.out.println("  -t 判断输入的类型 取值如下\n"+
	 "    xmlToJson 将xml转json\n"+
	 "    xml2xls 将xml转xls\n"+
	 "    xls2xml 将xls转xml\n"+
	 " -f 输入的文件\n"+
	 " -o 输出的文件\n"+
	 " -coding 文件的文本编码 默认utf-8");
					break;
				case "-coding":
					coding = args[i+1];
					break;

				default:
					break;
				}
				*/
			}
			//
			if(type==null){
				System.out.println("type unknown,please input -t.");
			}
			//检测是否已输入
			else if(input==null){
				System.out.println("file unknowm,please input -i.");
				
			}
			
			if(output==null){
//				System.out.println("input unknoen,please output -o.");
				if(type.equals("xmlToJson"))
				output =new File( input.getParentFile(),input.getName()+".json");
				if(type.equals("xml2xls")){
					output =new File( input.getParentFile(),input.getName()+".xls");
				}
			}
			
			switch (type) {
			case "xmlToJson":
				String text;
				try {
					text = readText(input, coding);
					XmlToJson xmlToJson = new XmlToJson(text);
//					ClipBoard.setText(xmlToJson.check());
					String jsonTest = xmlToJson.check(coding);
					saveText(output, jsonTest, coding);
					try{
						JSONObject jsonObject = new JSONObject(jsonTest);
					}
					catch(JSONException e){
						e.printStackTrace();
					}
					
					
//					System.exit(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				break;
			case "xml2xls":
				try {
					text = readText(input, coding);
					String text2 = null;
				    if(input2!=null){
				    	text2 = readText(input2, coding);
				    }
					XmlToJson xmlToJson = new XmlToJson(text);
					XmlToJson xmlToJson2 = null;
					if(text2!=null){
						xmlToJson2 = new XmlToJson(text2);
					}
					
				HashMap<String, String> map_string = xmlToJson.getHashList(coding);
				HashMap<String, String> map_string2 = new HashMap<>();
				if(xmlToJson2!=null){
					map_string2 = xmlToJson2.getHashList(coding);
				}
				
				    ArrayList<String> titles = new ArrayList<>();
				    titles.add("键");
				    titles.add("中文");
				    titles.add("英文");
				    ArrayList<ArrayList<String>> values = new ArrayList<>();
				    //添加一行
				    Iterator iterator = map_string.entrySet().iterator();
				    while(iterator.hasNext()){
				    	Map.Entry entry = (Map.Entry) iterator.next();
				    	String key = (String)entry.getKey();
				    	String value = (String)entry.getValue();
				    	ArrayList<String> item = new ArrayList<>();
				    	
				    	item.add((String)key);
				    	item.add((String)value);
				    	if(map_string2.get(key)!=null){
				    		item.add(map_string2.get(key));
				    	}
				    	values.add(item);
				    }
				    HSSFWorkbook work = ExcelUtil.getHSSFWorkbook("item", titles, values, null);
				    work.write(output);

				    
				    
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				break;
			case "xls2xml":
				xlsToXml(input);
				break;

			default:
				break;
			}
		}
		else{
			System.out.println("风的影子制作，Excal与strings.xml互转工具，用于实现安卓多语言编辑");
			System.out.println(app_help);
		}
	}
	
	
	//将xls 转xml
	public static void xlsToXml(File xlsfile) {
		ArrayList<StringBuffer> list_xml= new ArrayList<>();
//		list_xml.add(new StringBuffer());
		String key = null;
		//读取excel
		try {
			Workbook work = ExcelUtil.getWorkbook(xlsfile.getPath());
			Sheet sheet = work.getSheet("item");
			for(int iy =1;iy<sheet.getLastRowNum();iy++){
				Row row = sheet.getRow(iy);
				for(int ix = 0;ix<row.getLastCellNum();ix++){
					Cell col = row.getCell(ix);
					if(ix==0)
					key = col.getStringCellValue();
//					System.out.println(col.getStringCellValue());
					if(ix>=1){
						StringBuffer buffer = new StringBuffer();
						buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources xmlns:tools=\"http://schemas.android.com/tools\">\r\n");
						String value = col.getStringCellValue();
						if(ix>=list_xml.size()){
						list_xml.add(buffer);
					}
						StringBuffer tempbuffer = list_xml.get(ix-1);
						tempbuffer.append("    <string name=\""+key+"\" >"+value+"</string>\r\n");
					}
					
				}
//				System.out.println("----");
			}
			
			
			
			
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		for(int i=0;i<list_xml.size();i++){
			list_xml.get(i).append("</resources>");
			System.out.println(list_xml.get(i).toString());
			//依次写入文件
			try {
				saveText(new File(xlsfile.getParent(),"strings_"+i+".xml"), list_xml.get(i).toString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	//写入文本
	
	public static void saveText(File file,String text,String coding) throws UnsupportedEncodingException, IOException{
		  FileOutputStream outStream = new FileOutputStream(file);
          outStream.write(text.toString().getBytes(coding));
	}
	
	
	//读取文本
	public static String readText(File file,String coding) throws IOException {
		   FileInputStream input = new FileInputStream(file);
           byte[] buf = new byte[input.available()];
           input.read(buf);
           input.close();
           return new String(buf,coding);
	}
	
	
	
	
	
	
	
}
