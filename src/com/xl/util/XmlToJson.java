package com.xl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.json.JSONObject;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.StringBufferInputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import org.xml.sax.SAXException;

import com.xl.model.StringItem;

import java.util.ArrayList;
import java.util.HashMap;

public class XmlToJson {
	public static String filename = "/mnt/sdcard/.aide/1.xml";

	private File file1, file2;
	private String text;

	public XmlToJson(String text) {
		this.text = text;
		/*
		 * DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		 * try
		 * {
		 * 
		 * DocumentBuilder domBuilder = domfac.newDocumentBuilder();
		 * InputStream is = new FileInputStream(new File(filename));
		 * InputStream strin = new FileInputStream(file1);
		 * 
		 * Document doc = domBuilder.parse(strin);
		 * 
		 * Element root = doc.getDocumentElement();
		 * System.out.println("tagname "+root.getTagName());
		 * NodeList books = root.getChildNodes();
		 * if(books!=null){
		 * for (int i = 0; i < books.getLength(); i++)
		 * {
		 * Node book = books.item(i);
		 * listNodes(book);
		 * /*
		 * if(book.getNodeType()==Node.ELEMENT_NODE) {
		 * //（7）取得节点的属性值
		 * //String email=book.getAttributes().getNamedItem("email").getNodeValue();
		 * //System.out.println(email);
		 * //注意，节点的属性也是它的子节点。它的节点类型也是Node.ELEMENT_NODE
		 * 
		 * //（8）轮循子节点
		 * for(Node node=book.getFirstChild();node!=null;node=node.getNextSibling()) {
		 * if(node.getNodeType()==Node.ELEMENT_NODE) {
		 * //大括号<>
		 * if(node.getNodeName().equals("name"))
		 * {
		 * String name=node.getNodeValue();
		 * String name1=node.getFirstChild().getNodeValue();
		 * System.out.println(name);
		 * System.out.println(name1);
		 * }
		 * if(node.getNodeName().equals("price")) {
		 * String price=node.getFirstChild().getNodeValue();
		 * System.out.println(price);
		 * }
		 * 
		 * }
		 * }
		 * } *
		 * }
		 * }
		 * }
		 * catch(SAXException e)
		 * {
		 * e.printStackTrace();
		 * }
		 * catch (Exception e)
		 * {
		 * // TODO Auto-generated catch block
		 * e.printStackTrace();
		 * }
		 */
	}

	// 检测并输出json
	public String check(String coding) // 检测file2中是否具有file1的东西
	{
		StringBuffer buf = new StringBuffer();
		JSONObject jsonObject = new JSONObject();
		ArrayList<String> nameList1 = null;

		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();

		try {

			DocumentBuilder domBuilder = domfac.newDocumentBuilder();

			// InputStream strin = new FileInputStream(file1);

			Document doc = domBuilder.parse(new ByteArrayInputStream(text.getBytes(coding)));

			Element root = doc.getDocumentElement();
			// Element root2 = doc2.getDocumentElement();
			// System.out.println("tagname "+root.getTagName());
			NodeList books = root.getChildNodes();
			// NodeList books2 = root2.getChildNodes();
			if (books != null)
				nameList1 = listNames(books);

			// buf.append("搜索到"+nameList1.size()+"项\n");
			// buf.append("搜索到"+nameList2.size()+"项\n");
			System.out.println("{");
			buf.append("{\n");
			for (int i = 0; i < nameList1.size(); i++) {
				String name = nameList1.get(i);
				System.out.println(name);

				if (i == nameList1.size() - 1) {
					buf.append("  " + name + "\n");
				} else {
					buf.append("  " + name + ",\n");
				}
			}
			System.out.println("}");
			buf.append("}\n");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return buf.toString();
	}

	// 将文字进行处理 主要是处理转义字符
	String toEscape(String text) {
		StringBuffer buffer = new StringBuffer();
		int type = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			switch (type) {
				case 0:
					if (c == '\\') {
						type = 1;
					} else if (c == '\"') {
						type = 2;
					} else {
						buffer.append(c);
					}
					break;
				case 1:
					if (c == '\'') {
						buffer.append(c);
					} else {
						buffer.append('\\');
						buffer.append(c);
					}
					type = 0;
					break;
				case 2:
					if (c != '\"') {
						buffer.append(c);
					} else {
						type = 0;
					}

					break;
				default:
					break;
			}

		}

		return buffer.toString();
	}

