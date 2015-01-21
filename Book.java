import java.util.*;

public class Book implements Comparable<Book>{

    long book_id;
    //long label_id;
    double mis;
    ArrayList tag_id = null;

    
    public Book(){
        
    }


    @Override
    public String toString(){
    	return ""+ book_id + ":" + mis;
    }


    @Override
    public boolean equals(Object obj) {
		if (this.book_id != ((Book)(obj)).book_id) {
			return false;
		}
		return true;
	}

    @Override
    public Object clone(){
        Book book = new Book(this.book_id,this.mis);
        //this.mis = book.mis;
        book.tag_id = this.tag_id;
        return book;
    }
    public Book(long book_id,double mis){
    	this.book_id = book_id;
    	this.mis = mis;
    }
    public Book(long book_id){
        this.book_id = book_id;
    }




    public long getBook_id() {
        return book_id;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public Double getMis(){
    	return mis;
    }

    public void setMis(double mis) {
        this.mis = mis;
    }

    public ArrayList getTag_id() {
        return tag_id;
    }

    public void setTag_id(ArrayList tag_id) {
        this.tag_id = tag_id;
    }


	@Override
	public int compareTo(Book o) {
		return this.getMis().compareTo(o.getMis());
	}
    
}
