package martandrMC.jfilexc.protocol.socket.packet;

public class ConversionUtils {

	public static String bArrToHexStr(byte[] arr) {
		String buff = "";
		for(byte b:arr) {
			buff += nibbleToHexChr((byte)(b>>4));
			buff += nibbleToHexChr((byte)(b&15));
		}
		return buff;
	}
	
	public static char nibbleToHexChr(byte nibble) {
		if(nibble >= 0 && nibble <= 9) return (char)(nibble + 48);
		else if(nibble >= 10 && nibble <= 15) return (char)(nibble +55);
		else return '#';
	}
}
