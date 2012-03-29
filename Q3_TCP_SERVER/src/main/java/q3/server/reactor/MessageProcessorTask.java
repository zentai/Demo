package q3.server.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;

import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import q3.server.configuration.AppConfiguration;
import q3.server.db.PostgresAgent;
import q3.server.db.PostgresAgentInitialException;
import q3.server.protocol.ProtocolParser;
import q3.server.reactor.task.TaskFailedException;
import q3.server.reactor.task.TaskInf;

/**
 * A sample Message Processor
 * The Message Processor holds a received message, and a SocketChannel to the message sender;
 * this socket enables replying back to the sender.
 * This sample processor sends a simple reply back to the message sender, regardless of the message's content.
 * <B>You should either extend or rewrite this Message Processor to work properly with the assignment definition.</B>
 */
class MessageProcessorTask implements TaskInf {
	static final Logger logger = LoggerFactory.getLogger(MessageProcessorTask.class);
    protected SocketChannel _channel;
    protected byte[] _bytes;
    private PostgresAgent _postgresAgent;
    /**
     * Creates a new MessageProcessorTask
     * @param message the messge, as receieved from the sender
     * @param channel a channel which will be used to reply to the message sender
     * @throws PostgresAgentInitialException throw when PostgresAgent setting invalid
     * @throws ConfigurationException throws when PostgresAgent Configuration invalid
     */
    public MessageProcessorTask(byte[] bytes, SocketChannel channel) throws ConfigurationException, PostgresAgentInitialException {
        _bytes = bytes;
        _channel = channel;
        _postgresAgent = new PostgresAgent(new AppConfiguration("../resource/config.xml"));
    }


    /**
     * Executes the task
     * This simple implementation simply replies the sender with a general reply.
     * @throws TaskFailedException in case of a failure while executing the task 
     */
    public void executeTask() throws TaskFailedException {
    	String response = "";
    	ProtocolParser protocol = new ProtocolParser(_bytes);
		if (!protocol.isContainsHeader()) {
    		logger.error("Invalid Protocol. connection close.");
    		response = "Invalid Protocol. connection close.";
    		try {
				_channel.close();
			} catch (IOException e) {
				logger.error("SocketChannel close exception "+ e);
				return;
			}
    	}else{
    		String term = protocol.getTerm();
    		String definition = protocol.getDefinition();
    		try {
				_postgresAgent.update(term, definition);
				response = "Command Accept";
			} catch (ClassNotFoundException e) {
				logger.error("Server SQL installation error.");
				e.printStackTrace();
			} catch (SQLException e) {
				logger.error("SQL command error.");
				e.printStackTrace();
			}
    	} 
        
        try {
            _channel.write(ByteBuffer.wrap(response.getBytes()));
        }
        catch (IOException io) {
            throw new TaskFailedException("I/O exception while processing the message: " + new String(_bytes), io);
        }
    }
}
