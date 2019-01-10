package game.interfaces;

import geo.LatLon;
import geo.Vector2D;

/**
 * @author Elisha
 */
public interface IMobileGameObject extends IGameObject
{
	LatLon getPosition();

	void updatePosition(LatLon newPosition);

	Vector2D moveDirection();
}
