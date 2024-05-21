package com.aerologix.app.client.gui;

// import statements
import javax.swing.*;

import com.aerologix.app.client.controller.UserController;
import com.aerologix.app.server.jdo.User.UserType;
import com.aerologix.app.server.pojo.UserData;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * @brief Class for the login window.
 * 
 * 
 * Class for the login window.
 * <p>
 * It allows users to log in the application. Additionally, it
 * offers access to the {@link RegisterWindow}.
 */
public class LoginWindow extends JFrame {

    private static LoginWindow instanceLogin;
    private static final long serialVersionUID = 1L;

    // Panels
    protected JPanel pLogin = new JPanel(new GridLayout(5,1));
    protected JPanel pPrincipal = new JPanel(new BorderLayout());
    protected JPanel plUser = new JPanel(new FlowLayout());
    protected JPanel plb = new JPanel(new FlowLayout());
    protected JPanel plPass = new JPanel(new FlowLayout());
    protected JPanel pSignup = new JPanel(new FlowLayout());
    protected JPanel pLogo = new JPanel(new FlowLayout());
    protected JPanel pLanguage = new JPanel(new FlowLayout());

    // Components login
    private ResourceBundle messages;
    protected JLabel lMail;
    protected JLabel lPass;
    protected JTextField tMail = new JTextField(15);
    protected JPasswordField tPass = new JPasswordField(15);
    protected JButton bLogin;
    protected JLabel lError = new JLabel("");
    protected JButton bShow = new JButton("👁");
    protected JTextField tShow = new JTextField(15);
    protected JLabel lSignup;
    protected JButton bSignup;
    protected JLabel lLogo = new JLabel("AeroLogix");
    protected JComboBox<Locale> languageSelector;
    protected JLabel lLanguage;


    // Palette
    protected final Color background = new Color(240, 240, 240);
    protected final Color url = new Color(0, 93, 232);

    /**
     * Private constructor of the window.
     */
    private LoginWindow() {
        // Initialize ResourceBundle
        initResourceBundle(Locale.getDefault());

        // Initialize components with ResourceBundle
        lMail = new JLabel(messages.getString("mail"));
        lPass = new JLabel(messages.getString("password"));
        bLogin = new JButton(messages.getString("login"));
        lSignup = new JLabel(messages.getString("register"));
        bSignup = new JButton(messages.getString("create"));
        languageSelector = new JComboBox<>(new Locale[]{new Locale("es", "ES"),Locale.US});

        this.setTitle("Login - AeroLogix");
        this.setSize(450, 300);
        centerWindow();
        this.setLayout(new FlowLayout());

        // Construction login
        plUser.add(lMail);
        plUser.add(tMail);
        plPass.add(lPass);
        plPass.add(tPass);
        tShow.setVisible(false);
        tShow.setEditable(false);
        plPass.add(tShow);
        plPass.add(bShow);
        bShow.setBorder(BorderFactory.createEmptyBorder());
        bShow.setFont(new Font(null, Font.BOLD, 25));
        pLogin.add(plUser);
        pLogin.add(plPass);
        plb.add(bLogin);
        plb.add(lError);
        pLogin.add(plb);
        pSignup.add(lSignup);
        bSignup.setBorder(BorderFactory.createEmptyBorder());
        pSignup.add(bSignup);
        pLogin.add(pSignup);
        lLanguage = new JLabel(messages.getString("language"));
        pLanguage.add(lLanguage);
        pLanguage.add(languageSelector);
        pLogin.add(pLanguage);
        lLogo.setFont(new Font("Arial", Font.BOLD, 30));
        pLogo.add(lLogo);
        pPrincipal.add(pLogo, BorderLayout.NORTH);
        pPrincipal.add(pLogin, BorderLayout.CENTER);

        // Background Selection
        this.getContentPane().setBackground(background);
        pLogin.setBackground(background);
        pPrincipal.setBackground(background);
        plUser.setBackground(background);
        plb.setBackground(background);
        plPass.setBackground(background);
        pSignup.setBackground(background);
        pLogo.setBackground(background);
        bShow.setBackground(background);
        bSignup.setBackground(background);
        bSignup.setForeground(url);

        this.add(pPrincipal);
        this.setVisible(true);
        pLogin.getRootPane().setDefaultButton(bLogin);

        //    	Login
		bLogin.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(UserController.getInstance().login(tMail.getText(), tPass.getText())) {
						Thread hilo = new Thread() {
								
							@Override
							public void run() {
								lError.setText("Logging in...");
								waitTime(1000);
								lError.setText("");
								LoginWindow.instanceLogin = null;
								dispose();
								UserData user = new UserData();
								user.setEmail(tMail.getText());
								user.setName("");
								user.setPassword(tPass.getPassword().toString());
								user.setUserType(UserType.COUNTER_CLERK.toString());
								MainWindow mw = MainWindow.getInstance(user,new Locale("es", "ES"));
							}
						};
						hilo.start();
					} else {
						Thread hilo = new Thread() {
							@Override
							public void run() {
								lError.setText("Invalid login credentials");
								waitTime(4000);
								lError.setText("");
							}
						};
						hilo.start();
					}
				} catch(NullPointerException e1) {
					Thread hilo = new Thread() {
						@Override
						public void run() {
							lError.setText("Invalid login credentials");
							waitTime(4000);
							lError.setText("");
						}
					};
					hilo.start();
				}
			}
			
		});	

        // Close window
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                deinit();
                dispose();
            }
        });

        // Show/hide password
        bShow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHide();
            }
        });

        // Create account
        bSignup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	RegisterWindow rw = RegisterWindow.getInstance(new Locale("es", "ES"));
				rw.setVisible(true);
                System.out.println("Opening register window...");
            }
        });

        // Change language
        languageSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeLanguage((Locale) languageSelector.getSelectedItem());
            }
        });
    }

    /**
     * Method that sets a Thread to sleep so that the main Thread
     * of the application has to wait before executing the next
     * line of code.
     * @param tiempo Integer that indicates the milliseconds that the Thread will be sleeping.
     */
    public static void waitTime(int tiempo) {
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException e) { }
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
        lMail.setText(messages.getString("mail"));
        lPass.setText(messages.getString("password"));
        bLogin.setText(messages.getString("login"));
        lSignup.setText(messages.getString("register"));
        bSignup.setText(messages.getString("create"));
        this.setTitle(messages.getString("login") + " - AeroLogix");
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
        setLocation(largo, alto);
    }

    /**
     * Method that shows the content of the JPasswordField for 1 second.
     */
    public void showHide() {
        Thread hilo = new Thread() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                tShow.setText(tPass.getText());
                tShow.setVisible(true);
                tPass.setVisible(false);
                plPass.updateUI();
                waitTime(1000);
                tShow.setVisible(false);
                tPass.setVisible(true);
                plPass.updateUI();
            }
        };
        hilo.start();
    }

    /**
     * Method that returns the singleton instance of {@link LoginWindow}.
     * @return If there exists an instance of {@link LoginWindow} it will return it. If not, it will create a new one.
     */
    public static LoginWindow getInstanceLogin() {
        if (LoginWindow.instanceLogin == null) {
            LoginWindow.instanceLogin = new LoginWindow();
        }
        return LoginWindow.instanceLogin;
    }

    /** Method that ends the current instance of {@link LoginWindow}. */
    public static void deinit() {
        instanceLogin = null;
    }
}
