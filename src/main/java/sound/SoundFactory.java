package sound;

import detection.Fault;
import musical.Playable;

public interface SoundFactory
{
	public Playable buildSound(Fault fault);
}
