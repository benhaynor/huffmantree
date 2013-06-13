import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Stack;

public class HuffmanTree{
	
	private char[] alphabet;
	private int[] counts;
	private HNode root;
	private HashMap<Character,String> encodings;
	private HashMap<String,Character> reverseEncodings;

	public HuffmanTree(char[] alphabet, int[] counts){
		this.alphabet = alphabet;
		this.counts = counts;
	}
	
	private void createTree(){
		//Create heap in linear time, rather than NLogN time
		PriorityQueue<HNode> pq = new PriorityQueue<HNode>(alphabet.length);
		for (int i = 0; i < alphabet.length; i ++){
			pq.add(new HNode(alphabet[i], counts[i]));
		}
		while (pq.size() > 1){
			HNode biggest = pq.poll();
			HNode next_biggest = pq.poll();
			HNode combined = HNode.combine(biggest, next_biggest);
			pq.add(combined);
		}
		root = pq.poll();
	}

	public void createEncodingMap(){
		encodings = new HashMap<Character,String>();
		reverseEncodings = new HashMap<String, Character>();
		encode(root, "0");		
	}

	private void encode(HNode node, String path){
		if (node.code != '\\'){
			encodings.put(node.code, path);	
			reverseEncodings.put(path, node.code);	
		}
		if (node.lc != null){
			encode(node.lc, path + "0");
		}
		if (node.rc != null){
			encode(node.rc, path + "1");
		}
	}

	public void print(){
		root.print(0);
	}

	//A helper method to translate files
	public void encode(String inFile, String outFile){
		System.out.println("Encoding...");
		BitOutputStream out = null;
		BufferedReader in = null; 
		BufferedReader inc = null;
		int charCount = 0;
		try{
			inc = new BufferedReader( new FileReader(inFile));
			while(inc.read() != -1){
				charCount ++;
			}	
		} catch (IOException e){
			System.exit(1);
		} finally{
			try{
				if (inc != null) inc.close();
			} catch(IOException e){}
		}
		try{
			out = new BitOutputStream(new FileOutputStream(outFile));
			in = new BufferedReader( new FileReader(inFile));
			out.writeInt(charCount);
			for (int next = in.read(); next != -1; next = in.read()){	
				char nc = (char) next;
				out.writeBits(stringToIntArray(encodings.get(nc)));
			}
		} catch (IOException e){
			System.exit(1);
		} finally {
			try{
				if (in != null) in.close();
				if (out != null) out.close();
			} catch( IOException e){}
		}
	}

	public static int[] stringToIntArray(String s){
		int[] trans = new int[s.length()];
		for (int i = 0; i < trans.length; i ++){
			trans[i] = Integer.parseInt(Character.toString(s.charAt(i)));
		}
		return trans;
	}

	//A helper method to unstranslate files
	public void decode(String inFile, String outFile){
		System.out.println("decoding");
		try{
			BitInputStream in = new BitInputStream(new FileInputStream(inFile));
			PrintWriter out = new PrintWriter( new FileWriter(outFile));
			String s = "";
			int nchars = in.readInt();
			for(int nc = in.readBit(); nchars > 0 && nc != -1 ; nc = in.readBit()){
				s = s + Integer.toString(nc);
				if (reverseEncodings.containsKey(s)){
					out.write(reverseEncodings.get(s));
					s = "";
					nchars --;
				}
			}
			in.close();
			out.close();
		} catch (IOException e){System.exit(1);}
	}

	public static void main(String[] args){
		HuffmanTree ht = new HuffmanTree(new char[] {'a', 'e', 'i', 's', 't', 'p', '\n'},
			new int[] {10, 15, 12, 3, 4, 13, 1});
		ht.createTree();
		ht.createEncodingMap();
		if (args[0].equals("encode")){
			ht.encode("tochange.txt","tochange.dat");
		} else if (args[0].equals("decode")){
			ht.decode("tochange.dat","reconstructed.txt");
		}

	}

}

class HNode implements Comparable<HNode>{
	
	char code;
	int weight;
	HNode lc;
	HNode rc;

	public void print(int depth){
		String repr = "";
		for (int i = 0; i < depth; i ++){
			repr = repr + "\t";
		}
		if (this.code == '\\'){
			System.out.printf("%sWeight:%d%n", repr, weight);
		} else {
			System.out.printf("%sWeight:%d Value:%s%n", repr, weight, code);
		}
		if (lc != null){
			lc.print(depth + 1);
		}
		if (rc != null){
			rc.print(depth + 1);
		}
	}

	public HNode(char code, int weight){
		this.code = code;
		this.weight = weight;
	}

	public HNode(char code, int weight, HNode lc, HNode rc){
		this(code, weight);
		this.lc = lc;
		this.rc = rc;
	}

	public static HNode combine(HNode l, HNode r){
		return new HNode('\\', l.weight + r.weight, l, r);
	}

	public int compareTo(HNode other){
		return weight - other.weight;
	}
}
