package javaapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * This code is for Final Project
 * @author Muhammad N Zahid
 *
 */
public class Dao {
	// fields that will be used
	static Connection connect = null;
	static Statement Statement = null;
	static Boolean ifAdmin;// this is going to check if user is admin
	int id = 0;
	static String user;// this is provide the info of the user signed in

	/**
	 * This is the constructor 
	 */
	// constructor
	public Dao() {

	}

	/**
	 * This method will be creating the connection
	 * 
	 * @return will return the connection once made
	 */
	public Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	// CRUD implementation
	/**
	 * This method will create the tables in the database where the connection was
	 * made
	 */
	public void createTables() {
		// variables for SQL Query table creations
		// creating the table with all the fields
		final String createTicketsTable = "CREATE TABLE mzahid_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "user_signedin VARCHAR(30), ticket_issuer VARCHAR(30), ticket_description VARCHAR(200),ticket_sev VARCHAR(30), start_date DATE, close_date DATE)";
		// creating the tables for users
		final String createUsersTable = "CREATE TABLE mzahid_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";

		try {

			// execute queries to create tables

			Statement = getConnection().createStatement();
			Statement.executeUpdate(createTicketsTable);
			Statement.executeUpdate(createUsersTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			Statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
	}

	/**
	 * This method will be adding the users
	 */
	public void addUsers() {
		// add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;

		Statement Statement;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB

			Statement = getConnection().createStatement();

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "insert into mzahid_users(uname,upass,admin) " + "values('" + rowData.get(0) + "'," + " '"
						+ rowData.get(1) + "','" + rowData.get(2) + "');";
				Statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close statement object
			Statement.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 *  This method will be inserting the record
	 * @param ticketName Person creating the ticket
	 * @param ticketDesc Ticket description 
	 * @param ticketSev Ticket severity high low or medium 
	 * @param startDate this will be start date for the ticket
	 * @return
	 */
	public int insertRecords(String ticketName, String ticketDesc, String ticketSev, String startDate) {
		try {
			Statement = getConnection().createStatement();
			//inserting into the tablet
			Statement.executeUpdate("Insert into mzahid_tickets"
					+ "(user_signedin, ticket_issuer, ticket_description,  start_date,ticket_sev) values('" + user
					+ "','" + ticketName + "','" + ticketDesc + "','" + startDate + "','" + ticketSev + "')",
					Statement.RETURN_GENERATED_KEYS);

			// retrieve ticket id number newly auto generated upon record insertion
			ResultSet resultSet = null;
			resultSet = Statement.getGeneratedKeys();
			if (resultSet.next()) {
				// retrieve first field in table
				id = resultSet.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//returning the id
		return id;
	}

	/**
	 * 
	 * @return
	 */
	public static ResultSet readRecords() {

		ResultSet resultRead = null;
		try {
			Statement = connect.createStatement();
			//checking if the admin is signed in and then getting the info 
			if (ifAdmin) {
				resultRead = Statement.executeQuery("SELECT * FROM mzahid_tickets");
			} else {
				resultRead = Statement.executeQuery("SELECT * FROM mzahid_tickets WHERE user_signedin='" + user + "'");
			}
			// connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//returning the results
		return resultRead;
	}

	/**
	 * this method will be for closing the ticket
	 * @param tickID ticket id 
	 * @param closeDate closing date that will be entered
	 */
	public void closeRecord(int tickID, String closeDate) {

		try {
			Statement = getConnection().createStatement();
			//entering the data when upon closing
			String sql = "UPDATE mzahid_tickets " + "SET close_date='" + closeDate + "' WHERE ticket_id='" + tickID + "'";
			Statement.executeUpdate(sql);
			//printing when ticket is closed 
			System.out.println("Ticket " + tickID + " closed successfully!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method is for updating records
	 * @param tickID this is the ticket ID
	 * @param updateDesc this the new updated description 
	 */
	// continue coding for updateRecords implementation
	public void updateRecords(int tickID, String updateDesc) {
		// Execute update query
		System.out.println("Creating update statement...");
		try {
			Statement = getConnection().createStatement();
			//entering the updated info in the table
			String sql = "UPDATE mzahid_tickets " + "SET ticket_description='" + updateDesc + "' WHERE ticket_id='"
					+ tickID + "'";
			Statement.executeUpdate(sql);
			System.out.println("Updated ticket successfully!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method is for deleting the record
	 * @param tickID this is the ticket id
	 */
	// continue coding for deleteRecords implementation
	public void deleteRecords(int tickID) {
		// Execute delete query
		System.out.println("Creating statement...");
		try {
			Statement = getConnection().createStatement();
			String sql = "DELETE FROM mzahid_tickets  " + "WHERE ticket_id='" + tickID + "'";

			int response = JOptionPane.showConfirmDialog(null, "Delete ticket #" + tickID + "?", "Confirm",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (response == JOptionPane.NO_OPTION) {
				System.out.println("No record deleted");
			} else if (response == JOptionPane.YES_OPTION) {
				Statement.executeUpdate(sql);
				System.out.println("Record deleted successfully!");

			} else if (response == JOptionPane.CLOSED_OPTION) {
				System.out.println("Request cancelled");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}