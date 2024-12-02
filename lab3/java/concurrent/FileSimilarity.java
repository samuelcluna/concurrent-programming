import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class FileSimilarity{

    public static class SimilarityThread implements Runnable {

        private List<Long> base;
        private List<Long> target;

        public SimilarityThread(List<Long> base, List<Long> target) {
            this.base = base;
            this.target = target;
        }

        @Override
        public void run() {
            similarity(base, target);
        }

        private static void similarity(List<Long> base, List<Long> target) {
            int counter = 0;
            List<Long> targetCopy = new ArrayList<>(target);

            for (Long value : base) {
                if (targetCopy.contains(value)) {
                    counter++;
                    targetCopy.remove(value);
                }
            }

            float similarityScore = (float) counter / base.size();
            System.out.println("Similarity between " + base + " and " + target + ": " + (similarityScore * 100) + "%");
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java Sum filepath1 filepath2 filepathN");
            System.exit(1);
        }


        // Create a map to store the fingerprint for each file
        Map<String, List<Long>> fileFingerprints = new HashMap<>();

        // Calculate the fingerprint for each file
        for (String path : args) {
            List<Long> fingerprint = fileSum(path);
            fileFingerprints.put(path, fingerprint);
        }

        // Compare each pair of files

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            for (int j = i + 1; j < args.length; j++) {
                String file1 = args[i];
                String file2 = args[j];
                List<Long> fingerprint1 = fileFingerprints.get(file1);
                List<Long> fingerprint2 = fileFingerprints.get(file2);
                Thread myThread = new Thread(new SimilarityThread(fingerprint1, fingerprint2), "similarity");
                threads.add(myThread);
                myThread.start();
            }
        }

        for(Thread thread: threads){
            thread.join();
        }
    }

    private static List<Long> fileSum(String filePath) throws IOException {
        File file = new File(filePath);
        List<Long> chunks = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[100];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                long sum = sum(buffer, bytesRead);
                chunks.add(sum);
            }
        }
        return chunks;
    }

    private static long sum(byte[] buffer, int length) {
        long sum = 0;
        for (int i = 0; i < length; i++) {
            sum += Byte.toUnsignedInt(buffer[i]);
        }
        return sum;
    }

}
