package com.aerologix.app.client.gui;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.aerologix.app.client.AeroLogixClient;

public class LoginWindow extends JFrame {
	
	private static LoginWindow instanceLogin;
	
	private static final long serialVersionUID = 1L;
	
	//	Panels
	protected JPanel pLogin = new JPanel(new GridLayout(4,1));
	protected JPanel pPrincipal = new JPanel(new BorderLayout());
	protected JPanel plUser = new JPanel(new FlowLayout());
	protected JPanel plb = new JPanel(new FlowLayout());
	protected JPanel plPass = new JPanel(new FlowLayout());
	protected JPanel pSignup = new JPanel(new FlowLayout());
	protected JPanel pLogo = new JPanel(new FlowLayout());
	
	//	Components login

	protected JLabel lMail = new JLabel("Mail: ");
	protected JLabel lPass = new JLabel("Password: ");
	protected JTextField tMail = new JTextField(15);
	protected JPasswordField tPass = new JPasswordField(15);
	protected JButton bLogin = new JButton("Login");
	protected JLabel lError = new JLabel("");
	protected JButton bShow = new JButton("👁");
	protected JTextField tShow = new JTextField(15);
	protected JLabel lSignup = new JLabel("¿Not registered?");
	protected JButton bSignup = new JButton("Create an account");
	protected JLabel lLogo = new JLabel("AeroLogix");
	
	
	
	//	Palette
	
	protected final Color background = new Color(240, 240, 240);
	protected final Color url = new Color(0, 93, 232);
	
	private LoginWindow(AeroLogixClient aerologixClient) {
		this.setTitle("Login - AeroLogix");
		this.setSize(450, 280);
		centerWindow();
		this.setLayout(new FlowLayout());
		
		//	Background Selection
		this.getContentPane().setBackground(background);
		pLogin.setBackground(background);
		pPrincipal.setBackground(background);
		plUser.setBackground(background);
		plb.setBackground(background);
		plPass.setBackground(background);
		pSignup.setBackground(background);
		pLogo.setBackground(background);
		bShow.setBackground(background);
		removeBtnClickBg(bShow);
		bSignup.setBackground(background);
		removeBtnClickBg(bSignup);
		bSignup.setForeground(url);
		
		//	Construccion login
		plUser.add(lMail);
		plUser.add(tMail);
		plPass.add(lPass);
		plPass.add(tPass);
		tShow.setVisible(false);
		tShow.setEditable(false);
		plPass.add(tShow);
		bShow.setBorder(BorderFactory.createEmptyBorder());
		bShow.setFont(new Font(null, Font.BOLD, 25));
		plPass.add(bShow);
		pLogin.add(plUser);
		pLogin.add(plPass);
		plb.add(bLogin);
		plb.add(lError);
		pLogin.add(plb);
		pSignup.add(lSignup);
		bSignup.setBorder(BorderFactory.createEmptyBorder());
		pSignup.add(bSignup);
		pLogin.add(pSignup);
		lLogo.setFont(new Font("Arial", Font.BOLD, 30));
		pLogo.add(lLogo);
		pPrincipal.add(pLogo, BorderLayout.NORTH);
		pPrincipal.add(pLogin, BorderLayout.CENTER);
		
		this.add(pPrincipal);
		
		this.setVisible(true);
		
		pLogin.getRootPane().setDefaultButton(bLogin);
		
		//	Login
		bLogin.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(aerologixClient.login(tMail.getText(), tPass.getText())) {
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
		
		//	Close window
		this.addWindowListener(new WindowAdapter() {
				
			@Override
			public void windowClosing(WindowEvent e) {	
				System.exit(0);
			}
			
		});
		
		//	Mostrar/ocultar contraseña
		bShow.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showHide();
			}
			
		});
		
		//	Crear cuenta
		bSignup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RegisterWindow rw = RegisterWindow.getInstance(aerologixClient);
				rw.setVisible(true);
			}
		});
		
	}
	
	public static void waitTime(int tiempo) {
		try {
			Thread.sleep(tiempo);
		} catch (InterruptedException e) {	}
	}
	
	private void centerWindow() {
		Dimension windowSize = getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int largo = (screenSize.width-windowSize.width)/2;
		int alto = (screenSize.height-windowSize.height)/2;
		
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
	};
	
	public static LoginWindow getInstanceLogin(AeroLogixClient aeroLogixClient) {
		if(LoginWindow.instanceLogin == null) {
			LoginWindow.instanceLogin = new LoginWindow(aeroLogixClient);
		}
		return LoginWindow.instanceLogin;
	}
	
	public void removeBtnClickBg(JButton button) {
	    button.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            button.setBackground(background);
	        }
	    });

	    // Desactivar el pintado del foco
	    button.setFocusPainted(false);
	}
	
}
