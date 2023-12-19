import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DictionaryEmbedder {
    private static final int MAGIC_NUMBER = 0xABCDEF;
    HashMap<ArrayList<Byte>,String> dictionary;
    HashMap<String,ArrayList<Byte>> extracted;

    public DictionaryEmbedder() {
    }

    public DictionaryEmbedder(HashMap<ArrayList<Byte>, String> dictionary) {
        this.dictionary = dictionary;
    }
    void embedDictionary(DataOutputStream out) throws IOException {
        out.writeInt(MAGIC_NUMBER);
        out.writeInt(dictionary.size());
        for (ArrayList<Byte> chunk:dictionary.keySet()){
            out.writeInt(chunk.size());
            for (Byte b :
                    chunk) {
                out.writeByte(b);
            }
            out.writeUTF(dictionary.get(chunk));
        }
    }

    int extractDictionary(DataInputStream in) throws IOException {
        int start = in.available();
        int magicNumber = in.readInt();
        if(magicNumber != MAGIC_NUMBER){
            throw new RuntimeException("unsupported format");
        }
        int mapSize = in.readInt();
        extracted = new HashMap<>();
        extracted = new HashMap<>();
        for (int i = 0; i < mapSize; i++) {
            int keyLength = in.readInt();
            ArrayList<Byte> chunk = new ArrayList<>();
            for (int j = 0; j < keyLength; j++) {
                chunk.add(in.readByte());
            }
            String code = in.readUTF();
            extracted.put(code,chunk);
        }
        int end = in.available();
        return start-end;
    }

}
