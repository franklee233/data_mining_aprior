
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.io.*;
import java.util.*;

public class GetSource {
	//Data
	FileInputStream file ; //source file
	String fName ; //store source file name
	//BufferedReader br;
        Scanner sc;
	
	LinkedList list;
	/*public GetSource(String filename) throws FileNotFoundException{
		file  = new FileInputStream(filename) ;
	    br=new BufferedReader(new InputStreamReader(file)); 
		fName = filename ;
	}
         *
         */
        public GetSource(String filename) throws FileNotFoundException{
            file = new FileInputStream(filename);
            fName = filename;
            sc = new Scanner(file);

        }


	public void Reset() throws IOException,FileNotFoundException{ //reload source file
		file.close();
		file = new FileInputStream(fName) ;
		//br=new BufferedReader(new InputStreamReader(file));
                sc = new Scanner(file);
	}
	
	public int NumOfSets() throws IOException,FileNotFoundException{ //return the number of sets
		int num = 0 ;
		Reset();
		while(sc.hasNextLine()){
		    num ++;
		}
//		System.out.println("the line is "+num);				
		return num ;
	}
	
	public LinkedList<Book> GetSet(int k) throws IOException{ //get the kth set,return it as a vector
		int n=0;                                         //begin from 0
		String s=null;
		BookSet singleTransaction = new BookSet();
		Reset();
		while(!(n == k)){ //locate the kth set
			sc.nextLine();
			//s=br.readLine().split("\\s");
			n++ ;		
		}
		
		s = sc.nextLine();
                Scanner sc1 = new Scanner(s);
                while(sc1.hasNext()){
                    Book book = new Book(sc1.nextLong());
                    singleTransaction.add(book);                                    
                }

		return singleTransaction ;
	}


	
	public AllTransaction GetAll() throws IOException{ //return a vector including all sets
		//int n = NumOfSets();
//		System.out.println(n);
                Reset();
		int i = 0;
                String s;
                Scanner sc1;
                BookSet singleTransaction = null;
		AllTransaction allTransaction = new AllTransaction();
                while(sc.hasNext()){
                    s = sc.nextLine();
                    sc1 = new Scanner(s);
                    singleTransaction = new BookSet();
                    while(sc1.hasNext()){
                        //allTransaction = null;
                        Book book = new Book(sc1.nextLong());
                        singleTransaction.add(book);
                    }
                    allTransaction.add(singleTransaction);
          
                }
		return allTransaction;
	}


}
