package game.interfaces;

import geo.LatLon;

/**
 * @author Elisha
 */
public interface IImmobileGameObject extends IGameObject
{
	/**
	 * @return true if {@code point} is inside this object.
	 */
	boolean isInside(LatLon point);

	/**
	 * @return true if the direct path from {@code from} to {@code to} passes through this rectangle.
	 */
	boolean passesThrough(LatLon from, LatLon to);

	LatLon[] getCorners();

	LatLon[] getOuterPoints(double distance);
}
