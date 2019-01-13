package game.interfaces;

import geo.LatLon;

/**
 * @author Elisha
 */
public interface IEdible
{

	/**
	 * @return where you need to be to eat this IEdible
	 */
	LatLon getPosition();
}
