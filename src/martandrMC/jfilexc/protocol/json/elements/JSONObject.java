package martandrMC.jfilexc.protocol.json.elements;

import java.util.HashMap;
import java.util.LinkedList;

public class JSONObject implements IJSONElement {
	
	private LinkedList<IJSONElement> children_list = new LinkedList<>(); //Field holding an ordered list of the children
	private HashMap<String, Integer> children_map = new HashMap<>(); //Field holding a hash-map from the child's name to their index in the list
	private String name; //Field holding the object's name
	
	//Constructors
	public JSONObject(String name) {this.name = name;} //Constructor to assign a value to the name field on initialization
	
	//Class-specific methods
	public JSONObject addChild(IJSONElement child) throws JSONChildException {
		if(children_map.containsKey(child.getElementName())) //If the name of the child is already used
			throw new JSONChildException(this, "cnt", child); //Throw an exception
		children_map.put(child.getElementName(), children_list.size()); //Associate the child's name with it's index in the list
		children_list.add(child); //Add the child to the list
		return this; //Returns itself in order for the user to stack this method as many times as needed
	}
	
	public boolean hasChild(String name) {return children_map.containsKey(name);} //Check if the name of this child has an association in the map
	public int getIndexOfChild(String name) throws JSONChildException {
		if(!children_map.containsKey(name))throw new JSONChildException(this, "cnf", name); //If the entered name is not found in the map, throw an exception
		return children_map.get(name); //Else return the integer associated with the name
	}
	public int getChildrenCount() {return children_list.size();} //Gets the size of the list of childs
	
	public IJSONElement getChildByName(String name) throws JSONChildException {return getChildAtIndex(getIndexOfChild(name));} //Gets the child with the specified name
	public IJSONElement getChildAtIndex(int index) {return children_list.get(index);} //Gets the child at the specified index
	
	//Methods implemented from IJSONElement
	@Override
	public String getMultiLineJSONString(boolean inclname, boolean comma) {
		/* (inclname?"\""+name+"\": ":"") asks whether the name of the object is needed.
		 * If it is needed, add quotes arround the name field and also a colon and space at the end.
		 * If it is not needed, dont add any data prefixes.
		 * 
		 * (comma?",\n":"") does the same thing but about the need for a separator between sibling data
		 * If a separator is required, add a comma and a new-line character.
		 * If it's not required, don't add any data suffixes.
		 */
		if(getChildrenCount() == 0) //If this object doesn't have children
			return (inclname?"\""+name+"\": ":"")+"{}"+(comma?",\n":""); //Return an empty object wit optional name and separator
		String data = "{\n"; //Initialize the data string with an opening brace and a new-line
		for(int i=0;i<children_list.size();i++) { //For every child in the list
			/* cdat (Child Data) is initialized by getting the child at the current address of interest,
			 * and running its multi-line-json-string-get method (recursion) with the parameters:
			 * - Child name is requested,
			 * - Separator is requested as long as our child of interest is not the last one in the list
			 */
			String cdat = children_list.get(i).getMultiLineJSONString(true, i<children_list.size()-1);
			String[] cdatarr = cdat.split("\n"); //cdatarr (Child Data Array) is initialized as the array obtained from spliting cdat at the new-lines
			/* For every index in the cdatarr,
			 * append to data the current index from cdatarr, prefixed with a tab character
			 * and suffixed with a new-line that the split method took out previously
			 */
			for(int j=0;j<cdatarr.length;j++) data += "\t"+cdatarr[j]+"\n";
		}
		data += "}"+(comma?",\n":""); //Apend to data a closing brace and an optional separator
		return (inclname?"\""+name+"\": ":"")+data; //Return the data prefixed with an optional name
	}
	
	@Override
	public String getOneLineJSONString(boolean inclpname, boolean comma) {	
		return getMultiLineJSONString(inclpname, comma).replaceAll("\\s", ""); //Get the multi-line json string and remove all whitespaces
	}
	
	@Override
	public String getElementName() {return name;} //Get the object's name
	
	@Override
	public String getElementType() {return "obj";} //Get the type of JSON element
}
