package detection;

import java.util.ArrayList;
import java.util.List;

import marker.Coordinates;
import musical.Chord;
import musical.Note;
import musical.NoteName;
import musical.Playable;

public class SoundFactory
{
	private static final double MAXIMUM_HEIGHT = 2500;

	public static Playable buildErrorSound(Fault fault)
	{
		List<Note> noteList = new ArrayList<>();
		for (Coordinates coordinates : fault.coordinates())
		{
			noteList.add(buildHeightRelatedNote(coordinates.getZ()));
		}
		return new Chord(50, noteList.toArray(new Note[noteList.size()]));
	}

	private static Note buildHeightRelatedNote(double height)
	{
		int roundedHeight = (int) Math.round(height);
		int maximumOfNewRange = NoteName.values().length;

		int mappedIndex = mapToRange(roundedHeight, MAXIMUM_HEIGHT, maximumOfNewRange * 2);
		int noteIndex = mappedIndex % maximumOfNewRange;
		int octaveIndex = 4 + mappedIndex / maximumOfNewRange;

		return new Note(NoteName.values()[noteIndex], octaveIndex);
	}

	private static int mapToRange(int value, double maximumOfOldRange, int maximumOfNewRange)
	{
		double proportion = value / maximumOfOldRange;
		return (int) (maximumOfNewRange * proportion);
	}
}
