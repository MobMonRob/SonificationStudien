package sound;

import java.util.ArrayList;
import java.util.List;

import controlling.Controller;
import controlling.Player;
import detection.Fault;
import musical.Melody;
import musical.Note;
import musical.NoteName;
import musical.Playable;
import synthesis.OscillatorType;

public class CountRelatedSoundFactory implements SoundFactory
{
	private static final int DEFAULT_OCTAVE = 4;
	private static final int SEMITONES_PER_OCTAVE = 12;
	private static final int SEMITONE_STEPS = 5;
	private static final int MAX_OCTAVE_COUNT = 8;

	private int maxCount;
	private List<Playable> prebuildPlayables = new ArrayList<>();
	private Controller audioController;
	private Player player;
	private Fault currentFault;
	private Playable currentPlayable;

	public CountRelatedSoundFactory(int maxCount)
	{
		this.maxCount = maxCount;
		initPlayables();

		audioController = new Controller();
		audioController.setOscillatorType(OscillatorType.TRIANGLE);
		audioController.applyPreset(SoundPresets.countRelatedPreset());
		player = audioController.getPlayer();
	}

	@Override
	public Playable playSound(Fault fault)
	{
		int count = fault.markers().size();
		if (currentFault.markers().size() == count)
		{
			return null;
		}

		Playable playable = prebuildPlayables.get(count - 1 % maxCount);
		playAndHold(playable);
		return playable;
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

	private void initPlayables()
	{
		for (int i = 0; i <= maxCount; i++)
		{
			prebuildPlayables.add(buildPlayable(i));
		}
	}

	private Playable buildPlayable(int count)
	{
		List<Note> notes = new ArrayList<>();
		for (int i = 0; i <= count; i++)
		{
			int globalNoteIndex = count + (i * SEMITONE_STEPS);
			int noteIndex = globalNoteIndex % SEMITONES_PER_OCTAVE;
			int octave = DEFAULT_OCTAVE + globalNoteIndex / SEMITONES_PER_OCTAVE;
			notes.add(buildNote(noteIndex, octave));

		}
		Note[] noteArray = notes.toArray(new Note[count]);

		return new Melody(50, noteArray);
	}

	private Note buildNote(int noteIndex, int octave)
	{
		int checkedIndex = noteIndex % SEMITONES_PER_OCTAVE;
		NoteName noteName = NoteName.values()[checkedIndex];
		int checkedOctave = octave % MAX_OCTAVE_COUNT;

		return new Note(noteName, checkedOctave);
	}
}
