package com.aerologix.app.client.gui;

// import statements
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.client.controller.BookingController;
import com.aerologix.app.client.controller.PassengerController;
import com.aerologix.app.server.jdo.Booking;
import com.aerologix.app.server.pojo.BookingData;
import com.aerologix.app.server.pojo.PassengerData;

import java.awt.Dimension;

/**
 * @brief Class of the window that displays the details of a booking.
 * Class of the window that displays the details of a booking.
 */
public class BookingDetailsWindow extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private static BookingDetailsWindow instance;
    
    /**
     * Private constructor of the window.
     * @param bookingId Identification integer of the booking.
     */
    private BookingDetailsWindow(int bookingId) {
        getContentPane().setLayout(null);
        getContentPane().setPreferredSize(new Dimension(400, 300));
        this.setSize(new Dimension(430, 300));
        this.setResizable(false);
        
        this.setTitle("AeroLogix - Booking " + bookingId + " details");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 424, 272);
        getContentPane().add(panel);
        panel.setLayout(null);
        
        BookingData bookingData = BookingController.getInstance(AeroLogixClient.getInstance()).getBooking(bookingId);
        PassengerData passengerData = PassengerController.getInstance(AeroLogixClient.getInstance()).getPassenger(bookingData.getPassengerDNI());
        
        JLabel lBookingId = new JLabel("Booking ID: " + bookingData.getId());
        lBookingId.setBounds(10, 10, 200, 25);
        panel.add(lBookingId);
        
        JLabel lPassengerName = new JLabel("Passenger Name: " + passengerData.getName());
        lPassengerName.setBounds(10, 40, 300, 25);
        panel.add(lPassengerName);
        
        JLabel lPassengerDNI = new JLabel("Passenger DNI: " + passengerData.getDNI());
        lPassengerDNI.setBounds(10, 70, 300, 25);
        panel.add(lPassengerDNI);
        
        JLabel lPassengerEmail = new JLabel("Passenger Email: " + passengerData.getEmail());
        lPassengerEmail.setBounds(10, 100, 300, 25);
        panel.add(lPassengerEmail);
        
        JLabel lPassengerPhone = new JLabel("Passenger Phone: " + passengerData.getPhone());
        lPassengerPhone.setBounds(10, 130, 300, 25);
        panel.add(lPassengerPhone);
        
        JLabel lPassengerNationality = new JLabel("Nationality: " + passengerData.getNationality());
        lPassengerNationality.setBounds(10, 160, 300, 25);
        panel.add(lPassengerNationality);
        
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        JLabel lPassengerBirthdate = new JLabel("Birthdate: " + dateFormat.format(new Date(passengerData.getBirthdate())));
        lPassengerBirthdate.setBounds(10, 190, 300, 25);
        panel.add(lPassengerBirthdate);
        
        JLabel lAirline = new JLabel("Airline ID: " + bookingData.getAirlineId());
        lAirline.setBounds(10, 220, 300, 25);
        panel.add(lAirline);
        
        JButton bClose = new JButton("Close");
        bClose.setBounds(320, 220, 85, 25);
        panel.add(bClose);
        
        bClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deinit();
                dispose();
            }
        });
    }
    
    /**
     * Method that returns the singleton instance of {@link BookingDetailsWindow}.
     * @param bookingId Integer of the identification of the {@link Booking}.
     * @return If there exists an instance of {@link BookingDetailsWindow} it will return it. If not, it will create a new one.
     */
    public static BookingDetailsWindow getInstance(int bookingId) {
        if (instance == null) {
            instance = new BookingDetailsWindow(bookingId);
        }
        return instance;
    }

    /** Method that ends the current instance of {@link BookingDetailsWindow}. */
    public static void deinit() {
        instance = null;
    }
}
