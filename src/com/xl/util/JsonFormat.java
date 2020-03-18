package com.xl.util;
public class JsonFormat {
    /**
     * 格式化
     * 
     * @param jsonStr
     * @return
     * @author lizhgb
     * @Date 2015-10-14 下午1:17:35
     * @Modified 2017-04-28 下午8:55:35
     */
    public static String formatJson(String jsonStr) {
    	
        if (null == jsonStr || "".equals(jsonStr))
            return "";
        jsonStr = jsonStr.replace(" ","");
		jsonStr = jsonStr.replace("\t","");

        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        boolean isInQuotationMarks = false;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
				case '"':
					if (last != '\\'){
						isInQuotationMarks = !isInQuotationMarks;
					}
					sb.append(current);
					break;
				case '{':
				case '[':
					sb.append(current);
					if (!isInQuotationMarks) {
						sb.append('\n');
						indent++;
						addIndentBlank(sb, indent);
					}
					break;
				case '}':
				case ']':
					if (!isInQuotationMarks) {
						sb.append('\n');
						indent--;
						addIndentBlank(sb, indent);
					}
					sb.append(current);
					break;
				case ',':
					sb.append(current);
					if (last != '\\' && !isInQuotationMarks) {
						sb.append('\n');
						addIndentBlank(sb, indent);
					}
					break;
				case ' ':
					break;
				case '\n':
					break;
				default:
				    if(current!='\n')
					sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space
     * 
     * @param sb
     * @param indent
     * @author lizhgb
     * @Date 2015-10-14 上午10:38:04
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
    }
/*
    public static    void ifgetJson(String reqjson) throws JsonException{
        try {
            JSONObject jsonObject = JSONObject.fromObject(reqjson);
        } catch (JSONException e) {
            throw new JsonException("进件系统请求json串，不是正规json格式，请求json串为=="+reqjson);

        }
    }*/
	
	public static void main(String arr[]){
		System.out.println( JsonFormat.formatJson("{main:\"\",list:[],code:200}"));
	}
}
