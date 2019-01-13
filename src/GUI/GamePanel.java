package GUI;

import game.*;
import geo.LatLon;
import geo.Path;
import geo.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

/**
 * @author Elisha
 */
public class GamePanel extends JPanel
{
	private static final int playerSize = 10;
	private static final Color playerFill = Color.magenta;
	private static final Color playerOutline = Color.white;

	private static final int packmanSize = 10;
	private static final Color packmanFill = Color.yellow;
	private static final Color packmanOutline = Color.white;

	private static final int ghostSize = 10;
	private static final Color ghostFill = Color.blue;
	private static final Color ghostOutline = Color.white;

	private static final int fruitSize = 10;
	private static final Color fruitFill = Color.green;
	private static final Color fruitOutline = Color.white;

	private static final Color blockFill = Color.black;
	private static final Color blockOutline = Color.white;

	private static final Point scorePlace = new Point(10, 10);
	private static final Color scoreColor = Color.cyan;

	private static final int pathPointSize = 8;
	private static final Color pathFill = Color.gray;
	private static final Color pathOutline = Color.white;

	private static final int pointSize = 8;
	private static final Color pointFill = Color.darkGray;
	private static final Color pointOutline = Color.white;

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
	private ComputerPlay currentComputerPlay;

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
		if (game != null)
		{
			System.out.println("load user play");
			mode = Mode.Loading;
			repaint();
		}
	}

	synchronized public void loadComputerPlay()
	{
		if (game != null)
		{
			System.out.println("load computer play");
			mode = Mode.Play;
			currentComputerPlay = new ComputerPlay(this, map, game);
			repaint();
		}
	}

	Path pathToPaint = null;

	void paintPath(Path path)
	{
		pathToPaint = path;
	}

	LatLon[] pointsToPaint = null;

	void paintPoints(LatLon[] points)
	{
		pointsToPaint = points;
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
					if (pathToPaint != null)
					{
						paintPath(g, pathFill, pathOutline);
					}

					if (pointsToPaint != null)
					{
						paintPoints(g, pointFill, pointOutline);
					}
					break;

				case End:
					paintEnd(g);
					break;
			}
		}
	}

	@SuppressWarnings("SameParameterValue")
	private void paintPath(Graphics g, Color fill, Color outline)
	{
		List<LatLon> points = pathToPaint.getPoints();
		for (int i = 0; i < points.size(); i++)
		{
			Point place = map.worldToPixel(points.get(i));

			drawOval(g, place, pathPointSize, fill, outline);
			g.setColor(fill);
			if (i != 0)
			{
				Point lastPlace = map.worldToPixel(points.get(i - 1));
				g.drawLine(lastPlace.x, lastPlace.y, place.x, place.y);
			}
		}
	}

	@SuppressWarnings("SameParameterValue")
	private void paintPoints(Graphics g, Color fill, Color outline)
	{
		for (LatLon latLon : pointsToPaint)
		{
			Point place = map.worldToPixel(latLon);

			drawOval(g, place, pointSize, fill, outline);
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
		}
	}

	private void drawOval(Graphics g, Point place, int size, Color fill, Color outline)
	{
		g.setColor(fill);
		g.fillOval(place.x - size / 2, place.y - size / 2, size, size);

		g.setColor(outline);
		g.drawOval(place.x - size / 2, place.y - size / 2, size, size);
	}
}
