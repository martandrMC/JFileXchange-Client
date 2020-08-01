package martandrMC.jfilexc.protocol.json;

import martandrMC.jfilexc.protocol.json.elements.JSONArray;
import martandrMC.jfilexc.protocol.json.elements.JSONChildException;
import martandrMC.jfilexc.protocol.json.elements.JSONObject;
import martandrMC.jfilexc.protocol.json.elements.JSONPrimitive;

public class JSONParser {
	
	public static final String str_regex = "\"[A-Za-z0-9\\-_]+\""; //Matches a series of more than one characters surrounded by quotes
	public static final String num_regex = "-?[0-9]+"; //Matches a series of more than one number optionaly prefixed with a minus sign
	
	private static final String prim_named_regex = "^"+str_regex+":(true|false|null|"+num_regex+"|"+str_regex+"),?$";
	private static final String prim_nameless_regex = "^(true|false|null|"+num_regex+"|"+str_regex+"),?$";
	
	private static final String obj_named_regex = "^"+str_regex+":\\{.*?\\},?$";
	private static final String obj_nameless_regex = "^\\{.*?\\},?$";
	
	private static final String arr_named_regex = "^"+str_regex+":\\[.*?\\],?$";
	private static final String arr_nameless_regex = "^\\[.*?\\],?$";
	
	private static enum State {NEW,OBJ,ARR,DONE}; //State enum for the parser state machine
	
	/**
	 * Parses a JSON primitive from the input string.
	 * @param json JSON code containing the primitive.
	 * @param withname Whether to expect the primitve preffixed by a name or not.
	 * @return Returns a JSONPrimitive object containing the extracted name and value from the JSON string.
	 * @throws JSONFormatException Thrown when an invalid primitive format or integer overflow is found.
	 */
	public static JSONPrimitive parsePrimitive(String json, boolean withname) throws JSONFormatException {
		//Error Check
		json = json.replaceAll("\\s", ""); //Remove whitespace
		String match_remains = json.replaceAll(withname?prim_named_regex:prim_nameless_regex, "#"); //Replace a correctly matched primitive with a hashtag
		if(!match_remains.equals("#")) //If it didn't match throw error
			throw new JSONFormatException("pf", json);
		//Organise
		if(json.endsWith(",")) json = json.substring(0, json.length()-1); //Remove trailing comma
		String[] split_json = json.split(":"); //Split the json to name and data part
		String data = split_json[withname?1:0]; //Get out the data string
		String name = withname?split_json[0].substring(1, split_json[0].length()-1):null; //Get out the name string
		//Parse
		switch(data) { //Special literal keyword matches
			case "true": return new JSONPrimitive(name, true);
			case "false": return new JSONPrimitive(name, false);
			case "null": return new JSONPrimitive(name, null);
		}
		try {
			int num = Integer.parseInt(data); //Try parsing out int
			return new JSONPrimitive(name, num);
		}catch (NumberFormatException e) {
			if(data.replaceAll("-?[0-9]+", "").isEmpty()) //If integer parse fails check for unparsable number
				throw new JSONFormatException("pi", json);
			else return new JSONPrimitive(name, data.substring(1, data.length()-1)); //Else parse as string
		}
	}
	
	/**
	 * Parses a JSON object from the input string recursively.
	 * @param json JSON code containing the object.
	 * @param withname Whether to expect the object preffixed by a name or not.
	 * @return Returns a JSONObject object containing the extracted name and children from the JSON string.
	 * @throws JSONFormatException Thrown when an invalid object format is found.
	 * @throws JSONChildException Thrown when duplicate children names are found.
	 */
	public static JSONObject parseObject(String json, boolean withname) throws JSONFormatException, JSONChildException {
		//Error Check
		json = json.replaceAll("\\s", ""); //Remove whitespace
		String match_remains = json.replaceAll(withname?obj_named_regex:obj_nameless_regex, "#"); //Replace a correctly matched object with a hashtag
		if(!match_remains.equals("#")) //If it didn't match throw error
			throw new JSONFormatException("of", json);
		//Organise
		if(json.endsWith(",")) json = json.substring(0, json.length()-1); //Remove trailing comma
		String[] split_json = withname?json.replaceFirst(":", "\n").split("\n"):new String[] {json}; //Split the json to name and data part
		String data = split_json[withname?1:0].substring(1, split_json[withname?1:0].length()-1); //Get out the data string
		String name = withname?split_json[0].substring(1, split_json[0].length()-1):null; //Get out the name string
		//Parse
		JSONObject output = new JSONObject(name); //Initialize output object
		int chcnt = 0, brcnt = 1, type = 0; //Initialize character counter, bracket counter and type memory variables
		String buff = ""; //Initialize child separation buffer
		State state = State.NEW; //Initialize at state NEW
		while(chcnt <= data.length()) { //For each character in data +1 more
			char c = '#'; //Initialize current character with placeholder
			if(chcnt < data.length()) c = data.charAt(chcnt); //If not in the last round get from data the character at index chcnt
			if(state == State.NEW) { //If state is at NEW
				if(c == ',' || chcnt == data.length()-1) { //If current character is comma or at second-to-last round...
					state = State.DONE; //Set state at DONE
					type = 0; //Type memory set to primitive mode
				}
				else if(c == '{') state = State.OBJ; //If current character is opening curly brace, set state at OBJ
				else if(c == '[') state = State.ARR; //If current character is opening square bracket, set state at ARR
				buff += c; //Add current character to buffer
				chcnt++; //Increment character counter
			}else if(state == State.OBJ) { //If state is at OBJ
				if(c == '{') brcnt++; //If current character is opening curly brace, increment the bracket counter
				else if(c == '}') brcnt--; //If current character is closing curly brace, decrement the bracket counter
				if((c == ',' && brcnt == 0) || chcnt == data.length()-1) { //If current character is comma and bracket counter is 0 or at second-to-last round...
					state = State.DONE; //Set state at DONE
					type = 1; //Type memory set to object mode
				}
				buff += c; //Add current character to buffer
				chcnt++; //Increment character counter
			}else if(state == State.ARR) { //If state is at ARR
				if(c == '[') brcnt++; //If current character is opening square bracket, increment the bracket counter
				else if(c == ']') brcnt--; //If current character is closing square bracket, decrement the bracket counter
				if((c == ',' && brcnt == 0) || chcnt == data.length()-1) { //If current character is comma and bracket counter is 0 or at second-to-last round...
					state = State.DONE; //Set state at DONE
					type = 2; //Type memory set to array mode
				}
				buff += c; //Add current character to buffer
				chcnt++; //Increment character counter
			}else if(state == State.DONE) { //If state is at DONE
				if(type == 0) output.addChild(parsePrimitive(buff, true)); //If type memory at primitive mode parse buffer as named primitive
				else if(type == 1) output.addChild(parseObject(buff, true)); //If type object at primitive mode parse buffer as named object
				else if(type == 2) output.addChild(parseArray(buff, true)); //If type memory at array mode parse buffer as named array
				state = State.NEW; //Set state at NEW
				buff = ""; //Reset buffer
				brcnt = 1; //Reset bracket counter
			}
		}
		return output;
	}
	
