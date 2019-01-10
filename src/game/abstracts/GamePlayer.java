package game.abstracts;

import game.interfaces.IMobileGameObject;
import geo.LatLon;
import geo.Vector2D;

/**
 * Class for all the moving objects that interact with the player, including the player itself
 * (i.e. Packman, Ghost etc.).
 *
 * @author Elisha
 */
public abstract class GamePlayer implements IMobileGameObject
{

	/* position */
	private LatLon position;
	/* last known position (for direction)*/
	private LatLon lastPosition;
	public final double speed;
	/* radius in meters */
	protected double radius;

	protected GamePlayer(LatLon position,double speed, double radius)
	{
		this.position = position.clone();
		this.lastPosition = position.clone();
		this.speed = speed;
		this.radius = radius;
	}

	protected GamePlayer(GamePlayer other)
	{
		this.position = other.position.clone();
		this.lastPosition = other.position.clone();
		this.speed = other.speed;
		this.radius = other.radius;
	}

	@Override
	public LatLon getPosition()
	{
		return position;
	}

	@Override
	public void updatePosition(LatLon newPosition)
	{
		lastPosition = position;
		position = newPosition;
	}

	@Override
	public Vector2D moveDirection()
	{
		return lastPosition.distanceVector(position).normalized();
	}

	/**
	 * Shortest path from {@code point} to anywhere on this GameObject.
	 *
	 * @param point
	 */
	@Override
	public Vector2D pathFrom(LatLon point)
	{
		/* Shortest path from point to position minus radius */
		double scalar = radius / point.distance(position);
		Vector2D dist = point.distanceVector(position);
		return dist.minus(dist.times(scalar));
	}

	@Override
	public Object clone()
	{
		try
		{
			GamePlayer player = (GamePlayer) super.clone();

			player.position = position.clone();
			player.lastPosition = position.clone();
			player.radius = radius;

			return player;
		} catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}
}
