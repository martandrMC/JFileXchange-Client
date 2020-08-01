package martandrMC.jfilexc.protocol.json.elements;

public interface IJSONElement {
	public String getOneLineJSONString(boolean inclname, boolean comma);
	public String getMultiLineJSONString(boolean inclname, boolean comma);
	public String getElementName();
	public String getElementType();
}
