package game;

import game.abstracts.GamePlayer;
import game.interfaces.IEdible;
import geo.LatLon;

/**
 * @author Elisha
 */
public class Packman extends GamePlayer implements IEdible
{
	public static final int latIndex = 2, lonIndex = 3, speedIndex = 5, radiusIndex = 6;

	public Packman(LatLon position, double speed, double radius)
	{
		super(position, speed, radius);
	}

	public Packman(Packman other)
	{
		super(other);
	}

	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public Packman clone()
	{
		return new Packman(this);
	}
}
