package scenario2;

import java.util.Random;

public class Task implements Comparable<Task> {
    private final long id;
    private final int priority;
    private String producerName;
    private long executionTime;

    public Task(long id, int priority) {
        this.id = id;
        this.priority = priority;
    }

    public long getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void execute() throws InterruptedException {
        long execDuration = 1000 + (new Random().nextInt(14000));
        Thread.sleep(execDuration);  
    }

    @Override
    public int compareTo(Task other) {
        return Integer.compare(this.priority, other.priority); 
    }
}