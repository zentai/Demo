package q3.server.reactor.task;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskQueue{
	private ConcurrentLinkedQueue<TaskInf> queue = new ConcurrentLinkedQueue<TaskInf>();
	
	/**
	 * poll element from TaskQueue.
	 * @return the first Task object from TaskQueue
	 */
	public TaskInf getTask() {
		return this.queue.poll();
	}

	/**
	 * Add Task into TaskQueue.
	 * @param task
	 */
	public void addTask(TaskInf task) {
		if (task == null){
			return;
		}
		this.queue.add(task);
	}

	/**
	 * Check TaskQueue is empty
	 */
	public boolean isEmpty() {
		return this.queue.isEmpty();
	}

}
