package sound;

import java.util.ArrayList;
import java.util.List;

import detection.Fault;
import musical.Chord;
import musical.Note;
import musical.NoteName;
import musical.Playable;

public class CountRelatedSoundFactory implements SoundFactory
{
	private static final int SEMITONES_PER_OCTAVE = 12;
	private static final int SEMITONE_STEPS = 5;
	private static final int MAX_OCTAVE_COUNT = 8;
	private int maxCount;
	private List<Playable> prebuildPlayables = new ArrayList<>();

	public CountRelatedSoundFactory(int maxCount)
	{
		this.maxCount = maxCount;
		initPlayables();
	}

	@Override
	public Playable buildSound(Fault fault)
	{
		List<Note> noteList = new ArrayList<>();
		for (int i = 0; i < fault.coordinates().size(); i++)
		{

		}

		Note[] notes = noteList.toArray(new Note[noteList.size()]);

		return new Chord(50, notes);
	}

	private void initPlayables()
	{
		for (int i = 0; i < maxCount; i++)
		{
			prebuildPlayables.add(buildPlayable(i));
		}
	}

	private Playable buildPlayable(int count)
	{
		List<Note> notes = new ArrayList<>();
		for (int i = 0; i < count; i++)
		{
			int globalNoteIndex = 0 + (i * SEMITONE_STEPS);
			int noteIndex = globalNoteIndex % SEMITONES_PER_OCTAVE;
			int octave = globalNoteIndex / SEMITONES_PER_OCTAVE;
			notes.add(buildNote(noteIndex, octave));

		}
		Note[] noteArray = notes.toArray(new Note[count]);

		return new Chord(50, noteArray);
	}

	private Note buildNote(int noteIndex, int octave)
	{
		int checkedIndex = noteIndex % SEMITONES_PER_OCTAVE;
		NoteName noteName = NoteName.values()[checkedIndex];
		int checkedOctave = octave % MAX_OCTAVE_COUNT;

		return new Note(noteName, checkedOctave);
	}
}
