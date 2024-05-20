package com.aerologix.app.client.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.client.controller.FlightController;
import com.aerologix.app.server.pojo.FlightData;
import com.aerologix.app.server.pojo.UserData;

public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private static MainWindow instance;
    private UserData user;

    private JPanel panel;
    private JPanel logoPanel;
    private JLabel logo;
    private JPanel filterPanel;
    private JPanel flightFP;
    private JTextField flightFilter;
    private JLabel flightLabel;
    private JPanel airlinesFP;
    private JComboBox<String> airlinesFilter;
    private JLabel airlinesLabel;
    private JPanel originFP;
    private JComboBox<String> originFilter;
    private JLabel originLabel;
    private JPanel destinationFP;
    private JComboBox<String> destinationFilter;
    private JLabel destinationLabel;
    private JPanel dateFP;
    private JSpinner dateFilter;
    private JLabel dateLabel;
    private JButton bookFlightButton; // Nuevo botón

    private JTable flightTable;
    private DefaultTableModel flightTableModel;
    private ArrayList<FlightData> flights;
    private JScrollPane scrollPane;

    private MainWindow(UserData user) {
        this.user = user;

        panel = new JPanel();
        logoPanel = new JPanel();
        logo = new JLabel();
        filterPanel = new JPanel();
        flightFP = new JPanel();
        flightFilter = new JTextField();
        flightLabel = new JLabel();
        airlinesFP = new JPanel();
        airlinesFilter = new JComboBox<>();
        airlinesLabel = new JLabel();
        originFP = new JPanel();
        originFilter = new JComboBox<>();
        originLabel = new JLabel();
        destinationFP = new JPanel();
        destinationFilter = new JComboBox<>();
        destinationLabel = new JLabel();
        dateFP = new JPanel();
        dateFilter = new JSpinner();
        dateLabel = new JLabel();
        bookFlightButton = new JButton("Book flight"); // Nuevo botón con texto "Book flight"

        getContentPane().setLayout(new BorderLayout());
        this.setSize(new Dimension(1000, 500));
        this.setVisible(true);

        this.setTitle("AeroLogix - Menu");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        panel.setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);

        logo.setText("Aerologix");
        logo.setFont(new Font("Arial", Font.BOLD, 20));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(16, 10, 16, 0));
        logoPanel.add(logo);
        panel.add(logoPanel, BorderLayout.NORTH);

        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS)); // Cambiado a BoxLayout.X_AXIS
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        flightFP.setLayout(new BoxLayout(flightFP, BoxLayout.Y_AXIS));
        flightFilter.setPreferredSize(new Dimension(200, 20));
        flightLabel.setText("Flight name");
        flightLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        flightFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
        flightFP.add(flightLabel);
        flightFP.add(Box.createRigidArea(new Dimension(0, 5)));
        flightFP.add(flightFilter);
        filterPanel.add(flightFP);

        filterPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        airlinesFP.setLayout(new BoxLayout(airlinesFP, BoxLayout.Y_AXIS));
        airlinesFilter.setPreferredSize(new Dimension(100, 20));
        airlinesLabel.setText("Airline");
        airlinesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        airlinesFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
        airlinesFP.add(airlinesLabel);
        airlinesFP.add(Box.createRigidArea(new Dimension(0, 5)));
        airlinesFP.add(airlinesFilter);
        filterPanel.add(airlinesFP);

        filterPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        originFP.setLayout(new BoxLayout(originFP, BoxLayout.Y_AXIS));
        originFilter.setPreferredSize(new Dimension(100, 20));
        originLabel.setText("Origin");
        originLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        originFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
        originFP.add(originLabel);
        originFP.add(Box.createRigidArea(new Dimension(0, 5)));
        originFP.add(originFilter);
        filterPanel.add(originFP);

        filterPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        destinationFP.setLayout(new BoxLayout(destinationFP, BoxLayout.Y_AXIS));
        destinationFilter.setPreferredSize(new Dimension(100, 20));
        destinationLabel.setText("Destination");
        destinationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        destinationFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
        destinationFP.add(destinationLabel);
        destinationFP.add(Box.createRigidArea(new Dimension(0, 5)));
        destinationFP.add(destinationFilter);
        filterPanel.add(destinationFP);

        filterPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        dateFP.setLayout(new BoxLayout(dateFP, BoxLayout.Y_AXIS));
        Calendar calendar = Calendar.getInstance();
        Date initDate = calendar.getTime();
        calendar.add(Calendar.YEAR, -100);
        Date earliestDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 200);
        Date latestDate = calendar.getTime();
        SpinnerDateModel sdm = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.DAY_OF_MONTH); // Cambiado para que solo muestre fecha
        dateFilter.setModel(sdm);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateFilter, "dd/MM/yyyy");
        DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false); // Evita entradas inválidas
        formatter.setOverwriteMode(true); // Permite sobrescribir el texto seleccionado
        dateFilter.setEditor(editor);
        dateFilter.setPreferredSize(new Dimension(150, 20)); // Ajustado el tamaño
        dateLabel.setText("Date");
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateFP.add(dateLabel);
        dateFP.add(Box.createRigidArea(new Dimension(0, 5)));
        dateFP.add(dateFilter);
        filterPanel.add(dateFP);

        // Añadir el botón "Book flight"
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Espacio entre filtros y botón
        filterPanel.add(bookFlightButton);

        panel.add(filterPanel, BorderLayout.NORTH);

        flightTable = new JTable();
        flightTable.setPreferredSize(new Dimension(500, 400));
        loadFlights();

        scrollPane = new JScrollPane(flightTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    private void loadFlights() {
        String[] columnNames = { "ID", "Origin", "Destination", "Date", "Aircraft ID", "Bookings" };

        flightTableModel = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        flightTable.setModel(flightTableModel);

        flights = FlightController.getInstance(AeroLogixClient.getInstance()).getAllFlights();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (flights != null && flights.size() > 0) {
	        for (FlightData flight : flights) {
	            String formattedDate = dateFormat.format(new Date(flight.getDate()));
	
	            String bookings = flight.getBookingIds().toString();
	
	            Object[] row = { flight.getIdFlight(), flight.getOrigin(), flight.getDestination(), formattedDate,
	                    flight.getAircraftId(), bookings.length() };
	            
	            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	            for (int i = 0; i < flightTable.getColumnCount(); i++) {
	                flightTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
	            }
	
	            flightTableModel.addRow(row);
	        }
        }
    }


    public static MainWindow getInstance(UserData user) {
        if (instance == null) {
            instance = new MainWindow(user);
        }
        return instance;
    }

    public static void deinit() {
        instance = null;
    }
}
