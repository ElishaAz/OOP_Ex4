package game;

import Robot.Play;
import geo.LatLon;
import geo.Path;
import geo.Vector2D;

import java.io.Closeable;
import java.util.List;

/**
 * @author Elisha
 */
public class Game implements AutoCloseable
{
	private Play play;
	public final PackmanObjects objects;
	private boolean started = false;

	public Game(String file, Long... ids)
	{
		play = new Play(file);

		switch (ids.length)
		{
			case 0:
				break;
			case 1:
				play.setIDs(ids[0]);
				break;
			case 2:
				play.setIDs(ids[0], ids[1]);
				break;
			default:
				play.setIDs(ids[0], ids[1], ids[2]);
				break;
		}

		String boundingBox = play.getBoundingBox();
		List<String> board = play.getBoard();

		objects = new PackmanObjects(boundingBox, board);
	}

	public boolean setInitLocation(LatLon position)
	{
		if (started)
			throw new UnsupportedOperationException("Init location can only be set before starting!");

		return play.setInitLocation(position.getLat(), position.getLon());
	}

	public void start()
	{
		play.start();
		update();
	}

	public void update()
	{
		objects.update(play.getBoard());
	}

	public void rotate(double angle)
	{
		play.rotate(angle);
		objects.rotatePlayer(angle);
		update();
	}

	public Path pathTo(LatLon place)
	{
		return objects.getBlocks().getPath(objects.getPlayer().getPosition(), place);
	}

	/**
	 * <p>
	 * Moves the player along {@code path}.
	 * </p>
	 * USAGE:
	 * <pre>{@code
	 * int index = 1;
	 * while (index > 0)
	 * {
	 *      index = game.moveAlong(path,index);
	 * }
	 * }
	 * </pre>
	 *
	 * @param path  the {@link Path} that the player will move along to.
	 * @param index the index of the next point in the path.
	 * @return the index of the next point in the path, after moving along it. Special cases:
	 * <ul>
	 * <li> if the player reached the end of the path, returns -1.
	 * <li> if a negative value is passed as the index, that value will be returned.
	 * </ul>
	 */
	public int moveAlong(Path path, int index)
	{
		if (index >= path.size())
			return -1;
		else if (index < 0)
			return index;

		LatLon place = path.getPoint(index);

		if (!overshoots(place))
		{
			if (isSafeMove(place))
			{
				moveTo(place);
			} else return -2;

		} else
		{
			index++;

			if (index >= path.size())
			{
				return -1;
			}

			LatLon nextPlace = path.getPoint(index);


			if (isSafeMove(nextPlace))
			{
				moveTo(nextPlace);
			} else
			{
				if (isSafeMove(afterMove(place))) // if it's safe to overshoot
				{
					moveTo(place);
				} else return -2;
			}
		}
		return index;
	}

	/**
	 * Checks if the move towards {@code to} will overshoot it.
	 *
	 * @param to {@link LatLon} to move towards.
	 * @return true if the move towards {@code to} will overshoot it an false otherwise.
	 */
	private boolean overshoots(LatLon to)
	{
		double distance = player().getPosition().distance(to);
		return distance < player().speed;
	}

	/**
	 * Calculates the position that the player will be in after movnig towards {@code to}.
	 *
	 * @param to {@link LatLon} to move towards.
	 * @return the position that the player will be in after moving towards {@code to}.
	 */
	private LatLon afterMove(LatLon to)
	{
		Vector2D vector = player().getPosition().distanceVector(to);
		double distance = vector.length();

		return player().getPosition().transformedBy(vector.times(player().speed / distance));
	}

	/**
	 * Checks if it's safe to move towards {@code to}.
	 *
	 * @param to {@link LatLon} to move towards.
	 * @return true if it's safe to move towards {@code to} and false otherwise.
	 */
	private boolean isSafeMove(LatLon to)
	{
		return !blocks().passesThrough(player().getPosition(), to);
	}

	/**
	 * Moves towards {@code place}.
	 *
	 * @param place {@link LatLon} to move towards.
	 */
	private void moveTo(LatLon place)
	{
		rotate(player().getPosition().distanceVector(place).toDegrees());
	}

	public Player player()
	{
		return objects.getPlayer();
	}

	public Blocks blocks()
	{
		return objects.getBlocks();
	}

	public String getPlayReport()
	{
		return play.getStatistics();
	}


	/**
	 * Closes this resource, relinquishing any underlying resources.
	 * This method is invoked automatically on objects managed by the
	 * {@code try}-with-resources statement.
	 *
	 * <p>While this interface method is declared to throw {@code
	 * Exception}, implementers are <em>strongly</em> encouraged to
	 * declare concrete implementations of the {@code close} method to
	 * throw more specific exceptions, or to throw no exception at all
	 * if the close operation cannot fail.
	 *
	 * <p> Cases where the close operation may fail require careful
	 * attention by implementers. It is strongly advised to relinquish
	 * the underlying resources and to internally <em>mark</em> the
	 * resource as closed, prior to throwing the exception. The {@code
	 * close} method is unlikely to be invoked more than once and so
	 * this ensures that the resources are released in a timely manner.
	 * Furthermore it reduces problems that could arise when the resource
	 * wraps, or is wrapped, by another resource.
	 *
	 * <p><em>Implementers of this interface are also strongly advised
	 * to not have the {@code close} method throw {@link
	 * InterruptedException}.</em>
	 * <p>
	 * This exception interacts with a thread's interrupted status,
	 * and runtime misbehavior is likely to occur if an {@code
	 * InterruptedException} is {@linkplain Throwable#addSuppressed
	 * suppressed}.
	 * <p>
	 * More generally, if it would cause problems for an
	 * exception to be suppressed, the {@code AutoCloseable.close}
	 * method should not throw it.
	 *
	 * <p>Note that unlike the {@link Closeable#close close}
	 * method of {@link Closeable}, this {@code close} method
	 * is <em>not</em> required to be idempotent.  In other words,
	 * calling this {@code close} method more than once may have some
	 * visible side effect, unlike {@code Closeable.close} which is
	 * required to have no effect if called more than once.
	 * <p>
	 * However, implementers of this interface are strongly encouraged
	 * to make their {@code close} methods idempotent.
	 * <p>
	 * //@throws Exception if this resource cannot be closed
	 */
	@Override
	public void close()
	{
		play.stop();
	}
}
