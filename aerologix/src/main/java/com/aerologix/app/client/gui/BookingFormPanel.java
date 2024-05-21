package com.aerologix.app.client.gui;

// import statements
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.jdatepicker.*;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.client.controller.AirlineController;
import com.aerologix.app.server.pojo.AirlineData;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JComboBox;

/**
 * @brief Class of the panel for the form to register a new booking.
 * 
 * 
 * Class of the panel for the form to register a new booking.
 * <p>
 * It is a JPanel that contains multiple JTextFields
 * to manually type in the {@link com.aerologix.app.server.jdo.Passenger Passenger}
 * information and a JDatePicker
 * for the passenger birth date. Additionally, it contains a 
 * JComboBox to select the airline for the booking.
 */
public class BookingFormPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField tDNI;
	private JTextField tPhone;
	private JTextField tEmail;
	private JTextField tName;
	private JTextField tNationality;
	private UtilDateModel model;
	private JDatePicker datePicker;
	private JComboBox cbAirline;
	
	private static BookingFormPanel instance;
	
	/**
	 * This is the private constructor of the panel.
	 * 
	 * @param flightId	Identification integer of a {@link com.aerologix.app.server.jdo.Flight Flight} for which the booking is being created.
	 */
	private BookingFormPanel(int flightId) {
		setLayout(null);
		setSize(340, 500);
		
		JLabel lTitle = new JLabel("Creating booking for flight: " + flightId);
		lTitle.setFont(new Font("Arial", Font.PLAIN, 14));
		lTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lTitle.setBounds(26, 6, 286, 16);
		add(lTitle);
		
		JPanel pForm = new JPanel();
		pForm.setBorder(new LineBorder(new Color(0, 0, 0)));
		pForm.setBounds(26, 34, 286, 433);
		add(pForm);
		pForm.setLayout(new GridLayout(7, 1, 0, 0));
		
		JPanel pDNI = new JPanel();
		pForm.add(pDNI);
		pDNI.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lDNI = new JLabel("Type in passenger DNI");
		lDNI.setFont(new Font("Arial", Font.PLAIN, 13));
		pDNI.add(lDNI);
		
		tDNI = new JTextField();
		tDNI.setFont(new Font("Arial", Font.PLAIN, 13));
		pDNI.add(tDNI);
		tDNI.setColumns(10);
		
		JPanel pPhone = new JPanel();
		pForm.add(pPhone);
		pPhone.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel lPhone = new JLabel("Type in passenger phone number");
		lPhone.setFont(new Font("Arial", Font.PLAIN, 13));
		pPhone.add(lPhone);
		
		tPhone = new JTextField();
		tPhone.setFont(new Font("Arial", Font.PLAIN, 13));
		pPhone.add(tPhone);
		tPhone.setColumns(10);
		
		JPanel pEmail = new JPanel();
		pForm.add(pEmail);
		pEmail.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel lEmail = new JLabel("Type in passenger email address");
		lEmail.setFont(new Font("Arial", Font.PLAIN, 13));
		pEmail.add(lEmail);
		
		tEmail = new JTextField();
		tEmail.setFont(new Font("Arial", Font.PLAIN, 13));
		pEmail.add(tEmail);
		tEmail.setColumns(10);
		
		JPanel pName = new JPanel();
		pForm.add(pName);
		pName.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel lName = new JLabel("Type in passenger full name");
		lName.setFont(new Font("Arial", Font.PLAIN, 13));
		pName.add(lName);
		
		tName = new JTextField();
		tName.setFont(new Font("Arial", Font.PLAIN, 13));
		pName.add(tName);
		tName.setColumns(10);
		
		JPanel pNationality = new JPanel();
		pForm.add(pNationality);
		pNationality.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel lNationality = new JLabel("Type in passenger nationality");
		lNationality.setFont(new Font("Arial", Font.PLAIN, 13));
		pNationality.add(lNationality);
		
		tNationality = new JTextField();
		tNationality.setFont(new Font("Arial", Font.PLAIN, 13));
		pNationality.add(tNationality);
		tNationality.setColumns(10);
		
		JPanel pBirthdate = new JPanel();
		pForm.add(pBirthdate);
		pBirthdate.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel lBirthdate = new JLabel("Select passenger birth date");
		lBirthdate.setFont(new Font("Arial", Font.PLAIN, 13));
		pBirthdate.add(lBirthdate);
		
		
		model = new UtilDateModel();
        
		datePicker = new JDatePicker(model);
		datePicker.setFont(new Font("Arial", Font.PLAIN, 12));
		pBirthdate.add(datePicker);
		
		JPanel pAirline = new JPanel();
		pForm.add(pAirline);
		pAirline.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel lAirline = new JLabel("Select the airline");
		lAirline.setFont(new Font("Arial", Font.PLAIN, 13));
		pAirline.add(lAirline);
		
		JPanel pCombobox = new JPanel();
		pAirline.add(pCombobox);
		pCombobox.setLayout(null);
		
		cbAirline = new JComboBox<String>();
		cbAirline.setBounds(6, 6, 166, 27);
		pCombobox.add(cbAirline);
		cbAirline.setFont(new Font("Arial", Font.PLAIN, 13));
		
		JButton bRefresh = new JButton("Refresh list");
		bRefresh.setFont(new Font("Arial", Font.PLAIN, 13));
		bRefresh.setBounds(167, 4, 117, 29);
		pCombobox.add(bRefresh);
		
		loadAirlines();
		
		/**
		 * ActionListener that refreshes the JComboBox of
		 * airlines when '{@code bRefresh}' button is pressed.
		 */
		bRefresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadAirlines();
			}
			
		});
		
	}
	
	/**
	 * Method that cleans the items in the JComboBox that contains the
	 * airlines and adds the latest airline list available.
	 */
	public void loadAirlines() {
		cbAirline.removeAllItems();	// Initialize empty
		ArrayList<AirlineData> lAD = AirlineController.getInstance(AeroLogixClient.getInstance()).getAllAirlines();
		for(AirlineData ad : lAD) {
			cbAirline.addItem(ad.getName());
		}
	}

	/**
	 * Method that returns the singleton instance of BookingFormPanel.
	 * 
	 * @param flightId	Identification integer of a flight.
	 * @return	Instance of {@link BookingFormPanel}.
	 */
	public static BookingFormPanel getInstance(int flightId) {
        if (instance == null) {
            instance = new BookingFormPanel(flightId);
        }
        return instance;
    }
	
	/**
	 * Method that returns the value in the JTextField of the DNI.
	 * @return String with the DNI of the passenger.
	 */
	public String getDNI() {
		return tDNI.getText();
	}
	
	/**
	 * Method that returns the value in the JTextField of the phone number.
	 * @return String with the phone number of the passenger.
	 */
	public String getPhone() {
		return tPhone.getText();
	}
	
	/**
	 * Method that returns the value in the JTextField of the email.
	 * @return String with the email address of the passenger.
	 */
	public String getEmail() {
		return tEmail.getText();
	}
	
	/**
	 * Method that returns the value in the JTextField of the full name.
	 * @return String with the full name of the passenger.
	 */
	public String getName() {
		return tName.getText();
	}
	
	/**
	 * Method that returns the value in the JTextField of the nationality.
	 * @return String with the nationality of the passenger.
	 */
	public String getNationality() {
		return tNationality.getText();
	}
	
	/**
	 * Method that returns the value of the birthdate in the UtilDateModel from the JDatePicker.
	 * @return long with the birth date of the passenger in milliseconds.
	 */
	public long getBirthdate() {
		if(model.getValue() != null) {
			return model.getValue().getTime();
		} else {
			return -1;
		}
	}
	
	/**
	 * Method that ends the current instance of the panel.
	 */
	public static void deinit() {
		instance = null;
	}
	
	/**
	 * Method that returns the identification integer of the selected airline in the JComboBox.
	 * @return Identification integer of an {@link com.aerologix.app.server.jdo.Airline Airline}.
	 */
	public int getAirlineId() {
		return cbAirline.getSelectedIndex()+1;
	}
}
