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

public class BookSet extends LinkedList<Book> {
    public int count= 0;


    @Override
	public boolean equals(Object obj) {

		if (this.size()!= ((BookSet)obj).size()){
			return false;
		}
		Iterator<Book> it1 = this.iterator();
		Iterator<Book> it2 =((BookSet)obj).iterator();
		while (it1.hasNext()) {
			Book f1 = it1.next();
			Book f2 = it2.next();
			if (f1.getBook_id() != f2.getBook_id()) {
				return false;
			}
		}
		return true;
	}
    /*
    @Override
        public String toString(){
            String tostring = null;
            Iterator<Book> it1 = this.iterator();
            while(it1.hasNext()){
                Book tmpbook = it1.next();
                tostring = tmpbook.toString() + ", ";
            }
            return tostring;
        }
     *
     */

    public boolean removeAll(BookSet bSet){
        Iterator <Book> it = bSet.iterator();
        while(it.hasNext()){
            this.remove(it.next());

        }
        return true;
    }

    @Override
    public Object clone(){
        BookSet bookset = (BookSet)super.clone();
        bookset.count = this.count;
        return bookset;

    }

    	public boolean containsAll(BookSet bSet){
		 int i,j = 0;
		     for( i = 0; i < bSet.size(); i++){
		    	 Book bookC = bSet.get(i);
		    	 for( j = 0; j < this.size();j++){
		    		 Book bookT = this.get(j);
		    		 if(bookC.getBook_id() == bookT.getBook_id())
		    			 break;
		    	 }
		    	 if(j>=this.size())
		    		 return false;

		     }
		return true;


	}






}
