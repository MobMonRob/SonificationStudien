package sound;

import java.util.ArrayList;
import java.util.List;

import detection.Fault;
import marker.Coordinates;
import musical.Chord;
import musical.Note;
import musical.NoteName;
import musical.Playable;

public class CoordinateRelatedSoundFactory implements SoundFactory
{
	private final CoordinateType coordinateType;
	private final double maximum;

	public CoordinateRelatedSoundFactory(CoordinateType coordinateType, double maximumValue)
	{
		this.coordinateType = coordinateType;
		this.maximum = maximumValue;
	}

	public Playable buildSound(Fault fault)
	{
		List<Note> noteList = new ArrayList<>();
		for (Coordinates coordinates : fault.coordinates())
		{
			noteList.add(buildCoordinateRelatedNote(getCoordinate(coordinates)));
		}
		return new Chord(50, noteList.toArray(new Note[noteList.size()]));
	}

	private double getCoordinate(Coordinates coordinates)
	{
		if (CoordinateType.Z.equals(coordinateType))
		{
			return coordinates.getZ();
		}

		if (CoordinateType.X.equals(coordinateType))
		{
			return coordinates.getX();
		}

		if (CoordinateType.Y.equals(coordinateType))
		{
			return coordinates.getY();
		}
		return coordinates.getZ();
	}

	private Note buildCoordinateRelatedNote(double coordinate)
	{
		int roundedHeight = (int) Math.round(coordinate);
		int noteNameLength = NoteName.values().length;

		int mappedIndex = mapToRange(roundedHeight, maximum, noteNameLength * 2);
		int noteIndex = mappedIndex % noteNameLength;
		int octaveIndex = 4 + mappedIndex / noteNameLength;

		return new Note(NoteName.values()[noteIndex], octaveIndex);
	}

	private int mapToRange(int value, double maximumOfOldRange, int maximumOfNewRange)
	{
		double proportion = value / maximumOfOldRange;
		return (int) (maximumOfNewRange * proportion);
	}

	enum CoordinateType
	{
		X, Y, Z;
	}
}
