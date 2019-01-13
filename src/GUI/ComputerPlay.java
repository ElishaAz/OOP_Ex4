package GUI;

import game.*;
import game.interfaces.IEdible;
import game.interfaces.IGameObject;
import game.interfaces.IMobileGameObject;
import geo.LatLon;
import geo.Path;
import geo.Vector2D;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Elisha
 */
public class ComputerPlay
{
	private static final long timeout = 50;
	private double maxPackmanDist; // screen size / 2

	private final Map map;
	private final Game game;
	private final GamePanel panel;

	private boolean run = true;

	public ComputerPlay(GamePanel panel, Map map, Game game)
	{
		System.out.println("New Computer Play");
		this.panel = panel;
		this.map = map;
		this.game = game;

		player = game.objects.getPlayer();
		packmen = game.objects.getPackmen();
		ghosts = game.objects.getGhosts();
		fruits = game.objects.getFruits();
		blocks = game.objects.getBlocks();

		panel.paintPoints(game.objects.getBlocks().getOuterPoints(game.objects.getPlayer().radius));

		if (packmen.size() > 0)
			game.setInitLocation(packmen.values().iterator().next().getPosition());
		else if (fruits.size() > 0)
			game.setInitLocation(fruits.values().iterator().next().getPosition());
		else return;

		game.start();

		player = game.objects.getPlayer();

		game.randomSafeMove();
		update();

		new Thread(start).start();
	}

	private void update()
	{
		game.update();
		player = game.objects.getPlayer();
		packmen = game.objects.getPackmen();
		ghosts = game.objects.getGhosts();
		fruits = game.objects.getFruits();
		blocks = game.objects.getBlocks();

		maxPackmanDist = map.box.min.distance(map.box.max) / 2;
	}

	@SuppressWarnings("FieldCanBeLocal")
	private Runnable start = () ->
	{
		System.out.println("Starting computer play");
		while (run)
		{
			computerPlayLoop();
			try
			{
				Thread.sleep(timeout);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	};

	private Player player;
	private java.util.Map<Integer, Packman> packmen;
	private java.util.Map<Integer, Ghost> ghosts;
	private java.util.Map<Integer, Fruit> fruits;
	private Blocks blocks;

	private Path currentPath;
	private int pathIndex = 1;
	private IEdible beingEaten = null;

	private void computerPlayLoop()
	{
		update();

//		did not have time to finish
//		if (objectsToEat == null || objectsToEat.size() == 0)
//		{
//			findNextMove();
//		}
//
//		IEdible beingEaten;
//		for (beingEaten = objectsToEat.peek(); beingEaten == null; beingEaten = objectsToEat.peek())
//		{
//			objectsToEat.remove();
//
//			if (objectsToEat.size() == 0)
//			{
//				findNextMove();
//			}
//		}
//
//		if (currentPath == null || pathDest == null || !pathDest.equals(beingEaten.getPosition()))
//		{
//			currentPath = game.pathTo(beingEaten.getPosition());
//			pathDest = beingEaten.getPosition();
//			pathIndex = 1;
//			panel.paintPath(currentPath);
//		}

		IEdible current;

		if (packmen.size() == 0)
		{
			current = getClosestFruit();
		} else
		{
			Packman closestPackman = getClosestPackman();
			if (closestPackman != null)
			{
				current = closestPackman;
			} else
			{
				current = getClosestFruit();
				if (current == null)
					run = false;
			}
		}
		if (current == null)
			return;

		if (currentPath == null || beingEaten == null || beingEaten instanceof IMobileGameObject || !beingEaten.getPosition().equals(current.getPosition()))
		{
			currentPath = game.pathTo(current.getPosition());
			beingEaten = current;
			pathIndex = 1;
			panel.paintPath(currentPath);
		}

		pathIndex = game.moveAlong(currentPath, pathIndex);

		panel.repaint();
	}

//	did not have time to finish:
//	private Queue<IEdible> objectsToEat;
//
//	private boolean packmenFinished = false;
//
//	private void findNextMove()
//	{
//		if (objectsToEat == null)
//		{
//			objectsToEat = new LinkedList<>();
//		}
//
//		if (packmen.size() != 0)
//		{
//			Packman closestPackman = getPackmanWithClosestDest();
//
//			if (closestPackman != null)
//			{
//				Fruit dest = getPackmanDest(closestPackman);
//
//				if (game.pathTo(dest.position).distance() * player.speed >
//						closestPackman.getPosition().distance(dest.position) * closestPackman.speed)
//				{
//					objectsToEat.add(dest);
//					objectsToEat.add(closestPackman);
//				}
//			}
//
//
//		} else packmenFinished = true;
//
//		if (packmenFinished)
//		{
//			objectsToEat.add(getClosestFruit());
//		}
//	}

	private Packman getClosestPackman()
	{
		Packman closestPackman = null;
		double minDist = Double.POSITIVE_INFINITY;
		for (var packman : packmen.values())
		{
			double dist = distanceTo(packman.getPosition());
			if (dist < minDist && dist < maxPackmanDist)
			{
				minDist = dist;
				closestPackman = packman;
			}
		}
		return closestPackman;
	}

	private Fruit getClosestFruit()
	{
		Fruit closestFruit = null;
		double minDist = Double.POSITIVE_INFINITY;
		for (var fruit : fruits.values())
		{
			double dist = distanceTo(fruit.position);
			if (dist < minDist)
			{
				minDist = dist;
				closestFruit = fruit;
			}
		}
		return closestFruit;
	}

	private double distanceTo(LatLon point)
	{
		Path p = game.pathTo(point);
		if (p == null) return Double.POSITIVE_INFINITY;
		return p.distance();
	}


//	Did not have time to finish :
//
//	private Fruit getPackmanDest(Packman packman)
//	{
//		Vector2D direction = packman.moveDirection();
//		LatLon currentPoint = packman.getPosition();
//		LatLon nextPoint = currentPoint.transformedBy(direction);
//
//		double minDistFruit = Double.POSITIVE_INFINITY;
//		Fruit nextFruit = null;
//
//		for (var fruit : fruits.values())
//		{
//			if (nextPoint.isOnLine(currentPoint, fruit.position, Fruit.fruitRadius) || fruit.position.isOnLine
//			(currentPoint, nextPoint, Fruit.fruitRadius))
//			{
//				double dist = currentPoint.distance(fruit.position);
//				if (dist < minDistFruit)
//				{
//					minDistFruit = dist;
//					nextFruit = fruit;
//				}
//			}
//		}
//		return nextFruit;
//	}
//
//	private Packman getPackmanWithClosestDest()
//	{
//		Packman closestPackman = null;
//		double minDist = Double.POSITIVE_INFINITY;
//		for (var pac : packmen.values())
//		{
//			double dist = game.pathTo(getPackmanDest(pac).position).distance();
//			if (dist < minDist)
//			{
//				minDist = dist;
//				closestPackman = pac;
//			}
//		}
//		return closestPackman;
//	}
//

}
