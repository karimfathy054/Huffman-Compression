import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class FrequencyCounter {
    private String filePath;
    private int chunkSize;
    private HashMap<ArrayList<Byte>,Long> frequencyTable;

    public FrequencyCounter(String filePath,int chunkSize) {
        this.filePath = filePath;
        this.chunkSize = chunkSize;
        this.frequencyTable = new HashMap<>();
    }

    HashMap<ArrayList<Byte>,Long> countFrequency() throws IOException {
        InputStream input = new BufferedInputStream(new FileInputStream(filePath));
        byte[] bytes = input.readAllBytes();
        for (int i = 0; i < bytes.length;) {
            ArrayList<Byte> chunk = new ArrayList<>();
            for (int j = 0; j < chunkSize; j++) {
                if(i < bytes.length){
                    chunk.add(bytes[i++]);
                }
            }
            frequencyTable.put(chunk, frequencyTable.getOrDefault(chunk, 0L)+1L);
        }
        return this.frequencyTable;
    }
}
