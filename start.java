/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */

import java.io.*;
import java.util.*;


public class start {

    /**
     * @param args the command line arguments
     */

        // TODO code application logic here
    public static void main(String[] args) throws FileNotFoundException, IOException {
    	Book b0 = new Book(0,0.2);
        Book b1 = new Book(1,0.15);
        Book b2 = new Book(2,0.3);
        Book b3 = new Book(3,0.25);
        Book b4 = new Book(4,0.5);
        Book b5 = new Book(5,0.33);
        Book b6 = new Book(6,0.31);
        Book b7 = new Book(7,0.38);
        Book b8 = new Book(8,0.27);
        Book b9 = new Book(9,0.4);
        
        Apriori ap = new Apriori();
        ap.minSup = 0.3;
        ap.totalNum = 6;
        ap.minConf = 0.5;
        ap.MSD = 0.3;
        BookSet MS = ap.MS;
        MS.add(b0);
        MS.add(b1);
        MS.add(b2);
        MS.add(b3);
        MS.add(b4);
        MS.add(b5);
        MS.add(b6);
        MS.add(b7);
        MS.add(b8);
        MS.add(b9);

        GetSource getsource = new GetSource("transaction.txt");
        ap.allTransaction = getsource.GetAll();
        FrequentSets totalFrequentSets = ap.msApriori();
        System.out.println(totalFrequentSets.toString());
        System.out.println(ap.subF);
        for(FrequentSet fSet : ap.subF){
        	for(BookSet bSet : fSet){
        		System.out.println(bSet+"count:"+bSet.count);
        	}
        }
        
        StringWriter os = new StringWriter();
        
        os.append(totalFrequentSets.toString());
        os.append("hahaaaaaaaaaaaaaaaaaaaaa");
        System.out.println(os);
        ap.genRules();

        
    }


}
