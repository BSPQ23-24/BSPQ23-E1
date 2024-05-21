package com.aerologix.app.client.gui;

// import statements
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.client.controller.BookingController;
import com.aerologix.app.client.controller.FlightController;
import com.aerologix.app.client.controller.PassengerController;
import com.aerologix.app.server.pojo.BookingData;
import com.aerologix.app.server.pojo.PassengerData;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * @brief Class for the window that displays the details of a flight.
 * 
 * Class for the window that displays the details of a flight.
 * <p>
 * It contains list of all registered bookings for a flight. It
 * is the management window for all bookings in a flight, it allows 
 * to create and delete bookings.
 */
public class FlightWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static FlightWindow instance;
	
	static JScrollPane scrollPane;
	static JPanel pBookings;
	protected JPanel pLanguage = new JPanel(new FlowLayout());
	private ResourceBundle messages;
	protected JComboBox<Locale> languageSelector;
    protected JLabel lLanguage;
    protected JLabel lFlightId;
    protected JLabel lAirports;
    protected JButton bNewBooking;
    protected JButton bRefresh;
    protected JLabel lDate;
	
	/**
	 * Private constructor of the window.
	 * @param flightId	Identification integer of the flight.
	 * @param userEmail	Email address of the user in the counter.
	 */
	private FlightWindow(int flightId, String userEmail) {
		// Initialize ResourceBundle
        initResourceBundle(Locale.getDefault());
    
        // Initialize components with ResourceBundle
        languageSelector = new JComboBox<>(new Locale[]{new Locale("es", "ES"),Locale.US});
        lLanguage = new JLabel(messages.getString("language"));
		
		
		
		getContentPane().setLayout(null);
		getContentPane().setPreferredSize(new Dimension(800, 500));
		this.setSize(new Dimension(830, 500));
		this.setResizable(false);
		this.setTitle("AeroLogix -" + messages.getString("management") +  + flightId);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		
		JPanel topPanel = new JPanel();
		topPanel.setBounds(0, 0, 824, 34);
		getContentPane().add(topPanel);
		topPanel.setLayout(null);
		
		JLabel lAerologix = new JLabel("AeroLogix");
		lAerologix.setBounds(5, 5, 152, 27);
		lAerologix.setFont(new Font("Arial", Font.BOLD, 25));
		topPanel.add(lAerologix);
		
		lFlightId = new JLabel(messages.getString("flight") + flightId);
		lFlightId.setBounds(169, 12, 61, 16);
		topPanel.add(lFlightId);
		
		DateFormat obj = new SimpleDateFormat("dd MMM yyyy HH:mm"); 
		lDate = new JLabel(messages.getString("date") + obj.format(new Date(FlightController.getInstance(AeroLogixClient.getInstance()).getFlight(flightId).getDate())));
		lDate.setBounds(273, 12, 200, 16);
		topPanel.add(lDate);
		
		lAirports = new JLabel(FlightController.getInstance(AeroLogixClient.getInstance()).getFlight(flightId).getOrigin() + " - " + FlightController.getInstance(AeroLogixClient.getInstance()).getFlight(flightId).getDestination());
		lAirports.setBounds(485, 12, 204, 16);
		topPanel.add(lAirports);
		
		bNewBooking = new JButton(messages.getString("BookFlight"));
		bNewBooking.setBounds(701, 3, 117, 29);
		topPanel.add(bNewBooking);
		
		pBookings = new JPanel();
		pBookings.setLayout(new GridLayout(FlightController.getInstance(AeroLogixClient.getInstance()).getFlight(flightId).getBookingIds().size(), 1));
		pLanguage.add(lLanguage);
        pLanguage.add(languageSelector);
        pLanguage.setBounds(340, 410, 117, 60);
		getContentPane().add(pLanguage);
		
		scrollPane = new JScrollPane(pBookings);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(140, 67, 560, 331);
		getContentPane().add(scrollPane);
	
		bRefresh = new JButton(messages.getString("RefreshList"));
		bRefresh.setBounds(696, 415, 120, 29);
		getContentPane().add(bRefresh);
		
		/**
		 * ActionListener that books a flight when '{@code bNewBooking}' button is pressed.
		 */
		bNewBooking.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bookFlight(flightId, userEmail);
			}
			
			
		});
		
		languageSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeLanguage((Locale) languageSelector.getSelectedItem(), flightId, userEmail);
            }
        });
	
		
		/**
		 * ActionListener that refreshes the booking list when '{@code bRefresh}' button is pressed.
		 */
		bRefresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showBookings(flightId, userEmail, messages);	// Add them again
			}
			
		});
		
		showBookings(flightId, userEmail, messages);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				deinit();
			}
		});
	}
	
	private void initResourceBundle(Locale locale) {
        try {
            messages = ResourceBundle.getBundle("Multilingual.messages", locale);
        } catch (Exception e) {
            System.err.println("Failed to load resource bundle: " + e.getMessage());
        }
    }
	
	private void changeLanguage(Locale locale, int flightId, String userEmail) {
        Locale.setDefault(locale);
        initResourceBundle(locale);
        updateComponents(flightId, userEmail);
    }
	
	private void updateComponents(int flightId, String userEmail) {
		lFlightId.setText(messages.getString("flight") + flightId);
	    bNewBooking.setText(messages.getString("BookFlight"));
	    bRefresh.setText(messages.getString("RefreshList"));
		DateFormat obj = new SimpleDateFormat("dd MMM yyyy HH:mm"); 
	    lDate.setText(messages.getString("date") + obj.format(new Date(FlightController.getInstance(AeroLogixClient.getInstance()).getFlight(flightId).getDate())));
        this.setTitle("AeroLogix -" + messages.getString("flight") + flightId + messages.getString("management"));
        lLanguage.setText(messages.getString("language"));
		showBookings(flightId, userEmail, messages);
    }
	
	/**
	 * Method that registers a booking in the system.
	 * <p>
	 * It shows a JOptionPane message with a {@link BookingFormPanel}
	 * to manually type in the passenger details and select the airline.
	 * 
	 * @param flightId	Identification integer of the flight in which to make a booking.
	 * @param userEmail	Email address of the user in the counter which is making the booking for the passenger.
	 */
	public static void bookFlight(int flightId, String userEmail) {
		// Create a panel of the data form
		BookingFormPanel panel = BookingFormPanel.getInstance(flightId);
		panel.setPreferredSize(new Dimension(340, 550));
		int option = JOptionPane.showConfirmDialog(null, panel, "Booking flight " + flightId, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {
			int option2 = JOptionPane.showConfirmDialog(null, "Booking will be registered. Are you sure?", "Book flight " + flightId, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(option2 == JOptionPane.OK_OPTION && (!panel.getDNI().isBlank() && !panel.getPhone().isBlank() && !panel.getEmail().isBlank() && !panel.getName().isBlank() && !panel.getNationality().isBlank() && panel.getBirthdate() != -1 && panel.getAirlineId() != -1)) {
				PassengerData pd = new PassengerData();
				pd.setDNI(panel.getDNI());
				pd.setPhone(Integer.parseInt(panel.getPhone()));
				pd.setEmail(panel.getEmail());
				pd.setName(panel.getName());
				pd.setNationality(panel.getNationality());
				pd.setBirthdate(panel.getBirthdate());
				PassengerController.getInstance(AeroLogixClient.getInstance()).createPassenger(pd);
				BookingController.getInstance(AeroLogixClient.getInstance()).createBooking(pd.getDNI(), flightId, userEmail, panel.getAirlineId());
				JOptionPane.showMessageDialog(null, "Booking registered succesfully", "Flight booked", JOptionPane.PLAIN_MESSAGE);
				
				// Close the instance of the panel
				BookingFormPanel.deinit();
			} else {
				BookingFormPanel.deinit();
				JOptionPane.showMessageDialog(null, "No empty fields are allowed.", "Empty values", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			BookingFormPanel.deinit();
		}
	}
	
	/**
	 * Method that shows all the registered bookings in a flight.
	 * <p>
	 * It iterates over the list of bookings and adds a JPanel with
	 * some basic information about each of them, plus it adds a JButton
	 * for viewing the details of the booking and another one to cancel a booking.
	 * 
	 * @param flightId	Identification integer of the flight.
	 */
	public static void showBookings(int flightId, String userEmail, ResourceBundle messages) {
		// Get all booking data
		pBookings.removeAll(); // Remove all containers
		pBookings.setLayout(new GridLayout(FlightController.getInstance(AeroLogixClient.getInstance()).getFlight(flightId).getBookingIds().size(), 1));
		ArrayList<BookingData> bookingData = new ArrayList<BookingData>();
		for(int bookingId: FlightController.getInstance(AeroLogixClient.getInstance()).getFlight(flightId).getBookingIds()) {
			bookingData.add(BookingController.getInstance(AeroLogixClient.getInstance()).getBooking(bookingId));
		}
		Collections.reverse(bookingData); // Reverse order for displaying in ASC order
		
		// For each booking create a panel
		for(BookingData booking : bookingData) {
			JPanel lPanelBooking = new JPanel(new GridLayout(2, 3));
			lPanelBooking.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
			JLabel lBookingId = new JLabel(messages.getString("bookingId") + ": " + Integer.toString(booking.getId()));
			JLabel lPassenger = new JLabel(messages.getString("passengerDNI") + ": " + booking.getPassengerDNI());
			JPanel pEmpty = new JPanel();
			JButton bViewBooking = new JButton(messages.getString("booking_details"));
			JButton bModifyBooking = new JButton(messages.getString("modify_booking"));
			JButton bDeleteBooking = new JButton(messages.getString("cancel_booking"));
			bViewBooking.setForeground(Color.BLUE);
			bModifyBooking.setForeground(new Color(240, 118, 5));
			bDeleteBooking.setForeground(Color.RED);
			
			// Button actions
			bViewBooking.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					BookingDetailsWindow bw = BookingDetailsWindow.getInstance(booking.getId());
					bw.setVisible(true);
				}
				
			});
			
			bModifyBooking.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					modifyBooking(booking.getId(), flightId, userEmail);
				}
				
			});
			
			bDeleteBooking.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int option = JOptionPane.showConfirmDialog(null, "Booking (" + booking.getId() + ") will be deleted. Are you sure?", "Delete booking", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (option == JOptionPane.OK_OPTION) {
						BookingData bd = BookingController.getInstance(AeroLogixClient.getInstance()).getBooking(booking.getId());
						BookingController.getInstance(AeroLogixClient.getInstance()).deleteBooking(Integer.toString(booking.getId()));	// Delete booking
						PassengerController.getInstance(AeroLogixClient.getInstance()).deletePassenger(bd.getPassengerDNI());	// Delete passenger
						JOptionPane.showMessageDialog(null, "Booking cancelled succesfully", "Booking cancelled", JOptionPane.PLAIN_MESSAGE);
					}
				}
				
			});
			
			lPanelBooking.add(lBookingId);
			lPanelBooking.add(lPassenger);
			lPanelBooking.add(pEmpty);
			lPanelBooking.add(bViewBooking);
			lPanelBooking.add(bModifyBooking);
			lPanelBooking.add(bDeleteBooking);
			
			// Add it to the JScrollPane
			pBookings.add(lPanelBooking);
			pBookings.revalidate();
			scrollPane.setVisible(true);
			scrollPane.repaint();
			scrollPane.revalidate();
		}
	}
	
	public static void modifyBooking(int bookingId, int flightId, String userEmail ) {
		// Create a panel of the data form
		BookingModifyPanel panel = BookingModifyPanel.getInstance(bookingId);
		panel.setPreferredSize(new Dimension(340, 550));
		Object[] texts = {"Accept","Cancel"};
		int option = JOptionPane.showOptionDialog(null, panel, "Modifying booking " + bookingId, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, texts, texts[0]);
		if (option == JOptionPane.OK_OPTION) { //This is accept button
			if (panel.hasBeenModifyied(bookingId)) {
				int option2 = JOptionPane.showConfirmDialog(null, "Booking will be modified. Are you sure?", "Modify booking " + bookingId, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(option2 == JOptionPane.YES_OPTION && (!panel.getDNI().isBlank() && !panel.getPhone().isBlank() && !panel.getEmail().isBlank() && !panel.getName().isBlank() && !panel.getNationality().isBlank() && panel.getBirthdate() != -1)) {
					PassengerData pd = new PassengerData();
					pd.setDNI(panel.getDNI());
					pd.setPhone(Integer.parseInt(panel.getPhone()));
					pd.setEmail(panel.getEmail());
					pd.setName(panel.getName());
					pd.setNationality(panel.getNationality());
					pd.setBirthdate(panel.getBirthdate());
					PassengerController.getInstance(AeroLogixClient.getInstance()).modifyPassenger(pd);
					BookingController.getInstance(AeroLogixClient.getInstance()).modifyBooking(bookingId, pd.getDNI(), flightId, userEmail, panel.getAirlineId());
					JOptionPane.showMessageDialog(null, "Booking modified succesfully", "Booking modified", JOptionPane.PLAIN_MESSAGE);
					
					// Close the instance of the panel
					BookingModifyPanel.deinit();
					
					} else {
						BookingModifyPanel.deinit();
						JOptionPane.showMessageDialog(null, "No empty fields are allowed.", "Empty values", JOptionPane.ERROR_MESSAGE);
					}
			} else {
				JOptionPane.showMessageDialog(null, "Booking data is the same. Booking not be modified.", "Booking not modified", JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			BookingModifyPanel.deinit();
		}
	}
	
	
	
	/**
	 * Method that returns the singleton instance of {@link FlightWindow}.
	 * 
	 * @param flightId	Identification integer of the flight.
	 * @param userEmail	Email address of the user in the airport counter.
	 * @return If there exists an instance of {@link FlightWindow} it will return it. If not, it will create a new one.
	 */
	public static FlightWindow getInstance(int flightId, String userEmail) {
        if (instance == null) {
            instance = new FlightWindow(flightId, userEmail);
        }
		return instance;
	}

	/** Ends the current instance of {@link FlightWindow}. */
	public static void deinit() {
		instance = null;
	}
}
