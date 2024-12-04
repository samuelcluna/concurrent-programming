package scenario1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class Scenario1 {

    public static void main(String[] args) {
        BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
        ExecutorService executorService = Executors.newCachedThreadPool();

        Map<String, Long> totalExecutionTimePerProducer = new HashMap<>();
        Map<String, Integer> taskCounterPerProducer = new HashMap<>();

        for (int i = 1; i <= 5; i++) {
            TaskProducer producer = new TaskProducer(taskQueue, "Producer" + i);
            System.out.println(producer);
            executorService.submit(producer);
        }

        for (int i = 1; i <= 3; i++) {
            Node node = new Node(taskQueue, "Node" + i);

            Future<Long> future = executorService.submit(node);

            Executors.newCachedThreadPool().submit(() -> {
                while (true) {
                    try {
                        long executionTime = future.get();
                        Task task = taskQueue.peek();
                        String producerName = task.getProducerName();

                        totalExecutionTimePerProducer.merge(producerName, executionTime, Long::sum);
                        taskCounterPerProducer.merge(producerName, 1, Integer::sum);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            System.out.println("Exibindo status das tarefas...");
            totalExecutionTimePerProducer.forEach((producer, totalTime) -> {
                int taskCount = taskCounterPerProducer.getOrDefault(producer, 0);
                if (taskCount > 0){
                    long averageTime = totalTime / taskCount;
                    System.out.printf("Média de tempo de execução pro producer '%s': '%s' ms%n", producer, averageTime);
                }
            });
        }, 0, 5, TimeUnit.SECONDS);

    }
}