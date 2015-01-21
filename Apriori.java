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

public class Apriori {
	double minSup, minConf;

	ArrayList<Integer> allTid;
	AllTransaction allTransaction;
	BookSet booklist = new BookSet();
	//ArrayList<Double> bookcount = new ArrayList<Double>();
	//ArrayList<Integer> bCount = new ArrayList<Integer>();
	HashMap<Integer, Integer> bookCount = new HashMap<Integer, Integer>(1024);

	BookSet MS = new BookSet();
	double MSD;

	FrequentSets F = null;
	FrequentSets subF = new FrequentSets();

	CandidateSet C1;

	int totalNum;

	public FrequentSets msApriori() {
		F = new FrequentSets();
		BookSet L = init_pass(MS, allTransaction);
		// System.out.println(bookcount);
		FrequentSet F1 = new FrequentSet();
		Iterator<BookSet> it = C1.iterator();
		while (it.hasNext()) {
			BookSet f = it.next();
			if (((double) f.count) / totalNum >= f.getFirst().mis) {
				F1.add(f);
			}
		}
		F.add(F1);
		System.out.println(F1);
		for (int k = 1; !F.get(k - 1).isEmpty(); k++) {
			CandidateSet can;
			if (k == 1)
				can = level2CandidateGen(L, MSD);
			else
				can = MScandidateGen(F.get(k - 1), MSD);
			it = allTransaction.iterator();
			while (it.hasNext()) {
				BookSet t = it.next();
				Iterator<BookSet> itCan = can.iterator();
				FrequentSet temp = new FrequentSet();
				while (itCan.hasNext()) {
					BookSet c = itCan.next();
					if (t.containsAll(c))
						c.count++;
					BookSet c_c1 = getC_C1(k - 1, c);
					if (t.containsAll(c_c1) && !temp.contains(c_c1)) {
						c_c1.count++;
						temp.add(c_c1);
					}
				}
			}
			FrequentSet fSet = new FrequentSet();
			Iterator<BookSet> itFSet = can.iterator();
			while (itFSet.hasNext()) {
				BookSet b = itFSet.next();
				if (((double) b.count) / totalNum >= b.getFirst().mis) {
					fSet.add(b);
				}
			}
			F.add(fSet);

		}
		return F;

	}

	private BookSet init_pass(BookSet m, AllTransaction allTransaction2) {
		booklist = (BookSet) m.clone();
		C1 = new FrequentSet();
		for (Book b : booklist) {
			// bCount.add(0);
			bookCount.put((int) b.book_id, 0);
		}
		Iterator<BookSet> it;

		it = allTransaction2.iterator();
		while (it.hasNext()) {
			BookSet bSet = it.next();
			for (Book book : bSet) {
				/*
				 * int temp = bCount.get((int) (book.getBook_id())).intValue();
				 * temp++; bCount.set((int) (book.getBook_id()), new
				 * Integer(temp));
				 */
				int temp = bookCount.get((int) book.book_id);
				temp++;
				bookCount.put((int) book.book_id, temp);
			}

		}
		/*
		 * for (Integer i : bCount) { double temp = (double) i / totalNum;
		 * bookcount.add(temp); }
		 */
		Collections.sort(booklist);
		for (int i = 0; i < booklist.size(); i++) {
			BookSet temp = new BookSet();
			temp.add(booklist.get(i));
			C1.add(temp);
			// temp.count = bCount.get((int) (temp.getFirst().getBook_id()));
			temp.count = bookCount.get((int) booklist.get(i).book_id);
		}
		return booklist;

	}

	private BookSet getC_C1(int k, BookSet c) {
		BookSet bSet = (BookSet) c.clone();
		bSet.removeFirst();
		bSet.count = 0;
		if (subF.size() == k) {
			FrequentSet f = new FrequentSet();
			f.add(bSet);
			subF.add(f);
			return bSet;
		}
		FrequentSet fSet = subF.get(k);
		Iterator<BookSet> it = fSet.iterator();
		while (it.hasNext()) {
			BookSet b = it.next();
			if (b.equals(bSet)) {
				return b;
			}
		}
		fSet.add(bSet);
		return bSet;
	}

	private CandidateSet MScandidateGen(FrequentSet fSet, double diff) {
		CandidateSet can = new FrequentSet();

		for (int i = 0; i < fSet.size(); i++) { // 合并
			BookSet f1 = fSet.get(i);
			for (int j = i + 1; j < fSet.size(); j++) {
				BookSet f2 = fSet.get(j);
				if (isMerge(f1, f2, diff)) {
					BookSet merge = new BookSet();
					if (f1.getLast().getMis() <= f2.getLast().getMis()) {
						merge.addAll(f1);
						merge.addLast(f2.getLast());
					} else {
						merge.addAll(f2);
						merge.addLast(f1.getLast());
					}
					if (isCandidate(merge, fSet)) // 剪枝
						can.add(merge);

				}
			}
		}

		return can;
	}

