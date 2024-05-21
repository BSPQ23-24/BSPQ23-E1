package com.aerologix.app.client.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.client.controller.BookingController;
import com.aerologix.app.client.controller.PassengerController;
import com.aerologix.app.server.jdo.Booking;
import com.aerologix.app.server.pojo.BookingData;
import com.aerologix.app.server.pojo.PassengerData;


import java.awt.Dimension;

/**
 * @brief Class of the window that displays the details of a booking.
 * 
 * Class of the window that displays the details of a booking.
 */

public class BookingDetailsWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private static BookingDetailsWindow instance;
    private ResourceBundle messages;
    protected JPanel pLanguage;
    protected JComboBox<Locale> languageSelector;
    protected JLabel lLanguage;

    /**
     * Private constructor of the window.
     * @param bookingId Identification integer of the booking.
     */
    private BookingDetailsWindow(int bookingId) {
        initResourceBundle(Locale.getDefault());

        getContentPane().setLayout(null);
        getContentPane().setPreferredSize(new Dimension(400, 300));
        this.setSize(new Dimension(430, 330));
        this.setResizable(false);

        this.setTitle("AeroLogix - " + messages.getString("booking") + " " + bookingId + ": " + messages.getString("details"));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 424, 272);
        getContentPane().add(panel);
        panel.setLayout(null);

        BookingData bookingData = BookingController.getInstance(AeroLogixClient.getInstance()).getBooking(bookingId);
        PassengerData passengerData = PassengerController.getInstance(AeroLogixClient.getInstance()).getPassenger(bookingData.getPassengerDNI());

        JLabel lBookingId = new JLabel(messages.getString("bookingId") + ": " + bookingData.getId());
        lBookingId.setBounds(10, 10, 200, 25);
        panel.add(lBookingId);

        JLabel lPassengerName = new JLabel(messages.getString("passengerName") + ": " + passengerData.getName());
        lPassengerName.setBounds(10, 40, 300, 25);
        panel.add(lPassengerName);

        JLabel lPassengerDNI = new JLabel(messages.getString("passengerDNI") + ": " + passengerData.getDNI());
        lPassengerDNI.setBounds(10, 70, 300, 25);
        panel.add(lPassengerDNI);

        JLabel lPassengerEmail = new JLabel(messages.getString("passengerEmail") + ": " + passengerData.getEmail());
        lPassengerEmail.setBounds(10, 100, 300, 25);
        panel.add(lPassengerEmail);

        JLabel lPassengerPhone = new JLabel(messages.getString("passengerPhone") + ": " + passengerData.getPhone());
        lPassengerPhone.setBounds(10, 130, 300, 25);
        panel.add(lPassengerPhone);

        JLabel lPassengerNationality = new JLabel(messages.getString("nationality") + ": " + passengerData.getNationality());
        lPassengerNationality.setBounds(10, 160, 300, 25);
        panel.add(lPassengerNationality);

        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        JLabel lPassengerBirthdate = new JLabel(messages.getString("birthdate") + ": " + dateFormat.format(new Date(passengerData.getBirthdate())));
        lPassengerBirthdate.setBounds(10, 190, 300, 25);
        panel.add(lPassengerBirthdate);

        JLabel lAirline = new JLabel(messages.getString("airlineId") + ": " + bookingData.getAirlineId());
        lAirline.setBounds(10, 220, 300, 25);
        panel.add(lAirline);

        JButton bClose = new JButton(messages.getString("close"));
        bClose.setBounds(320, 260, 85, 25);
        panel.add(bClose);

        bClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deinit();
                dispose();
            }
        });

        // Language selector
        pLanguage = new JPanel(new FlowLayout());
        languageSelector = new JComboBox<>(new Locale[]{new Locale("es", "ES"), Locale.US});
        lLanguage = new JLabel(messages.getString("language"));

        pLanguage.add(lLanguage);
        pLanguage.add(languageSelector);
        pLanguage.setBounds(10, 260, 200, 40);
        getContentPane().add(pLanguage);

        languageSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeLanguage((Locale) languageSelector.getSelectedItem(), bookingId);
            }
        });
    }

    
    /**
     * Method that returns the singleton instance of {@link BookingDetailsWindow}.
     * @param bookingId Integer of the identification of the {@link Booking}.
     * @return If there exists an instance of {@link BookingDetailsWindow} it will return it. If not, it will create a new one.
     */

    private void initResourceBundle(Locale locale) {
        try {
            messages = ResourceBundle.getBundle("Multilingual.messages", locale);
        } catch (Exception e) {
            System.err.println("Failed to load resource bundle: " + e.getMessage());
        }
    }

    private void changeLanguage(Locale locale, int bookingId) {
        Locale.setDefault(locale);
        initResourceBundle(locale);
        updateComponents(bookingId);
    }

    private void updateComponents(int bookingId) {
        BookingData bookingData = BookingController.getInstance(AeroLogixClient.getInstance()).getBooking(bookingId);
        PassengerData passengerData = PassengerController.getInstance(AeroLogixClient.getInstance()).getPassenger(bookingData.getPassengerDNI());

        this.setTitle("AeroLogix - " + messages.getString("booking") + " " + bookingId + ": " + messages.getString("details"));

        JPanel panel = (JPanel) getContentPane().getComponent(0);
        ((JLabel) panel.getComponent(0)).setText(messages.getString("bookingId") + ": " + bookingData.getId());
        ((JLabel) panel.getComponent(1)).setText(messages.getString("passengerName") + ": " + passengerData.getName());
        ((JLabel) panel.getComponent(2)).setText(messages.getString("passengerDNI") + ": " + passengerData.getDNI());
        ((JLabel) panel.getComponent(3)).setText(messages.getString("passengerEmail") + ": " + passengerData.getEmail());
        ((JLabel) panel.getComponent(4)).setText(messages.getString("passengerPhone") + ": " + passengerData.getPhone());
        ((JLabel) panel.getComponent(5)).setText(messages.getString("nationality") + ": " + passengerData.getNationality());
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        ((JLabel) panel.getComponent(6)).setText(messages.getString("birthdate") + ": " + dateFormat.format(new Date(passengerData.getBirthdate())));
        ((JLabel) panel.getComponent(7)).setText(messages.getString("airlineId") + ": " + bookingData.getAirlineId());

        ((JButton) panel.getComponent(8)).setText(messages.getString("close"));
        lLanguage.setText(messages.getString("language"));
    }


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
