import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HuffmanDecompressor {
    private final String compressedFile;

    public HuffmanDecompressor(String compressedFile) {
        this.compressedFile = compressedFile;
    }

    void decomp() throws IOException {

        //read table
        DataInputStream in = new DataInputStream(new FileInputStream(compressedFile));
        DictionaryEmbedder embedder = new DictionaryEmbedder();
        embedder.extractDictionary(in);
        HashMap<String, ArrayList<Byte>> dictionary = embedder.extracted;
        //read file
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(compressedFile.replaceFirst(".hc",".txt")));
        // add bit to sequence
        // check sequence in table
            //if sequence is in table write chunk in new file and reset sequence
            //if sequence is not in table continue
        String sequence = "";
        while(in.available()>0){
            int b = in.readByte();
            for (int i = 7; i >=0 ; i--) {
                int x = b & (1<<i);
                if(x>0){
                    sequence += "1";
                }
                else {
                    sequence += "0";
                }
                if(dictionary.containsKey(sequence)){
                    ArrayList<Byte> chunk = dictionary.get(sequence);
                    for (Byte byt : chunk) {
                        out.write(byt);
                    }
                    sequence = "";
                }
            }
        }
        in.close();
        out.close();
    }

    public static void main(String[] args) throws IOException {
        HuffmanDecompressor dec = new HuffmanDecompressor("src/abc.hc");
        dec.decomp();
    }

}