	/**
	 * Parses a JSON array from the input string recursively.
	 * @param json JSON code containing the array.
	 * @param withname Whether to expect the array preffixed by a name or not.
	 * @return Returns a JSONArray object containing the extracted name and children from the JSON string.
	 * @throws JSONFormatException Thrown when an invalid array format is found.
	 * @throws JSONChildException Thrown when duplicate (grand)children names are found.
	 */
	public static JSONArray parseArray(String json, boolean withname) throws JSONFormatException, JSONChildException {
		//Error Check
		json = json.replaceAll("\\s", ""); //Remove whitespace
		String match_remains = json.replaceAll(withname?arr_named_regex:arr_nameless_regex, "#"); //Replace a correctly matched object with a hashtag
		if(!match_remains.equals("#"))  //If it didn't match throw error
			throw new JSONFormatException("af", json);
		//Organise
		if(json.endsWith(",")) json = json.substring(0, json.length()-1); //Remove trailing comma
		String[] split_json = withname?json.replaceFirst(":", "\n").split("\n"):new String[] {json}; //Split the json to name and data part
		String data = split_json[withname?1:0].substring(1, split_json[withname?1:0].length()-1); //Get out the data string
		String name = withname?split_json[0].substring(1, split_json[0].length()-1):null; //Get out the name string
		//Parse
		JSONArray output = new JSONArray(name); //Initialize output array
		int chcnt = 0, brcnt = 1, type = 0; //Initialize character counter, bracket counter and type memory variables
		String buff = ""; //Initialize child separation buffer
		State state = State.NEW; //Initialize at state NEW
		while(chcnt <= data.length()) { //For each character in data +1 more
			char c = '#'; //Initialize current character with placeholder
			if(chcnt < data.length()) c = data.charAt(chcnt); //If not in the last round get from data the character at index chcnt
			if(state == State.NEW) { //If state is at NEW
				if(c == ',' || chcnt == data.length()-1) { //If current character is comma or at second-to-last round...
					state = State.DONE; //Set state at DONE
					type = 0; //Type memory set to primitive mode
				}
				else if(c == '{') state = State.OBJ; //If current character is opening curly brace, set state at OBJ
				else if(c == '[') state = State.ARR; //If current character is opening square bracket, set state at ARR
				buff += c; //Add current character to buffer
				chcnt++; //Increment character counter
			}else if(state == State.OBJ) { //If state is at OBJ
				if(c == '{') brcnt++; //If current character is opening curly brace, increment the bracket counter
				else if(c == '}') brcnt--; //If current character is closing curly brace, decrement the bracket counter
				if((c == ',' && brcnt == 0) || chcnt == data.length()-1) { //If current character is comma and bracket counter is 0 or at second-to-last round...
					state = State.DONE; //Set state at DONE
					type = 1; //Type memory set to object mode
				}
				buff += c; //Add current character to buffer
				chcnt++; //Increment character counter
			}else if(state == State.ARR) { //If state is at ARR
				if(c == '[') brcnt++; //If current character is opening square bracket, increment the bracket counter
				else if(c == ']') brcnt--; //If current character is closing square bracket, decrement the bracket counter
				if((c == ',' && brcnt == 0) || chcnt == data.length()-1) { //If current character is comma and bracket counter is 0 or at second-to-last round...
					state = State.DONE; //Set state at DONE
					type = 2; //Type memory set to array mode
				}
				buff += c; //Add current character to buffer
				chcnt++; //Increment character counter
			}else if(state == State.DONE) { //If state is at DONE
				if(type == 0) output.addChild(parsePrimitive(buff, false)); //If type memory at primitive mode parse buffer as nameless primitive
				else if(type == 1) output.addChild(parseObject(buff, false)); //If type object at primitive mode parse buffer as nameless object
				else if(type == 2) output.addChild(parseArray(buff, false)); //If type memory at array mode parse buffer as nameless array
				state = State.NEW; //Set state at NEW
				buff = ""; //Reset buffer
				brcnt = 1; //Reset bracket counter
			}
		}
		return output;
	}
}
