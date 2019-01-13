package GUI;

import game.Game;
import geo.LatLon;
import geo.Path;
import geo.Vector2D;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * @author Elisha
 */
public class UserPlay
{
	private static final long timeout = 100;

	private final Map map;
	private final Game game;
	private final GamePanel panel;

	private boolean run = true;

	public UserPlay(GamePanel panel, Map map, Game game)
	{
		System.out.println("New Play");
		this.panel = panel;
		this.map = map;
		this.game = game;

		panel.addMouseListener(mouseListener);
		panel.addKeyListener(keyListener);

		panel.paintPoints(game.objects.getBlocks().getOuterPoints(game.objects.getPlayer().radius));


		new Thread(start).start();
	}

	@SuppressWarnings("FieldCanBeLocal")
	private Runnable start = () ->
	{
		System.out.println("Start");
		while (run)
		{

			try
			{
				userPlayLoopMouse();

				Thread.sleep(timeout);
			} catch (InterruptedException | IllegalArgumentException e)
			{
				e.printStackTrace();
			}
		}
	};

	private void userPlayLoopTemp()
	{
		game.rotate(90);
		panel.repaint();
	}

	private void userPlayLoopKeys()
	{
		Vector2D direction = Vector2D.zero();

		if (up)
		{
			System.out.println("up");
			direction = direction.plus(Vector2D.up());
		}
		if (down)
		{
			System.out.println("down");
			direction = direction.plus(Vector2D.down());
		}
		if (right)
		{
			System.out.println("right");
			direction = direction.plus(Vector2D.right());
		}
		if (left)
		{
			System.out.println("left");
			direction = direction.plus(Vector2D.left());
		}

		if (!direction.equals(Vector2D.zero()))
		{
			System.out.println("Moving " + (direction.toDegrees()) + " " + direction);
			game.rotate((direction.toDegrees() + 180.0) % 360.0);
		}

		if (game.objects.getFruits().size() <= 0)
		{
			run = false;
		}
		panel.repaint();
	}

	private LatLon oldTo = null;
	private Path path = null;
	private int pathIndex = 1;

	private void userPlayLoopMouse()
	{
		if (path == null || !to.equals(oldTo))
		{
			path = game.pathTo(to);
			oldTo = to.clone();
			pathIndex = 1;
		}
		if (isPressed && path != null)
		{
			pathIndex = game.moveAlong(path, pathIndex);
			panel.paintPath(path);
			panel.repaint();
		}
	}

	private boolean up = false, down = false, left = false, right = false;

	@SuppressWarnings("FieldCanBeLocal")
	private KeyListener keyListener = new KeyListener()
	{
		@Override
		public void keyTyped(KeyEvent e)
		{

		}

		@Override
		public void keyPressed(KeyEvent e)
		{
			set(e, true);
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			set(e, false);
		}

		private void set(KeyEvent e, boolean value)
		{
			switch (e.getKeyCode())
			{
				case KeyEvent.VK_UP:
					up = value;
					break;
				case KeyEvent.VK_DOWN:
					down = value;
					break;
				case KeyEvent.VK_RIGHT:
					right = value;
					break;
				case KeyEvent.VK_LEFT:
					left = value;
					break;
			}
		}
	};

	private LatLon to = new LatLon();
	private boolean isPressed = false;

	@SuppressWarnings("FieldCanBeLocal")
	private MouseListener mouseListener = new MouseAdapter()
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			to = map.pixelToWorld(e.getPoint());
			isPressed = true;
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			isPressed = false;
		}
	};
}
