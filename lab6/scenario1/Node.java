package scenario1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;


public class Node implements Callable<Long> {
    private final BlockingQueue<Task> taskQueue;
    private final String nodeName;

    public Node(BlockingQueue<Task> taskQueue, String nodeName) {
        this.taskQueue = taskQueue;
        this.nodeName = nodeName;
    }

    @Override
    public Long call() throws InterruptedException {
        while (true) {
            Task task = taskQueue.take();
            long startTime = System.currentTimeMillis();
            System.out.println("Node " + nodeName + " processando Task " + task.getId());
            task.execute();
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Node " + nodeName + " concluiu Task " + task.getId() + " em " + totalTime + "ms.");
            task.setExecutionTime(totalTime);
            return totalTime;
        }
    }

}