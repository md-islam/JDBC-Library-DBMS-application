import java.sql.*;
import java.util.*;
public class Book {
	private Connection conn; 
	private Statement stmt;
	
	public Book(Connection conn){
		this.conn = conn ; 
	}
	
	public void printAllAuthors(){
		try{
			stmt = conn.createStatement();
			String sql = "SELECT firstName, lastName from AUTHOR";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName"); 
				System.out.print("First Name: "+firstName+", Last Name: "+lastName+"\n");
			}
		}
		catch(Exception se){
			se.printStackTrace();
		}
	}
	
}
