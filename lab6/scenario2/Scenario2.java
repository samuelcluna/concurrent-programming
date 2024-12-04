package scenario2;

import java.util.concurrent.*;
import java.util.HashMap;
import java.util.Map;

public class Scenario2 {

    public static void main(String[] args) {
        BlockingQueue<Task> taskQueue = new PriorityBlockingQueue<>();

        ExecutorService executorService = Executors.newCachedThreadPool();

        Map<String, Long> totalExecutionTimePerProducer = new HashMap<>();
        Map<String, Integer> taskCountPerProducer = new HashMap<>();

        executorService.submit(new TaskProducer(taskQueue, "Producer1", 0, 13000));  // Prioridade 0 (mais alta)
        executorService.submit(new TaskProducer(taskQueue, "Producer2", 1, 7000));   // Prioridade 1
        executorService.submit(new TaskProducer(taskQueue, "Producer3", 2, 3000));   // Prioridade 2

        for (int i = 1; i <= 3; i++) {
            Node node = new Node(taskQueue, "Node" + i);
            Future<Long> future = executorService.submit(node);

            Executors.newSingleThreadExecutor().submit(() -> {
                while (true) {
                    try {
                        long executionTime = future.get();
                        Task task = taskQueue.peek();  
                        String producerName = task.getProducerName();

                        totalExecutionTimePerProducer.merge(producerName, executionTime, Long::sum);
                        taskCountPerProducer.merge(producerName, 1, Integer::sum);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            System.out.println("Exibindo status das tarefas...");
            totalExecutionTimePerProducer.forEach((producer, totalTime) -> {
                int taskCount = taskCountPerProducer.getOrDefault(producer, 0);
                if (taskCount > 0) {
                    long averageTime = totalTime / taskCount;
                    System.out.printf("Média de tempo de execução pro producer '%s': '%s' ms%n", producer, averageTime);
                }
            });
        }, 0, 5, TimeUnit.SECONDS);
    }
}