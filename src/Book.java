import java.sql.*;
import java.util.*;
public class Book {
	private Connection conn; 
	
	
	public Book(Connection conn){
		this.conn = conn ; 
	}
	
	public void printAllAuthors(){
		try{
			Statement stmt;
			stmt = conn.createStatement();
			String sql = "SELECT firstName, lastName from AUTHOR ORDER BY lastName, firstName";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName"); 
				System.out.print("First Name: "+firstName+", Last Name: "+lastName+"\n");
			}
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void printAllPublishers(){
		try{
			Statement stmt;
			stmt = conn.createStatement();
			String sql = "SELECT publisherName from Publisher";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("Publisher Names are -->");
			int i = 0; 
			while(rs.next()){
				String publisherName = rs.getString("publisherName");
				 
				System.out.print(++i+". "+publisherName+"\n");
				
			}
			rs.close();
			stmt.close();
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void listBooksByPublisherEntered(){
		try{
			Scanner in = new Scanner(System.in); 
			System.out.println("Enter Publisher Name -->");			
			String enteredPublisherName = in.nextLine();
			String modified = '%'+enteredPublisherName+'%';
			PreparedStatement stmt;
			
			//REQUIRED QUERY
			String sql = "SELECT title, copyright, ISBN, publisherName"+
						 " FROM Title, Publisher"+
						 " WHERE Title.publisherID = Publisher.publisherID"+
						 " AND PublisherName like ?"+
						 " ORDER BY Title.title";
			
			//PREPARE STATEMENT
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, modified);
			
			ResultSet rs = stmt.executeQuery();
			System.out.println("We have the following books with the publisher matching ** "+ enteredPublisherName +" -->");
			int i = 0; 
			while(rs.next()){
				String title = rs.getString("title");
				String publisher = rs.getString("publisherName");
				System.out.print(++i + ". "+title+ " published by --> "+ publisher +"\n");
			}
			rs.close();
			stmt.close();
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	public void addNewAuthor() throws SQLException {
		try{
		Scanner in = new Scanner(System.in);
		System.out.println("Enter author's full name");
		String fullName = in.nextLine();
		String[] splitName = fullName.split("\\s");
		String firstName = splitName[0];
		String lastName = splitName[1];
		PreparedStatement stmt;
		String insertIntoAuthorSQL = "INSERT INTO AUTHOR (firstname, lastname) "+
										   "VALUES (?, ?)";
		stmt = conn.prepareStatement(insertIntoAuthorSQL);
		stmt.setString(1, firstName);
		stmt.setString(2, lastName);
		stmt.executeUpdate();
		System.out.println("Data entered into author Database");
		stmt.close();
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void changeAuthorInformation(){
		//enter author ID and lastName and check
		//if it exists in the database
		//if true, change, else dont 
		try{
			Scanner in = new Scanner(System.in);
			PreparedStatement checkstmt = null;
			System.out.println("Enter the author's ID");
			int author_id = in.nextInt();
			in.nextLine();
			System.out.println("Enter the author's last name");
			String author_lastName = in.nextLine();
			
			String authorTupleQuery = "SELECT * "+
									  "FROM Author "+
									  "WHERE authorID = ? AND lastName = ?";
			checkstmt = conn.prepareStatement(authorTupleQuery);
			checkstmt.setInt(1, author_id);
			checkstmt.setString(2, author_lastName);
			ResultSet checkResultsSET = checkstmt.executeQuery();
			//checkstmt.close();
			
//          check if author is available in the database.
			if(!checkResultsSET.next()){
				System.out.println("There are no records in the database");
				checkstmt.close();
			}
//          enter else statement here
			else
			{
				checkstmt.close();
			System.out.println("Record found, enter the Author's NEW FULL NAME seperated by a space -->");
			String fullName = in.nextLine();			
			String[] splitName = fullName.split("\\s");
			String firstName = splitName[0];
			String lastName = splitName[1];
			PreparedStatement stmt = null;
			String updateAuthorSQL = "UPDATE AUTHOR SET firstName=?, lastName=? "+
											   "WHERE lastName = ? AND authorID = ?";
			stmt = conn.prepareStatement(updateAuthorSQL);
			stmt.setString(1, firstName);
			stmt.setString(2, lastName);
			stmt.setString(3, author_lastName);
			stmt.setInt(4, author_id);
			
			stmt.executeUpdate();
			System.out.println("Data updated in author relation");
			stmt.close();
			in.close();
			}
			}
			catch(SQLException se){
				se.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	
	
	public void addTitleForAnAuthor(){ 
//		Step 1. add a #isbn,title,edition number,copyright,publisherID,price into title table 
// 
//		Step 2. enter the author for this title.  NOT INTO TABLE 
// 
//		Step 3. get the #ID of this author from AUTHOR TABLE 
// 
//		Step 4. ADD INTO AUTHORISBN TABLE
//		        ->  the #isbn and the #ID --
		try{ 
			Scanner in = new Scanner(System.in); 
			PreparedStatement stmt = null; 
			String title; 
			char ISBN; 
			int editionNumber;  
			char copyright;  
			double bookPrice; 
			String insertIntoTITLE = "INSERT INTO AUTHOR (isbn, title, editionNumber, copyright, publisherID, price)"+
							"VALUES(?,?,?,?,?,?)";
			//prepare the statements
			//get author first name and last name
			String author_first_name; // unfinished 
			String author_last_name;  // unfinished
			String getAuthorInformation ="SELECT * from Author WHERE "+
										 "firstName = ? AND lastName = ?";
			//extract ID information from preparedquery 
			int author_ID;			
			String insertIntoAUTHORISBN = "INSERT INTO authorISBN(authorID, isbn) VALUES (?,?)";
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
				
	}
	
	public void addNewPublisher(){
		try{
			Scanner in = new Scanner(System.in);
			System.out.println("Enter new publisher name");
			String publisherName = in.nextLine();
			PreparedStatement stmt;
			String insertIntoPublisherSQL = "INSERT INTO PUBLISHER (publisherName) "+
											   "VALUES (?)";
			stmt = conn.prepareStatement(insertIntoPublisherSQL);
			stmt.setString(1, publisherName);
			stmt.executeUpdate();
			System.out.println("Data entered into publisher Table");
			stmt.close();
			}
			catch(SQLException se){
				se.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		
	}
	
	public void changePublisherInformation(){
		
	}
	  

}
	

