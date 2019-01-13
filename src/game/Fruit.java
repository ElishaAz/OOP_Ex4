package game;

import game.interfaces.IEdible;
import game.interfaces.IImmobileGameObject;
import geo.LatLon;
import geo.Vector2D;

/**
 * @author Elisha
 */
public class Fruit implements IImmobileGameObject, IEdible
{
	public static final int latIndex = 2, lonIndex = 3, weightIndex = 5;
	public static final double fruitRadius = 1.0;

	public final LatLon position;
	public final double weight;

	public Fruit(LatLon position, double weight)
	{
		this.position = position;
		this.weight = weight;
	}


	/**
	 * @return true if {@code point} is inside this object.
	 */
	@Override
	public boolean isInside(LatLon point)
	{
		return point.equals(position);
	}

	/**
	 * @return true if the direct path from {@code from} to {@code to} passes through this rectangle.
	 */
	@Override
	public boolean intersects(LatLon from, LatLon to)
	{
		return position.isOnLine(from,to,fruitRadius);
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

	@Override
	public LatLon getPosition()
	{
		return position;
	}
}
