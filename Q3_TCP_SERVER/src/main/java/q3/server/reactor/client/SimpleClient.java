package q3.server.reactor.client;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A simple client implementation
 */
public class SimpleClient {

    protected String _host;
    protected int _port;
    protected Socket _socket;

    /**
     * Creates a new SimpleClient object
     * @param host the server's host
     * @param port the server's port
     */
    public SimpleClient(String host, int port) {
        _host = host;
        _port = port;
    }

    /**
     * Connects to the server
     * @throws IOException in case of a connection failure
     */
    public void connect() throws IOException {
        _socket = new Socket(_host, _port);
    }

    /**
     * Disconnects from the server
     * @throws IOException in the case of a disconnection failure
     */
    public void disconnect() throws IOException {
        _socket.close();
    }

    /**
     * Sends bytes to the server
     * @param bytes to send
     * @throws IOException in the case of sending failure
     */
    public void send(byte[] bytes) throws IOException {
        _socket.getOutputStream().write(bytes);
        _socket.getOutputStream().flush();
    }

    /**
     * Receives information from the server
     * @return the received message, or null of no message received
     * @throws IOException in the case of reception failure
     */
    public String receive() throws IOException {
        byte []buff = new byte[8192];
        int nBytes = _socket.getInputStream().read(buff);
        if (nBytes>0) {
            return new String(buff, 0, nBytes);
        }
        else {
            return null;
        }
    }

    public static void main(String args[]) {

//        if (args.length!=2) {
//            System.err.println("Usage: java SimpleClient <host> <port>");
//            System.exit(1);
//        }
    	String host = "127.0.0.1";
    	int port = 18000;
        try {
            SimpleClient client = new SimpleClient(host, port);
            client.connect();
            for (int i=0; i<10; i++) {
        		String term = "passwd";
        		String definition = "Allows you to change your password. _" + Math.random();
        		System.out.println(definition);
        		byte[] header = {0x0a, (byte) 0xdd};
        		byte[] term_length = ByteBuffer.allocate(2).putShort((short) term.getBytes().length).array(); // convert term length to bytes
        		byte[] term_byte = term.getBytes();
        		byte[] definition_length = ByteBuffer.allocate(4).putInt(definition.getBytes().length).array(); // convert term length to bytes
        		byte[] definition_byte = definition.getBytes();
        		
        		ByteArrayOutputStream f = new ByteArrayOutputStream(); 
        		f.write(header);
        		f.write(term_length);
        		f.write(term_byte);
        		f.write(definition_length);
        		f.write(definition_byte);
        		
        		byte[] message = f.toByteArray();
            	
                client.send(message);

                String res = client.receive();
                System.out.println("Got  \"" + res + "\"");

                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException ie) {
                    ie.printStackTrace(System.err);
                }
            }
            client.disconnect();
        } catch (IOException io) {
            io.printStackTrace(System.err);
        }

    }

}
