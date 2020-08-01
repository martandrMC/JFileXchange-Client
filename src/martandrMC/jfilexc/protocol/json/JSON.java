package martandrMC.jfilexc.protocol.json;

import martandrMC.jfilexc.protocol.json.elements.IJSONElement;
import martandrMC.jfilexc.protocol.json.elements.JSONArray;
import martandrMC.jfilexc.protocol.json.elements.JSONChildException;
import martandrMC.jfilexc.protocol.json.elements.JSONObject;
import martandrMC.jfilexc.protocol.json.elements.JSONPrimitive;

public class JSON {
	
	public static JSONObject parseObj(String json) throws JSONFormatException, JSONChildException {return JSONParser.parseObject(json, false);}
	public static JSONArray parseArr(String json) throws JSONFormatException, JSONChildException {return JSONParser.parseArray(json, false);}
	
	public static String getJSONString(IJSONElement json, boolean with_name) {return json.getMultiLineJSONString(with_name, false);}
	public static String getMinifiedJSONString(IJSONElement json, boolean with_name) {return json.getOneLineJSONString(with_name, false);}
	
	public static JSONArray packageArray(String[] array, String array_name) throws JSONChildException {
		JSONArray jarr = new JSONArray(array_name);
		for(String s:array) jarr.addChild(new JSONPrimitive(null, s));
		return jarr;
	}
	
	public static JSONArray packageArray(int[] array, String array_name) throws JSONChildException {
		JSONArray jarr = new JSONArray(array_name);
		for(int i:array) jarr.addChild(new JSONPrimitive(null, i));
		return jarr;
	}
	
	public static JSONArray packageArray(boolean[] array, String array_name) throws JSONChildException {
		JSONArray jarr = new JSONArray(array_name);
		for(boolean b:array) jarr.addChild(new JSONPrimitive(null, b));
		return jarr;
	}
	
	public static JSONArray packageArray(int null_ammount, String array_name) throws JSONChildException {
		JSONArray jarr = new JSONArray(array_name);
		for(int i=0;i<null_ammount;i++) jarr.addChild(new JSONPrimitive(null, null));
		return jarr;
	}
}
