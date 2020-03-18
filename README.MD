# ExcalConversion
一个java写的excel转多语言翻译的工具，目前已支持安卓的strings.xml与excel互转
## 应用场景：
当你需要将翻译文件交给其他人翻译APP时，将一份待翻译的excel交给他，然后将翻译之后的excel转换为APP所需的代码文件。  

## 使用方法
本工具使用命令行运行
将strings.xml转换为excel
```
java -jar D:\\app\\Excal.jar -t xml2xls -i values\strings.xml -o output.xls -coding UTF-8
```
-  
将多个strings.xml转换为excel
```
java -jar D:\\app\\Excal.jar -t xml2xls -i values-zh\strings.xml values\strings.xml -o output.xls -coding UTF-8
```
-  
将excel转换为strings.xml
···
java -jar ExcalConversion.jar -t xls2xml -i test.xls -o strings.xml -coding UTF-8
···

## 命令行说明
  -t 判断输入的类型 取值如下
    xmlToJson 将xml转json
    xml2xls 将xml转xls
    xls2xml 将xls转xml
 -f 输入的文件
 -o 输出的文件
 -coding 文件的文本编码 默认utf-8
 