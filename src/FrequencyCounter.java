import java.io.*;
import java.util.HashMap;

public class FrequencyCounter {
    private String filePath;
    private int wordSize;
    private HashMap<Chunk, Data> frequencyTable;

    public FrequencyCounter(String filePath,int wordSize) {
        this.filePath = filePath;
        this.wordSize = wordSize;
        this.frequencyTable = new HashMap<>(1024);
    }

    HashMap<Chunk, Data> countFrequency() throws IOException {

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));
        byte[] buffer;
        while(in.available()>0){
            buffer = in.readNBytes(wordSize);
            Chunk chunk = new Chunk(buffer);
            frequencyTable.put(chunk, frequencyTable.getOrDefault(chunk, new Data(0)).increment());
        }
        in.close();
        return this.frequencyTable;
    }

//    public static void main(String[] args) throws IOException {
//        FrequencyCounter fc = new FrequencyCounter("src/abc",1);
//        fc.countFrequency();
//        System.out.println(fc.frequencyTable);
//    }
}
