# ExcelConversion
一个java写的excel转多语言翻译的工具，目前已支持安卓的strings.xml与excel互转

1. 支持多语言转换，将excel转换为多版本的strings.xml
2. 支持将多个语言的strings.xml转换为一个excel表格
3. 支持将strings.xml转换为json格式，用于Flutter或其它游戏项目

## 应用场景：
当你需要将翻译文件交给其他人翻译APP时，将一份待翻译的excel交给他，然后将翻译之后的excel转换为APP所需的代码文件。  

## 使用方法
本工具使用命令行运行  
将strings.xml转换为excel

```
java -jar D:\\app\\Excal.jar -t xml2xls -i values\strings.xml -o output.xls -coding UTF-8
```

将多个strings.xml转换为excel
```
java -jar D:\\app\\Excal.jar -t xml2xls -i values-zh\strings.xml values\strings.xml -o output.xls -coding UTF-8
```

将excel转换为strings.xml
```
java -jar ExcalConversion.jar -t xls2xml -i test.xls -o strings.xml -coding UTF-8
```

将strings.xml转json
```
java -jar ExcelConversion.jar -t xmlToJson -i values\\strings.xml -o locale\\i18n_en.json
```

## 命令行说明  

  -t 判断输入的类型 取值如下  
    xmlToJson 将xml转json  
    xml2xls 将xml转xls  
    xls2xml 将xls转xml  
 -f 输入的文件  
 -o 输出的文件  
 -coding 文件的文本编码 默认utf-8  

