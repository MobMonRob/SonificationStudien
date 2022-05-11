package sound;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import controlling.Controller;
import controlling.Player;
import detection.Fault;
import marker.Coordinates;
import marker.Marker;
import musical.Chord;
import musical.Note;
import musical.NoteName;
import musical.Playable;

public class CoordinateRelatedSoundFactory implements SoundFactory
{
	private final CoordinateType coordinateType;
	private final double maximum;
	private Controller audioController;
	private Player player;
	private Fault currentFault;
	private Playable currentPlayable;

	public CoordinateRelatedSoundFactory(CoordinateType coordinateType, double maximumValue)
	{
		this.coordinateType = coordinateType;
		this.maximum = maximumValue;

		audioController = new Controller();
		player = audioController.getPlayer();
	}

	public Playable playSound(Fault fault)
	{
		if (currentFault.equals(fault))
		{
			return null;
		}

		Playable playable = buildPlayable(fault);
		playAndHold(playable);
		return playable;
	}

	public Playable buildPlayable(Fault fault)
	{
		List<Note> noteList = new ArrayList<>();
		List<Coordinates> coordinatesList = getCoordinatesList(fault.markers());
		for (Coordinates coordinates : coordinatesList)
		{
			noteList.add(buildCoordinateRelatedNote(getCoordinate(coordinates)));
		}
		Note[] notes = noteList.toArray(new Note[noteList.size()]);

		return new Chord(50, notes);
	}

	@Override
	public void stopSound()
	{
		stopHoldingNotes();
	}

	private void playAndHold(Playable playable)
	{
		if (playable == currentPlayable)
		{
			return;
		}

		if (currentPlayable != null)
		{
			stopHoldingNotes();
		}

		for (Note note : playable.getNotes())
		{
			player.noteOn(note);
		}
		currentPlayable = playable;
	}

	private void stopHoldingNotes()
	{
		if (currentPlayable == null)
		{
			return;
		}

		for (Note note : currentPlayable.getNotes())
		{
			player.noteOff(note);
		}
		currentPlayable = null;
	}

	private List<Coordinates> getCoordinatesList(List<Marker> notTrackableMarkers)
	{
		return notTrackableMarkers.stream().flatMap(marker -> Stream.of(marker.coordinates()))
				.toList();
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

	public enum CoordinateType
	{
		X, Y, Z;
	}
}
