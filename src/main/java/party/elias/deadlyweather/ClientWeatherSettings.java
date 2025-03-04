package party.elias.deadlyweather;

import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientWeatherSettings {
    private static WeatherSettings settings = WeatherSettings.fromConfig();

    public static boolean get(WeatherSettings.BoolSettings key) {
        return settings.bools().get(key);
    }

    public static int get(WeatherSettings.IntSettings key) {
        return settings.ints().get(key);
    }

    public static double get(WeatherSettings.DoubleSettings key) {
        return settings.doubles().get(key);
    }

    public static void handleWeatherSettingsPayload(WeatherSettings settingsPayload, IPayloadContext context) {
        settings = settingsPayload;
    }
}
