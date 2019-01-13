package GUI;


import game.MapBox;
import geo.LatLon;
import geo.Vector2D;

import java.awt.*;

/**
 * @author Elisha
 */
public class Map
{
	public final Image map;
	public final MapBox box;

	private int screenWidth;
	private int screenHeight;

	private final LatLon top_left, bottom_right;

	public Map(Image map, MapBox box)
	{
		this.map = map;
		this.box = box;

		top_left = new LatLon(box.max.getLat(), box.min.getLon());
		bottom_right = new LatLon(box.min.getLat(), box.max.getLon());
	}

	public void updateScreenSize(int screenWidth, int screenHeight)
	{
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	public Point worldToPixel(LatLon point) // (point pixels) = (point meters)/(screen meters) * (screen pixels)
	{
		final Vector2D screenMeters = flip(box.min).distanceVector(flip(box.max));
		final Vector2D pointMeters = flip(box.min).distanceVector(flip(point));

		double x = (pointMeters.x / screenMeters.x) * screenWidth;
		double y = (1 - (pointMeters.y / screenMeters.y)) * screenHeight;

		return new Point((int) (x), (int) (y));
	}

	public LatLon pixelToWorld(Point point) // (point meters) = (point pixels)/(screen pixels) * (screen meters)
	{
		final Vector2D screenMeters = flip(box.min).distanceVector(flip(box.max));

		double x = (point.x / (double) screenWidth) * screenMeters.x;
		double y = (1 - (point.y / (double) screenHeight)) * screenMeters.y;

		return flip(flip(box.min).transformedBy(new Vector2D(x, y)));
	}

	private LatLon flip(LatLon ll)
	{
		return new LatLon(ll.getLon(), ll.getLat());
	}

	public int getScreenWidth()
	{
		return screenWidth;
	}

	public int getScreenHeight()
	{
		return screenHeight;
	}
}