package martandrMC.jfilexc.protocol.socket.packet;

public class Packet {
	
	private static final short POLY = 0x1021;
	public static final int INI = 1<<7, FIN = 1<<6, PLD = 1<<5, CHK = 1<<4, AID = 1<<3, PID = 1<<2, RSD = 1<<1, ACK = 1<<0;
	
	private int flags;
	
	public Packet(int flags) {
		this.flags = flags;
	}
	
	public static int crc16(String data) {
		byte[] buffer = new byte[data.length()];
		for(int i=0;i<data.length();i++) buffer[i] = (byte)(data.charAt(i)&255);
		
        int crc = 0;
        for(byte b:buffer) for(int i=0;i<8;i++) {
        	boolean dp = ((b>>7-i)&1) == 1;
        	boolean cp = ((crc>>15)&1) == 1;
        	crc <<= 1;
        	if (dp^cp) crc ^= POLY;
        }
        crc &= (1<<16)-1;
        return crc;
	}
}
