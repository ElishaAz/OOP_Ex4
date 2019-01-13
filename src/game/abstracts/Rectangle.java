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
			return min.getLon() <= point.getLon() && point.getLon() <= max.getLon();

//		if (max.getLat() <= point.getLat() && point.getLat() <= min.getLat())
//			if (max.getLon() <= point.getLon() && point.getLon() <= min.getLon())
//				return true;

		return false;
	}

	/**
	 * @return true if the direct path from {@code from} to {@code to} intersects this rectangle.
	 */
	@Override
	public boolean intersects(LatLon from, LatLon to)
	{
		if (isInside(from) || isInside(to))
		{
			return true;
		}

		LatLon[] corners = getCorners();

		for (int i = 0; i < corners.length; i++)
		{
			for (int j = 0; j < corners.length; j++)
			{
				if (j > i)
				{
					if (LatLon.intersect(from, to, corners[i], corners[j]))
					{
						return true;
					}
				}
			}
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
		LatLon outerMax = max.transformedBy(max.distanceVector(min).normalized().times(-distance));
		LatLon outerMin = min.transformedBy(min.distanceVector(max).normalized().times(-distance));

		return new LatLon[]{outerMax, new LatLon(outerMax.getLat(), outerMin.getLon()), new LatLon(outerMin.getLat(),
				outerMax.getLon()), outerMin};
	}

	@Override
	public String toString()
	{
		return "Rect{" + max + ", " + min + '}';
	}
}
