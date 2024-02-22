import java.io.*;
import java.util.HashMap;

public class FrequencyCounter {
    private String filePath;
    private int wordSize;
    private HashMap<Chunk, Data> frequencyTable;
    long bytesNumber;

    public FrequencyCounter(String filePath, int wordSize) {
        this.filePath = filePath;
        this.wordSize = wordSize;
        this.frequencyTable = new HashMap<>(256);
        this.bytesNumber = 0;
    }

    HashMap<Chunk, Data> countFrequency() throws IOException {

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));
        while (in.available() > 0) {
            int g = 10000000;
            int r = g % wordSize;
            int sign = (int) Math.signum(r - (wordSize / 2.0));
            byte[] buffer = in.readNBytes(g + (r * sign));
            this.bytesNumber += buffer.length;
            for (int i = 0; i < buffer.length;) {
                byte[] word = new byte[wordSize];
                for (int j = 0; j < wordSize; j++) {
                    if (i < buffer.length)
                        word[j] = buffer[i++];
                }
                Chunk chunk = new Chunk(word);
                frequencyTable.put(chunk, frequencyTable.getOrDefault(chunk, new Data(0)).increment());
            }
        }
        in.close();
        return this.frequencyTable;
    }

    public static void main(String[] args) throws IOException {
        FrequencyCounter fc = new FrequencyCounter(
                "C:\\Users\\mg514\\OneDrive\\Desktop\\New folder\\gbbct10.seq\\gbbct10.seq", 3);
        fc.countFrequency();
        System.out.println(fc.frequencyTable);
    }
}
