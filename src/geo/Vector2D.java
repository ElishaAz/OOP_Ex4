package geo;

/**
 * @author Elisha
 */
public class Vector2D
{
	public double x, y;

	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector2D(Vector2D vector)
	{
		this(vector.x, vector.y);
	}

	public static Vector2D fromDegrees(double degrees, double length)
	{
		return fromRadians(Math.toRadians(degrees), length);
	}

	public static Vector2D fromRadians(double radians, double length)
	{
		double x, y;

		x = Math.sin(radians) * length;

		y = Math.cos(radians) * length;

		return new Vector2D(x, y);
	}

	public Vector2D times(double scalar)
	{
		return new Vector2D(x * scalar, y * scalar);
	}

	public Vector2D plus(Vector2D vector)
	{
		return new Vector2D(x + vector.x, y + vector.y);
	}

	public Vector2D minus(Vector2D vector)
	{
		return new Vector2D(x - vector.x, y - vector.y);
	}

	public double length()
	{
		return Math.sqrt(x * x + y * y);
	}

	public Vector2D normalized()
	{
		Vector2D ans = new Vector2D(this);
		return ans.times(1 / ans.length());
	}

	/**
	 * @return the representation of this vector as an angle in degrees.
	 */
	public double toDegrees()
	{
		return Math.toDegrees(toRadians());
	}

	/**
	 * @return the representation of this vector as an angle in radians.
	 */
	public double toRadians()
	{
		if (y == 0)
		{
			if (x > 0)
				return 0;
			else
				return Math.PI;
		} else if (x == 0)
		{
			if (y > 0)
				return Math.PI / 2.0;
			else
				return Math.PI * 3.0 / 2.0;
		}

		if (x > 0)
		{
			if (y > 0)
			{
				return Math.atan(y / x);
			} else
			{
				return 2 * Math.PI - Math.atan(-y / x);
			}
		} else
		{
			if (y > 0)
			{
				return Math.PI - Math.atan(y / -x);
			} else
			{
				return Math.PI + Math.atan(y / x);
			}
		}
	}

	@Override
	public Vector2D clone()
	{
		try
		{
			Vector2D vector2D = (Vector2D) super.clone();

			vector2D.x = x;
			vector2D.y = y;

			return vector2D;
		} catch (CloneNotSupportedException e)
		{
			throw new InternalError();
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Vector2D)
		{
			Vector2D vec = (Vector2D) obj;
			return x == vec.x && y == vec.y;
		} else
		{
			return false;
		}
	}

	/**
	 * @return a new {@link Vector2D} with x and y set to 0.
	 */
	public static Vector2D zero()
	{
		return new Vector2D(0, 0);
	}

	/**
	 * @return a new {@link Vector2D} with x and y set to 1.
	 */
	public static Vector2D one()
	{
		return new Vector2D(1, 1);
	}

	/**
	 * @return {@code new Vector2D(0, 1)}.
	 */
	public static Vector2D up()
	{
		return new Vector2D(0, 1);
	}

	/**
	 * @return {@code new Vector2D(0, -1)}.
	 */
	public static Vector2D down()
	{
		return new Vector2D(0, -1);
	}

	/**
	 * @return {@code new Vector2D(1, 0)}.
	 */
	public static Vector2D right()
	{
		return new Vector2D(1, 0);
	}

	/**
	 * @return {@code new Vector2D(-1, 0)}.
	 */
	public static Vector2D left()
	{
		return new Vector2D(-1, 0);
	}


	public static Vector2D min(Vector2D a, Vector2D b)
	{
		return (a.length() <= b.length()) ? a : b;
	}

	public static Vector2D min(Vector2D... args)
	{
		if (args.length <= 0)
			return null;

		Vector2D ans = args[0];
		for (Vector2D vec : args)
		{
			if (vec.length() < ans.length())
				ans = vec;
		}
		return ans;
	}

	public static Vector2D max(Vector2D a, Vector2D b)
	{
		return (a.length() >= b.length()) ? a : b;
	}

	public static Vector2D max(Vector2D... args)
	{
		if (args.length <= 0)
			return null;

		Vector2D ans = args[0];
		for (Vector2D vec : args)
		{
			if (vec.length() > ans.length())
				ans = vec;
		}
		return ans;
	}

	@Override
	public String toString()
	{
		return "[" + x + ", " + y + "]";
	}
}
