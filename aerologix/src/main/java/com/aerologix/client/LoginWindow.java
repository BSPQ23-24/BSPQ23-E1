package com.aerologix.client;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * 	Clase de la ventana gráfica de inicio de sesión en el programa
 *
 */
public class LoginWindow extends JFrame {
	
	private static LoginWindow instanceLogin;
	
	private static final long serialVersionUID = 1L;
	
	//	Paneles
	protected JPanel pLogin = new JPanel(new GridLayout(4,1));
	protected JPanel pPrincipal = new JPanel(new BorderLayout());
	protected JPanel plUser = new JPanel(new FlowLayout());
	protected JPanel plb = new JPanel(new FlowLayout());
	protected JPanel plPass = new JPanel(new FlowLayout());
	protected JPanel pSignup = new JPanel(new FlowLayout());
	protected JPanel pLogo = new JPanel(new FlowLayout());
	
	//	Componentes login

	protected JLabel lMail = new JLabel("Username: ");
	protected JLabel lPass = new JLabel("Password: ");
	protected JTextField tMail = new JTextField(15);
	protected JPasswordField tPass = new JPasswordField(15);
	protected JButton bLogin = new JButton("Login");
	protected JLabel lError = new JLabel("");
	protected JButton bShow = new JButton("Show");
	protected JTextField tShow = new JTextField(15);
	protected JLabel lSignup = new JLabel("¿Not registered?");
	protected JButton bSignup = new JButton("Create an account");
	protected JLabel lLogo = new JLabel("AeroLogix");
	
	private LoginWindow() {
		this.setTitle("Login - AeroLogix");
		this.setSize(450, 280);
		centrarVentana();
		this.setLayout(new FlowLayout());
		
		//	Construccion login
		plUser.add(lMail);
		plUser.add(tMail);
		plPass.add(lPass);
		plPass.add(tPass);
		tShow.setVisible(false);
		tShow.setEditable(false);
		plPass.add(tShow);
		plPass.add(bShow);
		pLogin.add(plUser);
		pLogin.add(plPass);
		plb.add(bLogin);
		plb.add(lError);
		pLogin.add(plb);
		lSignup.setForeground(new Color(0, 93, 232));
		pSignup.add(lSignup);
		bSignup.setForeground(new Color(0, 93, 232));
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
					
						if(true) {
								Thread hilo = new Thread() {
								
									@Override
									public void run() {
										lError.setText("Logging in...");
										muestraTemporal(1000);
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
										lError.setText("Login credentials are not correct.");
										muestraTemporal(4000);
										lError.setText("");
									}
								};
								hilo.start();
						}
				} catch(NullPointerException e1) {
					Thread hilo = new Thread() {
						@Override
						public void run() {
							lError.setText("Login credentials are not correct");
							muestraTemporal(4000);
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
				mostrarOcultar();
			}
			
		});
		
		//	Crear cuenta
		bSignup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Under development");
			}
			
		});
		
	}
	
	public static void main(String[] args) {		//	MAIN (get data)
		@SuppressWarnings("unused")
		LoginWindow lv = new LoginWindow();
	}
	
	/**
	 * Método que realiza una pausa en el hilo aplicado
	 * @param tiempo Integer del tiempo de espera en milisegundos
	 */
	
	public static void muestraTemporal(int tiempo) {
		try {
			Thread.sleep(tiempo);
		} catch (InterruptedException e) {	}
	}
	
	/**
	 *  Método que posiciona la ventana en el centro de la pantalla
	 */

	private void centrarVentana() {
		Dimension tamanyoVentana = getSize();
		Dimension tamanyoPantalla = Toolkit.getDefaultToolkit().getScreenSize();
		int largo = (tamanyoPantalla.width-tamanyoVentana.width)/2;
		int alto = (tamanyoPantalla.height-tamanyoVentana.height)/2;
		
		setLocation(largo, alto);
	}
	
	/**
	 * Método que sirve para mostrar/ocultar la contraseña en el login temporalmente
	 */
	
	public void mostrarOcultar() {
		Thread hilo = new Thread() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				tShow.setText(tPass.getText());
				tShow.setVisible(true);
				tPass.setVisible(false);
				plPass.updateUI();
				muestraTemporal(1000);
				tShow.setVisible(false);
				tPass.setVisible(true);
				plPass.updateUI();
			}
		};
		hilo.start();
	};
	
	/**
	 * Método que devuelve una instancia de la ventana siguiendo el patrón de diseño Singleton para evitar que puedan instanciarse múltiples ventanas a la vez
	 * @return instancia de la ventana
	 */
	public static LoginWindow getInstanceLogin() {
		if(LoginWindow.instanceLogin == null) {
			LoginWindow.instanceLogin = new LoginWindow();
		}
		return LoginWindow.instanceLogin;
	}
	
}
