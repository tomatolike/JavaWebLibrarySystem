package library;

import java.text.SimpleDateFormat;

public class Borrow {

	//Properties of borrow
	public int id = -1;
	public int bid = -1;
	public String bookname = "";
	public String time = "";
	
	public Borrow(int id, int bid, String bookname, java.sql.Timestamp time)
	{
		this.id = id;
		this.bid = bid;
		this.bookname = bookname;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.time = sdf.format(time);
	}
}
