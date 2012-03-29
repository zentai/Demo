package q3.server.reactor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import q3.server.db.PostgresAgentInitialException;
import q3.server.reactor.task.ThreadPool;

/**
 * Handles messages from clients
 */
public class ConnectionReader {
	static final Logger logger = LoggerFactory.getLogger(ConnectionReader.class);
    public static final int BUFFER_SIZE = 256;

    protected SocketChannel _sChannel;
    protected ByteArrayOutputStream _incomingData;
    protected ThreadPool _pool;

    /**
     * Creates a new ConnectionReader object
     * @param sChannel the SocketChannel of the client
     * @param pool the ThreadPool to which new Tasks should be inserted
     */
    public ConnectionReader(SocketChannel sChannel, ThreadPool pool) {
        _sChannel = sChannel;
        _pool = pool;
    }

    /**
     * Reads bytes messages from the client:
     * <UL>
     * <LI>Reads the entire SocketChannel's buffer
     * <LI>For each message:
     * <UL>Creates a Task for the message
     * <LI>Inserts the Task to the ThreadPool
     * </UL>
     * </UL>
     * @throws IOException in case of an IOException during reading
     * @throws PostgresAgentInitialException 
     * @throws ConfigurationException 
     */
    public void read() throws IOException, ConfigurationException, PostgresAgentInitialException {
        SocketAddress address = _sChannel.socket().getRemoteSocketAddress();
        logger.info("Reading from " + address);
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
        _incomingData = new ByteArrayOutputStream();
        /*
         * Read whole incoming date into a byte array.
         */
        while (true) {
            buf.clear();
            int numBytesRead = _sChannel.read(buf);

            if (numBytesRead == -1) {
            	logger.info("client on " + address + " has disconnected");
                _sChannel.close();
                break;
            }

            if (numBytesRead > 0) {
                buf.flip();
                _incomingData.write(buf.array());
            }

            if (numBytesRead < BUFFER_SIZE) {
                break;
            }
        }
        
        
		byte[] bytes = _incomingData.toByteArray();
		_incomingData.close();
		
		if (bytes.length == 0) {
			logger.error("Empty incoming data. stop process");
			return;
		}
		
		/*
		 * Requirement:
		 * - You server MUST handle concurrency correctly. If three clients are all trying
		 * 	 to operate on the same term at the same time, you server must give consistent
		 *   results as if operations of the three clients are performed in a sequence.
		 *
		 * Design:
		 *   we provide a independence task queue to reach the requirement.
		 *   for client, the response time will be cut down, because client no need to waiting server reply.
		 *         
		 */
		_pool.addTask(new MessageProcessorTask(bytes, _sChannel));
    }
}
