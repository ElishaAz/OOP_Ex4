package GUI;

import game.*;
import geo.LatLon;
import geo.Path;
import geo.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * @author Elisha
 */
public class GamePanel extends JPanel
{
	private static final int playerSize = 10;
	private final Color playerFill = Color.magenta;
	private final Color playerOutline = Color.white;

	private static final int packmanSize = 10;
	private final Color packmanFill = Color.yellow;
	private final Color packmanOutline = Color.white;

	private static final int ghostSize = 10;
	private final Color ghostFill = Color.blue;
	private final Color ghostOutline = Color.white;

	private static final int fruitSize = 10;
	private final Color fruitFill = Color.green;
	private final Color fruitOutline = Color.white;

	private final Color blockFill = Color.black;
	private final Color blockOutline = Color.white;

	private final Point scorePlace = new Point(10, 10);
	private final Color scoreColor = Color.CYAN;

	private final long timeout = 100;

	private Map map;
	private Game game;

	private enum Mode
	{
		Start, Loading, Play, Pause, End
	}

	private Mode mode;

	public GamePanel()
	{
		mode = Mode.Start;
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.addMouseListener(mouseListener);
	}

	synchronized public void setGame(File gameFile, Image mapImage, Long... ids)
	{
		this.game = new Game(gameFile.getPath(), ids);
		this.map = new Map(mapImage, game.objects.getMapBox());
		map.updateScreenSize(getWidth(), getHeight());
		repaint();
	}

	private UserPlay currentPlay;

	@SuppressWarnings("FieldCanBeLocal")
	private MouseListener mouseListener = new MouseAdapter()
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (map != null && mode == Mode.Loading)
			{
				LatLon place = map.pixelToWorld(e.getPoint());
				switch (JOptionPane.showConfirmDialog(GamePanel.this, place + "?"))
				{
					case 0:
						System.out.println("User play " + place);
						if (game.setInitLocation(place))
						{
							game.start();
							mode = Mode.Play;
							currentPlay = new UserPlay(GamePanel.this, map, game);
						}
						repaint();
						break;
					case 2:
						mode = Mode.Start;
						break;
				}
			}
		}
	};

	synchronized public void loadUserPlay()
	{
		System.out.println("load user play");
		mode = Mode.Loading;
		repaint();
	}


	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		if (map != null)
		{
			map.updateScreenSize(getWidth(), getHeight());

			if (map.map != null)
				g.drawImage(map.map, 0, 0, this.getWidth(), this.getHeight(), null);

			switch (mode)
			{
				case Start:
					if (game != null && map != null)
						paintMap(g);
					break;
				case Play:
				case Loading:
				case Pause:
					paintMap(g);
					paintScore(g);
					break;

				case End:
					paintEnd(g);
					break;
			}
		}
	}

	private void paintEnd(Graphics g)
	{
		mode = Mode.Start;
		paintMap(g);
	}

	private void paintScore(Graphics g)
	{
		if (game != null)
		{
			g.setColor(scoreColor);
			g.drawString(game.getPlayReport(), scorePlace.x, scorePlace.y);
		}
	}

	private void paintMap(Graphics g)
	{
		paintPlayer(g);
		paintPackmen(g);
		paintGhosts(g);
		paintFruits(g);
		paintBlocks(g);
	}

	private void paintPlayer(Graphics g)
	{
		Player player = game.objects.getPlayer();
		Point place = map.worldToPixel(player.getPosition());

		drawOval(g, place, playerSize, playerFill, playerOutline);
	}

	private void paintPackmen(Graphics g)
	{
		for (Packman packman : game.objects.getPackmen().values())
		{
			Point place = map.worldToPixel(packman.getPosition());

			drawOval(g, place, packmanSize, packmanFill, packmanOutline);
		}
	}

	private void paintGhosts(Graphics g)
	{
		for (Ghost ghost : game.objects.getGhosts().values())
		{
			Point place = map.worldToPixel(ghost.getPosition());

			drawOval(g, place, ghostSize, ghostFill, ghostOutline);
		}
	}

	private void paintFruits(Graphics g)
	{
		for (Fruit fruit : game.objects.getFruits().values())
		{
			Point place = map.worldToPixel(fruit.position);

			drawOval(g, place, fruitSize, fruitFill, fruitOutline);
		}
	}

	private void paintBlocks(Graphics g)
	{
		for (Block block : game.objects.getBlocks().values())
		{
			Point max = map.worldToPixel(block.max);
			Point min = map.worldToPixel(block.min);

			Point place = new Point(min.x, max.y);

			Point size = new Point(max.x - min.x, min.y - max.y);

			g.setColor(blockFill);
			g.fillRect(place.x, place.y, size.x, size.y);

			g.setColor(blockOutline);
			g.drawRect(place.x, place.y, size.x, size.y);
			System.out.println("Block " + max + ", " + min);
		}
	}

	private void drawOval(Graphics g, Point place, int size, Color fill, Color outline)
	{
		g.setColor(fill);
		g.fillOval(place.x, place.y, size, size);

		g.setColor(outline);
		g.drawOval(place.x, place.y, size, size);
	}
}
