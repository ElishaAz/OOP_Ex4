package geo;

import java.awt.geom.Line2D;

/**
 * @author Elisha
 */
public class LatLon implements Cloneable
{
	public static final double minLatitude = -180;
	public static final double maxLatitude = +180;
	public static final double minLongitude = -90;
	public static final double maxLongitude = +90;

	public static final double earth_radius = 6371 * 1000;

	private double lat, lon;

	public LatLon(double lat, double lon)
	{
		this.lat = lat;
		this.lon = lon;
	}

	public LatLon(LatLon latLon)
	{
		this(latLon.getLat(), latLon.getLon());
	}

	public LatLon()
	{
		this(0, 0);
	}

	/**
	 * Measures the distance in meters from this LLA point to {@code point}.
	 *
	 * @return a vector in meters of the distance from this LLA point to {@code point}.
	 */
	public Vector2D distanceVector(LatLon point)
	{
		double lon_norm = Math.cos(getLat() * 2 * Math.PI / 360);
		LatLon diff = point.minus(this);

		return new Vector2D(Math.sin(diff.getLat() * 2 * Math.PI / 360) * earth_radius,
				Math.sin(diff.getLon() * 2 * Math.PI / 360) * earth_radius * lon_norm);
	}

	/**
	 * computes the 3D distance (in meters) between the two gps like points
	 *
	 * @return distance in meters from this point to {@code point}.
	 */
	public double distance(LatLon point)
	{
		Vector2D vector = this.distanceVector(point);
		return Math.sqrt(Math.pow(vector.x, 2) + Math.pow(vector.y, 2));
	}

	/**
	 * Calculates an LatLon point which is this point transformedBy by {@code vector}.
	 * code from
	 * <a href = https://gis.stackexchange.com/questions/2951/algorithm-for-offsetting-a-latitude-longitude-by-some-amount-of-meters>
	 * https://gis.stackexchange.com/questions/2951/algorithm-for-offsetting-a-latitude-longitude-by-some-amount-of
	 * -meters</a>
	 *
	 * @param vector the vector to transformedBy this point by.
	 * @return an LLA point which is this point transformedBy by {@code vector}.
	 */
	public LatLon transformedBy(Vector2D vector)
	{
		//Coordinate offsets in radians
		double dLat = vector.x / earth_radius;
		double dLon = vector.y / (earth_radius * Math.cos(Math.PI * getLat() / 180));

		//OffsetPosition, decimal degrees
		double latO = getLat() + dLat * 180 / Math.PI;
		double lonO = getLon() + dLon * 180 / Math.PI;

		return new LatLon(latO, lonO);
	}


	public LatLon minus(LatLon latLon)
	{
		return new LatLon(getLat() - latLon.getLat(), getLon() - latLon.getLon());
	}

	public LatLon plus(LatLon latLon)
	{
		return new LatLon(getLat() + latLon.getLat(), getLon() + latLon.getLon());
	}

	public boolean isOnLine(LatLon start, LatLon end, double distanceMeters)
	{
		double distStart = start.distance(this);
		double distEnd = this.distance(end);
		double distStartEnd = start.distance(end);
		double dist = distStart + distEnd;

		return distStartEnd + distanceMeters >= dist;
	}

	/**
	 * algorithm for checking if lines intersect:
	 * <a href=https://gamedev.stackexchange.com/questions/111100/intersection-of-a-line-and-a-rectangle>
	 * https://gamedev.stackexchange.com/questions/111100/intersection-of-a-line-and-a-rectangle
	 * </a>
	 *
	 * @param start1 starting point of first line.
	 * @param end1   ending point of first line.
	 * @param start2 starting point of second line.
	 * @param end2   ending point of second line.
	 * @return true if the ines intersect and false otherwise.
	 */
	public static boolean intersect(LatLon start1, LatLon end1, LatLon start2, LatLon end2)
	{
		return Line2D.linesIntersect(start1.getLat(), start1.getLon(), end1.getLat(), end1.getLon(),
				start2.getLat(), start2.getLon(), end2.getLat(), end2.getLon());
	}

	@Override
	public LatLon clone()
	{
		try
		{
			LatLon latLon = (LatLon) super.clone();

			latLon.lat = lat;
			latLon.lon = lon;

			return latLon;
		} catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof LatLon)
		{
			LatLon latLon = (LatLon) obj;
			return getLat() == latLon.getLat() && getLon() == latLon.getLon();
		} else return false;
	}

	@Override
	public String toString()
	{
		return "Lat Lon (" + getLat() + ", " + getLon() + ")";
	}

	public double getLat()
	{
		return lat;
	}

	public double getLon()
	{
		return lon;
	}

	public boolean setLat(double lat)
	{
		if (minLatitude <= lat && lat <= maxLatitude)
		{
			this.lat = lat;
			return true;
		}
		return false;
	}

	public boolean setLon(double lon)
	{
		if (minLongitude <= lon && lon <= maxLongitude)
		{
			this.lon = lon;
			return true;
		}
		return false;
	}
}
