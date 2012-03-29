package q3.server.reactor;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import q3.server.reactor.task.ThreadPool;

/**
 * Handles new client connections.
 * An Acceptor is bound on a ServerSocketChannel objects, which can produce new
 * SocketChannels for new clients using its <CODE>accept</CODE> method.
 */
public class ConnectionAcceptor {
	static final Logger logger = LoggerFactory.getLogger(ConnectionAcceptor.class);
	public static int current_connection = 0; 
    protected Selector _selector;
    protected ServerSocketChannel _ssChannel;
    protected ThreadPool _pool;
	private int _max_client_limit = 50;

    /**
     * Creates a new ConnectionAcceptor
     * @param selector the Selector to which new SocketChannels will be registered
     * @param ssChannel the ServerSocketChannel which can accept new connections
     * @param pool the thread pool, which is needed by the new ConnectionReaders
     */
    public ConnectionAcceptor(Selector selector, ServerSocketChannel ssChannel, ThreadPool pool, int max_client_limit) {
        _selector = selector;
        _ssChannel = ssChannel;
        _pool = pool;
        _max_client_limit = max_client_limit;
    }

    /**
     * Accepts a connection:
     * <UL>
     * <LI>Creates a SocketChannel for it
     * <LI>Creates a ConnectionReader for it
     * <LI>Registers the SocketChannel and the ConnectionReader to the Selector
     * </UL>
     * @throws IOException in case of an IOException during the acceptance of a new connection
     */
    public void accept() throws IOException {
    	if(current_connection > _max_client_limit){
    		logger.error("Server Hit Max Client limit. connection rejected.");
    		SocketChannel sChannel = _ssChannel.accept();
    		String response = "Server Hit Max Client limit. connection rejected.";
    		try {
    			sChannel.write(ByteBuffer.wrap(response.getBytes()));
    			sChannel.close();
			} catch (IOException e) {
				logger.error("SocketChannel close exception "+ e);
				return;
			}
    	}
        // Get a new channel for the connection request
        SocketChannel sChannel = _ssChannel.accept();

        // If serverSocketChannel is non-blocking, sChannel may be null
        if (sChannel != null) {
            SocketAddress address = sChannel.socket().getRemoteSocketAddress();
            logger.info("Accepting connection from " + address);
            sChannel.configureBlocking(false);
            sChannel.register(_selector, SelectionKey.OP_READ, new ConnectionReader(sChannel, _pool));
            current_connection += 1;
        }
    }
}
