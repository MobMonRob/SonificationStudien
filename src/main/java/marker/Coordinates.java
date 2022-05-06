package marker;

public class Coordinates
{
	private final double x;
	private final double y;
	private final double z;

	public Coordinates(double xCoordinate, double yCoordinate, double zCoordinate)
	{
		this.x = xCoordinate;
		this.y = yCoordinate;
		this.z = zCoordinate;
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	public double getZ()
	{
		return z;
	}

	public boolean isZero()
	{
		return (getX() == 0.0) && (getY() == 0.0) && (getZ() == 0.0);
	}

	@Override
	public boolean equals(Object markerCoordinatesObject)
	{
		if (!(markerCoordinatesObject instanceof Coordinates))
		{
			return false;
		}

		Coordinates markerCoordinates = (Coordinates) markerCoordinatesObject;

		int compareResult = compareTo(markerCoordinates.getX(), markerCoordinates.getY(),
				markerCoordinates.getZ());
		return compareResult == 0;
	}

	public boolean equals(double[] coordinates)
	{
		if (coordinates == null || coordinates.length != 3)
		{
			return false;
		}

		int compareResult = compareTo(coordinates[0], coordinates[1], coordinates[2]);
		return compareResult == 0;
	}

	public int compareTo(double xCoordinate, double yCoordinate, double zCoordinate)
	{
		int xCompare = Double.compare(x, xCoordinate);
		int yCompare = Double.compare(y, yCoordinate);
		int zCompare = Double.compare(z, zCoordinate);

		return Math.abs(xCompare) + Math.abs(yCompare) + Math.abs(zCompare);
	}

	@Override
	public String toString()
	{
		return String.format("%s %s %s", getX(), getY(), getZ());
	}
}
