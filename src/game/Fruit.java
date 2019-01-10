package game;

import game.interfaces.IImmobileGameObject;
import geo.LatLon;
import geo.Vector2D;

/**
 * @author Elisha
 */
public class Fruit implements IImmobileGameObject
{
	public static final int latIndex = 2, lonIndex = 3, weightIndex = 5;

	public final LatLon position;
	public final double weight;

	public Fruit(LatLon position, double weight)
	{
		this.position = position;
		this.weight = weight;
	}


	/**
	 * @param point
	 * @return true if {@code point} is inside this object.
	 */
	@Override
	public boolean isInside(LatLon point)
	{
		return point.equals(position);
	}

	/**
	 * @param from
	 * @param to
	 * @return true if the direct path from {@code from} to {@code to} passes through this rectangle.
	 */
	@Override
	public boolean passesThrough(LatLon from, LatLon to)
	{
		throw new UnsupportedOperationException("Fruit passesThrough not supported");
	}

	@Override
	public LatLon[] getCorners()
	{
		return new LatLon[]{position.clone()};
	}

	@Override
	public LatLon[] getOuterPoints(double distance)
	{
		return new LatLon[]{position.clone()};
	}

	/**
	 * Shortest path from {@code point} to anywhere on this GameObject.
	 *
	 * @param point
	 */
	@Override
	public Vector2D pathFrom(LatLon point)
	{
		return point.distanceVector(position);
	}
}
