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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.aerologix.app.client.controller.UserController;

public class RegisterWindow extends JFrame {

	private static RegisterWindow instance;

	private static final long serialVersionUID = 1L;
	// Panels
	protected JPanel pPrincipal = new JPanel(new GridLayout(2, 1));
	protected JPanel pSignup = new JPanel(new GridLayout(5, 1));
	protected JPanel pUser = new JPanel(new FlowLayout());
	protected JPanel psb = new JPanel(new FlowLayout());
	protected JPanel pPass = new JPanel(new FlowLayout());
	protected JPanel psPass = new JPanel(new FlowLayout());
	protected JPanel pNombre = new JPanel(new FlowLayout());

	// Components signup
	protected JLabel lsMail = new JLabel("Email: ");
	protected JLabel lsPass = new JLabel("Password: ");
	protected JLabel lssPass = new JLabel("Confirm password: ");
	protected JLabel lsNombre = new JLabel("Name:");
	protected JTextField tsMail = new JTextField(15);
	protected JTextField tsPass = new JTextField(15);
	protected JTextField tssPass = new JTextField(15);
	protected JTextField tsNombre = new JTextField(15);
	protected JButton bSignup = new JButton("Register");
	protected JLabel lsError = new JLabel("");

	private RegisterWindow() {

		this.setTitle("Register - AeroLogix");
		this.setSize(450, 300);
		centerWindow();
		this.setLayout(new FlowLayout());

		// Construccion signup
		pSignup.setBorder(BorderFactory.createTitledBorder("Register user"));
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
		pPrincipal.add(pSignup);
		this.add(pPrincipal);

		this.setVisible(true);

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				deinit();
				dispose();
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

	private void centerWindow() {
		Dimension windowSize = getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int largo = (screenSize.width - windowSize.width) / 2;
		int alto = (screenSize.height - windowSize.height) / 2;

		setLocation(largo - (largo / 4), alto + (largo / 4));
	}

	public static void waitTime(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static RegisterWindow getInstance() {
		if (RegisterWindow.instance == null) {
			RegisterWindow.instance = new RegisterWindow();
		}
		return RegisterWindow.instance;
	}

	public static void deinit() {
		instance = null;
	}

}
