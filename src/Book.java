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
			System.out.println("Enter the title of the book");
			String title = in.nextLine();
			System.out.println("Enter the ISBN of the book");
			String ISBN = in.nextLine();
			System.out.println("Enter the edition number of the book");
			int editionNumber = in.nextInt();
			in.nextLine();
			System.out.println("Enter the copyright information of the book");
			String copyright = in.nextLine();
			System.out.println("Enter publisher ID");
			int publisherID = in.nextInt();
			in.nextLine();
			System.out.println("Enter book price");
			double bookPrice = in.nextDouble();
			in.nextLine();
			
			
			//insert into TITLE TABLE
			String insertIntoTITLE = "INSERT INTO TITLE (isbn, title, editionNumber, copyright, publisherID, price)"+
							"VALUES(?,?,?,?,?,?)";
			//prepare the statements
			stmt = conn.prepareStatement(insertIntoTITLE);
			stmt.setString(1, ISBN);
			stmt.setString(2, title);
			stmt.setInt(3, editionNumber);
			stmt.setString(4, copyright);
			stmt.setInt(5, publisherID);
			stmt.setDouble(6, bookPrice);
			stmt.executeUpdate();
			
			
			System.out.println("Enter the full name of the author! --> ");
			//get author first name and last name
			String fullName = in.nextLine();
			String[] splitName = fullName.split("\\s");
			String author_first_name = splitName[0];
			String author_last_name = splitName[1];
			String getAuthorInformation ="SELECT * from Author WHERE "+
										 "firstName = ? AND lastName = ?";
			stmt = conn.prepareStatement(getAuthorInformation);
			stmt.setString(1,author_first_name);
			stmt.setString(2, author_last_name);
			ResultSet rs = stmt.executeQuery();
			
			//extract ID information from preparedquery 
			int extracted_author_ID = rs.getInt("authorID");			
			
			
			
			//final insert into author isbn table to complete this section. 
			String insertIntoAUTHORISBN = "INSERT INTO authorISBN(authorID, isbn) VALUES (?,?)";
			stmt = conn.prepareStatement(insertIntoAUTHORISBN); 
			stmt.setInt(1, extracted_author_ID);
			stmt.setString(2, ISBN); 
			stmt.executeUpdate();
			
			stmt.close();
			
			
		}
		catch(SQLException se){
			se.printStackTrace();
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
		//enter author ID and lastName and check
		//if it exists in the database
		//if true, change, else dont 
		try{
			Scanner in = new Scanner(System.in);
			PreparedStatement checkstmt = null;
			System.out.println("Enter the publisher's ID");
			int publisher_id = in.nextInt();
			in.nextLine();
			System.out.println("Enter the publisher's name correctly");
			String publisher_name = in.nextLine();
			String modified = '%'+publisher_name+'%';
			String publisherTupleQuery = "SELECT * "+
									  "FROM Publisher "+
									  "WHERE publisherID = ? AND publisherName like ?";
			checkstmt = conn.prepareStatement(publisherTupleQuery);
			checkstmt.setInt(1, publisher_id);
			checkstmt.setString(2, modified);
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
			System.out.println("Record found, enter the Publisher's NEW NAME seperated by a space -->");
			String newPublisherName = in.nextLine();			
			PreparedStatement stmt = null;
			String updatePublisherTuple = "UPDATE Publisher SET publisherName=? "+
											   "WHERE publisherID = ?";
			stmt = conn.prepareStatement(updatePublisherTuple);
			stmt.setString(1, newPublisherName);
			stmt.setInt(2, publisher_id);
			
			stmt.executeUpdate();
			System.out.println("Data updated in Publisher relation");
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
					
//		          check if author is available in the database.
					if(!checkResultsSET.next()){
						System.out.println("There are no records in the database");
						checkstmt.close();
					}
//		          enter else statement here
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
	}
	

