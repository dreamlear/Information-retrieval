package assign2;

import java.util.HashMap;
import java.util.LinkedList;

public class Index {
	private HashMap<String, LinkedList<Posting>> inverted_file;
	private static int[] tf_max;
	
	public Index() {
		inverted_file = new HashMap<String, LinkedList<Posting>>();
		tf_max = new int[Docs.getDocnum()];
	}
	
	public static int[] getTf_max() {
		return tf_max;
	}
	
	public HashMap<String, LinkedList<Posting>> getInverted_file() {
		return inverted_file;
	}
	
	public void addPosting(String word, int id, int position) {
		LinkedList<Posting> posting_list;
		Posting posting = new Posting(id);
		if(inverted_file.containsKey(word)) {
			posting_list = inverted_file.get(word);
			
			//add new position to the old posting
			int i;
			for(i=0;i<posting_list.size();++i) {
				if(id == posting_list.get(i).getDid()) {
					posting_list.get(i).addPosition(position);
					if(posting_list.get(i).getPositions().size()>tf_max[id-1]) {
						tf_max[id-1] = posting_list.get(i).getPositions().size();
					}
					break;
				}
			}
			
			//create new posting
			if(i == posting_list.size()) {
				posting.addPosition(position);
				posting_list.add(posting);
			}
		}
		else {
			posting_list = new LinkedList<Posting>();
			posting.addPosition(position);
			posting_list.add(posting);
			inverted_file.put(word, posting_list);
		}
		if(posting.getPositions().size()>tf_max[id-1]) {
			tf_max[id-1] = posting.getPositions().size();
		}
		
	}
	
	public void toString(String[] words) {
		String s = "";
		for(int i=0;i<words.length;++i) {
			if(inverted_file.containsKey(words[i])) {
				System.out.print(words[i]+"\t"+"-> | ");
				for(int j=0;j<inverted_file.get(words[i]).size();++j) {
					System.out.print(inverted_file.get(words[i]).get(j).toString());
				}
				System.out.println();
			}
		}
	}
}
