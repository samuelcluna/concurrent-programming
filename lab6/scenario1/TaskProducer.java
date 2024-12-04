package scenario1;

import java.util.concurrent.BlockingQueue;

public class TaskProducer implements Runnable {
    private final BlockingQueue<Task> taskQueue;
    private final String producerName;
    private static long taskIdCounter = 0;

    public TaskProducer(BlockingQueue<Task> taskQueue, String producerName) {
        this.taskQueue = taskQueue;
        this.producerName = producerName;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Task task = new Task(++taskIdCounter);
                task.setProducerName(producerName);
                taskQueue.put(task); 
                System.out.println("Producer " + producerName + " criou Task " + task.getId());
                Thread.sleep(5000); 
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}