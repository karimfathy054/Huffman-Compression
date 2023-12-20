import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HuffmanDecompressor {
    private String filePath;

    public HuffmanDecompressor(String filePath) {
        this.filePath = filePath;
    }

    //read compressed file
    //get dictionary
    //read bitcount
    //if bitcount is 0 stop reading
    //read data bit by bit and check for equivalence in extracted dictionary
    //if found write in decompressed file
    //else wait till accumulation of a write code

    void decompress() throws IOException {
        long then = System.currentTimeMillis();
        DataInputStream in  = new DataInputStream(new FileInputStream(filePath));
        FileNameManipulator fm = new FileNameManipulator();
        DataOutputStream out = new DataOutputStream(new FileOutputStream(fm.extractedFilePath(filePath)));
        DictionaryEmbedder embedder = new DictionaryEmbedder();
        HashMap<Pair,ArrayList<Byte>> dictionary = embedder.extractDictionary(in);
        long bitCount = in.readLong();
        Pair sequence  = new Pair(0,0);
        int z;//for debugging;
        while ((bitCount > 0) && (z = in.available()) > 0){
            int b = in.readByte();
            for (int i = 7; i >=0 ; i--) {
                if(bitCount==0) break;
                int x = b & (1<<i);
                sequence.code = sequence.code<<1;
                if(x > 0){
                    sequence.code |= 1;
                }
                bitCount--;
                sequence.len++;
                if(dictionary.containsKey(sequence)){
                    ArrayList<Byte> chunk = dictionary.get(sequence);
                    for (Byte byt : chunk) {
                        out.writeByte(byt);
                    }
                    sequence.code = 0;
                    sequence.len = 0;
                }
            }
        }
        long now = System.currentTimeMillis();
        in.close();
        out.close();
        System.out.println("decompressing time : "+(now-then)+" ms");
    }

//    public static void main(String[] args) {
//        HuffmanDecompressor decomp = new HuffmanDecompressor("C:\\Users\\Administrator\\IdeaProjects\\HuffmanCode\\src\\20011116.1.toCompress.txt.hc");
//        try {
//            decomp.decompress();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
