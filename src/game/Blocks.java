package game;

import game.interfaces.IImmobileGameObject;
import geo.LatLon;
import geo.Path;
import geo.Vector2D;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Elisha
 */
public class Blocks extends HashMap<Integer, Block> implements IImmobileGameObject
{
	private Path[][] moves;
	private double distance;

	public Blocks(double distance)
	{
		super();
		this.distance = distance;
		updatePaths();
	}

	public Blocks()
	{
		this(0);
	}

	public void setDistance(double distance)
	{
		this.distance = distance;
		updatePaths();
	}

	/**
	 * Updates the Paths for {@link #getPath(LatLon, LatLon)}. This is  called after every change to the list.
	 */
	private void updatePaths()
	{
		outerPoints = getOuterPoints(distance);
		moves = new Path[outerPoints.length][outerPoints.length];

		for (int i = 0; i < outerPoints.length; i++)
		{
			for (int j = 0; j < outerPoints.length; j++)
			{
				if (!this.passesThrough(outerPoints[i], outerPoints[j]))
				{
					Path newPath = new Path();
					newPath.add(outerPoints[i]);
					newPath.add(outerPoints[j]);
					if (moves[i][j] == null)
					{
						moves[i][j] = newPath;
					} else
					{
						if (moves[i][j].distance() > newPath.distance())
						{
							moves[i][j] = newPath;
						}
					}
				}
			}
		}

		for (int i = 0; i < outerPoints.length; i++)
		{
			for (int x = 0; x < outerPoints.length; x++)
			{
				for (int y = 0; y < outerPoints.length; y++)
				{
					if (x == y)
					{
						moves[x][y] = new Path().add(outerPoints[x]).add(outerPoints[y]);
					}
					if (dist(x, i) + dist(i, y) < dist(x, y))
					{
						moves[x][y] = moves[x][i].clone().addAll(moves[i][y]);
					}
				}
			}
		}
	}

	private double dist(int x, int y)
	{
		if (moves[x][y] == null)
			return Double.POSITIVE_INFINITY;
		else
			return moves[x][y].distance();
	}

	private LatLon[] outerPoints;

	/**
	 * Calculates the shortest path from {@code from} to {@code to}.
	 *
	 * @param from starting point.
	 * @param to   ending point.
	 * @return the shortest path from {@code from} to {@code to}. Null if there isn't one.
	 */
	public Path getPath(LatLon from, LatLon to)
	{
		if (isInside(from) || isInside(to))
		{
			throw new IllegalArgumentException(" from and to need to be accessible");
		}

		if (!this.passesThrough(from, to))
		{
			return new Path().add(from).add(to);
		}

		List<Integer> visiFrom = new ArrayList<>();
		List<Integer> visiTo = new ArrayList<>();

		for (int i = 0; i < outerPoints.length; i++)
		{
			if (!this.passesThrough(from, outerPoints[i]))
				visiFrom.add(i);
			if (!this.passesThrough(outerPoints[i], to))
				visiTo.add(i);
		}

		double dist = Double.POSITIVE_INFINITY;
		int indexFrom = -1, indexTo = -1;

		for (int f = 0; f < visiFrom.size(); f++)
		{
			for (int t = 0; t < visiTo.size(); t++)
			{
				if (moves[f][t].distance() < dist)
				{
					dist = moves[f][t].distance();
					indexFrom = f;
					indexTo = t;
				}
			}
		}

		if (dist != Double.POSITIVE_INFINITY)
		{
			return new Path().add(from).addAll(moves[indexFrom][indexTo]).add(to);
		} else return null;
	}

	/**
	 * Shortest path from {@code point} to anywhere on this GameObject.
	 */
	@Override
	public Vector2D pathFrom(LatLon point)
	{
		Vector2D[] args = new Vector2D[this.size()];

		for (int i = 0; i < this.size(); i++)
		{
			args[i] = this.get(i).pathFrom(point);
		}

		return Vector2D.min(args);
	}

	/**
	 * @return true if {@code point} is inside any of the blocks.
	 */
	@Override
	public boolean isInside(LatLon point)
	{
		for (Block block : this.values())
		{
			if (block.isInside(point))
				return true;
		}
		return false;
	}

	/**
	 * @return true if the direct path from {@code from} to {@code to} passes through any of the blocks.
	 */
	@Override
	public boolean passesThrough(LatLon from, LatLon to)
	{
		for (Block block : this.values())
		{
			if (block.passesThrough(from, to))
				return true;
		}
		return false;
	}

	/**
	 * @return all the outer corners of all the blocks
	 * (doesn't include corners of a Block that are inside another Block).
	 */
	@Override
	public LatLon[] getCorners()
	{
		List<LatLon> ans = new ArrayList<>();
		for (Block b : this.values())
		{
			LatLon[] corners = b.getCorners();
			for (var corner : corners)
			{
				if (!this.isInside(corner))
				{
					ans.add(corner);
				}
			}
		}
		return ans.toArray(new LatLon[0]);
	}

	/**
	 * @return returns an array of points that are {@code distance} meters from the corners.
	 */
	@Override
	public LatLon[] getOuterPoints(double distance)
	{
		List<LatLon> ans = new ArrayList<>();
		for (Block b : this.values())
		{
			LatLon[] points = b.getOuterPoints(distance + 0.0001);
			for (var point : points)
			{
				if (!this.isInside(point))
				{
					ans.add(point);
				}
			}
		}
		return ans.toArray(new LatLon[0]);
	}


