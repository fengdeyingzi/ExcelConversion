import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
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

import com.sun.xml.bind.v2.schemagen.xmlschema.List;
import com.xl.util.ClipBoard;
import com.xl.util.ExcelUtil;
import com.xl.util.FileUtils;
import com.xl.util.UIUtil;
import com.xl.util.XmlToJson;
import com.xl.window.JSONToCodeWindow;

public class ExcelConversionMain {
	static String url = "https://github.com/fengdeyingzi/ExcelConversion.git";
	static String app_help = "控制台支持以下命令\n  -t 判断输入的类型 取值如下\n" + "    xmlToJson 将xml转json\n" + "    xml2xls 将xml转xls\n"
			+ "    xls2xml 将xls转xml\n"+ "    xls2rc 将xls转rc文件\n" + " -i 输入的文件\n" + " -o 输出的文件\n" + " -coding 文件的文本编码 默认utf-8\n" + "开源地址：" + url;

	/*
	 * -t 判断输入的类型 取值如下 xmlToJson 将xml转json xml2xls 将xml转xls xls2xml 将xls转xml -f
	 * 输入的文件 -o 输出的文件 -coding 文件的文本编码 默认utf-8
	 */
	public static void main(String[] args) {
		UIUtil.setWindowsStyle();// 设置windows风格

		/*
		 * if(args.length==0){ System.out.println("输入内容为空"); String test =
		 * "-t xml2xls -i D:\\strings.xml D:\\strings_en.xml -o D:\\test.xls -coding UTF-8"
		 * ; args = test.split(" "); }
		 */
		// File file = null;
		String type = null;
		File input = null;
		File output = null;
		ArrayList<File> list_input = new ArrayList<>();

		String coding = "UTF-8";
		int type_index = 0;

		if (args.length > 0) {
			// 获取类型

			// 获取输入文件

			// 获取输出文件
			for (int i = 0; i < args.length - 1; i++) {
				String item = args[i];
				switch (type_index) {
				case 0:
					if (item.equals("-t")) {
						type = args[i + 1];
					} else if (item.equals("-f")) {
						// file = new File(args[i + 1]);
						input = (new File(args[i + 1]));
						list_input.add(input);
						type_index = 1;
					} else if (item.equals("-i")) {
						input = (new File(args[i + 1]));
						list_input.add(input);
						type_index = 1;
					} else if (item.equals("-o")) {
						output = new File(args[i + 1]);

					} else if (item.equals("-h")) {
						System.out.println(app_help);
					} else if (item.equals("-coding")) {
						coding = args[i + 1];
					}
					break;
				case 1:
					if (i < args.length - 1) {
						if (!args[i + 1].startsWith("-")) {
							list_input.add(new File(args[i + 1]));
						} else {
							type_index = 0;
						}
					} else {
						type_index = 0;

					}

					break;

				default:
					break;
				}

				/*
				 * switch (item) { case "-t": type = args[i+1]; break; case
				 * "-f": file = new File(args[i+1]); break; case "-i":
				 * 
				 * input = (new File(args[i+1]));
				 * 
				 * break; case "-o":
				 * 
				 * output = new File(args[i+1]);
				 * 
				 * 
				 * break; case "-h": System.out.println("  -t 判断输入的类型 取值如下\n"+
				 * "    xmlToJson 将xml转json\n"+ "    xml2xls 将xml转xls\n"+
				 * "    xls2xml 将xls转xml\n"+ " -f 输入的文件\n"+ " -o 输出的文件\n"+
				 * " -coding 文件的文本编码 默认utf-8"); break; case "-coding": coding =
				 * args[i+1]; break;
				 * 
				 * default: break; }
				 */
			}
			//
			if (type == null) {
				System.out.println("type unknown,please input -t.");
			}
			// 检测是否已输入
			else if (input == null) {
				System.out.println("file unknowm,please input -i.");

			}

			if (output == null) {
				// System.out.println("input unknoen,please output -o.");
				if (type.equals("xmlToJson"))
					output = new File(input.getParentFile(), input.getName() + ".json");
				if (type.equals("xml2xls")) {
					output = new File(input.getParentFile(), input.getName() + ".xls");
				}
			}

			switch (type) {
			case "xmlToJson":
				String text;
				try {
					text = readText(input, coding);
					XmlToJson xmlToJson = new XmlToJson(text);
					// ClipBoard.setText(xmlToJson.check());
					String jsonTest = xmlToJson.check(coding);
					saveText(output, jsonTest, coding);
					try {
						JSONObject jsonObject = new JSONObject(jsonTest);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					// System.exit(0);
				} catch (IOException e) {

					e.printStackTrace();
				}

				break;
			case "xml2xls":
				try {
					text = readText(input, coding);
					String text2 = null;
					ArrayList<HashMap<String, String>> list_mapstring = new ArrayList<>();
					for (int i = 0; i < list_input.size(); i++) {
						text2 = readText(list_input.get(i), coding);
						XmlToJson xmlToJson2 = new XmlToJson(text2);
						list_mapstring.add(xmlToJson2.getHashList(coding));
					}

					ArrayList<String> titles = new ArrayList<>();
					titles.add("键");
					for (int n = 0; n < list_input.size(); n++) {
						String dirname = list_input.get(n).getParentFile().getName();
						titles.add(dirname);
					}
					ArrayList<ArrayList<String>> values = new ArrayList<>();
					// 添加一行
					Iterator iterator = list_mapstring.get(0).entrySet().iterator();
					while (iterator.hasNext()) {
						Map.Entry entry = (Map.Entry) iterator.next();
						String key = (String) entry.getKey();
						String value = (String) entry.getValue();
						ArrayList<String> item = new ArrayList<>();

						item.add((String) key);
						item.add((String) value);
						for (int n = 1; n < list_mapstring.size(); n++) {
							if (list_mapstring.get(n).get(key) != null) {
								item.add(list_mapstring.get(n).get(key));
							} else {
								item.add("");
							}
						}

						values.add(item);
					}
					HSSFWorkbook work = ExcelUtil.getHSSFWorkbook("item", titles, values, null);
					work.write(output);

				} catch (IOException e) {
					e.printStackTrace();
				}

				break;
			case "xls2xml":
				xlsToXml(input);
				break;
			case "xls2rc":
				ArrayList<byte[]> listRC = xlsToRC(input,output);
				
				
			default:
				break;
			}
		} else {
			System.out.println("风的影子制作，Excal与strings.xml互转工具，用于实现安卓多语言编辑");
			System.out.println(app_help);
		}
	}

	// 将xls 转xml
	public static void xlsToXml(File xlsfile) {
		ArrayList<StringBuffer> list_xml = new ArrayList<>();
		// list_xml.add(new StringBuffer());
		String key = null;
		// 读取excel
		try {
			Workbook work = ExcelUtil.getWorkbook(xlsfile.getPath());
			Sheet sheet = work.getSheet("item");
			for (int iy = 1; iy < sheet.getLastRowNum(); iy++) {
				Row row = sheet.getRow(iy);
				for (int ix = 0; ix < row.getLastCellNum(); ix++) {
					Cell col = row.getCell(ix);
					if (ix == 0)
						key = col.getStringCellValue();
					// System.out.println(col.getStringCellValue());
					if (ix >= 1) {
						StringBuffer buffer = new StringBuffer();
						buffer.append(
								"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources xmlns:tools=\"http://schemas.android.com/tools\">\r\n");
						String value = col.getStringCellValue();
						if (ix >= list_xml.size()) {
							list_xml.add(buffer);
						}
						StringBuffer tempbuffer = list_xml.get(ix - 1);
						tempbuffer.append("    <string name=\"" + key + "\" >" + value + "</string>\r\n");
					}

				}
				// System.out.println("----");
			}

		} catch (EncryptedDocumentException e) {

			e.printStackTrace();
		} catch (InvalidFormatException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		for (int i = 0; i < list_xml.size(); i++) {
			list_xml.get(i).append("</resources>");
			System.out.println(list_xml.get(i).toString());
			// 依次写入文件
			try {
				saveText(new File(xlsfile.getParent(), "strings_" + i + ".xml"), list_xml.get(i).toString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	
	public static byte[] getIntByte(int number) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (number & 0xff);
		bytes[1] = (byte) ((number >> 8) & 0xff);
		bytes[2] = (byte) ((number >> 16) & 0xff);
		bytes[3] = (byte) ((number >> 24) & 0xff);
		return bytes;
	}

	public static byte[] getShortByte(int number) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) (number & 0xff);
		bytes[1] = (byte) ((number >> 8) & 0xff);
		return bytes;
	}

	// 将xls 转 rc文件
	public static ArrayList<byte[]> xlsToRC(File xlsfile,File outputfile) {
		ArrayList<byte[]> listByte = new ArrayList<byte[]>();
		
		ArrayList<String> listKey = new ArrayList<String>();
		StringBuffer buffer_h = new StringBuffer();
//		ArrayList<String> listRC = new ArrayList<>();
		buffer_h.append("//begin the strings\n\n");
		String coding = "UTF-16BE";
		String key = null;
		// 读取excel
		try {
			Workbook work = ExcelUtil.getWorkbook(xlsfile.getPath());
			
			Sheet sheet = work.getSheetAt(0);
			int columns = 0;
			int RES_STRING_COUNT = 0;
			for (int iy = 1; iy < sheet.getLastRowNum()+1; iy++) {
				Row row = sheet.getRow(iy);
				columns = row.getLastCellNum();
				if(columns>3)columns = 3;
				for (int ix = 0; ix < columns; ix++) {
					Cell col = row.getCell(ix);
					if(iy==2){
						listByte.add(new byte[]{0x00});
					}
					
					
					key = col.getStringCellValue();
					if (ix == 0){
						
						buffer_h.append(String.format("#define %s %d\n", key,iy-1));
						RES_STRING_COUNT ++;
						listKey.add(key);
					}
					
				}
			}
			buffer_h.append("\n\n#define RES_STRING_COUNT "+RES_STRING_COUNT+"\n");
			Row row = sheet.getRow(2);
			for (int ix = 0; ix < columns; ix++) {
				ArrayList<String> listRC = new ArrayList<>();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				try {
					for (int iy = 1; iy < sheet.getLastRowNum()+1; iy++) {
//						System.out.println("ix = "+ix+", iy="+iy);
						row = sheet.getRow(iy);
						Cell col = row.getCell(ix);
						if(col!=null){
							key = col.getStringCellValue();
						if (ix >= 1) {
							String value = col.getStringCellValue();
							listRC.add(String.format("%s", value));
							System.out.println("key="+key+",value="+value);
						}
						}else{
							listRC.add("");
							System.out.println(String.format("获取数据失败，行 %d，列 %d", iy,ix));
						}
						
					}
				} catch (Exception e) {
					System.out.println("ix="+ix);
					e.printStackTrace();
				}
				
				
				int temp_int = 0;
				try {
					for (int i = 0; i < listRC.size(); i++) {
						String item = listRC.get(i);
						// 转换为byte
						outputStream.write(getShortByte(temp_int));
						temp_int += (item.getBytes(coding).length + 2);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				for(int i=0;i<listRC.size();i++){
					String item = listRC.get(i);
//					System.out.println(item);
					try {
						outputStream.write(item.getBytes(coding));
						outputStream.write(new byte[]{0,0});
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				listByte.set(ix, outputStream.toByteArray());
			}

		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(buffer_h.toString());
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(new File(outputfile, "res_str.h"));
			outputStream.write(buffer_h.toString().getBytes("UTF-8"));
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i=1;i<listByte.size();i++){
			File file = new File(outputfile, String.format("res_lang%d.rc", i-1));
			
			try {
				outputStream = new FileOutputStream(file);
				try {
					outputStream.write(listByte.get(i));
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			
			
		}
		
		return listByte;
	}

	// 写入文本

	public static void saveText(File file, String text, String coding)
			throws UnsupportedEncodingException, IOException {
		FileOutputStream outStream = new FileOutputStream(file);
		outStream.write(text.toString().getBytes(coding));
	}

	// 读取文本
	public static String readText(File file, String coding) throws IOException {
		FileInputStream input = new FileInputStream(file);
		byte[] buf = new byte[input.available()];
		input.read(buf);
		input.close();
		return new String(buf, coding);
	}

}
