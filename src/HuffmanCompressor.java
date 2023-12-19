import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HuffmanCompressor {
    String filePath;
    private HashMap<ArrayList<Byte>,String> dictionary;
    private int chunkSize = 1;



    public HuffmanCompressor(String filePath, int chunkSize) {
        this.filePath = filePath;
        this.chunkSize = chunkSize;
    }

    //read file
        //get frequency count
        //generate huffman codes
        //embed the hashtable
    void prepareDictionary(DataOutputStream out){
        int dataStart = 0;
        FrequencyCounter fc = new FrequencyCounter(filePath,chunkSize);
        HashMap<ArrayList<Byte>,Long> frequencies = null;
        try {
            frequencies = fc.countFrequency();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HuffmanCodeGenerator generator = new HuffmanCodeGenerator(frequencies);
        this.dictionary = generator.getDictionary();
        DictionaryEmbedder embedder = new DictionaryEmbedder(dictionary);
        try {
            embedder.embedDictionary(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    //read file
        //change each chunk to corresponding value
        //write value in file
    void writeCompressedFile(DataOutputStream out) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));
        byte[] buffer = in.readAllBytes();
        in.close();
        ArrayList<String> codes = new ArrayList<>();
        for (int i = 0; i < buffer.length;) {
            ArrayList<Byte> chunk = new ArrayList<>();
            for (int j = 0; j < chunkSize; j++) {
                if(i < buffer.length){
                    chunk.add(buffer[i++]);
                }
            }
            String code = dictionary.get(chunk);
            codes.add(code);
        }
        int writingBuffer =0;
        int limit = 8;
        for (String code : codes) {
            for (char c : code.toCharArray()){
                if(limit == 0){
                    out.write(writingBuffer);
                    writingBuffer =0;
                    limit = 8;
                }
                if(c == '1'){
                    writingBuffer = writingBuffer<<1;
                    writingBuffer |= 1;
                    limit--;
                }
                else if(c == '0'){
                    writingBuffer = writingBuffer<<1;
                    limit--;
                }
            }
        }
        if(limit < 8){//
            while(limit != 0){
                writingBuffer = writingBuffer<<1;
                limit--;
            }
            out.write(writingBuffer);
            writingBuffer =0;
            limit = 8;
        }
        out.close();
    }
    void compress() throws FileNotFoundException {
        DataOutputStream out = new DataOutputStream(new FileOutputStream(filePath+".hc"));
        prepareDictionary(out);
        try {
            writeCompressedFile(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        HuffmanCompressor comp = new HuffmanCompressor("src/abc",1);
        try {
            comp.compress();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



}
