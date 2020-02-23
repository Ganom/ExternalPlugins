package net.runelite.client.plugins.externals.olmswapper;

import net.runelite.api.Actor;
import net.runelite.api.Model;
import net.runelite.api.Node;
import net.runelite.api.Projectile;

public class TestProjectile implements Projectile
{
	private final int id;

	public TestProjectile(int id)
	{
		this.id = id;
	}

	@Override
	public int getId()
	{
		return this.id;
	}

	@Override
	public Actor getInteracting()
	{
		return null;
	}

	@Override
	public int getX1()
	{
		return 0;
	}

	@Override
	public int getY1()
	{
		return 0;
	}

	@Override
	public int getFloor()
	{
		return 0;
	}

	@Override
	public int getHeight()
	{
		return 0;
	}

	@Override
	public int getEndHeight()
	{
		return 0;
	}

	@Override
	public int getStartMovementCycle()
	{
		return 0;
	}

	@Override
	public int getEndCycle()
	{
		return 0;
	}

	@Override
	public int getRemainingCycles()
	{
		return 0;
	}

	@Override
	public int getSlope()
	{
		return 0;
	}

	@Override
	public int getStartHeight()
	{
		return 0;
	}

	@Override
	public double getX()
	{
		return 0;
	}

	@Override
	public double getY()
	{
		return 0;
	}

	@Override
	public double getZ()
	{
		return 0;
	}

	@Override
	public double getScalar()
	{
		return 0;
	}

	@Override
	public double getVelocityX()
	{
		return 0;
	}

	@Override
	public double getVelocityY()
	{
		return 0;
	}

	@Override
	public double getVelocityZ()
	{
		return 0;
	}

	@Override
	public Model getModel()
	{
		return null;
	}

	@Override
	public int getModelHeight()
	{
		return 0;
	}

	@Override
	public void setModelHeight(int modelHeight)
	{

	}

	@Override
	public void draw(int orientation, int pitchSin, int pitchCos, int yawSin, int yawCos, int x, int y, int z, long hash)
	{

	}

	@Override
	public Node getNext()
	{
		return null;
	}

	@Override
	public Node getPrevious()
	{
		return null;
	}

	@Override
	public long getHash()
	{
		return 0;
	}
}
