package com.aerologix.app.client.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.Locale;
import java.util.ResourceBundle;


import com.aerologix.app.client.controller.UserController;

/**
 * @brief Class for the user registration window.
 * 
 * 
 * Class for the user registration window.
 * <p>
 * It allows users to create a new user account in the application.
 */
public class RegisterWindow extends JFrame {

    private static RegisterWindow instance;

    private static final long serialVersionUID = 1L;

    // Panels
    protected JPanel pPrincipal = new JPanel(new GridLayout(2, 1));
    protected JPanel pSignup = new JPanel(new GridLayout(6, 1));
    protected JPanel pUser = new JPanel(new FlowLayout());
    protected JPanel psb = new JPanel(new FlowLayout());
    protected JPanel pPass = new JPanel(new FlowLayout());
    protected JPanel psPass = new JPanel(new FlowLayout());
    protected JPanel pNombre = new JPanel(new FlowLayout());
    protected JPanel pLanguage = new JPanel(new FlowLayout());

    // Components signup
    protected JLabel lsMail;
    protected JLabel lsPass;
    protected JLabel lssPass;
    protected JLabel lsNombre;
    protected JTextField tsMail = new JTextField(15);
    protected JTextField tsPass = new JTextField(15);
    protected JTextField tssPass = new JTextField(15);
    protected JTextField tsNombre = new JTextField(15);
    protected JButton bSignup;
    protected JLabel lsError = new JLabel("");
    protected JLabel lLanguage;
    private JComboBox<Locale> languageSelector;

    private ResourceBundle messages;

    /**
     * Private constructor of the window.
     * @param locale represents a specific region 
     * for which the resource bundle is to be loaded.
     */
    private RegisterWindow(Locale locale) {
        // Initialize ResourceBundle
        initResourceBundle(locale);

        // Initialize components with ResourceBundle
        lsMail = new JLabel(messages.getString("mail"));
        lsPass = new JLabel(messages.getString("password"));
        lssPass = new JLabel(messages.getString("confirm_password"));
        lsNombre = new JLabel(messages.getString("name"));
        bSignup = new JButton(messages.getString("create"));
        lLanguage = new JLabel(messages.getString("language"));

        languageSelector = new JComboBox<>(new Locale[]{new Locale("es", "ES"), Locale.US});
        languageSelector.setPreferredSize(new Dimension(100, 25));
        this.setTitle(messages.getString("create") + " - AeroLogix");
        this.setSize(450, 320);
        centerWindow();
        this.setLayout(new FlowLayout());

        // Construction signup
        pSignup.setBorder(BorderFactory.createTitledBorder(messages.getString("register_user")));
        pUser.add(lsMail);
        pUser.add(tsMail);
        pNombre.add(lsNombre);
        pNombre.add(tsNombre);
        pPass.add(lsPass);
        pPass.add(tsPass);
        psPass.add(lssPass);
        psPass.add(tssPass);
        pSignup.add(pUser);
        pSignup.add(pNombre);
        pSignup.add(pPass);
        pSignup.add(psPass);
        psb.add(bSignup);
        psb.add(lsError);
        pSignup.add(psb);
        pLanguage.add(lLanguage);
        pLanguage.add(languageSelector);
        pPrincipal.add(pSignup);
        pPrincipal.add(pLanguage);
        this.add(pPrincipal);

        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                deinit();
                dispose();
            }
        });
        
        languageSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeLanguage((Locale) languageSelector.getSelectedItem());
            }
        });
		// Signup
		bSignup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!tsMail.getText().isEmpty() & !tsPass.getText().isEmpty() & !tssPass.getText().isEmpty() & !tsNombre.getText().isEmpty()) {
					if (tsPass.getText().equals(tssPass.getText())) {
						int code = UserController.getInstance().registerUser(tsMail.getText(), tsPass.getText(), "COUNTER_CLERK", tsNombre.getText());
						if (code == 0) {
							lsError.setText("");
							tsMail.setText("");
							tsNombre.setText("");
							tsPass.setText("");
							tssPass.setText("");

							lsError.setText("User registered successfully, try to log in");
							Thread hilo = new Thread() {
								@Override
								public void run() {
									waitTime(4000);
									lsError.setText("");
									RegisterWindow.instance = null;
									dispose();
								}

							};
							hilo.start();
						} else {
							Thread hilo = new Thread() {
								@Override
								public void run() {
									lsError.setText("Email is already in use");
									waitTime(4000);
									lsError.setText("");
								}

							};
							hilo.start();
						}
					} else {
						lsError.setText("Passwords do not match");
					}
				} else {
					Thread hilo = new Thread() {

						@Override
						public void run() {
							lsError.setText("Fill in all fields");
							waitTime(4000);
							lsError.setText("");
						}
					};
					hilo.start();
				}
			}
		});

	}

    /**
     * Initializes the resource bundle for multilingual support based on the specified locale.
     * <p>
     * This method attempts to load a resource bundle named "Multilingual.messages" using the provided locale.
     * If the resource bundle cannot be loaded, an error message is printed to the standard error stream.
     * </p>
     * 
     * @param locale represents a specific region 
     * for which the resource bundle is to be loaded.
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
     * @param locale represents a specific region 
     * for which the resource bundle is to be loaded.
     */
    private void changeLanguage(Locale locale) {
        Locale.setDefault(locale);
        initResourceBundle(locale);
        updateComponents();
    }

    /**
     * Method that updates the components that support multilingualism.
     */
    private void updateComponents() {
        lsMail.setText(messages.getString("mail"));
        lsPass.setText(messages.getString("password"));
        lssPass.setText(messages.getString("confirm_password"));
        lsNombre.setText(messages.getString("name"));
        bSignup.setText(messages.getString("register"));
        pSignup.setBorder(BorderFactory.createTitledBorder(messages.getString("register_user")));
        this.setTitle(messages.getString("signup") + " - AeroLogix");
        lLanguage.setText(messages.getString("language"));
    }
    
    /**
     * Method that sets the window in the center of the user screen.
     */
	private void centerWindow() {
		Dimension windowSize = getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int largo = (screenSize.width - windowSize.width) / 2;
		int alto = (screenSize.height - windowSize.height) / 2;

		setLocation(largo - (largo / 4), alto + (largo / 4));
	}

    /**
     * Method that sets a Thread to sleep so that the main Thread
     * of the application has to wait before executing the next
     * line of code.
     * @param tiempo Integer that indicates the milliseconds that the Thread will be sleeping.
     */
	public static void waitTime(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

    /**
     * Method that returns the singleton instance of {@link RegisterWindow}.
     * @return If there exists an instance of {@link RegisterWindow} it will return it. If not, it will create a new one.
     */
	public static RegisterWindow getInstance(Locale locale) {
		if (RegisterWindow.instance == null) {
			RegisterWindow.instance = new RegisterWindow(locale);
		}
		return RegisterWindow.instance;
	}

    /** Method that ends the current instance of {@link RegisterWindow}. */
	public static void deinit() {
		instance = null;
	}

}