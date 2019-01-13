package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

/**
 * @author Elisha
 */
public class GameFrame extends JFrame
{
	private static final File[] games;

	private static final File imageFile = new File("C:\\Users\\Elisha\\Documents\\Programing\\Java\\eclipse" +
			"-workspace\\OOP_Ex4\\data\\Ariel1.png");
	private static final Image image;
	private static final Long[] ids = new Long[]{213992464L};

	static
	{
		Image im;
		try
		{
			//noinspection ConstantConditions
			if (imageFile == null)
				im = null;
			else
				im = ImageIO.read(imageFile);
		} catch (IOException e)
		{
			im = null;
		}
		image = im;


		games = new File[9];

		for (int i = 0; i < games.length; i++)
		{
			games[i] = new File("C:\\Users\\Elisha\\Documents\\Programing\\Java\\eclipse-workspace\\OOP_Ex4\\data" +
					"\\Ex4_OOP_example" + (i + 1) + ".csv");
		}
	}

	public GameFrame(String title, int width, int height)
	{
		super(title);
		GamePanel panel = new GamePanel();
		add(panel);
		setJMenuBar(menu(panel));

		setSize(width, height);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		panel.setGame(new File("C:\\Users\\Elisha\\Documents\\Programing\\Java\\eclipse-workspace\\OOP_Ex4\\data" +
				"\\temp.csv"), image);
	}

	private static JMenuBar menu(GamePanel panel)
	{
		JMenuBar menubar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		JMenu loadMenu = new JMenu("Load");
		loadMenu.setMnemonic(KeyEvent.VK_L);

		ButtonGroup group = new ButtonGroup();

		for (int i = 0; i < games.length; i++)
		{
			File currentGame = games[i];
			newJRadioButton(loadMenu, group, "Game " + (i + 1), "Set to Game " + (i + 1),
					KeyEvent.VK_1 + i, false, (event) -> panel.setGame(currentGame, image, ids));
		}

		fileMenu.add(loadMenu);

		newJMenuItem(fileMenu, "score board", KeyEvent.VK_S, "Shows a pop-up with the score board", showData);

		menubar.add(fileMenu);

		JMenu playMenu = new JMenu("Play");
		playMenu.setMnemonic(KeyEvent.VK_P);

		newJMenuItem(playMenu, "User", KeyEvent.VK_U, "User play", (event) -> panel.loadUserPlay());

		newJMenuItem(playMenu, "Computer", KeyEvent.VK_C, "Computer play", (event) -> panel.loadComputerPlay());

		menubar.add(playMenu);

		return menubar;
	}

	private static ActionListener showData = (event) ->
	{
		showLongTextMessageInDialog(ConnectToDataBase.getData(),null);
	};

	@SuppressWarnings("SameParameterValue")
	private static void showLongTextMessageInDialog(String longMessage, Frame frame) {
		SwingUtilities.invokeLater(() -> {
			JTextArea textArea = new JTextArea(6, 25);
			textArea.setText(longMessage);
			textArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(textArea);
			JOptionPane.showMessageDialog(frame, scrollPane);
		});
	}

	@SuppressWarnings("SameParameterValue")
	private static void newJRadioButton(JMenu parent, ButtonGroup group, String text, String toolTp, int mnemonic,
										boolean selected, ActionListener listener)
	{
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(text);
		item.setMnemonic(mnemonic);
		item.setToolTipText(toolTp);
		item.setSelected(selected);
		item.addActionListener(listener);
		group.add(item);
		parent.add(item);
	}

	@SuppressWarnings("SameParameterValue")
	private static void newJMenuItem(JMenu parent, String text, int mnemonic, String toolTipText,
									 ActionListener listener)
	{
		JMenuItem item = new JMenuItem(text);
		item.setMnemonic(mnemonic);
		item.setToolTipText(toolTipText);
		item.addActionListener(listener);
		parent.add(item);
	}
}
