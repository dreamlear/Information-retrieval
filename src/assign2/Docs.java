package assign2;

/**
 * @author Li Zhiyuan
 * @date:2/10/2018
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Docs {
	private ArrayList<Doc> docList;
	private Index index;
	private String path;
	private int keyword_num;
	private static int doc_num = 100;
	public static double[][] weight;
	public static String[] words_all;
	public static double[][] weight_all;
	public static int[][] tf;
	public static int[] df;
	private LinkedList<Integer> top_document;
	
	public Docs() {
		docList = new ArrayList<Doc>();
		index = new Index();
		top_document = new LinkedList<Integer>();
		clear();
		path = "src/assign2/collection-100";
	}
	
	public Index getIndex() {
		return index;
	}
	
	public ArrayList<Doc> getDocList() {
		return docList;
	}
	
	public static int getDocnum() {
		return doc_num;
	}
	
	public int getKeywordnum() {
		return keyword_num;
	}
	
	public void preProcess() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String line = "";
		int id = 1;
		while(line != null) {
			//get a line
			try {
				line = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//if it is a new passage
			if(line != null && !line.equals("")) {
				Doc doc = new Doc(id, getProcess(id, line));
				id++;
				docList.add(doc);
			}
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getWeightAll();
	}
	
	//Preprocess one document, and update the inverted files.
	public ArrayList<String> getProcess(int id, String line){
		ArrayList<String> list = new ArrayList<String>();
		//Discard all spaces, punctuation marks, and words that have less than 4 characters.
		//Remove ending “s” from a word
		line = line.replaceAll("[\\p{Punct}\\pP]", " ");
		String[] s = line.trim().split("\\s+");
		int pos = 0;
		for(int i=0;i<s.length;++i) {
			s[i] = s[i].toLowerCase();
			if(s[i].length() >= 4) {
				if(s[i].charAt(s[i].length()-1) == 's')
		    		s[i] = s[i].substring(0, s[i].length()-1);
				index.addPosting(s[i], id, pos++);
			}
		}
		return list;
	}
	
	public void retrieve(String query) {
		String[] words = changeQuery(query);
		getWeight(words);
		getTopDoc(words);
	}
	
	public void getTopDoc(String[] words){
		for(int i=0;i<doc_num;++i) {
			Doc doc = docList.get(i);
			double a = 0;
			double b = Math.sqrt(words.length);
			double c = doc.getNorm();
			for(int j=0;j<words.length;++j) {
				a += weight[i][j];
			}
			doc.setSimilarity_score(a/(b*c));
			
			//update top 3 document
			for(int k=0;k<3;++k) {
				if(doc.getSimilarity_score()>docList.get(top_document.get(k)).getSimilarity_score()) {
					top_document.add(k, i);
					top_document.removeLast();
					break;
				}
			}
		}
		
		display();
		
	}
	
	public void display() {
		for(int i=0;i<3;++i) {
			int did = top_document.get(i)+1;
			//print did
			System.out.println("DID is : "+did);
			
			//print five highest weighted keywords of the document and the posting lists
			index.toString(docList.get(did-1).getTop_five_words());
			System.out.println();
			
			//print the number of unique keywords in the document
			System.out.println("The number of unique keywords in the document is : "+docList.get(did-1).getUnique_count());
			
			//print the magnitude (L2 norm) of the document vector
			System.out.println("The magnitude of the document vector is : "+docList.get(did-1).getNorm());
			
			//print the similarity score
			System.out.println("The similarity score is : "+docList.get(did-1).getSimilarity_score());
			System.out.println();
		}
	}
	
	//Format a query, and get the keywords.
	public String[] changeQuery(String query) {
		ArrayList<String> list = new ArrayList<String>();
		String[] s = query.trim().split(" ");
		for(int i=0;i<s.length;++i) {
			s[i] = s[i].replaceAll("[\\p{Punct}\\pP]", "");
			s[i] = s[i].toLowerCase();
			
			if(s[i].length() >= 4) {
				if(s[i].charAt(s[i].length()-1) == 's')
		    		s[i] = s[i].substring(0, s[i].length()-1);
				list.add(s[i]);
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
	public void getWeightAll() {
		keyword_num = index.getInverted_file().size();
		weight_all = new double[doc_num][keyword_num];
		tf = new int[doc_num][keyword_num];
		df = new int[keyword_num];
		words_all = new String[keyword_num]; 
		int j=0;
		for(String s : index.getInverted_file().keySet()) {
			words_all[j] = s;
			j++;
		}
		for(j=0;j<keyword_num;++j) {
			if(index.getInverted_file().containsKey(words_all[j])) {
				LinkedList<Posting> posting_list = index.getInverted_file().get(words_all[j]);
				df[j] = posting_list.size();
				for(int i=0;i<df[j];++i) {
					int did = posting_list.get(i).getDid();
					tf[did-1][j] = posting_list.get(i).getPositions().size();
					weight_all[did-1][j] = ((double)(tf[did-1][j])/Index.getTf_max()[did-1])
							* (Math.log((double)doc_num/df[j]) / Math.log((double)2));
				}
			}
		}
	}
	
	public void getWeight(String[] words) {
		int len = words.length;
		weight = new double[doc_num][len];
		for(int i=0;i<doc_num;++i) 
			for(int j=0;j<len;++j) 
				for(int k=0;k<words_all.length;++k) 
					if(words[j].equals(words_all[k])) { 
						weight[i][j] = weight_all[i][k];
					}
	}
	
	public void clear() {
		top_document.clear();
		top_document.add(0);
		top_document.add(1);
		top_document.add(2);
		for(int i=0;i<docList.size();++i) {
			docList.get(i).setSimilarity_score(0);
		}
	}
	
}
