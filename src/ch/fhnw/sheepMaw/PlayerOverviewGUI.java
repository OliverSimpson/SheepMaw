package ch.fhnw.sheepMaw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

public class PlayerOverviewGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ImageIcon logo;
	JLabel logoLabel;

	JScrollPane gamesScrollPane;

	JButton newGameButton;
	JButton joinGameButton;

	JScrollPane playerScrollPane;
	JList gameView;
	JPanel playerView;
	JButton endServer;

	JPanel mainPanel;

	Client client;
	GameInput gameInput;

	private JPanel logoPanel;
	private JPanel backgroundPanel;
	private JPanel avGamesPanel;
	private JPanel createPlayersPanel;

	public PlayerOverviewGUI(String screenName, final GameInput gameInput,
			final Client client) {
		super(screenName);
		this.gameInput = gameInput;
		this.client = client;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				GameInput leaveGame = new GameInput();
				leaveGame.setAction("leaveServer");
				leaveGame.setPlayer(gameInput.getPlayer());
				client.sendData(leaveGame);

			}
		});

		backgroundPanel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				try {
					BufferedImage image = ImageIO.read(getClass()
							.getClassLoader().getResource(
									"ch/fhnw/sheepMaw/images/overview.jpg"));
					Image image1 = image.getScaledInstance(getWidth(),
							getHeight(), Image.SCALE_SMOOTH);
					g.drawImage(image1, 0, 0, null);
				} catch (Exception ex) {
					System.out.println(ex);
				}
			}
		};

		backgroundPanel.setLayout(new BorderLayout());
		backgroundPanel.setPreferredSize(new Dimension(500, 500));

		// Main Panel
		mainPanel = new JPanel();
		mainPanel.setOpaque(false);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.setPreferredSize(new Dimension(200, 500));
		backgroundPanel.add(mainPanel, BorderLayout.WEST);

		add(backgroundPanel);
		properties();

		setLocation(300, 300);
		setSize(500, 500);

		setVisible(true);
	}

	public void properties() {
		createAvailableGames();
		createButtons();
		createPlayersOnline();

	}

	private void createAvailableGames() {

		avGamesPanel = new JPanel();
		mainPanel.add(avGamesPanel);

		avGamesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		avGamesPanel.setLayout(new BorderLayout());
		avGamesPanel.setMaximumSize(new Dimension(200, 200));

		avGamesPanel.setBorder(BorderFactory
				.createLineBorder(new java.awt.Color(0, 0, 0)));
		avGamesPanel.setOpaque(false);

		JLabel overview = new JLabel("GAMES :");
		fontSettings(overview);

		avGamesPanel.add(overview, BorderLayout.NORTH);

		Game[] games = new Game[gameInput.getGames().size()];
		gameView = new JList(games);

		gameView.setOpaque(false);

		gameView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		gameView.setLayoutOrientation(JList.HORIZONTAL_WRAP);

		gameView.setLayout(new BoxLayout(gameView, BoxLayout.Y_AXIS));
		gameView.setPreferredSize(new Dimension(50, 180));
		avGamesPanel.add(gamesScrollPane = new JScrollPane(gameView),
				BorderLayout.SOUTH);
		gamesScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		gamesScrollPane.setPreferredSize(new Dimension(50, 180));

		mainPanel.add(avGamesPanel);
		revalidate();

	}

	private void createPlayersOnline() {
		createPlayersPanel = new JPanel();
		mainPanel.add(createPlayersPanel);

		createPlayersPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		createPlayersPanel.setLayout(new BoxLayout(createPlayersPanel,
				BoxLayout.Y_AXIS));
		createPlayersPanel.setMaximumSize(new Dimension(200, 200));

		createPlayersPanel.setBorder(BorderFactory
				.createLineBorder(new java.awt.Color(0, 0, 0)));
		createPlayersPanel.setOpaque(false);


		JLabel serverLabel = new JLabel("SERVER: " + Client.lGui.getIP());

		fontSettings(serverLabel);

		JLabel playersOnline = new JLabel("PLAYER :");
		fontSettings(playersOnline);
		createPlayersPanel.add(playersOnline);
		createPlayersPanel.add(serverLabel);
		
		//Player View
		playerView = new JPanel();
		playerView.setAlignmentX(LEFT_ALIGNMENT);
		playerView.setLayout(new BoxLayout(playerView, BoxLayout.Y_AXIS));
		playerView.setOpaque(false);
		
		//add playerScrollPane to createPlayersPanel. playerScrollPane accommodates playerView
		createPlayersPanel.add(playerScrollPane = new JScrollPane(playerView));
		playerScrollPane.getHorizontalScrollBar();
		playerScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		playerScrollPane.setPreferredSize(new Dimension(130, 150));
	}

	// Test Button
	private void createButtons() {

		JPanel ButtonPanel = new JPanel();
		// TODO disable button after clicket once
		newGameButton = new JButton("New Game");
		joinGameButton = new JButton("Join Game");

		ButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		ButtonPanel.add(newGameButton);
		ButtonPanel.add(joinGameButton);
		ButtonPanel.setMaximumSize(new Dimension(200, 200));
		ButtonPanel.setOpaque(false);
		joinGameButton.addActionListener(new ActionListener() {
			// Test!!!!!!!!!!!!!!
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gameView.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Select game to join first!");
				} else {
					Game toJoin = (Game) gameView.getSelectedValue();
					System.out.println("Joining: " + toJoin);
					// /
					// /______________________________-
					GameInput sendObject = new GameInput();
					sendObject.setGame(toJoin);
					sendObject.setPlayer(gameInput.getPlayer());
					sendObject.setAction("joinGame");
					//
					client.sendData(sendObject);
				}
			}
		});
		newGameButton.addActionListener(new ActionListener() {
			// Test!!!!!!!!!!!!!!
			@Override
			public void actionPerformed(ActionEvent e) {

				// /
				// /______________________________-
				GameInput sendObject = new GameInput();
				sendObject.setPlayer(gameInput.getPlayer());
				gameInput.setAction("createGame");
				//
				client.sendData(gameInput);

			}
		});

		mainPanel.add(ButtonPanel);
	}

	public void addToPlayerOverview(String playerName) {
		JLabel label = new JLabel(playerName);
		playerView.add(label);

	}

	public void addToGameOverview(String gameName, String gameId) {

		JLabel gameLabel = new JLabel(gameName);
		gameLabel.setName(gameId);
		gameLabel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});
		gameView.add(gameLabel);
	}

	public void fontSettings(JLabel label) {
		label.setForeground(Color.white);
		label.setFont(new Font("Verdana", Font.PLAIN, 18));
	}

	public JPanel getLogoPanel() {
		return logoPanel;
	}

	public void setLogoPanel(JPanel logoPanel) {
		this.logoPanel = logoPanel;
	}

}
