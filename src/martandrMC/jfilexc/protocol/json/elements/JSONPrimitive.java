package martandrMC.jfilexc.protocol.json.elements;

public class JSONPrimitive implements IJSONElement {
	
	private String name, type, value_str; //Fields holding the name and type of the primitive and the data for str (and null) type primitives
	private int value_int; //Field holding the integer data for the int type primitives
	private boolean value_bool; //Field holding the boolean data for the bool type primitives
	
	//Constructors
	public JSONPrimitive(String name, String value) { //Constructor for strings and nulls
		this.name = name; //Asign name to field
		value_str = value; //Asign data to field
		if(value == null) type = "null"; //If data is null set type to null
		else type = "str"; //Else set type to str
	}
	
	public JSONPrimitive(String name, boolean value) { //Constructor for booleans
		this.name = name; //Asign name to field
		value_bool = value; //Asign data to field
		type = "bool"; //Set type to bool
	}
	
	public JSONPrimitive(String name, int value) { //Constructor for integers
		this.name = name; //Asign name to field
		value_int = value; //Asign data to field
		type = "int"; //Set type to int
	}
	
	//Class-specific methods
	public String getPrimitiveType() {return type;} //Returns the type of data the primitive object is holding
	
	//Methods implemented from IJSONElement
	@Override
	public String getMultiLineJSONString(boolean inclname, boolean comma) {
		switch(type) {
			/* (inclname?"\""+name+"\": ":"") asks whether the name of the primitve is needed.
			 * If it is needed, add quotes arround the name field and also a colon and space at the end.
			 * If it is not needed, dont add any data prefixes.
			 * 
			 * (comma?",\n":"") does the same thing but about the need for a separator between sibling data
			 * If a separator is required, add a comma and a new-line character.
			 * If it's not required, don't add any data suffixes.
			 */
			case "null": return (inclname?"\""+name+"\": ":"")+"null"+(comma?",\n":""); //Aalways add "null"
			case "str":	return (inclname?"\""+name+"\": ":"")+"\""+value_str+"\""+(comma?",\n":""); //Get the string data field
			case "int":	return (inclname?"\""+name+"\": ":"")+String.valueOf(value_int)+(comma?",\n":""); //Parse the int data field to string
			case "bool": return (inclname?"\""+name+"\": ":"")+(value_bool?"true":"false")+(comma?",\n":""); //If boolean data field is true add "true", else add "false"
		}
		return ""; //If the primitive has invalid type, return an empty string
	}

	@Override
	public String getOneLineJSONString(boolean inclpname, boolean comma) {
		return getMultiLineJSONString(inclpname, comma).replaceAll("\\s" , ""); //Get the multi-line json string and remove all whitespaces
	}
	
	@Override
	public String getElementName() {return name;} //Get the primitive's name
	
	@Override
	public String getElementType() {return "prim";} //Get the type of JSON element
}
