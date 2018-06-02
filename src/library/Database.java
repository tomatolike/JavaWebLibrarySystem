package library;

import java.sql.*;

public class Database {
	
	//Database Driving Info	public Database()
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/Library";
	
	//Database User Info
	static final String User = "root";
	static final String Pass = "1997lk421";
	
	//Connection
	static Connection conn = null;
	static PreparedStatement stmt = null;
	
	public void connect() 
	{
		// Register JDBC driver
				try {
					Class.forName(JDBC_DRIVER);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Establish the connection
				try {
					conn = DriverManager.getConnection(DB_URL, User, Pass);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	}
	
	public void disconnect()
	{
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int checkuser(String username, String password)
	{
		String sql = "select * from User where username = ? and pass = ?;";
		System.out.println(username);
		System.out.println(password);
		try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, username);
			stmt.setString(2, password);
			
			ResultSet rs = stmt.executeQuery();
			
			if(rs.wasNull()){
				System.out.println("Not Found");
				rs.close();
				stmt.close();
				return 0;
			}
			else {
				String type="";
				while(rs.next()) {
					System.out.println("Found");
					type = rs.getString("type");
					
				}
				rs.close();
				stmt.close();
				System.out.println(type);
				if(type.equals("S"))return 1;
				else if(type.equals("M"))return 2;
				else return 0;
			} 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}

	public Book[] searchbook(String name)
	{
		Book[] booklist = new Book[0];
		
		String sql = "select * from Book where name like ?;";
		System.out.println(name);
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%"+name+"%");
			ResultSet res = stmt.executeQuery();
			System.out.println(name);
			res.last();
			System.out.println("find out "+String.valueOf(res.getRow()));
			booklist = new Book[res.getRow()];
			res.beforeFirst();
			
			while(res.next())
			{
				System.out.println("????");
				Book newbook = new Book(res.getInt("bid"),res.getString("name"),res.getInt("numbers"));
				booklist[res.getRow()-1] = newbook;
				System.out.println(booklist[res.getRow()-1].name);
			}
			res.close();
			stmt.close();
			return booklist;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return booklist;
		}	
		
	}
	
	public boolean borrowbook(String username, int bid)
	{
		if(this.insertborrow(username,bid))
		{
			String sql = "update Book set numbers = numbers - 1 where bid = ?;";
			
			try {
				stmt = conn.prepareStatement(sql);
				
				stmt.setInt(1, bid);
				
				int cout = stmt.executeUpdate();
				stmt.close();
				if(cout>0) {
					return true;
				}
				else {
					return false;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	private boolean insertborrow(String username, int bid)
	{
		int id = this.getuserid(username);
		if(id!=-1)
		{
			String sql = "insert into Borrow(id,bid,time) values(?,?,?);";
			
			try {
				stmt = conn.prepareStatement(sql);
				
				stmt.setInt(1, id);
				stmt.setInt(2, bid);
				stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				
				int cout = stmt.executeUpdate();
				stmt.close();
				if(cout>0)return true;
				else return false;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	private int getuserid(String username)
	{
		String sql = "select * from User where username = ?;";
		int id = -1;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			ResultSet res = stmt.executeQuery();
			while(res.next())
			{
				id = res.getInt("id");
			}
			res.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	public Borrow[] borrowlist(String username)
	{
		Borrow[] borrowlist = new Borrow[0];
		int id = this.getuserid(username);
		if(id==-1) return borrowlist;
		
		String sql = "select bid,name,time from Borrow natural join Book where id = ?;";
		
		try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, id);
			
			ResultSet res = stmt.executeQuery();
			
			res.last();
			borrowlist = new Borrow[res.getRow()];
			res.beforeFirst();
			
			while(res.next())
			{
				Borrow newborrow = new Borrow(id,res.getInt("bid"),res.getString("name"),res.getTimestamp("time"));
				borrowlist[res.getRow()-1] = newborrow;
			}
			res.close();
			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return borrowlist;
		
	}

	public boolean returnbook(String username, int bid)
	{
		if(!this.doreturn(username, bid))return false;
		
		String sql = "update Book set numbers = numbers + 1 where bid = ?;";
		
		try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, bid);
			
			int cout = stmt.executeUpdate();
			stmt.close();
			if(cout>0) {
				return true;
			}
			else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean doreturn(String username, int bid)
	{
		int id = this.getuserid(username);
		if(id==-1)return false;
		
		String sql = "delete from Borrow where id = ? and bid = ? and time in (select time from (select * from Borrow having id = ? and bid = ? order by time desc limit 1) as A);";
		
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.setInt(2, bid);
			stmt.setInt(3, id);
			stmt.setInt(4, bid);
			
			int cout = stmt.executeUpdate();
			stmt.close();
			
			if(cout>0)return true;
			else return false;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean addbook(String bookname, int number)
	{
		String sql = "insert into Book(name,numbers) values(?,?);";
		
		try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, bookname);
			stmt.setInt(2, number);
			
			int count = stmt.executeUpdate();
			
			stmt.close();
			
			if(count>0)return true;
			else return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	public boolean deletebook(int bid, int number)
	{
		if(!this.numbercheck(bid,number))return false;
		
		String sql = "update Book set numbers = numbers - ? where bid = ?;";
		
		try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, number);
			stmt.setInt(2, bid);
			
			int count = stmt.executeUpdate();
			
			stmt.close();
			
			if(count>0)return true;
			else return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	}
	
	private boolean numbercheck(int bid, int number)
	{
		String sql = "select * from Book where bid = ? and numbers >= ?;";
		
		try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, bid);
			stmt.setInt(2, number);
			
			ResultSet res = stmt.executeQuery();
			
			if(res.wasNull()) {
				res.close();
				stmt.close();
				return false;
			}
			else {
				res.close();
				stmt.close();
				return true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
}