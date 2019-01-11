package geo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elisha
 */
public class Path implements Cloneable
{
	private List<LatLon> points;

	public Path()
	{
		super();
		points = new ArrayList<>();
	}

	public Path(Path other)
	{
		this.points = new ArrayList<>(other.points);
	}

	public boolean add(Vector2D move)
	{
		if (points.size() == 0)
			return false;

		LatLon lastPoint = points.get(points.size() - 1);
		points.add(lastPoint.transformedBy(move));

		return true;
	}

	public Path add(LatLon point)
	{
		points.add(point);
		return this;
	}

	public Path addAll(Path other)
	{
		points.addAll(other.points);

		return this;
	}

	public LatLon getPoint(int index)
	{
		return points.get(index);
	}

	public List<LatLon> getPoints()
	{
		return points;
	}

	public int size()
	{
		return points.size();
	}

	public double distance()
	{
		if (points.size() <= 0)
			return 0;

		double ans = 0;
		LatLon lastPoint = points.get(0);
		for (int i = 1; i < points.size(); i++)
		{
			ans += lastPoint.distance(points.get(i));
		}
		return ans;
	}

	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public Path clone()
	{
		return new Path(this);
	}


	@Override
	public String toString()
	{
		return "Path{" + points + '}';
	}
}
