
import java.sql.*;
import java.util.*;
public class LibraryRunner {

	private static final String user_Name = "root";
	private static final String password = "157b";
	private static final String dbURL = "jdbc:mysql://localhost:3306/";
	private static Connection conn = null;
	private static Statement stmt = null;
	public static void main(String[] args) throws SQLException{
		System.out.println("-------- MySQL JDBC Connection Testing ------------");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("connection succeeded");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		}
		System.out.println("MySQL JDBC Driver Registered!");
			 
		try {
			conn = DriverManager.getConnection(dbURL, user_Name, password);
			createDatabase();
			createTables();
			loadDataIntoTables();
			startLibraryApplication();
			
			
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}
	 
		if (conn != null) {
			conn.close();
		} else {
			System.out.println("Failed to make connection!");
		}
	} //end main method
	
	public static void createDatabase() throws SQLException{
		System.out.println("Creating Library database.....");
		stmt = conn.createStatement();
		String dropDBStatement = "DROP DATABASE IF EXISTS LibraryDBMS";
		stmt.executeUpdate(dropDBStatement);
		String createDBStatement = "CREATE DATABASE LibraryDBMS";
		stmt.executeUpdate(createDBStatement);	
		System.out.println("Database Library database created successfully");
	}
	
	public static void createTables() throws SQLException{
		try{
			conn = DriverManager.getConnection(dbURL+"LibraryDBMS", user_Name, password);
			System.out.println("Connected to LibraryDBMS successfully");
			System.out.println("Creating table in given database...");
		    stmt = conn.createStatement();
		    String drop_table_AUTHOR = "DROP TABLE IF EXISTS AUTHOR";
		    stmt.executeUpdate(drop_table_AUTHOR);
		    String create_table_AUTHOR = "create table Author("+
		    							  "authorID INT NOT NULL AUTO_INCREMENT, "+
		    							  "firstName CHAR(20) NOT NULL, "+
		    							  "lastName CHAR(20) NOT NULL, "+
		    							  "primary key(authorID))";		   
		    stmt.executeUpdate(create_table_AUTHOR);
		    
		    String author_AUTO_INCREMENT = "ALTER TABLE Author AUTO_INCREMENT=1001";
		    stmt.execute(author_AUTO_INCREMENT);
		    
		    		    
		    String drop_table_PUBLISHER = "DROP TABLE IF EXISTS PUBLISHER";
		    stmt.executeUpdate(drop_table_PUBLISHER);
		    String create_table_PUBLISHER = "create table PUBLISHER("+
											  "publisherID INT NOT NULL AUTO_INCREMENT, "+
											  "publisherName CHAR(50) NOT NULL, "+
											  "PRIMARY KEY(publisherID))";		
		    stmt.executeUpdate(create_table_PUBLISHER);
		    
		    String publisher_AUTO_INCREMENT = "ALTER TABLE PUBLISHER AUTO_INCREMENT=2001";
		    stmt.execute(publisher_AUTO_INCREMENT);
		    		    
		    String drop_table_TITLE = "DROP TABLE IF EXISTS TITLE";
		    stmt.executeUpdate(drop_table_TITLE);
		    String create_table_TITLE = "create table TITLE("+
		    							"isbn CHAR(10) NOT NULL, "+
										"title VARCHAR(50) NOT NULL, "+
										"editionNumber INT NOT NULL, "+
										"copyright CHAR(4) NOT NULL, "+
										"publisherID INT NOT NULL, "+
										"price NUMERIC(8,2) NOT NULL, "+
										"PRIMARY KEY(isbn), "+
										"FOREIGN KEY(publisherID) REFERENCES PUBLISHER(publisherID))";
		    stmt.executeUpdate(create_table_TITLE);
		    
		    String drop_table_authorISBN = "DROP TABLE IF EXISTS authorISBN";
		    stmt.executeUpdate(drop_table_authorISBN);
		    String create_table_authorISBN = "create table authorISBN("+
		    								 "authorID INT NOT NULL, "+
		    								 "isbn CHAR(10) NOT NULL, "+
		    								 "FOREIGN KEY(authorID) REFERENCES Author(authorID), "+
		    								 "FOREIGN KEY(isbn) REFERENCES TITLE(isbn))";
		    stmt.executeUpdate(create_table_authorISBN);
		    System.out.println("Tables created successfully");
			
			
		}
		catch(SQLException e){
			System.out.println("something went wrong while creating tables");
		}
		
		
	}
	
	public static void loadDataIntoTables(){
		try{
			conn = DriverManager.getConnection(dbURL+"LibraryDBMS", user_Name, password);
			
			if(conn==null){
				System.out.println("Could not establish connection to database");
				}
			else{
				System.out.println("Connection established successflly");
			}
			stmt = conn.createStatement();
			String LOAD_AUTHOR = "LOAD DATA LOCAL INFILE "+
								  "'C:/Users/MD/Dropbox/School Work/spring 2016/CS 157B/157A project/Author.tsv' "+
								  "INTO TABLE AUTHOR";
			String LOAD_PUBLISHER = "LOAD DATA LOCAL INFILE "+
					  				"'C:/Users/MD/Dropbox/School Work/spring 2016/CS 157B/157A project/Publisher.tsv' "+
					  				"INTO TABLE PUBLISHER";
			String LOAD_TITLE = "LOAD DATA LOCAL INFILE "+
					  "'C:/Users/MD/Dropbox/School Work/spring 2016/CS 157B/157A project/Title.tsv' "+
					  "INTO TABLE title";
			String LOAD_authorISBN = "LOAD DATA LOCAL INFILE "+
					  "'C:/Users/MD/Dropbox/School Work/spring 2016/CS 157B/157A project/authorISBN.tsv' "+
					  "INTO TABLE authorISBN";
			System.out.println("Attempting to load data...");
			stmt.execute(LOAD_AUTHOR);
			stmt.execute(LOAD_PUBLISHER);
			stmt.execute(LOAD_TITLE);
			stmt.execute(LOAD_authorISBN);
			System.out.println("Data loaded!");
			}
		catch(SQLException se){
			se.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	//Initiate Book Data Management System
	public static void startLibraryApplication() throws SQLException{
		int choice;
		Book b = new Book(conn);
		Scanner in = new Scanner(System.in);
		System.out.println("===========================================================");
		System.out.println("Choose an SQL operation from below");
		System.out.println("1. Select all authors from the authors table. "+ 
							"Order the information alphabetically by the author’s last name and first name. \n"+
						   "2. Select all publishers from the publishers table. \n"+
						   "3. Select a specific publisher and list all books published by that publisher. \n" +
						   	   "   This includes the ttle, year and ISBN number ordered alphabetically by title. \n"+
						   "4. Add New Author \n"+
						   "5. Edit/Update the existing information about an author \n"+
						   "6. Add a new title for an author \n"+
						   "7. Add a new Publisher \n"+
						   "8. Edit/Update the existing informaiton about a publisher \n"+
						   "9. EXIT THE LIBRARY APPLICATION");
		do{
		choice = in.nextInt();
		switch(choice){
		case 1: 
			System.out.println("You have chosen 1");
			System.out.println("===========================================================");
			b.printAllAuthors();			
			startLibraryApplication();
		case 2: 
			System.out.println("You have chosen 2");
			System.out.println("===========================================================");
			b.printAllPublishers();			
		    startLibraryApplication();
		case 3: 
			System.out.println("You have chosen 3");
			System.out.println("===========================================================");
			b.listBooksByPublisherEntered();			
		    startLibraryApplication();
		case 4: 
			System.out.println("You have chosen 4");
			System.out.println("===========================================================");
			b.addNewAuthor();		
		    startLibraryApplication();
		case 5: 
			System.out.println("You have chosen 5");
			System.out.println("===========================================================");
			b.changeAuthorInformation();			
		    startLibraryApplication();
		case 6: 
			System.out.println("You have chosen 6");
			System.out.println("===========================================================");
			b.addTitleForAnAuthor();			
			startLibraryApplication();
		case 7: 
			System.out.println("You have chosen 7");
			System.out.println("===========================================================");
			b.addNewPublisher();			
			startLibraryApplication();
		case 8: 
			System.out.println("You have chosen 8");
			System.out.println("===========================================================");
			b.changePublisherInformation();			
			startLibraryApplication();
		case 9: 
			System.out.println("EXITING......");
			break;
		default: 
			System.out.println("Invalid choice");
		}
		}while(choice<1 || choice > 9);
		}
}
