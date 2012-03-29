package q3.server.reactor.task;

import java.io.IOException;

public class TaskFailedException extends Exception {
	public TaskFailedException(String msg, IOException io){
		super(msg, io);
	}
}
