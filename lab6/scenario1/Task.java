package scenario1;

import java.util.Random;

public class Task {
    private final long id;
    private String producerName;
    private long executionTime;

    public Task(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getProducerName(){
        return this.producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public long getExecutionTime(){
        return this.executionTime;
    }

    public void execute() throws InterruptedException {
        long execDuration = 1000 + (new Random().nextInt(14000));
        Thread.sleep(execDuration);
    }
}