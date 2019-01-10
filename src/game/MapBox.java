package game;

import game.abstracts.Rectangle;
import geo.LatLon;

/**
 * @author Elisha
 */
public class MapBox extends Rectangle
{
	public static final int minLatIndex = 2, minLonIndex = 3, maxLatIndex = 5, maxLonIndex = 6,
			scoreReduseIndex = 8;

	public final double scoreReduse;

	public MapBox(LatLon max, LatLon min, double scoreReduse)
	{
		super(max, min);
		this.scoreReduse = scoreReduse;
	}

	public MapBox(MapBox other)
	{
		super(other);
		this.scoreReduse = other.scoreReduse;
	}

	@Override
	public MapBox clone()
	{
		return new MapBox(this);
	}
}
