package assign2;

import java.util.ArrayList;
import java.util.LinkedList;

public class Doc {
	private int DID;
	private String[] top_five_words;
	private int unique_count;
	private double norm;
	private double similarity_score;
	

	public Doc(int DID, ArrayList<String> keywords){
		this.DID = DID;
	}
	
	public int getDID() {
		return DID;
	}
	
	public String[] getTop_five_words() {
		top_five_words = new String[5];
		double[] w = Docs.weight_all[DID-1];
		LinkedList<Integer> list = new LinkedList<Integer>();//indexes of five words in Docs.words_all
		for(int i=0;i<5;++i) {
			list.add(i);
		}
		for(int i=0;i<Docs.weight_all[DID-1].length;++i) {
			for(int j=0;j<5;++j) {
				if(Docs.weight_all[DID-1][i]>Docs.weight_all[DID-1][list.get(j)]) {
					list.add(j, i);
					list.removeLast();
					break;
				}
			}
		}
		for(int i=0;i<5;++i) {
			top_five_words[i] = Docs.words_all[list.get(i)];
		}
		return top_five_words;
	}

	public void setTop_five_words(String[] top_five_words) {
		this.top_five_words = top_five_words;
	}

	public int getUnique_count() {
		int count = 0;
		for(int i=0;i<Docs.words_all.length;++i) {
			if(Docs.weight_all[DID-1][i] == 0)
				count++;
		}
		unique_count = Docs.words_all.length-count;
		return unique_count;
	}

	public void setUnique_count(int unique_count) {
		this.unique_count = unique_count;
	}

	public double getNorm() {
		double num = 0;
		for(int i=0;i<Docs.weight_all[DID-1].length;++i) {
			num = num + ((Docs.weight_all[DID-1][i]) * (Docs.weight_all[DID-1][i]));
		}
		norm = Math.sqrt(num);
		return norm;
	}

	public void setNorm(double norm) {
		this.norm = norm;
	}

	public double getSimilarity_score() {
		return similarity_score;
	}

	public void setSimilarity_score(double similarity_score) {
		this.similarity_score = similarity_score;
	}

}
