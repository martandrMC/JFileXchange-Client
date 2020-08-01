package martandrMC.jfilexc.protocol.json.elements;

import java.util.LinkedList;

public class JSONArray implements IJSONElement {
	
	private LinkedList<IJSONElement> children_list = new LinkedList<>(); //Field holding an ordered list of the children
	public String name, ptype, type = "";  //Fields holding the array's name and type (and primitive subtype)
	
	//Constructors
	public JSONArray(String name) {this.name = name;} //Constructor to assign a value to the name field on initialization
	
	//Class-specific methods
	public JSONArray addChild(IJSONElement child) throws JSONChildException {
		boolean error_flag = false; //Initialize error state as false
		if(type.isEmpty()) { //If the array has not been given a type yet
			type = child.getElementType(); //Asign the array the type of the child being added
			if(type.equals("prim")) ptype = ((JSONPrimitive)child).getPrimitiveType(); //If type equals prim also assign the primitive subtype
			children_list.add(child); //Add child to the list
		}else { //Else if the array has a state
			if(type.equals("prim") && child.getElementType().equals("prim")) { //If the child and array types match as prim
				if(ptype.equals(((JSONPrimitive)child).getPrimitiveType())) //Check if the primitive subtypes match
					children_list.add(child); //If they do, add the child to the list
				else error_flag = true; //Else set the error flag
			}else if(type.equals(child.getElementType())) //If the child and array types match but aren't prim 
				children_list.add(child); //Add child to the list
			else error_flag = true; //If types dont match, set the error flag
		}
		if(error_flag) throw new JSONChildException(this, "cti", child); //If the error flag is set, throw an exception
		return this; //Returns itself in order for the user to stack this method as many times as needed
	}
	
	public int getChildCount() {return children_list.size();} //Gets the size of the list of childs
	public IJSONElement getChildAtIndex(int index) {return children_list.get(index);} //Gets the child at the specified index
	
	//Methods implemented from IJSONElement
	@Override
	public String getMultiLineJSONString(boolean inclpname, boolean comma) {
		/* (inclname?"\""+name+"\": ":"") asks whether the name of the array is needed.
		 * If it is needed, add quotes arround the name field and also a colon and space at the end.
		 * If it is not needed, dont add any data prefixes.
		 * 
		 * (comma?",\n":"") does the same thing but about the need for a separator between sibling data
		 * If a separator is required, add a comma and a new-line character.
		 * If it's not required, don't add any data suffixes.
		 */
		if(getChildCount() == 0) //If this array doesn't have children
			return (inclpname?"\""+name+"\": ":"")+"[]"+(comma?",\n":""); //Return an empty array wit optional name and separator
		String data = "[" + (type.equals("prim")?"":"\n"); //Initialize the data string with an opening bracket and a new-line only if the array type is not prim
		for(int i=0;i<children_list.size();i++) { //For every child in the list
			/* cdat (Child Data) is initialized by getting the child at the current address of interest,
			 * and running its multi-line-json-string-get method (recursion) with the parameters:
			 * - Child name is not requested,
			 * - Separator is requested as long as our child of interest is not the last one in the list
			 */
			String cdat = children_list.get(i).getMultiLineJSONString(false, i<children_list.size()-1);
			String[] cdatarr = cdat.split("\n"); //cdatarr (Child Data Array) is initialized as the array obtained from spliting cdat at the new-lines
			/* For every index in the cdatarr,
			 * append to data the current index from cdatarr, prefixed with a tab character only if the array type is not prim
			 * and suffixed with a new line only if the array type is not prim or a space as long as the current line is not the last in cdatarr
			 */
			for(int j=0;j<cdatarr.length;j++) data += (type.equals("prim")?"":"\t")+cdatarr[j]+(type.equals("prim")?(i<children_list.size()-1?" ":""):"\n");
		}
		data += "]"+(comma?",\n":""); //Apend to data a closing bracket and an optional separator
		return (inclpname?"\""+name+"\": ":"")+data; //Return the data prefixed with an optional name
	}
	
	@Override
	public String getOneLineJSONString(boolean inclpname, boolean comma) {	
		return getMultiLineJSONString(inclpname, comma).replaceAll("\\s", ""); //Get the multi-line json string and remove all whitespaces
	}
	
	@Override
	public String getElementName() {return name;} //Get the array name
	
	@Override
	public String getElementType() {return "arr";} //Get the type of JSON element
}
