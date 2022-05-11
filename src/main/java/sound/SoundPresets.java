package sound;

import configuration.EnvelopeConfiguration;
import configuration.FilterConfiguration;
import configuration.Preset;

public class SoundPresets
{
	private SoundPresets()
	{
		// Hide public constructor
	}

	public static Preset countRelatedPreset()
	{
		EnvelopeConfiguration envConfig = new EnvelopeConfiguration(0, 0.1, 0.1, 0.1, 0.7);
		FilterConfiguration filterConfig = new FilterConfiguration(500, 0, 1.0);
		return new Preset(filterConfig, envConfig);
	}

	public static Preset coordinateRelatedPreset()
	{
		EnvelopeConfiguration envConfig = new EnvelopeConfiguration(0, 0.1, 0.1, 0.1, 0.1);
		FilterConfiguration filterConfig = new FilterConfiguration(1000, 0, 1.0);
		return new Preset(filterConfig, envConfig);
	}
}
