import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DictionaryEmbedder {
    //not including MAGIC_NUMBER can save 8 bytes but it acts as a good indication of working code
    private static final int MAGIC_NUMBER = 0xABCDEF; //to mark the start and the end of the dictionary
    private HashMap<Chunk, Pair> dictionary;
    private HashMap<Pair,Chunk> extracted;

    public DictionaryEmbedder() {
    }

    public DictionaryEmbedder(HashMap<Chunk, Pair> dictionary) {
        this.dictionary = dictionary;
    }

    void embedDictionary(DataOutputStream out) throws IOException {
        out.writeInt(MAGIC_NUMBER);
        out.writeInt(dictionary.size());
        for (Chunk chunk:dictionary.keySet()){
            out.writeInt(chunk.bytes.length);
            for (byte b :
                    chunk.bytes) {
                out.writeByte(b);
            }
            Pair p = dictionary.get(chunk);
            out.writeByte(p.len);
            out.writeLong(p.code);
        }
        out.writeInt(MAGIC_NUMBER);
    }

    HashMap<Pair,Chunk> extractDictionary(DataInputStream in) throws IOException {
        int magicNumber = in.readInt();
        if(magicNumber != MAGIC_NUMBER){
            throw new RuntimeException("unsupported format");
        }
        int mapSize = in.readInt();
        extracted = new HashMap<>(mapSize);
        for (int i = 0; i < mapSize; i++) {
            int keyLength = in.readInt();
            byte[] word = in.readNBytes(keyLength);
            Chunk chunk = new Chunk(word);
            int len = in.readByte();
            long code = in.readLong();
            Pair p = new Pair(code,len);
            extracted.put(p,chunk);
        }
        magicNumber = in.readInt();
        if(magicNumber != MAGIC_NUMBER){
            throw new RuntimeException("error extracting the dictionary");
        }
        return this.extracted;
    }

//    public static void main(String[] args) throws IOException {
//        FrequencyCounter fc = new FrequencyCounter("src/abc",1);
//        HashMap<Chunk,Long> freqs = fc.countFrequency();
//        HuffmanCodeGenerator gem = new HuffmanCodeGenerator(freqs);
//        HashMap<Chunk, Pair> dic = gem.getDictionary();
//        System.out.println("original : "+dic);
//        DictionaryEmbedder emb = new DictionaryEmbedder(dic);
//        DataOutputStream out = new DataOutputStream(new FileOutputStream("src/abc.dic"));
//        emb.embedDictionary(out);
//        DataInputStream in = new DataInputStream(new FileInputStream("src/abc.dic"));
//        HashMap<Pair,Chunk> dicx = emb.extractDictionary(in);
//        System.out.println("extracted : "+dicx);
////        System.out.println(dic.equals(dicx));
//    }

}
