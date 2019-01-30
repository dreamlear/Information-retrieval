package assign2;

import java.util.ArrayList;

public class Posting {
	private int did;
	private ArrayList<Integer> positions;
	
	public Posting(int did) {
		this.did = did;
		positions = new ArrayList<Integer>();
	}
	
	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public ArrayList<Integer> getPositions() {
		return positions;
	}

	public void setPositions(ArrayList<Integer> positions) {
		this.positions = positions;
	}
	
	public void addPosition(int position) {
		positions.add(position);
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder("");
		s.append("D"+did+":");
		int len = positions.size();
		for(int i=0;i<len;++i) {
			s.append(positions.get(i));
			if(i != len-1)
				s.append(",");
		}
		s.append("\t"+"| ");
		return s.toString();
	}
	
}
