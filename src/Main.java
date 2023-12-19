import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {

//        HashMap<Character,Integer> h = new HashMap<Character,Integer>();
//        h.put('g',20);
//        h.put('b',30);
//        h.put('c',35);
//        h.put('d',45);
//        h.put('h',50);
//        h.put('f',90);
//        h.put('a',110);
//        h.put('e',250);
//        HuffmanAlgorithm huff = new HuffmanAlgorithm();
//        System.out.println(huff.generateHuffman(h));
        String z = "101";
        ArrayList<String> codes = new ArrayList<>();
        codes.add(z);
        codes.add("001");
        int writingBuffer =0;
        int limit = 8;
        DataOutputStream out = new DataOutputStream(new FileOutputStream("src/output"));
        for (String code : codes) {
            for (char c : code.toCharArray()){
                if(limit == 0){

//                    System.out.println("writing buffer: "+Integer.toBinaryString(writingBuffer));
                    out.writeByte(writingBuffer);
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
            out.writeByte(writingBuffer);
//            System.out.println("writing buffer: "+Long.toBinaryString(writingBuffer));
            writingBuffer =0;
            limit = 8;
        }
        DataInputStream in = new DataInputStream(new FileInputStream("src/output"));
        byte b = in.readByte();
        System.out.println(Integer.toBinaryString(b));
        for (int i = 7; i >=0 ; i--) {
            System.out.println(Integer.toBinaryString(1<<i));
            int x = b & (1<<i);
            if(x>0){
                System.out.println(true);
            }
            else System.out.println(false);
        }
    }
}