package ch.fhnw.sheepMaw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class LoginGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int port = 40000;

	private Client client;
	private JTextField txtField_IP;
	private String IP;
	private JTextField txtField_Username;
	private String Username;

	private static JPanel mainPanel;
	private JPanel labelPanel;
	private JPanel txtField_Panel;
	private JPanel buttonPanel;

	private int width = 340;
	private int height_mP = 80;
	private int height_bP = 80;
	private int height_lP = 300;
	private int heightTotal = height_mP + height_bP + height_lP;

	public LoginGUI() {
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	// Konstruktor
	public LoginGUI(String name) {
		super(name);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		labelPanel = new JPanel();
		txtField_Panel = new JPanel();
		buttonPanel = new JPanel();

		setLocation(500, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(width, height_mP));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.setOpaque(true);
		mainPanel.setBackground(Color.white);

		this.add(mainPanel, BorderLayout.CENTER);

		properties(); // Siehe GUI-Elemente

		setResizable(false);

		setSize(width, heightTotal);

		setVisible(true);

	}

	// -----------------------------------Getter------------------------------------------
	// getIP() R???ckgabewert IP
	public String getIP() {
		return IP;
	}

	// getUsername R???ckgabewert getUsername
	public String getUsername() {
		return Username;
	}

	// ----------------------------------GUI-Elemente--------------------------------------------
	public void properties() {
		logoPanel();

		// 1*
		labels();

		// 2*
		txtFieldPanel();

		// 3*
		button_Connect();

		// repaint();

	}

	// --------------------------------------------------------------------------------------------

	// 1* labels USER and SERVER IP
	private void labels() {

		mainPanel.add(labelPanel);
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

		labelPanel.setPreferredSize(new Dimension((width / 2), height_mP));
		labelPanel.setOpaque(false);

		JLabel usernameLabel = new JLabel("USER");

		// usernameLabel.setBounds(70, 80 , 160, 20);
		labelPanel.add(usernameLabel);

		// Font Properties
		fontSettings(usernameLabel);

		JLabel ipLabel = new JLabel("SERVER IP");

		fontSettings(ipLabel);

		labelPanel.add(ipLabel);

	}

	// 2* text Input field for Username and IP
	private void txtFieldPanel() {

		mainPanel.add(txtField_Panel);
		txtField_Panel
				.setLayout(new BoxLayout(txtField_Panel, BoxLayout.Y_AXIS));
		txtField_Panel.setPreferredSize(new Dimension((width / 2), height_mP));

		txtField_Panel.setOpaque(false);

		txtField_Username = new JTextField(10);
		txtField_Username.setOpaque(false);
		txtField_Username.setPreferredSize(new Dimension(150, 20));

		txtField_Username.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				Username = txtField_Username.getText();

			}

			@Override
			public void keyReleased(KeyEvent e) {
				Username = txtField_Username.getText();

			}

			@Override
			public void keyTyped(KeyEvent e) {
				Username = txtField_Username.getText();

			}

		});

		txtField_IP = new JTextField(10);
		txtField_IP.setOpaque(false);

		txtField_IP.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				IP = txtField_IP.getText();

			}

			@Override
			public void keyReleased(KeyEvent e) {
				IP = txtField_IP.getText();

			}

			@Override
			public void keyTyped(KeyEvent e) {
				IP = txtField_IP.getText();

			}

		});
		txtField_Panel.add(txtField_Username);
		txtField_Panel.add(txtField_IP);

	}

	// 3* Button contents
	private void button_Connect() {

		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setPreferredSize(new Dimension(width, height_bP));
		buttonPanel.setBackground(Color.white);

		JButton startServer = new JButton("Start Server");
		JButton joinServer = new JButton("Join Server");

		joinServer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clientConnectsThread("joinServer");
			}
		});

		startServer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				clientConnectsThread("startServer");

			}

		});

		buttonPanel.add(startServer);
		buttonPanel.add(joinServer);

		this.add(buttonPanel, BorderLayout.SOUTH);

	}

	public void logoPanel() {

		JPanel logoPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				try {
					BufferedImage image = ImageIO
							.read(getClass()
									.getClassLoader()
									.getResource(
											"ch/fhnw/sheepMaw/images/SheepMaw_Logo.jpg"));
					Image image1 = image.getScaledInstance(getWidth(),
							getHeight(), Image.SCALE_SMOOTH);
					g.drawImage(image1, 0, 0, null);
				} catch (Exception ex) {
					System.out.println(ex);
				}
			}
		};

		logoPanel.setPreferredSize(new Dimension(width, height_lP));

		this.add(logoPanel, BorderLayout.NORTH);
	}

	// ------------------------------Threads--------------------------------------------------------------

	// 3.1* Methode clientConnectsThread()
	private void clientConnectsThread(String intention) {

		if (Username != null) {
			client = new Client(this);
			if (intention.equals("startServer")) {

				client.createServer();
			}
			String ipAddress = getIP();
			client.connect(ipAddress);

			System.out.println(ipAddress);
			if (ipAddress == null) {
				try {
					IP = InetAddress.getLocalHost().getHostAddress().toString();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
			System.out.println(IP);

			new Thread(client).start();
			System.out.println("send stuff");
			GameInput gameInput = new GameInput();

			Player myPlayer = new Player(this.getUsername());
			gameInput.setPlayer(myPlayer);
			gameInput.setAction("joinServer");
			client.setGameInput(gameInput);
			client.sendData(gameInput);
			dispose();
		} else {
			JOptionPane.showMessageDialog(null, "Type in username!");
		}
	}

	// -------------- Hilfsmethoden
	// ---------------------------------------------
	public void fontSettings(JLabel label) {
		label.setForeground(Color.black);
		label.setFont(new Font("Verdana", Font.BOLD, 18));
	}

}
