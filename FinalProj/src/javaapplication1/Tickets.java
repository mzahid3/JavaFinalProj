package javaapplication1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * This code is for Final Project
 * @author Muhammad N Zahid
 *
 */
@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = null;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemCloseTicket;
	JMenuItem mnuItemViewTicket;
	
/**
 * This is the tickets method 
 * @param isAdmin this will be checking if this is admin
 */
	public Tickets(Boolean isAdmin) {

		chkIfAdmin = isAdmin;
		Dao.ifAdmin = chkIfAdmin;
		createMenu();
		prepareGUI();
		Dao.readRecords();
	}
/**
 * This is method which is for creating menu
 */
	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// This is specific for normal menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// This is specific menu for admin
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);

		// This is specific menu for admin
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);

		// This is specific for normal menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// This is specific for normal menu
		mnuItemCloseTicket = new JMenuItem("Close Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemCloseTicket);

		// This is specific for normal menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemCloseTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);

		/*
		 * continue implementing any other desired sub menu items (like for update and
		 * delete sub menus for example) with similar syntax & logic as shown above*
		 */

		System.out.println("Created the Menu");
	}
/**
 * This will be making the gui
 */
	private void prepareGUI() {

		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		if (chkIfAdmin) {//checking if it's admin then showing user menu 
			bar.add(mnuAdmin);
		}
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// setting this frame size this big so the records can be seen properly
		setSize(1000, 1000);
		getContentPane().setBackground(Color.CYAN);
		setLocationRelativeTo(null);
		setVisible(true);
	}
/**
 * This will be giving the menu to the user
 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// implement actions for sub menu items
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
		} else if (e.getSource() == mnuItemOpenTicket) {

			System.out.println("Opening ticket...");

			// get ticket information
			String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
			String ticketSev = JOptionPane.showInputDialog(null, "Enter Severity HIGH MEDIUM LOW");
			String startDate = JOptionPane.showInputDialog(null, "Enter start date: 'yyyy-mm-dd");

			// inserting ticket information
			int id = dao.insertRecords(ticketName, ticketDesc, ticketSev, startDate);

			// display results if successful or not to console / dialog box
			if (id != 0) {
				System.out.println("Ticket ID : " + id + " created successfully!!!");
				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
			} else {
				System.out.println("Ticket cannot be created!!!");
			}
		
		} else if (e.getSource() == mnuItemCloseTicket) {
			//this will be shown for closing menu
			int tickID = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter ticket ID to close"));
			String closeDate = (JOptionPane.showInputDialog(null, "Enter ticket close date: 'yyyy-mm-dd"));
			
			dao.closeRecord(tickID, closeDate);
			System.out.println("Ticket Closed");
			
		} else if (e.getSource() == mnuItemUpdate) {
			//this will be shown for updating the menu
			int tickID = Integer.parseInt(JOptionPane.showInputDialog(null, "Choose ticket ID to update"));
			String updatedDesc = JOptionPane.showInputDialog(null, "Please Enter New Description");
			
			dao.updateRecords(tickID, updatedDesc);
			
			System.out.println("Ticket Updated");
		} else if (e.getSource() == mnuItemDelete) 
		{//this will be shown for deleting  menu
			int tickID = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter ticket ID to delete"));
			
			dao.deleteRecords(tickID);
			System.out.println("Ticket Deleted");
		}

		else if (e.getSource() == mnuItemViewTicket) {
			//this will be shown for viewing menu
			// retrieve all tickets details for viewing in JTable
			try {
				System.out.println("Record Displayed");
				// Use JTable built in functionality to build a table model and
				// display the table model of your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
				jt.setBounds(30, 40, 200, 400);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); // refreshes or repaints frame on screen
				System.out.println("Data Shown with a Success");
			} catch (SQLException d) {
				d.printStackTrace();
			}
		}

	}

}
