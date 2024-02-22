import java.io.*;
import java.util.HashMap;

public class HuffmanCompressor {

    private String filepath;
    private int wordSize = 1;

    public HuffmanCompressor(String filepath, int wordSize) {
        this.filepath = filepath;
        this.wordSize = Math.max(wordSize, 1);
    }

    // read file
    // collect frequencies
    // generate dictionary
    // embed dictionary
    // read file
    // convert each chunk to equivalent code
    void compress() throws IOException {
        long then = System.currentTimeMillis();
        FrequencyCounter fc = new FrequencyCounter(filepath, wordSize);
        HashMap<Chunk, Data> frequencies = fc.countFrequency();
        HuffmanCodeGenerator generator = new HuffmanCodeGenerator(frequencies);
        HashMap<Chunk, Data> dictionary = generator.getDictionary();
        DictionaryEmbedder embedder = new DictionaryEmbedder(dictionary, fc.bytesNumber);
        FileNameManipulator fm = new FileNameManipulator();
        String compressedFilePath = fm.compressedFilePath(filepath, wordSize);
        DataOutputStream out = new DataOutputStream(new FileOutputStream(compressedFilePath));
        embedder.embedDictionary(out);
        long expectedBitCount = getExpectedNumberOfBits(dictionary);
        out.writeLong(expectedBitCount);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(filepath));
        int writingBuffer = 0;
        int limit = 8;
        int z;
        byte[] outBuffer = new byte[10000000];
        // byte[] outBuffer = new byte[(int)(Math.ceil(expectedBitCount*1.0/8))];
        int indx = 0;
        while ((z = in.available()) > 0) {
            int g = 10000000;
            int r = g % wordSize;
            int sign = (int) Math.signum(r - (wordSize / 2.0));
            byte[] buffer = in.readNBytes(g + (r * sign));
            for (int i = 0; i < buffer.length;) {
                byte[] word = new byte[wordSize];
                for (int j = 0; j < wordSize; j++) {
                    if (i < buffer.length)
                        word[j] = buffer[i++];
                }
                Chunk chunk = new Chunk(word);
                Data data = dictionary.get(chunk);
                if (data == null) {
                    System.out.println("chunk = " + chunk);
                }
                int y = 1;
                for (int k = data.len - 1; k >= 0; k--) {
                    if (limit == 0) {
                        if (indx == (10000000 - 1)) {
                            out.write(outBuffer, 0, indx);
                            indx = 0;
                        }

                        outBuffer[indx++] = (byte) writingBuffer;
                        // out.write(writingBuffer);
                        writingBuffer = 0;
                        limit = 8;
                    }
                    writingBuffer = writingBuffer << 1;
                    if ((data.code & (1L << k)) > 0) {
                        writingBuffer |= 1;
                    }
                    limit--;
                }
            }
        }

        if (limit < 8) {
            while (limit != 0) {
                writingBuffer = writingBuffer << 1;
                limit--;
            }
            outBuffer[indx++] = (byte) writingBuffer;
            // out.write(writingBuffer);
        }
        out.write(outBuffer, 0, indx);
        long now = System.currentTimeMillis();
        in.close();
        out.close();
        File original = new File(filepath);
        File compressed = new File(compressedFilePath);
        double compressionRatio = (compressed.length() * 1.0 / original.length()) * 100;
        System.out.println("compression ratio = " + compressionRatio + " %");
        System.out.println("Compression time : " + (now - then) + " ms");
    }

    private long getExpectedNumberOfBits(HashMap<Chunk, Data> dictionary) {
        long bits = 0;
        for (Chunk chunk : dictionary.keySet()) {
            bits += dictionary.get(chunk).freq * dictionary.get(chunk).len;
        }
        return bits;
    }

    public static void main(String[] args) {
        HuffmanCompressor compressor = new HuffmanCompressor(
                "C:\\Users\\mg514\\OneDrive\\Desktop\\New folder\\gbbct10.seq\\gbbct10.seq", 3);
        try {
            compressor.compress();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
