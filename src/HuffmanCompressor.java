import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HuffmanCompressor {

    private String filepath;
    private int chunkSize =1;

    public HuffmanCompressor(String filepath, int chunkSize) {
        this.filepath = filepath;
        this.chunkSize = Math.max(chunkSize, 1);
    }

    //read file
    //collect frequencies
    //generate dictionary
    //embed dictionary
    //read file
    //convert each chunk to equivalent code
    void compress() throws IOException {
        long then = System.currentTimeMillis();
        FrequencyCounter fc = new FrequencyCounter(filepath,chunkSize);
        HashMap<Chunk,Long> frequencies = fc.countFrequency();
        HuffmanCodeGenerator generator = new HuffmanCodeGenerator(frequencies);
        HashMap<Chunk,Pair> dictionary = generator.getDictionary();
        DictionaryEmbedder embedder = new DictionaryEmbedder(dictionary);
        FileNameManipulator fm = new FileNameManipulator();
        String compressedFilePath = fm.compressedFilePath(filepath,chunkSize);
        DataOutputStream out = new DataOutputStream(new FileOutputStream(compressedFilePath));
        embedder.embedDictionary(out);
        long expectedBitCount = getExpectedNumberOfBits(frequencies,dictionary);
        out.writeLong(expectedBitCount);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(filepath));
        int writingBuffer = 0;
        int limit = 8;
        while(in.available()>0){
            byte[] buffer = in.readNBytes(chunkSize);
            Chunk chunk = new Chunk(buffer);
            Pair pair = dictionary.get(chunk);
            for (int i= pair.len-1 ; i>=0 ; i--) {
                if(limit == 0){
                    out.write(writingBuffer);
                    writingBuffer = 0;
                    limit = 8;
                }
                if((pair.code & (1L << i))>0){
                    writingBuffer = writingBuffer<<1;
                    writingBuffer |= 1;
                    limit--;
                }
                else {
                    writingBuffer = writingBuffer<<1;
                    limit--;
                }
            }
        }
        if(limit < 8){
            while (limit != 0){
                writingBuffer = writingBuffer<<1;
                limit--;
            }
            out.write(writingBuffer);
        }
        long now = System.currentTimeMillis();
        in.close();
        out.close();
        File original = new File(filepath);
        File compressed = new File(compressedFilePath);
        double compressionRatio = (compressed.length()* 1.0 / original.length()) * 100;
        System.out.println("compression ratio = " + compressionRatio);
        System.out.println("Compression time : "+(now-then)+" ms");
    }
    private long getExpectedNumberOfBits(HashMap<Chunk,Long> frequencies,HashMap<Chunk,Pair> dictionary){
        long bits = 0;
        for (Chunk chunk : frequencies.keySet()) {
            bits+= frequencies.get(chunk)*dictionary.get(chunk).len;
        }
        return bits;
    }

    public static void main(String[] args) {
        HuffmanCompressor compressor = new HuffmanCompressor("src/toCompress.txt",1);
        try {
            compressor.compress();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
