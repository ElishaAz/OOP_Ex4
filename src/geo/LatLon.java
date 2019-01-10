package geo;

/**
 * @author Elisha
 */
public class LatLon implements Cloneable
{
	public final double latMin = 0, latMax = 0;
	public final double lonMin = 0, lonMax = 0;

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
		Vector2D dist = new Vector2D(Math.sin(diff.getLat() * 2 * Math.PI / 360) * earth_radius,
				Math.sin(diff.getLon() * 2 * Math.PI / 360) * earth_radius * lon_norm);

		return dist;
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
		if (latMin <= lat && lat <= latMax)
		{
			this.lat = lat;
			return true;
		}
		return false;
	}

	public boolean setLon(double lon)
	{
		if (lonMin <= lon && lon <= lonMax)
		{
			this.lon = lon;
			return true;
		}
		return false;
	}
}
