package com.aerologix.app.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;

import com.aerologix.app.client.AeroLogixClient;
import com.aerologix.app.client.controller.AirlineController;
import com.aerologix.app.client.controller.FlightController;
import com.aerologix.app.server.pojo.AirlineData;
import com.aerologix.app.server.pojo.FlightData;
import com.aerologix.app.server.pojo.UserData;

/**
 * @brief Class for the main menu window.
 * 
 *        Class for the main menu window.
 *        <p>
 *        It allows users to navigate, search, filter flights and allows to view
 *        their details by opening an instance of {@link FlightWindow}.
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private static MainWindow instance;
	private UserData user;

	private JPanel panel;
	private JLabel logo;
	private JPanel filterPanel;
	private JPanel flightFP;
	private JTextField flightFilter;
	private JLabel flightLabel;
	private JPanel originFP;
	private JComboBox<String> originFilter;
	private JLabel originLabel;
	private JPanel destinationFP;
	private JComboBox<String> destinationFilter;
	private JLabel destinationLabel;
	private JPanel dateFP;
	private JSpinner dateFilter;
	private JLabel dateLabel;
	private JButton bookFlightButton;
	private JTable flightTable;
	private DefaultTableModel flightTableModel;
	private ArrayList<FlightData> flights;
	private ArrayList<AirlineData> airlines;
	private JScrollPane scrollPane;
	private JButton resetFiltersButton;
	private JButton resetFlightFilterButton;
	private JButton resetAirlinesFilterButton;
	private JButton resetOriginFilterButton;
	private JButton resetDestinationFilterButton;
	private JButton resetDateFilterButton;
	private JPanel resetFlightP;
	private JPanel resetAirlinesP;
	private JPanel resetOriginP;
	private JPanel resetDestiantionP;
	private JPanel resetDateP;
	private JLabel lLanguage;

	protected JPanel pLanguage = new JPanel(new FlowLayout());
	protected JComboBox<Locale> languageSelector;
	private ResourceBundle messages;

	/**
	 * Private constructor of the window.
	 * 
	 * @param user   {@link UserData} of the user that is logged in.
	 * @param locale represents a specific region for which the resource bundle is
	 *               to be loaded.
	 */
	private MainWindow(UserData user, Locale locale) {
		this.user = user;
		initResourceBundle(Locale.getDefault());
		languageSelector = new JComboBox<>(new Locale[] { new Locale("es", "ES"), Locale.US });

		messages = ResourceBundle.getBundle("Multilingual.messages", locale);
		ImageIcon originalIcon = new ImageIcon("static/img/retry.png");
		Image scaledImage = originalIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(scaledImage);

		panel = new JPanel();
		logo = new JLabel();
		filterPanel = new JPanel();
		filterPanel.setBackground(new Color(200, 200, 200));
		flightFP = new JPanel();
		flightFP.setBackground(new Color(200, 200, 200));
		flightFilter = new JTextField();
		flightLabel = new JLabel();
		originFP = new JPanel();
		originFP.setBackground(new Color(200, 200, 200));
		originFilter = new JComboBox<>();
		originLabel = new JLabel();
		destinationFP = new JPanel();
		destinationFP.setBackground(new Color(200, 200, 200));
		destinationFilter = new JComboBox<>();
		destinationLabel = new JLabel();
		dateFP = new JPanel();
		dateFP.setBackground(new Color(200, 200, 200));
		dateFilter = new JSpinner();
		dateLabel = new JLabel();
		bookFlightButton = new JButton(messages.getString("view_flight"));
		resetFiltersButton = new JButton(messages.getString("reset_all"));
		resetFlightP = new JPanel();
		resetFlightP.setBackground(new Color(200, 200, 200));
		resetFlightP.setLayout(new FlowLayout(FlowLayout.LEFT));
		resetFlightFilterButton = new JButton(scaledIcon);
		resetFlightFilterButton.addActionListener(e -> resetFlightFilter());
		resetFlightFilterButton.setBorder(null);
		resetFlightFilterButton.setBackground(filterPanel.getBackground());
		resetOriginP = new JPanel();
		resetOriginP.setBackground(new Color(200, 200, 200));
		resetOriginP.setLayout(new FlowLayout(FlowLayout.LEFT));
		resetOriginFilterButton = new JButton(scaledIcon);
		resetOriginFilterButton.addActionListener(e -> resetOriginFilter());
		resetOriginFilterButton.setBorder(null);
		resetOriginFilterButton.setBackground(filterPanel.getBackground());
		resetDestiantionP = new JPanel();
		resetDestiantionP.setBackground(new Color(200, 200, 200));
		resetDestiantionP.setLayout(new FlowLayout(FlowLayout.LEFT));
		resetDestinationFilterButton = new JButton(scaledIcon);
		resetDestinationFilterButton.addActionListener(e -> resetDestinationFilter());
		resetDestinationFilterButton.setBorder(null);
		resetDestinationFilterButton.setBackground(filterPanel.getBackground());
		resetDateP = new JPanel();
		resetDateP.setBackground(new Color(200, 200, 200));
		resetDateP.setLayout(new FlowLayout(FlowLayout.LEFT));
		resetDateFilterButton = new JButton(scaledIcon);
		resetDateFilterButton.addActionListener(e -> resetDateFilter());
		resetDateFilterButton.setBorder(null);
		resetDateFilterButton.setBackground(filterPanel.getBackground());

		getContentPane().setLayout(new BorderLayout());
		this.setSize(new Dimension(1200, 500));
		this.setVisible(true);

		this.setTitle(messages.getString("title"));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		panel.setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.CENTER);

		filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
		filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		logo.setText(messages.getString("logo"));
		logo.setFont(new Font("Arial", Font.BOLD, 20));
		logo.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		filterPanel.add(logo);
		lLanguage = new JLabel(messages.getString("language"));
		pLanguage.add(lLanguage);
		pLanguage.add(languageSelector);
		panel.add(pLanguage, BorderLayout.SOUTH);

		languageSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeLanguage(user, (Locale) languageSelector.getSelectedItem());
			}
		});

		bookFlightButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onBookFlightButtonClick();
			}
		});

		flightFP.setLayout(new BoxLayout(flightFP, BoxLayout.Y_AXIS));
		flightLabel.setText(messages.getString("flight_name"));
		resetFlightP.add(flightLabel);
		resetFlightP.add(resetFlightFilterButton);
		flightFP.add(resetFlightP);
		flightFP.add(Box.createRigidArea(new Dimension(0, 5)));
		flightFP.add(flightFilter);
		filterPanel.add(flightFP);

		filterPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		filterPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		originFP.setLayout(new BoxLayout(originFP, BoxLayout.Y_AXIS));
		originFilter.setPreferredSize(new Dimension(100, 20));
		originLabel.setText(messages.getString("origin"));
		resetOriginP.add(originLabel);
		resetOriginP.add(resetOriginFilterButton);
		originFP.add(resetOriginP);
		originFP.add(Box.createRigidArea(new Dimension(0, 5)));
		originFP.add(originFilter);
		filterPanel.add(originFP);

		filterPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		destinationFP.setLayout(new BoxLayout(destinationFP, BoxLayout.Y_AXIS));
		destinationFilter.setPreferredSize(new Dimension(100, 20));
		destinationLabel.setText(messages.getString("destination"));
		resetDestiantionP.add(destinationLabel);
		resetDestiantionP.add(resetDestinationFilterButton);
		destinationFP.add(resetDestiantionP);
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
		SpinnerDateModel sdm = new SpinnerDateModel(initDate, earliestDate, null, Calendar.DAY_OF_MONTH);
		dateFilter.setModel(sdm);
		JSpinner.DateEditor editor = new JSpinner.DateEditor(dateFilter, "dd/MM/yyyy");
		DateFormatter formatter = (DateFormatter) editor.getTextField().getFormatter();
		formatter.setAllowsInvalid(false);
		formatter.setOverwriteMode(true);
		dateFilter.setEditor(editor);
		dateFilter.setPreferredSize(new Dimension(150, 20));
		dateLabel.setText(messages.getString("date"));
		resetDateP.add(dateLabel);
		resetDateP.add(resetDateFilterButton);
		dateFP.add(resetDateP);
		dateFP.add(Box.createRigidArea(new Dimension(0, 5)));
		dateFP.add(dateFilter);
		filterPanel.add(dateFP);
		filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		filterPanel.add(resetFiltersButton);
		filterPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		filterPanel.add(bookFlightButton);

		panel.add(filterPanel, BorderLayout.NORTH);

		flightTable = new JTable();
		flightTable.setPreferredSize(new Dimension(500, 400));
		loadFlights();
		loadFilters();

		centerWindow();

		scrollPane = new JScrollPane(flightTable);
		flightTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && flightTable.getSelectedRow() != -1) {
					int row = flightTable.getSelectedRow();
					FlightData flightData = flights.get(row);
					onRowDoubleClick(flightData);
				}
			}
		});
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.revalidate();
		panel.repaint();
		flightFilter.getDocument().addDocumentListener(new FilterListener(this::filterFlightsByFlightId));
		originFilter.addActionListener(e -> filterFlightsByOrigin());
		destinationFilter.addActionListener(e -> filterFlightsByDestination());
		dateFilter.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				filterFlightsByDate();
			}
		});
	}

	/**
	 * Initializes the resource bundle for multilingual support based on the
	 * specified locale.
	 * <p>
	 * This method attempts to load a resource bundle named "Multilingual.messages"
	 * using the provided locale. If the resource bundle cannot be loaded, an error
	 * message is printed to the standard error stream.
	 * </p>
	 * 
	 * @param locale represents a specific region for which the resource bundle is
	 *               to be loaded.
	 */
	private void initResourceBundle(Locale locale) {
		try {
			messages = ResourceBundle.getBundle("Multilingual.messages", locale);
		} catch (Exception e) {
			System.err.println("Failed to load resource bundle: " + e.getMessage());
		}
	}

	/**
	 * Method that changes the language of the window.
	 * 
	 * @param locale represents a specific region for which the resource bundle is
	 *               to be loaded.
	 */
	private void changeLanguage(UserData user, Locale locale) {
		Locale.setDefault(locale);
		initResourceBundle(locale);
		loadFlights();
		loadFilters();
		updateComponents(MainWindow.getInstance(user, locale));
	}

	/**
	 * Method that updates the components that support multilingualism.
	 */
	private void updateComponents(MainWindow instance) {
		flightLabel.setText(messages.getString("flight_name"));
		originLabel.setText(messages.getString("origin"));
		destinationLabel.setText(messages.getString("destination"));
		dateLabel.setText(messages.getString("date"));
		bookFlightButton.setText(messages.getString("view_flight"));
		resetFiltersButton.setText(messages.getString("reset_all"));
		lLanguage.setText(messages.getString("language"));
		instance.setTitle(messages.getString("title"));

	}

	/**
	 * Method that resets all set filters (flight ID, airline ID, origin,
	 * destination and date).
	 */
	private void resetAllFilters() {
		resetFlightFilter();
		resetOriginFilter();
		resetDestinationFilter();
		resetDateFilter();
	}

	/** Method that resets the filter for the flight ID of flights. */
	private void resetFlightFilter() {
		flightFilter.setText("");
	}

	/** Method that resets the filter for the origin of flights. */
	private void resetOriginFilter() {
		originFilter.setSelectedIndex(-1);
	}

	/** Method that resets the filter for the destination of the flight. */
	private void resetDestinationFilter() {
		destinationFilter.setSelectedIndex(-1);
	}

	/** Method that resets the filter for the date of flights. */
	private void resetDateFilter() {
		Calendar calendar = Calendar.getInstance();
		dateFilter.setValue(calendar.getTime());
	}

	/**
	 * Method that loads the data of all flights into the JComboBoxes for the
	 * origin, destination and airline ID.
	 */
	private void loadFilters() {
		airlines = AirlineController.getInstance(AeroLogixClient.getInstance()).getAllAirlines();
		Set<String> origins = new HashSet<>();
		Set<String> destinations = new HashSet<>();
		Set<String> airlineNames = new HashSet<>();

		// Add unique origins and destinations
		for (FlightData flight : flights) {
			origins.add(flight.getOrigin().toString());
			destinations.add(flight.getDestination().toString());
		}

		// Add unique airlines
		for (AirlineData airline : airlines) {
			airlineNames.add(airline.getName());
		}

		// Populate originFilter with unique origins
		for (String origin : origins) {
			originFilter.addItem(origin);
		}

		// Populate destinationFilter with unique destinations
		for (String destination : destinations) {
			destinationFilter.addItem(destination);
		}
	}

	/**
	 * Method that loads the data of each flight registered into a new line in the
	 * table model of the JTable.
	 */
	private void loadFlights() {
		String[] columnNames = new String[] { "ID", messages.getString("origin"), messages.getString("destination"),
				messages.getString("date"), messages.getString("bookings") };

		flightTableModel = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		flightTable.setModel(flightTableModel);

		flights = FlightController.getInstance(AeroLogixClient.getInstance()).getAllFlights();

		DateFormat obj = new SimpleDateFormat("dd MMM yyyy HH:mm");

		for (FlightData flight : flights) {
			String formattedDate = obj.format(new Date(flight.getDate()));

			Object[] row = { flight.getIdFlight(), flight.getOrigin(), flight.getDestination(), formattedDate,
					flight.getAircraftId(), Integer.toString(flight.getBookingIds().size()) };

			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			for (int i = 0; i < flightTable.getColumnCount(); i++) {
				flightTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			}

			flightTableModel.addRow(row);
		}
	}

	/** Method that filters flights by the flight ID introduced in the filter. */
	private void filterFlightsByFlightId() {
		String flightIdFilter = flightFilter.getText().toLowerCase();
		flightTableModel.setRowCount(0);

		for (FlightData flight : flights) {
			if (!flightIdFilter.isEmpty()
					&& !String.valueOf(flight.getIdFlight()).toLowerCase().contains(flightIdFilter)) {
				continue;
			}
			addRowToTable(flight);
		}
	}

	/** Method that filters flights by the origin selected in the filter. */
	private void filterFlightsByOrigin() {
		String originFilterText = originFilter.getSelectedItem() != null
				? originFilter.getSelectedItem().toString().toLowerCase()
				: "";
		flightTableModel.setRowCount(0);

		for (FlightData flight : flights) {
			if (!originFilterText.isEmpty() && !flight.getOrigin().toLowerCase().contains(originFilterText)) {
				continue;
			}
			addRowToTable(flight);
		}
	}

	/** Method that filters flights by the destination selected in the filter. */
	private void filterFlightsByDestination() {
		String destinationFilterText = destinationFilter.getSelectedItem() != null
				? destinationFilter.getSelectedItem().toString().toLowerCase()
				: "";
		flightTableModel.setRowCount(0);

		for (FlightData flight : flights) {
			if (!destinationFilterText.isEmpty()
					&& !flight.getDestination().toLowerCase().contains(destinationFilterText)) {
				continue;
			}
			addRowToTable(flight);
		}
	}

	/** Method that filters flights by the introduced date in the filter. */
	private void filterFlightsByDate() {
	    Date dateFilterValue = (Date) dateFilter.getValue();
	    flightTableModel.setRowCount(0);

	    for (FlightData flight : flights) {
	        if (dateFilterValue != null) {
	            Date flightDate = new Date(flight.getDate() * 1000L);
	            Calendar calendar = Calendar.getInstance();
	            calendar.setTime(flightDate);

	            if (dateFilterValue.before(calendar.getTime())) {
	                continue;
	            }
	        }
	        addRowToTable(flight);
	    }
	}

	/**
	 * Method that adds a row to the table model.
	 * 
	 * @param flight {@link FlightData} instance of the flight which data will be
	 *               added to the table.
	 */
	private void addRowToTable(FlightData flight) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
		String formattedDate = dateFormat.format(new Date(flight.getDate()));
		Object[] row = { flight.getIdFlight(), flight.getOrigin(), flight.getDestination(), formattedDate,
				flight.getAircraftId(), flight.getBookingIds().size() };
		flightTableModel.addRow(row);
	}

	/**
	 * A listener that triggers a specified filter method whenever a change occurs
	 * in the document.
	 * <p>
	 * This listener can be used to execute a filtering operation in response to
	 * insert, remove, or change events in a text component's document.
	 * </p>
	 */
	private class FilterListener implements DocumentListener {
		private Runnable filterMethod;

		/**
		 * Constructs a new {@code FilterListener} with the specified filter method.
		 *
		 * @param filterMethod the {@link Runnable} to run when a document event occurs
		 */
		public FilterListener(Runnable filterMethod) {
			this.filterMethod = filterMethod;
		}

		/**
		 * Invoked when text is inserted into the document.
		 *
		 * @param e the document event
		 */
		@Override
		public void insertUpdate(DocumentEvent e) {
			filterMethod.run();
		}

		/**
		 * Invoked when text is removed from the document.
		 *
		 * @param e the document event
		 */
		@Override
		public void removeUpdate(DocumentEvent e) {
			filterMethod.run();
		}

		/**
		 * Invoked when a style change occurs in the document.
		 *
		 * @param e the document event
		 */
		@Override
		public void changedUpdate(DocumentEvent e) {
			filterMethod.run();
		}
	}

	/**
	 * Method that opens a new {@link FlightWindow} when a row of the table is
	 * double-clicked.
	 * 
	 * @param flight {@link FlightData} instance of the flight in the selected row.
	 */
	private void onRowDoubleClick(FlightData flight) {
		FlightWindow.getInstance(flight.getIdFlight(), user.getEmail()).setVisible(true);
		;
	}

	/**
	 * Method that opens a new {@link FlightWindow} when the JButton
	 * {@code bookFlightButton} is pressed.
	 */
	private void onBookFlightButtonClick() {
		int selectedRow = flightTable.getSelectedRow();
		if (selectedRow != -1) {
			int flightId = (int) flightTable.getValueAt(selectedRow, 0);
			FlightWindow.getInstance(flightId, user.getEmail()).setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, messages.getString("error_sel"), "Error", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Method that sets the window in the center of the user screen.
	 */
	private void centerWindow() {
		Dimension windowSize = getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int largo = (screenSize.width - windowSize.width) / 2;
		int alto = (screenSize.height - windowSize.height) / 2;
		setLocation(largo, alto);
	}

	/**
	 * Method that returns the singleton instance of {@link MainWindow}.
	 * 
	 * @return If there exists an instance of {@link MainWindow} it will return it.
	 *         If not, it will create a new one.
	 */
	public static MainWindow getInstance(UserData user, Locale locale) {
		if (instance == null) {
			instance = new MainWindow(user, locale);
		}
		return instance;
	}

	/** Method that ends the current instance of {@link MainWindow}. */
	public static void deinit() {
		instance = null;
	}
}