	/**
	 * Returns a shallow copy of this {@code Blocks} instance.  (The
	 * elements themselves are not copied.)
	 *
	 * @return a clone of this {@code ArrayList} instance
	 */
	@Override
	public Object clone()
	{
		Blocks blocks = (Blocks) super.clone();
		blocks.moves = Arrays.copyOf(moves, moves.length);
		blocks.distance = distance;

		return blocks;
	}


	/*
	 * *************** - overrides of ArrayList that call updatePaths() after editing the list - ***************
	 */


	/**
	 * Associates the specified value with the specified key in this map.
	 * If the map previously contained a mapping for the key, the old
	 * value is replaced.
	 *
	 * @param key   key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with {@code key}, or
	 * {@code null} if there was no mapping for {@code key}.
	 * (A {@code null} return can also indicate that the map
	 * previously associated {@code null} with {@code key}.)
	 */
	@Override
	public Block put(Integer key, Block value)
	{
		Block put = super.put(key, value);
		updatePaths();
		return put;
	}

	/**
	 * Copies all of the mappings from the specified map to this map.
	 * These mappings will replace any mappings that this map had for
	 * any of the keys currently in the specified map.
	 *
	 * @param m mappings to be stored in this map
	 * @throws NullPointerException if the specified map is null
	 */
	@Override
	public void putAll(Map<? extends Integer, ? extends Block> m)
	{
		super.putAll(m);
		updatePaths();
	}

	/**
	 * Removes the mapping for the specified key from this map if present.
	 *
	 * @param key key whose mapping is to be removed from the map
	 * @return the previous value associated with {@code key}, or
	 * {@code null} if there was no mapping for {@code key}.
	 * (A {@code null} return can also indicate that the map
	 * previously associated {@code null} with {@code key}.)
	 */
	@Override
	public Block remove(Object key)
	{
		Block remove = super.remove(key);
		updatePaths();
		return remove;
	}

	/**
	 * Removes all of the mappings from this map.
	 * The map will be empty after this call returns.
	 */
	@Override
	public void clear()
	{
		super.clear();
		updatePaths();
	}

	@Override
	public Block putIfAbsent(Integer key, Block value)
	{
		Block block = super.putIfAbsent(key, value);
		updatePaths();
		return block;
	}

	@Override
	public boolean remove(Object key, Object value)
	{
		boolean remove = super.remove(key, value);
		updatePaths();
		return remove;
	}

	@Override
	public boolean replace(Integer key, Block oldValue, Block newValue)
	{
		boolean replace = super.replace(key, oldValue, newValue);
		updatePaths();
		return replace;
	}

	@Override
	public Block replace(Integer key, Block value)
	{
		Block replace = super.replace(key, value);
		updatePaths();
		return replace;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>This method will, on a best-effort basis, throw a
	 * {@link ConcurrentModificationException} if it is detected that the
	 * mapping function modifies this map during computation.
	 *
	 * @param key
	 * @param mappingFunction
	 * @throws ConcurrentModificationException if it is detected that the
	 *                                         mapping function modified this map
	 */
	@Override
	public Block computeIfAbsent(Integer key, Function<? super Integer, ? extends Block> mappingFunction)
	{
		Block block = super.computeIfAbsent(key, mappingFunction);
		updatePaths();
		return block;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>This method will, on a best-effort basis, throw a
	 * {@link ConcurrentModificationException} if it is detected that the
	 * remapping function modifies this map during computation.
	 *
	 * @param key
	 * @param remappingFunction
	 * @throws ConcurrentModificationException if it is detected that the
	 *                                         remapping function modified this map
	 */
	@Override
	public Block computeIfPresent(Integer key,
								  BiFunction<? super Integer, ? super Block, ? extends Block> remappingFunction)
	{
		Block block = super.computeIfPresent(key, remappingFunction);
		updatePaths();
		return block;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>This method will, on a best-effort basis, throw a
	 * {@link ConcurrentModificationException} if it is detected that the
	 * remapping function modifies this map during computation.
	 *
	 * @param key
	 * @param remappingFunction
	 * @throws ConcurrentModificationException if it is detected that the
	 *                                         remapping function modified this map
	 */
	@Override
	public Block compute(Integer key, BiFunction<? super Integer, ? super Block, ? extends Block> remappingFunction)
	{
		Block compute = super.compute(key, remappingFunction);
		updatePaths();
		return compute;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>This method will, on a best-effort basis, throw a
	 * {@link ConcurrentModificationException} if it is detected that the
	 * remapping function modifies this map during computation.
	 *
	 * @param key
	 * @param value
	 * @param remappingFunction
	 * @throws ConcurrentModificationException if it is detected that the
	 *                                         remapping function modified this map
	 */
	@Override
	public Block merge(Integer key, Block value,
					   BiFunction<? super Block, ? super Block, ? extends Block> remappingFunction)
	{
		Block merge = super.merge(key, value, remappingFunction);
		updatePaths();
		return merge;
	}

	@Override
	public void forEach(BiConsumer<? super Integer, ? super Block> action)
	{
		super.forEach(action);
		updatePaths();
	}

	@Override
	public void replaceAll(BiFunction<? super Integer, ? super Block, ? extends Block> function)
	{
		super.replaceAll(function);
		updatePaths();
	}
}
