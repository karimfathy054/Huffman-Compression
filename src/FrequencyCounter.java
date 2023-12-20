import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FrequencyCounter {
    private String filePath;
    private int chunkSize;
    private HashMap<ArrayList<Byte>,Long> frequencyTable;

    public FrequencyCounter(String filePath,int chunkSize) {
        this.filePath = filePath;
        this.chunkSize = chunkSize;
        this.frequencyTable = new HashMap<>(1024);
    }

    HashMap<ArrayList<Byte>,Long> countFrequency() throws IOException {

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));
        byte[] buffer;
        while(in.available()>0){
            buffer = in.readNBytes(chunkSize);
            ArrayList<Byte> chunk = new ArrayList<>();
            for (byte b :
                    buffer) {
                chunk.add(b);
            }
            frequencyTable.put(chunk, frequencyTable.getOrDefault(chunk, 0L)+1L);
        }
        in.close();
        return this.frequencyTable;
    }

//    public static void main(String[] args) throws IOException {
//        FrequencyCounter fc = new FrequencyCounter("src/toCompress.txt",1);
//        fc.countFrequency();
//        System.out.println(fc.frequencyTable);
//    }
}
