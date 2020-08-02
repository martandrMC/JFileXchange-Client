package martandrMC.jfilexc.protocol.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketInterface implements Runnable {
	
	private Socket socket;
	private SocketAddress socket_address;
	private DataInputStream input_stream;
	private DataOutputStream output_stram;
	
	public SocketInterface(String address, int port) throws IOException {
		socket = new Socket();
		socket_address = new InetSocketAddress(InetAddress.getByName(address), port);
		input_stream = new DataInputStream(socket.getInputStream());
		output_stram = new DataOutputStream(socket.getOutputStream());
	}
	
	@Override
	public void run() {
		while(true) {
			
		}
	}

	public SocketAddress getSocketAddress() {return socket_address;}
	public DataInputStream getInputStream() {return input_stream;}
	public DataOutputStream getOutputStram() {return output_stram;}
}