	private CandidateSet level2CandidateGen(BookSet L, double diff) {
		CandidateSet C2 = new FrequentSet();
		// Iterator<Book> it = L.iterator();
		for (int i = 0; i < L.size(); i++) {
			Book l = L.get(i);

			// double lCount = (double) bookcount.get((int) l.book_id);
			double lCount = (double) bookCount.get((int) l.book_id) / totalNum;
			if (lCount >= l.mis) {
				for (int j = i + 1; j < L.size(); j++) {
					Book h = L.get(j);
					// double hCount = (double) bookcount.get((int) h.book_id);
					double hCount = (double) bookCount.get((int) h.book_id)
							/ totalNum;
					if (hCount >= l.mis && Math.abs(lCount - hCount) < diff) {
						BookSet newBookSet = new BookSet();
						newBookSet.add(l);
						newBookSet.add(h);
						C2.add(newBookSet);
					}
				}
			}

		}
		return C2;
	}

	private boolean isCandidate(BookSet merge, FrequentSet fSet) {
		for (int i = 0; i < merge.size(); i++) {
			BookSet tempBookSet = (BookSet) merge.clone();
			tempBookSet.remove(i);
			if (tempBookSet.get(0).equals(merge.get(0))
					|| (tempBookSet.get(0).mis == tempBookSet.get(1).mis)) {
				if (!fSet.contains(tempBookSet))
					return false;
			}
		}
		return true;
	}

	private boolean isMerge(BookSet f1, BookSet f2, double diff) {
		for (int k = 0; k < f2.size() - 1; k++) {
			if (f1.get(k).getBook_id() != f2.get(k).getBook_id())
				return false;
		}
		if (f1.getLast().getBook_id() != f2.getLast().getBook_id())
			if (Math.abs((double) bookCount.get((int) f1.getLast().book_id)
					/ totalNum
					- (double) bookCount.get((int) f2.getLast().book_id)
					/ totalNum) < diff) {
				return true;
			}
		return false;

	}

	/* generator the rules */
	public void genRules() {
		for (int i = 1; i < F.size(); i++) {
			FrequentSet fSet = F.get(i);
			Iterator<BookSet> it = fSet.iterator();
			while (it.hasNext()) {
				BookSet bookSet = (BookSet) it.next().clone();
				FrequentSet H = new FrequentSet();
				int flag = 0;
				for (int j = bookSet.size() - 1; j >= 0; j--) {
					Book book = (Book) bookSet.get(j).clone();
					BookSet tail = new BookSet();
					tail.add(book);
					BookSet front = (BookSet) bookSet.clone();
					front.remove(book);
					if (j > 0) {
						FrequentSet tmpA = F.get(front.size() - 1);
						int sizeA = tmpA.size();
						for (int k = flag; k < sizeA; k++) {
							BookSet bSet = tmpA.get(k);
							if (bSet.equals(front)) {
								front.count = bSet.count;
								flag = k;
								break;
							}
						}
					} else {
						FrequentSet tmpB = subF.get(front.size() - 1);
						int sizeB = tmpB.size();
						for (int k = 0; k < sizeB; k++) {
							BookSet bSet = tmpB.get(k);
							if (bSet.equals(front)) {
								front.count = bSet.count;
								break;
							}
						}

					}
					if (front.count != 0) {
						double conf = (double) bookSet.count / front.count;
						if (conf >= minConf) {
							H.addFirst(tail);
							System.out.println(front + "-->" + tail);
							System.out.println("Confidence:" + conf
									+ "  Support:" + (double) bookSet.count
									/ totalNum);
						}
					}

				}
				ap_genRules(bookSet, H, 1);
			}
		}
	}

	private void ap_genRules(BookSet f, FrequentSet H, int m) {
		if ((f.size() > m + 1) && (!H.isEmpty())) {
			FrequentSet canH = new FrequentSet();
			if (m == 1) {
				BookSet L = new BookSet();
				for (BookSet b : H) {
					L.add(b.getFirst());
				}
				canH = (FrequentSet) level2CandidateGen(L, MSD);
			} else {
				canH = (FrequentSet) MScandidateGen(H, MSD);
			}
			Iterator<BookSet> it = canH.iterator();
			while (it.hasNext()) {
				BookSet h = it.next();
				BookSet f_h = getRest(f, h);
				double conf = (double) f.count / f_h.count;
				if (conf >= minConf) {
					System.out.println(f_h + "-->" + h);
					System.out.println("Confidence:" + conf + "  Support:"
							+ (double) f.count / totalNum);
				} else {
					// canH.remove(h);
					it.remove();
				}
			}
			ap_genRules(f, canH, m + 1);
		}
	}

