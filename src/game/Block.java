package game;

import game.abstracts.Rectangle;
import geo.LatLon;

/**
 * @author Elisha
 */
public class Block extends Rectangle
{
	public static final int  minLatIndex = 2, minLonIndex = 3, maxLatIndex = 5, maxLonIndex = 6,
			scoreReduseIndex = 8;

	public final double scoreReduce;

	public Block(LatLon max, LatLon min, double scoreReduce)
	{
		super(max, min);
		this.scoreReduce = scoreReduce;
	}

	public Block(Block other)
	{
		super(other);
		this.scoreReduce = other.scoreReduce;
	}


	@Override
	public Block clone()
	{
		return new Block(this);
	}

}
