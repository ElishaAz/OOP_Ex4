package game;

import game.interfaces.IGameObjects;
import geo.LatLon;

import java.util.*;

/**
 * @author Elisha
 */
public class PackmanObjects implements IGameObjects, Cloneable
{
	private static String splitRegex = ",";
	private static int typeIndex = 0, idIndex = 1;

	private interface UpdateValues
	{
		void update(int id, double... args);
	}

	private final Map<String, UpdateValues> set = new HashMap<>()
	{
		{
			put("M", PackmanObjects.this::setPlayer);
			put("P", PackmanObjects.this::setPackman);
			put("G", PackmanObjects.this::setGhosts);
			put("F", PackmanObjects.this::setFruit);
			put("B", PackmanObjects.this::setBlocks);
		}
	};


	private final Map<String, UpdateValues> update = new HashMap<>()
	{
		{
			put("M", PackmanObjects.this::updatePlayer);
			put("P", PackmanObjects.this::updatePackman);
			put("G", PackmanObjects.this::updateGhosts);
			put("F", PackmanObjects.this::updateFruit);
		}
	};

	private MapBox mapBox;
	private Blocks blocks;

	private Player player;
	private Map<Integer, Packman> packmen;
	private Map<Integer, Ghost> ghosts;
	private Map<Integer, Fruit> fruits;

	public PackmanObjects(String boundingBox, List<String> objects)
	{
		setMapBox(boundingBox.split(splitRegex, -1));
		blocks = new Blocks();
		packmen = new HashMap<>();
		ghosts = new HashMap<>();
		fruits = new HashMap<>();

		for (String object : objects)
		{
			String[] argsStr = object.split(splitRegex, -1);
			double[] args = new double[argsStr.length];

			for (int i = 0; i < argsStr.length; i++)
			{
				if (i != typeIndex && i != idIndex)
				{
					args[i] = Double.parseDouble(argsStr[i]);
				}
			}
			int id = Integer.parseInt(argsStr[idIndex]);
			String typeString = argsStr[typeIndex].trim();

			if (set.containsKey(typeString))
			{
				set.get(typeString).update(id, args);
			}
		}

	}

	private void setMapBox(String... args)
	{
		double maxLat = Double.parseDouble(args[MapBox.maxLatIndex].trim());
		double maxLon = Double.parseDouble(args[MapBox.maxLonIndex].trim());

		LatLon max = new LatLon(maxLat, maxLon);

		double minLat = Double.parseDouble(args[MapBox.minLatIndex].trim());
		double minLon = Double.parseDouble(args[MapBox.minLonIndex].trim());

		LatLon min = new LatLon(minLat, minLon);

		double scoreReduse = Double.parseDouble(args[MapBox.scoreReduseIndex].trim());

		mapBox = new MapBox(max, min, scoreReduse);
	}

	private void setPlayer(int id, double... args)
	{
		LatLon position = new LatLon(args[Player.latIndex], args[Player.lonIndex]);

		player = new Player(position, args[Player.speedIndex], args[Player.radiusIndex]);

		blocks.setDistance(player.radius);
	}

	private void setPackman(int id, double... args)
	{
		LatLon position = new LatLon(args[Packman.latIndex], args[Packman.lonIndex]);

		packmen.put(id, new Packman(position, args[Packman.speedIndex], args[Packman.radiusIndex]));
	}

	private void setGhosts(int id, double... args)
	{
		LatLon position = new LatLon(args[Ghost.latIndex], args[Ghost.lonIndex]);

		ghosts.put(id, new Ghost(position, args[Ghost.speedIndex], args[Ghost.radiusIndex]));
	}

	private void setFruit(int id, double... args)
	{
		LatLon position = new LatLon(args[Fruit.latIndex], args[Fruit.lonIndex]);

		fruits.put(id, new Fruit(position, args[Fruit.weightIndex]));
	}

	private void setBlocks(int id, double... args)
	{
		double minLat = args[Block.minLatIndex];
		double minLon = args[Block.minLonIndex];

		LatLon min = new LatLon(minLat, minLon);

		double maxLat = args[Block.maxLatIndex];
		double maxLon = args[Block.maxLonIndex];

		LatLon max = new LatLon(maxLat, maxLon);

		double scoreReduse = args[Block.scoreReduseIndex];

		Block block = new Block(max, min, scoreReduse);

		blocks.put(id, block);
	}


	@Override
	public void update(List<String> newObjects)
	{
		packmenUpdated = new ArrayList<>();
		fruitsUpdated = new ArrayList<>();

		for (String object : newObjects)
		{
			String[] argsStr = object.split(splitRegex, -1);
			double[] args = new double[argsStr.length];

			for (int i = 0; i < argsStr.length; i++)
			{
				if (i != typeIndex && i != idIndex)
				{
					args[i] = Double.parseDouble(argsStr[i]);
				}
			}
			int id = Integer.parseInt(argsStr[idIndex]);
			String typeString = argsStr[typeIndex].trim();

			if (update.containsKey(typeString))
			{
				update.get(typeString).update(id, args);
			}
		}

		List<Integer> packmenToRemove = new ArrayList<>();
		List<Integer> fruitsToRemove = new ArrayList<>();

		for (var key : packmen.keySet())
		{
			if (!packmenUpdated.contains(key))
			{
				packmenToRemove.add(key);
			}
		}

		for (var key : fruits.keySet())
		{
			if (!fruitsUpdated.contains(key))
			{
				fruitsToRemove.add(key);
			}
		}

		for (var pac : packmenToRemove)
		{
			packmen.remove(pac);
		}

		for (var fruit : fruitsToRemove)
		{
			fruits.remove(fruit);
		}
	}

	@SuppressWarnings("unused")
	private void updatePlayer(int id, double... args)
	{
		LatLon position = new LatLon(args[Player.latIndex], args[Player.lonIndex]);

		player.updatePosition(position);
	}

	private List<Integer> packmenUpdated = new ArrayList<>();

	private void updatePackman(int id, double... args)
	{
		packmenUpdated.add(id);

		LatLon position = new LatLon(args[Player.latIndex], args[Player.lonIndex]);

		packmen.get(id).updatePosition(position);
	}

	private void updateGhosts(int id, double... args)
	{
		LatLon position = new LatLon(args[Player.latIndex], args[Player.lonIndex]);

		ghosts.get(id).updatePosition(position);
	}

	private List<Integer> fruitsUpdated = new ArrayList<>();

	private void updateFruit(int id, double... args)
	{
		fruitsUpdated.add(id);
	}

	public void rotatePlayer(double angle)
	{
		player.rotate(angle);
	}

	public MapBox getMapBox()
	{
		return mapBox;
	}

	public Blocks getBlocks()
	{
		return blocks;
	}

	public Player getPlayer()
	{
		return player;
	}

	public Map<Integer, Packman> getPackmen()
	{
		return packmen;
	}

	public Map<Integer, Ghost> getGhosts()
	{
		return ghosts;
	}

	public Map<Integer, Fruit> getFruits()
	{
		return fruits;
	}
}
