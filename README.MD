# ExcalConversion
һ��javaд��excelת�����Է���Ĺ��ߣ�Ŀǰ��֧�ְ�׿��strings.xml��excel��ת
## Ӧ�ó�����
������Ҫ�������ļ����������˷���APPʱ����һ�ݴ������excel��������Ȼ�󽫷���֮���excelת��ΪAPP����Ĵ����ļ���  

## ʹ�÷���
������ʹ������������
��strings.xmlת��Ϊexcel
```
java -jar D:\\app\\Excal.jar -t xml2xls -i values\strings.xml -o output.xls -coding UTF-8
```
-  
�����strings.xmlת��Ϊexcel
```
java -jar D:\\app\\Excal.jar -t xml2xls -i values-zh\strings.xml values\strings.xml -o output.xls -coding UTF-8
```
-  
��excelת��Ϊstrings.xml
������
java -jar ExcalConversion.jar -t xls2xml -i test.xls -o strings.xml -coding UTF-8
������

## ������˵��
  -t �ж���������� ȡֵ����
    xmlToJson ��xmlתjson
    xml2xls ��xmlתxls
    xls2xml ��xlsתxml
 -f ������ļ�
 -o ������ļ�
 -coding �ļ����ı����� Ĭ��utf-8
 