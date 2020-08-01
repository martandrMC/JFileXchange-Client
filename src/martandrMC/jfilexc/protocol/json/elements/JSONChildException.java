package martandrMC.jfilexc.protocol.json.elements;

public class JSONChildException extends Exception {
	
	private static final long serialVersionUID = 4276972210313081425L; //Serialization extravaganza
	
	private IJSONElement child, caller; //Fields holding references to the invalid child and the object that rejected it
	private String error_type, child_name; //Fields holding the error type and depending on the constructor called, the rejected child's name
	
	public JSONChildException(IJSONElement caller, String error_type, IJSONElement child) {
		super(); //Call superclass
		this.error_type = error_type; //Asign error type to field
		this.caller = caller; //Asign a caller reference to field
		this.child = child; //Assign a child reference to field
	}
	
	public JSONChildException(IJSONElement caller, String error_type, String child_name) {
		super(); //Call superclass
		this.error_type = error_type; //Asign error type to field
		this.caller = caller; //Asign a caller reference to field
		this.child_name = child_name; //Asign a child name to field
	}
	
	public IJSONElement getChild() {return child;} //Get the referenced child
	public IJSONElement getCaller() {return caller;} //Get the referenced caller
	public String getChildName() {return child_name;} //Get the child's name
	public String getErrorType() {return error_type;} //Get the error type
}
