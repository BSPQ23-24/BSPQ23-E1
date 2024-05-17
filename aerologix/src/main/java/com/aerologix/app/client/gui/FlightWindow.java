package com.aerologix.app.client.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.client.controller.AirlineController;
import com.aerologix.app.client.controller.BookingController;
import com.aerologix.app.client.controller.FlightController;
import com.aerologix.app.client.controller.PassengerController;
import com.aerologix.app.server.pojo.BookingData;
import com.aerologix.app.server.pojo.PassengerData;
import com.aerologix.app.server.service.BookingService;
import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import java.awt.Color;
import java.awt.Dimension;

public class FlightWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static FlightWindow instance;
	
	static JScrollPane scrollPane;
	static JPanel pBookings;
	
	public FlightWindow(int flightId, String userEmail) {
		getContentPane().setLayout(null);
		getContentPane().setPreferredSize(new Dimension(800, 500));
		this.setSize(new Dimension(830, 500));
		this.setResizable(false);
		
		this.setTitle("AeroLogix - Flight " + flightId + " management");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel topPanel = new JPanel();
		topPanel.setBounds(0, 0, 824, 34);
		getContentPane().add(topPanel);
		topPanel.setLayout(null);
		
		JLabel lAerologix = new JLabel("AeroLogix");
		lAerologix.setBounds(5, 5, 152, 27);
		lAerologix.setFont(new Font("Arial", Font.BOLD, 25));
		topPanel.add(lAerologix);
		
		JLabel lFlightId = new JLabel("Flight: " + flightId);
		lFlightId.setBounds(169, 12, 61, 16);
		topPanel.add(lFlightId);
		
		DateFormat obj = new SimpleDateFormat("dd MMM yyyy HH:mm"); 
		JLabel lDate = new JLabel("Date: " + obj.format(new Date(FlightController.getInstance(AeroLogixClient.getInstance()).getFlight(flightId).getDate())));
		lDate.setBounds(273, 12, 200, 16);
		topPanel.add(lDate);
		
		JLabel lAirports = new JLabel(FlightController.getInstance(AeroLogixClient.getInstance()).getFlight(flightId).getOrigin() + " - " + FlightController.getInstance(AeroLogixClient.getInstance()).getFlight(flightId).getDestination());
		lAirports.setBounds(485, 12, 204, 16);
		topPanel.add(lAirports);
		
		JButton bNewBooking = new JButton("Book flight");
		bNewBooking.setBounds(701, 3, 117, 29);
		topPanel.add(bNewBooking);
		
		pBookings = new JPanel();
		pBookings.setLayout(new GridLayout(FlightController.getInstance(AeroLogixClient.getInstance()).getFlight(flightId).getBookingIds().size(), 1));
		
		scrollPane = new JScrollPane(pBookings);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(153, 67, 494, 331);
		getContentPane().add(scrollPane);
		
		JButton bRefresh = new JButton("Refresh list");
		bRefresh.setBounds(696, 423, 117, 29);
		getContentPane().add(bRefresh);
		
		// Book flight
		bNewBooking.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bookFlight(flightId, userEmail);
			}
			
		});
		
		// Refresh list
		bRefresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pBookings.removeAll(); // Remove all containers
				pBookings.setLayout(new GridLayout(FlightController.getInstance(AeroLogixClient.getInstance()).getFlight(flightId).getBookingIds().size(), 1));
				showBookings(flightId);	// Add them again
			}
			
		});
		
		// Show bookings the first time
		showBookings(flightId);
		
	}
	
	public static void bookFlight(int flightId, String userEmail) {
		// Create a panel of the data form
		BookingFormPanel panel = BookingFormPanel.getInstance(flightId);
		panel.setPreferredSize(new Dimension(340, 500));
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
				
				// Empty the form panel inputs
				panel.emptyInputs();
			} else {
				JOptionPane.showMessageDialog(null, "No empty fields are allowed.", "Empty values", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public static void showBookings(int flightId) {
		// Get all booking data
		ArrayList<BookingData> bookingData = new ArrayList<BookingData>();
		for(int bookingId: FlightController.getInstance(AeroLogixClient.getInstance()).getFlight(flightId).getBookingIds()) {
			bookingData.add(BookingController.getInstance(AeroLogixClient.getInstance()).getBooking(bookingId));
		}
		Collections.reverse(bookingData); // Reverse order for displaying in ASC order
		
		// For each booking create a panel
		for(BookingData booking : bookingData) {
			JPanel lPanelBooking = new JPanel(new GridLayout(2, 2));
			lPanelBooking.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
			JLabel lBookingId = new JLabel("Booking: " + Integer.toString(booking.getId()));
			JLabel lPassenger = new JLabel("Passenger DNI: " + booking.getPassengerDNI());
			JButton bViewBooking = new JButton("View booking details");
			JButton bDeleteBooking = new JButton("Cancel booking");
			bViewBooking.setForeground(Color.BLUE);
			bDeleteBooking.setForeground(Color.RED);
			
			// Button actions
			bViewBooking.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// Open booking window
					
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
			lPanelBooking.add(lBookingId);
			lPanelBooking.add(lPassenger);
			lPanelBooking.add(bViewBooking);
			lPanelBooking.add(bDeleteBooking);
			
			// Add it to the JScrollPane
			pBookings.add(lPanelBooking);
			pBookings.revalidate();
			scrollPane.setVisible(true);
			scrollPane.repaint();
			scrollPane.revalidate();
		}
	}
	
	public static FlightWindow getInstance(int flightId, String userEmail) {
        if (instance == null) {
            instance = new FlightWindow(flightId, userEmail);
        }
        return instance;
    }
}
