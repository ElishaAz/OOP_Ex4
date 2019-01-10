package game.interfaces;

import geo.LatLon;
import geo.Vector2D;

/**
 * @author Elisha
 */
public interface IGameObject extends Cloneable
{
	/**
	 * Shortest path from {@code point} to anywhere on this GameObject.
	 */
	Vector2D pathFrom (LatLon point);
}
