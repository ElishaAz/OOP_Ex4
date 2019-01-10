package game;

import game.abstracts.GamePlayer;
import geo.LatLon;

/**
 * @author Elisha
 */
public class Ghost extends GamePlayer
{
	public static final int latIndex = 2, lonIndex = 3, speedIndex = 5, radiusIndex = 6;

	public Ghost(LatLon position, double speed, double radius)
	{
		super(position, speed, radius);
	}

	public Ghost(Ghost other)
	{
		super(other);
	}

	@Override
	public Object clone()
	{
		return super.clone();
	}
}
