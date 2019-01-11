package game.abstracts;

import game.interfaces.IImmobileGameObject;
import geo.LatLon;
import geo.Vector2D;

import java.util.Arrays;

/**
 * @author Elisha
 */
public abstract class Rectangle implements IImmobileGameObject
{
	/* top right point */
	public final LatLon max;
	/* bottom left point */
	public final LatLon min;

	protected Rectangle(LatLon max, LatLon min)
	{
		this.max = max;
		this.min = min;
	}

	protected Rectangle(Rectangle other)
	{
		max = other.max.clone();
		min = other.min.clone();
	}

	@Override
	public boolean isInside(LatLon point)
	{
		if (min.getLat() <= point.getLat() && point.getLat() <= max.getLat())
			if (min.getLon() <= point.getLon() && point.getLon() <= max.getLon())
			{
				return true;
			}

//		if (max.getLat() <= point.getLat() && point.getLat() <= min.getLat())
//			if (max.getLon() <= point.getLon() && point.getLon() <= min.getLon())
//				return true;

		return false;
	}


	/**
	 * Shortest path from {@code point} to anywhere on this GameObject.
	 */
	@Override
	public Vector2D pathFrom(LatLon point)
	{
		if (isInside(point))
			return Vector2D.zero();
		if (point.getLat() >= max.getLat())
		{
			if (point.getLon() >= max.getLon())
			{
				return point.distanceVector(max);
			} else if (point.getLon() >= min.getLon())
			{
				return point.distanceVector(new LatLon(max.getLat(), point.getLon()));
			} else
			{
				return point.distanceVector(new LatLon(max.getLat(), min.getLon()));
			}
		} else if (point.getLat() >= min.getLat())
		{
			if (point.getLon() >= max.getLon())
			{
				return point.distanceVector(new LatLon(point.getLat(), max.getLon()));
			} else if (point.getLon() >= min.getLon())
			{
				return Vector2D.zero();
			} else
			{
				return point.distanceVector(new LatLon(point.getLat(), min.getLon()));
			}
		} else
		{
			if (point.getLon() >= max.getLon())
			{
				return point.distanceVector(new LatLon(min.getLat(), max.getLon()));
			} else if (point.getLon() >= min.getLon())
			{
				return point.distanceVector(new LatLon(min.getLat(), point.getLon()));
			} else
			{
				return point.distanceVector(min);
			}
		}
	}

	/**
	 * @return true if the direct path from {@code from} to {@code to} passes through this rectangle.
	 */
	@Override
	public boolean passesThrough(LatLon from, LatLon to)
	{
		if (isInside(from) || isInside(to))
			return true;

		if (min.getLat() <= from.getLat() && from.getLat() <= max.getLat())
		{
			if (min.getLat() <= to.getLat() && to.getLat() <= max.getLat())
			{
				if (from.getLon() <= min.getLon() && max.getLon() <= to.getLon())
					return true;
				else if (to.getLon() <= min.getLon() && max.getLon() <= from.getLon())
					return true;
			} else if (min.getLon() <= to.getLon() && to.getLon() <= max.getLon())
				return true;
		}
		if (min.getLon() <= from.getLon() && from.getLon() <= max.getLon())
		{
			if (min.getLon() <= to.getLon() && to.getLon() <= max.getLon())
			{
				if (from.getLat() <= min.getLat() && max.getLat() <= to.getLat())
					return true;
				else if (to.getLat() <= min.getLat() && max.getLat() <= from.getLat())
					return true;
			} else if (min.getLat() <= to.getLat() && to.getLat() <= max.getLat())
				return true;
		}
		return false;
	}

	/**
	 * @return an array with 4 point that are the corners.
	 */
	@Override
	public LatLon[] getCorners()
	{
		return new LatLon[]{max, new LatLon(max.getLat(), min.getLon()), new LatLon(min.getLat(), max.getLon()), min};
	}

	/**
	 * @return an array with 4 points that are {@code distance} meters from the corners.
	 */
	@Override
	public LatLon[] getOuterPoints(double distance)
	{
		LatLon outerMax = max.transformedBy(new Vector2D(distance, distance));
		LatLon outerMin = min.transformedBy(new Vector2D(-distance, -distance));

		return new LatLon[]{outerMax, new LatLon(outerMax.getLat(), outerMin.getLon()), new LatLon(outerMin.getLat(),
				outerMax.getLon()), outerMin};
	}

	@Override
	public String toString()
	{
		return "Rect{" + max + ", " + min + '}';
	}
}
