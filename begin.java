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
import java.sql.*;


public class begin {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
        // TODO code application logic here

        Apriori ap = new Apriori();
        ap.minSup = 0.2;
        ap.minConf = 0.3;
        ap.MSD = 0.5;

        //GetSource getsource = new GetSource("transaction.txt");
        //ap.allTransaction = getsource.GetAll();

        ArrayList<String> tag1 = new ArrayList<String>();
        tag1.add("java");
        //tag1.add("introduction");
        if(DBConn.conn == null){
            new DBConn();
        }

        ArrayList<Integer> commonidlist1 = ap.getIDList(tag1);
        System.out.println();
        ap.classifyTransaction(commonidlist1, 1000);
        ArrayList<Integer> allTid = ap.getAllTid();
        ap.getAllTransaction(allTid);
        ap.MS = ap.getAllBook();
        System.out.println("MS:"+ap.MS);
        System.out.println("allTransaction:"+ap.allTransaction);
        ap.totalNum = allTid.size();
        System.out.println("totalNum:"+ap.totalNum);
        FrequentSets totalFrequentSets = ap.msApriori();
        System.out.println(totalFrequentSets.toString());
        System.out.println(ap.subF);
        for(FrequentSet fSet : ap.subF){
        	for(BookSet bSet : fSet){
        		System.out.println(bSet+"count:"+bSet.count);
        	}
        }
        ap.genRules();
        System.out.println("bookCount:"+ap.bookCount);
    }

}