	private BookSet getRest(BookSet f, BookSet h) {
		int size = f.size() - h.size();
		BookSet temp = (BookSet) f.clone();
		temp.removeAll(h);
		if (!h.contains(f.getFirst())) {
			Iterator<BookSet> it = F.get(size - 1).iterator();
			while (it.hasNext()) {
				BookSet c = it.next();
				if (c.equals(temp)) {
					temp.count = c.count;
					return temp;
				}

			}
		} else {
			Iterator<BookSet> it = subF.get(size - 1).iterator();
			while (it.hasNext()) {
				BookSet c = it.next();
				if (c.equals(temp)) {
					temp.count = c.count;
					return temp;
				}

			}
		}
		return temp;

	}

	/*
	 * 涉及数据库部分
	 */

	public ArrayList<Integer> getAllTid() throws SQLException {
		ArrayList alltid = new ArrayList();
		String sql = "SELECT DISTINCT tid FROM transaction";
		ResultSet rs = DBConn.executeQuery(sql);
		while (rs.next()) {
			alltid.add(rs.getInt("tid"));

		}
		return alltid;
	}

	public void getAllTransaction(ArrayList<Integer> alltid)
			throws SQLException {
		// 此处，transaction里的book，似乎不需要mis值，似乎可删去
		allTransaction = new AllTransaction();
		String sql = "SELECT transaction.id,mis " + "FROM transaction,item "
				+ "WHERE tid = ? and transaction.id = item.id";
		PreparedStatement pstmt = DBConn.conn.prepareStatement(sql);
		for (int i = 0; i < alltid.size(); i++) {
			pstmt.setInt(1, alltid.get(i).intValue());
			ResultSet rs = pstmt.executeQuery();
			BookSet trans = new BookSet();
			while (rs.next()) {
				trans.add(new Book((long) rs.getInt("id"), rs.getDouble("mis")));
			}
			this.allTransaction.add(trans);
		}

	}

	public void classifyTransaction(ArrayList<Integer> idlist, long newid) {
		// 改写数据库数据，划分出等价类
		String ids = "";
		Iterator<Integer> it = idlist.iterator();
		if (it.hasNext())
			ids += it.next();
		while (it.hasNext()) {
			ids += "," + it.next();
		}
		String sql1 = "INSERT INTO transaction(tid,id) "
				+ "SELECT DISTINCT tid," + newid + " FROM transaction "
				+ " WHERE id IN (" + ids + ")";
		String sql2 = "DELETE " + "FROM transaction " + " WHERE id IN (" + ids
				+ ")";
		DBConn.executeUpdate(sql1);
		DBConn.executeUpdate(sql2);

		// String sql2 = "select * from transaction where tid = ";
	}

	public ArrayList<Integer> getIDList(ArrayList<String> taglist)
			throws SQLException {
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
		Iterator<String> it = taglist.iterator();
		String sql = "select id " + "from tag " + "where tagname = ?";
		PreparedStatement pstmt = DBConn.conn.prepareStatement(sql);
		while (it.hasNext()) {
			String tag = it.next();
			pstmt.setString(1, tag);
			ResultSet rs = pstmt.executeQuery();
			ArrayList<Integer> temp = new ArrayList<Integer>();
			while (rs.next()) {
				temp.add(rs.getInt("id"));
			}
			list.add(temp);

		}
		return this.getSameID(list);

	}

	public ArrayList<Integer> getSameID(ArrayList<ArrayList<Integer>> list) {
		ArrayList<Integer> commonid = new ArrayList<Integer>();
		return list.get(0);
	}

	public BookSet getAllBook() throws SQLException {
		// return AS MS
		BookSet bookset = new BookSet();
		String sql = "select id,mis" + " from item";
		ResultSet rs = DBConn.executeQuery(sql);
		while (rs.next()) {
			long book_id = rs.getInt("id");
			double mis = rs.getDouble("mis");
			bookset.add(new Book(book_id, mis));
		}
		return bookset;

	}

	/*
	 * 
	 * public BookSet getSingleTransaction(int tid) throws SQLException{ BookSet
	 * bookset = new BookSet();
	 * 
	 * if (DBConn.conn == null) new DBConn(); PreparedStatement ps =
	 * DBConn.conn.prepareStatement(
	 * "SELECT id FROM 'transaction' WHERE tid = ?");
	 * 
	 * }
	 */

}

class FrequentSets extends ArrayList<FrequentSet> {
	// int frequentSetLayer = this.size();
	/*
	 * @Override public String toString(){ String tostring = null;
	 * Iterator<FrequentSet> it1= this.iterator(); while(it1.hasNext()){
	 * tostring = it1.next().toString() + "\r\n";
	 * 
	 * } return tostring; }
	 */
}

class AllTransaction extends LinkedList<BookSet> {

}

class CandidateSet extends LinkedList<BookSet> {

}

class Rule {
	BookSet head;
	BookSet tail;
	double sup;
	double conf;

	public Rule() {

	}

	public Rule(BookSet head, BookSet tail, double sup, double conf) {
		this.head = head;
		this.tail = tail;
		this.sup = sup;
		this.conf = conf;
	}

}

class FrequentSet extends CandidateSet {

}

class BookList extends LinkedList<Book> {

}
