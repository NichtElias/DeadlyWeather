package party.elias.deadlyweather;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.neoforged.neoforge.network.PacketDistributor;

public class WeatherSettingsSD extends SavedData {

    public static final SavedDataType<WeatherSettingsSD> TYPE = new SavedDataType<>(
            "deadlyweather_settings",
            WeatherSettingsSD::new,
            WeatherSettings.CODEC.xmap(WeatherSettingsSD::new, WeatherSettingsSD::getSettings)
    );

    private final WeatherSettings settings;

    public WeatherSettingsSD() {
        settings = WeatherSettings.fromConfig();
    }

    public WeatherSettingsSD(WeatherSettings weatherSettings) {
        settings = weatherSettings;
    }

    public static WeatherSettingsSD from(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    @Override
    public void setDirty() {
        super.setDirty();

        PacketDistributor.sendToAllPlayers(settings);
    }

    public WeatherSettings getSettings() {
        return settings;
    }

    public boolean get(WeatherSettings.BoolSettings key) {
        return settings.bools().get(key);
    }

    public int get(WeatherSettings.IntSettings key) {
        return settings.ints().get(key);
    }

    public double get(WeatherSettings.DoubleSettings key) {
        return settings.doubles().get(key);
    }

    public void set(WeatherSettings.BoolSettings key, boolean value) {
        settings.bools().put(key, value);
    }

    public void set(WeatherSettings.IntSettings key, int value) {
        settings.ints().put(key, value);
    }

    public void set(WeatherSettings.DoubleSettings key, double value) {
        settings.doubles().put(key, value);
    }
}
