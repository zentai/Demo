package q3.server.reactor.task;

public interface TaskInf {

	void executeTask() throws TaskFailedException, InterruptedException;

}
