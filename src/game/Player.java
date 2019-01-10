package game;

import game.abstracts.GamePlayer;
import geo.LatLon;
import geo.Path;
import geo.Vector2D;

/**
 * @author Elisha
 */
public class Player extends GamePlayer
{
	public static final int latIndex = 2, lonIndex = 3, speedIndex = 5, radiusIndex = 6;

	private double angle;
	public double score;

	public Player(LatLon position, double speed, double radius, double angle)
	{
		super(position,speed, radius);
		this.angle = angle;
		score = 0;
	}

	public Player(LatLon position, double speed, double radius)
	{
		this(position, speed, radius, 0);
	}

	public Player(Player other)
	{
		super(other);
		score = 0;
	}

	public void rotate(double angle)
	{
		this.angle = angle;
	}


	@Override
	public Vector2D moveDirection()
	{
		return super.moveDirection();
	}

	@Override
	public Player clone()
	{
		return new Player(this);
	}

	public double getAngle()
	{
		return angle;
	}
}
