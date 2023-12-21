import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanCodeGenerator {


    private HashMap<Chunk,Long> frequencyMap ;
    private HashMap<Chunk,Pair> dictionary;

    public HuffmanCodeGenerator(HashMap<Chunk, Long> frequencyMap) {
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
        for (Chunk chunk :
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

    private void getCode(Node root, long code,int bitLen){
//        System.out.println(root);
        if(root.isLeaf){
            this.dictionary.put(root.chunk,new Pair(code,bitLen));
            return;
        }
        getCode(root.left,(code<<1),bitLen+1);
        getCode(root.right,((code<<1)|1),bitLen+1);
    }

    HashMap<Chunk,Pair> getDictionary(){
        PriorityQueue<Node> queue = initialize();
        Node root = codeTree(queue);
        getCode(root,0,0);
        return this.dictionary;
    }

//    public static void main(String[] args) throws IOException {
//        FrequencyCounter fc = new FrequencyCounter("src/abc",1);
//        HashMap<Chunk,Long> freqs = fc.countFrequency();
//        HuffmanCodeGenerator gem = new HuffmanCodeGenerator(freqs);
//        gem.getDictionary();
//        System.out.println(gem.dictionary);
//    }



}
