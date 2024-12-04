package scenario2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class TaskProducer implements Callable<Void> {
    private final BlockingQueue<Task> taskQueue;
    private final String producerName;
    private static long taskIdCounter = 0;
    private final int priority;
    private final long productionDelay;

    public TaskProducer(BlockingQueue<Task> taskQueue, String producerName, int priority, long productionDelay) {
        this.taskQueue = taskQueue;
        this.producerName = producerName;
        this.priority = priority;
        this.productionDelay = productionDelay;
    }

    @Override
    public Void call() throws InterruptedException {
        while (true) {
            Task task = new Task(++taskIdCounter, priority);
            task.setProducerName(producerName);
            taskQueue.put(task);  
            System.out.println("Producer " + producerName + " criou Task " + task.getId() + " com prioridade " + priority);
            Thread.sleep(productionDelay);  
        }
    }
}