	// 转换string.xml里的转义字符为json
	String exStringToJSON(String text) {
		StringBuffer buffer = new StringBuffer();
		int type = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			switch (type) {
				case 0:
					if (c == '\\') {
						type = 1;
					} else if (c == '\"') {
						type = 2;
					} else if (c == '\n') {
						buffer.append("\\n");
					} else if (c == '\r') {
						buffer.append("\\r");
					} else if (c == '\t') {
						buffer.append("\\t");
					} else {
						buffer.append(c);
					}
					break;
				case 1:
					if (c == '\'') {
						buffer.append(c);
					} else {
						buffer.append('\\');
						buffer.append(c);
					}
					type = 0;
					break;
				case 2:
					if (c != '\"') {
						buffer.append(c);
					} else {
						type = 0;
					}

					break;
				case 3:

					break;
				default:
					break;
			}

		}

		return buffer.toString();
	}

	// 返回键值对数据
	ArrayList<String> listNames(NodeList nodelist) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node book = nodelist.item(i);

			// 节点是什么类型的节点
			if (book.getNodeType() == Node.ELEMENT_NODE) {
				// 判断是否是元素节点
				Element element = (Element) book;
				// System.out.println("——notename=" + element.getNodeName());
				// 判断此元素节点是否有属性
				if (element.hasAttributes()) {
					// 获取属性节点的集合
					NamedNodeMap namenm = element.getAttributes();// Node
					// 遍历属性节点的集合
					for (int k = 0; k < namenm.getLength(); k++) {
						// 获取具体的某个属性节点
						Attr attr = (Attr) namenm.item(k);
						if (attr.getNodeName().equals("name")) {
							list.add("\"" + attr.getNodeValue() + "\":" + "\"" + exStringToJSON(book.getTextContent())
									+ "\"");
							// System.out.println("添加"+attr.getNodeValue() + book.getTextContent()
							// +attr.getFirstChild().getNodeValue()+"成功"+list.size());
						} else {
							// list.add("未知:"+attr.getNodeName());
							// System.out.println("name:"+attr.getNodeName()
							// +" value:" +attr.getNodeValue()
							// +" type:"+attr.getNodeType());
						}
					}
				}
			}
		}
		return list;
	}

	public HashMap<String, String> getHashList(String coding) {
		StringBuffer buf = new StringBuffer();
		ArrayList<String> nameList1 = null;

		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();

		try {

			DocumentBuilder domBuilder = domfac.newDocumentBuilder();

			// InputStream strin = new FileInputStream(file1);

			Document doc = domBuilder.parse(new ByteArrayInputStream(text.getBytes(coding)));

			Element root = doc.getDocumentElement();
			// Element root2 = doc2.getDocumentElement();
			// System.out.println("tagname "+root.getTagName());
			NodeList books = root.getChildNodes();
			return getHashList(books);
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	}

	public ArrayList<StringItem> getStringList(String coding) {
		StringBuffer buf = new StringBuffer();
		ArrayList<String> nameList1 = null;

		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();

		try {

			DocumentBuilder domBuilder = domfac.newDocumentBuilder();

			// InputStream strin = new FileInputStream(file1);

			Document doc = domBuilder.parse(new ByteArrayInputStream(text.getBytes(coding)));

			Element root = doc.getDocumentElement();
			// Element root2 = doc2.getDocumentElement();
			// System.out.println("tagname "+root.getTagName());
			NodeList books = root.getChildNodes();
			return getStringList(books);
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	}

	// 返回一个哈希表数据
	HashMap<String, String> getHashList(NodeList nodelist) {
		HashMap<String, String> hashmap = new HashMap<>();
		// ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node book = nodelist.item(i);

			// 节点是什么类型的节点
			if (book.getNodeType() == Node.ELEMENT_NODE) {
				// 判断是否是元素节点
				Element element = (Element) book;
				// System.out.println("——notename=" + element.getNodeName());
				// 判断此元素节点是否有属性
				if (element.hasAttributes()) {
					// 获取属性节点的集合
					NamedNodeMap namenm = element.getAttributes();// Node
					// 遍历属性节点的集合
					for (int k = 0; k < namenm.getLength(); k++) {
						// 获取具体的某个属性节点
						Attr attr = (Attr) namenm.item(k);
						if (attr.getNodeName().equals("name")) {
							hashmap.put(attr.getNodeValue(), toEscape(book.getTextContent()));
							// list.add("\""+attr.getNodeValue()+"\":"+"\""+toEscape(
							// book.getTextContent())+"\"");
						} else {
							// list.add("未知:"+attr.getNodeName());
							System.out.println("未知：" + attr.getNodeName());
							// System.out.println("name:"+attr.getNodeName()
							// +" value:" +attr.getNodeValue()
							// +" type:"+attr.getNodeType());
						}
					}
				}
			}
		}
		return hashmap;
	}

	// 返回一个哈希表数据
	ArrayList<StringItem> getStringList(NodeList nodelist) {
		ArrayList<StringItem> hashmap = new ArrayList<StringItem>();
		// ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node book = nodelist.item(i);

			// 节点是什么类型的节点
			if (book.getNodeType() == Node.ELEMENT_NODE) {
				// 判断是否是元素节点
				Element element = (Element) book;
				// System.out.println("——notename=" + element.getNodeName());
				// 判断此元素节点是否有属性
				if (element.hasAttributes()) {
					// 获取属性节点的集合
					NamedNodeMap namenm = element.getAttributes();// Node
					// 遍历属性节点的集合
					for (int k = 0; k < namenm.getLength(); k++) {
						// 获取具体的某个属性节点
						Attr attr = (Attr) namenm.item(k);
						if (attr.getNodeName().equals("name")) {
							hashmap.add(new StringItem(attr.getNodeValue(), toEscape(book.getTextContent())));
							// list.add("\""+attr.getNodeValue()+"\":"+"\""+toEscape(
							// book.getTextContent())+"\"");
						} else {
							// list.add("未知:"+attr.getNodeName());
							System.out.println("未知：" + attr.getNodeName());
							// System.out.println("name:"+attr.getNodeName()
							// +" value:" +attr.getNodeValue()
							// +" type:"+attr.getNodeType());
						}
					}
				}
			}
		}
		return hashmap;
	}

	ArrayList<String> listString(NodeList nodelist) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node book = nodelist.item(i);

			// 节点是什么类型的节点
			if (book.getNodeType() == Node.ELEMENT_NODE) {
				// 判断是否是元素节点
				Element element = (Element) book;
				System.out.println("——notename=" + element.getNodeName());
				// 判断此元素节点是否有属性
				if (element.hasAttributes()) {
					// 获取属性节点的集合
					NamedNodeMap namenm = element.getAttributes();// Node
					// 遍历属性节点的集合
					for (int k = 0; k < namenm.getLength(); k++) {
						// 获取具体的某个属性节点
						Attr attr = (Attr) namenm.item(k);
						if (attr.getNodeName().equals("name")) {
							list.add(attr.getNodeValue());
							System.out.println("添加" + attr.getNodeValue() + book.getTextContent()
									+ attr.getFirstChild().getNodeValue() + "成功" + list.size());
						} else {
							list.add("未知:" + attr.getNodeName());
							System.out.println("name:" + attr.getNodeName()
									+ " value:" + attr.getNodeValue()
									+ "  type:" + attr.getNodeType());
						}
					}
				}
			}
		}
		return list;
	}

	ArrayList<String> listString(Node node) {
		ArrayList<String> list = new ArrayList<String>();
		// 节点是什么类型的节点
		if (node.getNodeType() == Node.ELEMENT_NODE) {// 判断是否是元素节点
			Element element = (Element) node;
			System.out.println("——notename=" + element.getNodeName());
			// 判断此元素节点是否有属性
			if (element.hasAttributes()) {
				// 获取属性节点的集合
				NamedNodeMap namenm = element.getAttributes();// Node
				// 遍历属性节点的集合
				for (int k = 0; k < namenm.getLength(); k++) {
					// 获取具体的某个属性节点
					Attr attr = (Attr) namenm.item(k);
					if (attr.getNodeName().equals("name")) {
						list.add(attr.getNodeValue());
						System.out.println("添加" + attr.getNodeValue() + attr.getTextContent() + "成功" + list.size());
					} else {
						list.add("未知:" + attr.getNodeName());
						System.out.println("name:" + attr.getNodeName()
								+ " value:" + attr.getNodeValue()
								+ "  type:" + attr.getNodeType());
					}

				}
			}
			// 获取元素节点的所有孩子节点
			NodeList listnode = element.getChildNodes();
			// 遍历
			for (int j = 0; j < listnode.getLength(); j++) {
				// 得到某个具体的节点对象
				Node nd = listnode.item(j);
				System.out.println("name::" + nd.getNodeName()
						+ "  value:::"
						+ nd.getNodeValue()
						+ "  type:::" + nd.getNodeType());
				// 重新调用遍历节点的操作的方法
				listNodes(nd);
			}

		} else {
			System.out.println("......type=" + node.getNodeType() + " " + node.getBaseURI());
		}
		return list;
	}

	/**
	 * 遍历根据节点对象下面的所有的节点对象
	 * 
	 * @param node
	 */
	public void listNodes(Node node) {
		System.out.println("输出" + node.getNodeName());
		// 节点是什么类型的节点
		if (node.getNodeType() == Node.ELEMENT_NODE) {// 判断是否是元素节点
			Element element = (Element) node;
			System.out.println("——notename=" + element.getNodeName());
			// 判断此元素节点是否有属性
			if (element.hasAttributes()) {
				// 获取属性节点的集合
				NamedNodeMap namenm = element.getAttributes();// Node
				// 遍历属性节点的集合
				for (int k = 0; k < namenm.getLength(); k++) {
					// 获取具体的某个属性节点
					Attr attr = (Attr) namenm.item(k);
					System.out.println("name:" + attr.getNodeName() + " value:"
							+ attr.getNodeValue() + "  type:" + attr.getNodeType());
				}
			}
			// 获取元素节点的所有孩子节点
			NodeList listnode = element.getChildNodes();
			// 遍历
			for (int j = 0; j < listnode.getLength(); j++) {
				// 得到某个具体的节点对象
				Node nd = listnode.item(j);
				System.out.println("name::" + nd.getNodeName() + "  value:::"
						+ nd.getNodeValue() + "  type:::" + nd.getNodeType());
				// 重新调用遍历节点的操作的方法
				listNodes(nd);
			}

		} else {
			System.out.println("......type=" + node.getNodeType() + " " + node.getBaseURI());
		}
	}

	// 4.查询某个节点对象(简单列举一些案例)

	/**
	 * 根据标签的名称查找所有该名称的节点对象
	 */
	public void findNode(Document document) {
		// 根据标签名称获取该名称的所有节点对象
		NodeList nodelist = document.getElementsByTagName("teacher");
		// 遍历
		for (int i = 0; i < nodelist.getLength(); i++) {
			// 得到具体的某个节点对象
			Node node = nodelist.item(i);
			System.out.println(node.getNodeName());
		}
	}

	/**
	 * 根据属性的值 查询某个节点对象
	 * 属性值是唯一（假设）
	 * 
	 * @param document
	 * @param value
	 * @return
	 */
	public Node findNodeByAttrValue(Document document, String value) {
		// 根据标签名称获取该名称的节点对象集合
		NodeList nodelist = document.getElementsByTagName("teacher");
		// 遍历
		for (int i = 0; i < nodelist.getLength(); i++) {
			// 获取某个具体的元素节点对象
			Element node = (Element) nodelist.item(i);
			// 根据属性名称获取该节点的属性节点对象
			Attr attr = node.getAttributeNode("name");
			// 获取属性节点的值是否给指定的节点属性值相同
			if (attr.getNodeValue().equals(value)) {
				// 返回此节点
				return node;
			}
		}
		return null;
	}

	/**
	 * 根据id获取某个节点对象
	 * 
	 * @param document
	 * @param id
	 * @return
	 */
	public Node findNodeById(Document document, String id) {
		return document.getElementById(id);
	}

	// 5.删除指定的节点对象

	/**
	 * 删除某个节点对象
	 * 
	 * @param document
	 * @param id
	 * @throws TransformerException
	 */
	public void deleteNodeById(Document document, String id)
			throws TransformerException {
		// 获取删除的节点对象
		Node node = document.getElementById(id);
		// 是通过父节点调用removeChild(node)把子节点给删除掉
		Node node1 = node.getParentNode().removeChild(node);

		// 创建TransformerFactory对象
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		// Transformer类用于把代表XML文件的Document对象转换为某种格式后进行输出
		// Transformer对象通过TransformerFactory获得
		Transformer transformer = transformerFactory.newTransformer();
		// 把Document对象又重新写入到一个XML文件中。
		transformer.transform(new DOMSource(document), new StreamResult(
				new File("src//a.xml")));
	}

	// 6.更新某个节点对象
	/**
	 * 更新某个节点
	 * 
	 * @param document
	 * @param id
	 * @throws TransformerException
	 */
	public void updateNodeById(Document document, String id)
			throws TransformerException {
		// 根据id获取元素指定的元素节点对象
		Element node = document.getElementById(id);
		// 获取元素节点的id属性节点对象
		Attr attr = node.getAttributeNode("id");
		// 修改元素节点的属性值
		attr.setValue("x122");

		// 获取该节点对象的所有孩子节点对象name、age、sex节点
		NodeList nodelist = node.getChildNodes();
		// 遍历
		for (int i = 0; i < nodelist.getLength(); i++) {
			// 得到具体的节点对象
			Node n = nodelist.item(i);
			// 判断是否是元素节点对象
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				// 看是否是name节点
				if (n.getNodeName().equals("name")) {
					n.setTextContent("君君");// 修改其值
				} else if (n.getNodeName().equals("age")) {// 看看是否是age节点
					n.setTextContent("80");// 修改其值
				} else if (n.getNodeName().equals("sex")) {// 看看是否是sex节点
					n.setTextContent("男");// 修改其值
				} else {
					System.out.println("不做处理");
				}
			}
		}

		// 创建TransformerFactory对象
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		// Transformer类用于把代表XML文件的Document对象转换为某种格式后进行输出
		// Transformer对象通过TransformerFactory获得
		Transformer transformer = transformerFactory.newTransformer();
		// 把Document对象又重新写入到一个XML文件中。
		transformer.transform(new DOMSource(document), new StreamResult(
				new File("src//b.xml")));
	}

	// 7.在某个节点的下方添加新的节点

	/**
	 * 在指定的节点下方添加新得某个节点
	 * 
	 * @param document
	 * @param id
	 * @throws TransformerException
	 */
	public void addNodeById(Document document, String id)
			throws TransformerException {
		// 获取要添加位置节点的兄弟节点对象
		Element node = document.getElementById(id);
		// 获取其父节点对象
		Node parentNode = node.getParentNode();
		// 创建元素节点
		Element nd = document.createElement("student");
		// 设置元素节点的属性值
		nd.setAttribute("id", "x123");
		// 创建name元素节点
		Node name = document.createElement("name");
		// 设置name节点的文本值
		name.appendChild(document.createTextNode("陈红军"));
		// 创建age元素节点
		Node age = document.createElement("age");
		// 设置age节点的文本值
		age.appendChild(document.createTextNode("20"));
		// 创建sex元素节点
		Node sex = document.createElement("sex");
		// 设置sex节点的文本值
		sex.appendChild(document.createTextNode("男"));
		// 在nd节点中添加3个子节点
		nd.appendChild(name);
		nd.appendChild(age);
		nd.appendChild(sex);
		// 在父节点中添加nd节点
		parentNode.appendChild(nd);

		// 创建TransformerFactory对象
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		// Transformer类用于把代表XML文件的Document对象转换为某种格式后进行输出
		// Transformer对象通过TransformerFactory获得
		Transformer transformer = transformerFactory.newTransformer();
		// 把Document对象又重新写入到一个XML文件中。
		transformer.transform(new DOMSource(document), new StreamResult(
				new File("src//c.xml")));
	}

}

/*
 * <?xml version="1.0" encoding="GB2312" standalone="no"?>
 * <books>
 * <book email="zhoujunhui">
 * <name>rjzjh</name>
 * <price>jjjjjj</price>
 * </book>
 * </books>
 */
