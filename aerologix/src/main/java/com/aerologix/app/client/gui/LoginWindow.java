package com.aerologix.app.client.gui;

import javax.swing.*;

import com.aerologix.app.client.controller.UserController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

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
    private JList<String> stat1Info;

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


    // Palette
    protected final Color background = new Color(240, 240, 240);
    protected final Color url = new Color(0, 93, 232);

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
        this.setSize(450, 280);
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
        pLanguage.add(new JLabel("Language:"));
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
            	RegisterWindow rw = RegisterWindow.getInstance();
				rw.setVisible(true);
                System.out.println("Opening register window...");
            }
        });

        languageSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeLanguage((Locale) languageSelector.getSelectedItem());
            }
        });
    }

    public static void waitTime(int tiempo) {
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException e) { }
    }

    private void initResourceBundle(Locale locale) {
        try {
            messages = ResourceBundle.getBundle("Multilingual.messages", locale);
        } catch (Exception e) {
            System.err.println("Failed to load resource bundle: " + e.getMessage());
        }
    }

    private void changeLanguage(Locale locale) {
        Locale.setDefault(locale);
        initResourceBundle(locale);
        updateComponents();
    }

    private void updateComponents() {
        lMail.setText(messages.getString("mail"));
        lPass.setText(messages.getString("password"));
        bLogin.setText(messages.getString("login"));
        lSignup.setText(messages.getString("register"));
        bSignup.setText(messages.getString("create"));
        this.setTitle(messages.getString("login") + " - AeroLogix");
    }

    private void centerWindow() {
        Dimension windowSize = getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int largo = (screenSize.width - windowSize.width) / 2;
        int alto = (screenSize.height - windowSize.height) / 2;
        setLocation(largo, alto);
    }

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

    public static LoginWindow getInstanceLogin() {
        if (LoginWindow.instanceLogin == null) {
            LoginWindow.instanceLogin = new LoginWindow();
        }
        return LoginWindow.instanceLogin;
    }

    public static void deinit() {
        instanceLogin = null;
    }
}
