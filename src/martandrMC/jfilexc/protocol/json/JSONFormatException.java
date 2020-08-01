package martandrMC.jfilexc.protocol.json;

public class JSONFormatException extends Exception {
	
	private static final long serialVersionUID = -415150617065671149L; //Serialization extravaganza
	
	private String error_type, json; //Fields holding th error type and the invalid json string
	
	public JSONFormatException(String error_type, String json) {
		super(); //Call superclass
		this.error_type = error_type; //Asign error type to field
		this.json = json; //Asign invalid json string to field
	}
	
	//Getters
	public String getErrorType() {return error_type;} //Get the error type
	public String getJSON() {return json;} //Get the invalid json
}
