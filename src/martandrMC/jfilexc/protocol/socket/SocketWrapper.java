package martandrMC.jfilexc.protocol.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketWrapper {
	
	private Socket socket;
	private SocketAddress socket_address;
	private DataInputStream input_stream;
	private DataOutputStream output_stream;
	
	public SocketWrapper(String address, int port) throws IOException {
		socket = new Socket();
		socket_address = new InetSocketAddress(InetAddress.getByName(address), port);
	}
	
	public void connect() throws IOException {
		socket.connect(socket_address);
		input_stream = new DataInputStream(socket.getInputStream());
		output_stream = new DataOutputStream(socket.getOutputStream());
	}
	
	public void close() throws IOException {
		input_stream.close();
		output_stream.close();
		socket.close();
	}
	
	public DataInputStream getInputStream() {return input_stream;}
	public DataOutputStream getOutputStream() {return output_stream;}
}
