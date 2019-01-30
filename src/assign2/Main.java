package assign2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		Docs docs = new Docs();
		docs.preProcess();
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("src/assign2/query-10"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String line = reader.readLine();
		do {
			//get a query
			System.out.println("Query : "+line);
			docs.retrieve(line);
			System.out.println("==================================================\n");
			docs.clear();
			line = reader.readLine();
		} while(line != null);
		reader.close();
		
	}
}
