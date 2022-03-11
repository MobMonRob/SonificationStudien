/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sonificationstuditest.Marker;

/**
 *
 * @author noahh
 */
public class Marker
{

    private final String subjectName;
    private final String markerName;
    private final MarkerCoordinates coordinates;

    public Marker(String subjectName, String markerName, MarkerCoordinates coordinates)
    {
        this.subjectName = subjectName;
        this.markerName = markerName;
        this.coordinates = coordinates;
    }

    public String getSubjectName()
    {
        return subjectName;
    }

    public String getMarkerName()
    {
        return markerName;
    }

    public MarkerCoordinates getCoordinates()
    {
        return coordinates;
    }

}
