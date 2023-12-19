import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanCodeGenerator {

    private HashMap<ArrayList<Byte>,Long> frequencyMap ;
    private HashMap<ArrayList<Byte>,String> dictionary;

    public HuffmanCodeGenerator(HashMap<ArrayList<Byte>, Long> frequencyMap) {
        this.frequencyMap = frequencyMap;
        this.dictionary = new HashMap<>();
    }

    //prepare the queue
    private PriorityQueue<Node> initialize(){
        PriorityQueue<Node> queue = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return (int)(o1.frequency- o2.frequency);
            }
        });
        for (ArrayList<Byte> chunk :
                frequencyMap.keySet()) {
            Node leaf = new Node(frequencyMap.get(chunk), chunk, null, null, true);
            queue.add(leaf);
        }
        return queue;
    }

    private Node codeTree(PriorityQueue<Node> characters){
        while(characters.size()>1){
            Node left = characters.poll();
            Node right = characters.poll();
            Long newValue = left.frequency + right.frequency;
            Node internalNode = new Node(newValue,left,right,false);
            characters.add(internalNode);
        }
        return characters.poll();
    }

    private void getCode(Node root, String code){
        if(root.isLeaf){
            this.dictionary.put(root.chunk,code);
            return;
        }
        getCode(root.left,code+"0");
        getCode(root.right,code+"1");
    }

    HashMap<ArrayList<Byte>,String> getDictionary(){
        PriorityQueue<Node> queue = initialize();
        Node root = codeTree(queue);
        getCode(root,"");
        return this.dictionary;
    }

//    public static void main(String[] args) {
//        FrequencyCounter fc = new FrequencyCounter("src/abc",1);
//        try {
//            HashMap<ArrayList<Byte>,Long> freqs = fc.countFrequency();
//            System.out.println("frequencies = "+freqs);
//            HuffmanCodeGenerator gen = new HuffmanCodeGenerator(freqs);
//            HashMap<ArrayList<Byte>,String> codes = gen.getDictionary();
//            System.out.println("codes = "+codes);
//            DictionaryEmbedder embedder = new DictionaryEmbedder(codes);
//            embedder.embedDictionary();
//            int x = embedder.extractDictionary();
//            System.out.println("original");
//            System.out.println(embedder.dictionary);
//            System.out.println("extracted");
//            System.out.println(embedder.extracted);
//            DataInputStream input = new DataInputStream(new FileInputStream("src/dictionary.dic"));
//            byte[] buffer = new byte[1024];
//            int z = input.skipBytes(x);
//            System.out.println(x);
//            System.out.println(z);
//            input.read(buffer);
//
//            for (int i = 0; i < buffer.length;) {
//                byte[] k = new byte[2];
//                for (int j = 0; j < 2; j++) {
//                    k[j]=buffer[i++];
//                }
//            }
//
//            for (byte b : buffer) {
//
//                System.out.println((char)b);
//            }
//
////            String s = String.valueOf(buffer);
////            System.out.println(s);
////            System.out.println(buffer);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }



}
