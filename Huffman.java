/**
 *  
 * @author Vivian Liu
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Huffman {
	
	private static final Scanner CONSOLE = new Scanner(System.in);
	private static ArrayList <String> document = new ArrayList<String>();
	private static HashMap<String, Integer> char_frequencies = new HashMap<>();
	private static PriorityQueue<HuffmanNode> pq = new PriorityQueue<HuffmanNode>();
	private static HashMap<String, String> encodings = new HashMap<>();
	private static HuffmanNode root;
 
	public static void main(String[] args) throws IOException {
		System.out.println("Please enter the name of the file you'd like to encode.");
		String file_name = CONSOLE.nextLine();
		readInFile(file_name);
		construct_minheap();
		encode_file();
		if (root.left == null && root.right == null) { // if file has 1 unique character
			display_encoding(root, "0");
		}
		else {
			display_encoding(root, "");
		}
		System.out.println("Please enter a sequence of 0's and 1's you'd like to decode.");
		String to_decode = CONSOLE.nextLine();
		decode_text(to_decode);
		System.out.println("Please enter a sequence of characters you'd like to encode.");
		String to_encode = CONSOLE.nextLine();
		encode_text(to_encode);
		
	}
	
	/**
	 * Given the text file, constructs the minheap using characters and their
	 * frequencies.
	 * @return
	 */
	public static void construct_minheap() {
		for (int i = 0; i < document.size(); i++) {
			for (int j= 0; j < document.get(i).length(); j++) {
				String the_char = String.valueOf(document.get(i).charAt(j));
				if (!char_frequencies.containsKey(the_char)) {
					char_frequencies.put(the_char, 1);
				}
				else {
					int current = char_frequencies.get(the_char);
					char_frequencies.put(the_char, current + 1);
				}
			}
		}
		for (Map.Entry<String, Integer> word: char_frequencies.entrySet()) {
			 HuffmanNode huff_node = new HuffmanNode(word.getKey(), word.getValue(), null, null);
			 pq.add(huff_node);
		}
		
	}
	
	/**
	 * Encodes the file using Huffman Encoding and stores in a tree
	 * @return
	 */
	public static void encode_file() {
		while (pq.size() > 1) {
			HuffmanNode h1 = pq.poll();
			HuffmanNode h2 = pq.poll();
			HuffmanNode combined = new HuffmanNode("", h1.freq + h2.freq, h1, h2);
			pq.offer(combined);
		}
		root = pq.poll();
		
	}
	
	/**
	 * Traverses the tree in order to display encoding.
	 * @return
	 */
	public static void display_encoding(HuffmanNode hn, String encoding) {
		if (hn == null) {
			return;
		}
		if (hn.left == null && hn.right == null) {
			System.out.println(hn.the_char + ": " + encoding);
			encodings.put(hn.the_char, encoding);
		}
		else {
			display_encoding(hn.left, encoding + "0");
			display_encoding(hn.right, encoding + "1");
		}
	}
	
	/**
	 * Decodes given text with the encoding table constructed with the 
	 * text file.
	 * @return
	 */
	public static void decode_text(String text) {
		String decoded = "";
		HuffmanNode current = root;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '0') {
				current = current.left;
			}
			else {
				current = current.right;
			}
			if (current.left == null && current.right == null) {
				decoded += current.the_char;
				current = root;
			}
		}
		if (current != root) {
			System.out.println("Invalid decoding entry.");
		}
		else {
			System.out.println(decoded);
		}
	}
	
	/**
	 * Encodes the text given a string. If given an invalid character to
	 * encode, gives error message and breaks.
	 * @return
	 */
	public static void encode_text(String text) {
		String encoded = "";
		for (int i = 0; i < text.length(); i++) {
			String to_encode = String.valueOf(text.charAt(i));
			if (encodings.containsKey(to_encode)) {
				encoded += encodings.get(to_encode);
			}
			else {
				System.out.println("Invalid message to encode.");
				return;
			}
		}
		System.out.println(encoded);
	}
	
	/*
	 * Reads in the file.
	 */
	public static void readInFile(String file_name) throws IOException {
		Scanner file = openTheFile(file_name);
		if (file != null) {
			while (file.hasNextLine()) {
				addLine(file.nextLine());
			}
			addLine("");
		}
	}
	/*
	 * Adds each line of the file to read in to an ArrayList
	 */
	public static void addLine(String s){
		document.add(s);
	}
	/*
	 * Opens the file. Throws an error if the file is not found.
	 */
	public static Scanner openTheFile(String file_name)
	{
		Scanner file = null;
		try {
			file = new Scanner(new File(file_name));
		} catch (IOException e)
		{
			System.out.println("File error - file not found");
			return null;
		}
		return file;
	}
	
	/**
	 * I made a private inner class HuffmanNode which stores both the character
	 * and the frequency with which it occurs. Also stores pointers to left and
	 * right nodes.
	 * @author vivianliu
	 */
	private static class HuffmanNode implements Comparable<HuffmanNode>{
		private String the_char;
		private int freq;
		private HuffmanNode left;
		private HuffmanNode right;
		
		public HuffmanNode(String the_charIn, int freqIn, HuffmanNode leftIn, HuffmanNode rightIn) {
			the_char = the_charIn;
			freq = freqIn;
			left = leftIn;
			right = rightIn;
		}
		
		/**
		 * Compares HuffmanNodes by the frequency
		 */
		public int compareTo(HuffmanNode h2) {
			if (this.freq < h2.freq) {
				return -1;
			}
			else if (this.freq == h2.freq) {
				return 0;
			}
			return 1;
		}
	}
}
