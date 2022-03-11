/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sonificationstuditest.Marker;

/**
 *
 * @author noahh
 */
public class MarkerCoordinates
{

    private final double x;
    private final double y;
    private final double z;

    public MarkerCoordinates(double xCoordinate, double yCoordinate, double zCoordinate)
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
        if (!(markerCoordinatesObject instanceof MarkerCoordinates))
        {
            return false;
        }

        MarkerCoordinates markerCoordinates = (MarkerCoordinates) markerCoordinatesObject;

        int compareResult = compareTo(markerCoordinates.getX(), markerCoordinates.getY(), markerCoordinates.getZ());
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

        int compareResult = Math.abs(xCompare) + Math.abs(yCompare) + Math.abs(zCompare);
        return compareResult;
    }

    @Override
    public String toString()
    {
        return String.format("%s %s %s", getX(), getY(), getZ());
    }
}
