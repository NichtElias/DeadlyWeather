package party.elias.deadlyweather;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.network.PacketDistributor;

public class WeatherSettingsSD extends SavedData {

    private static final Factory<WeatherSettingsSD> FACTORY = new Factory<>(WeatherSettingsSD::new, WeatherSettingsSD::load);
    private static final String FILENAME = "deadlyweather_settings";

    private final WeatherSettings settings;

    public WeatherSettingsSD() {
        settings = WeatherSettings.fromConfig();
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {

        CompoundTag boolSettingsTag = new CompoundTag();
        for (WeatherSettings.BoolSettings key: settings.bools().keySet()) {
            boolSettingsTag.put(key.toString(), ByteTag.valueOf(settings.bools().get(key)));
        }

        compoundTag.put("boolSettings", boolSettingsTag);

        CompoundTag intSettingsTag = new CompoundTag();
        for (WeatherSettings.IntSettings key: settings.ints().keySet()) {
            intSettingsTag.put(key.toString(), IntTag.valueOf(settings.ints().get(key)));
        }

        compoundTag.put("intSettings", intSettingsTag);

        CompoundTag doubleSettingsTag = new CompoundTag();
        for (WeatherSettings.DoubleSettings key: settings.doubles().keySet()) {
            doubleSettingsTag.put(key.toString(), DoubleTag.valueOf(settings.doubles().get(key)));
        }

        compoundTag.put("doubleSettings", doubleSettingsTag);

        return compoundTag;
    }

    private static WeatherSettingsSD load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        WeatherSettingsSD sd = new WeatherSettingsSD();

        CompoundTag boolSettingsTag = tag.getCompound("boolSettings");
        for (String key: boolSettingsTag.getAllKeys()) {
            sd.settings.bools().put(WeatherSettings.BoolSettings.valueOf(key), boolSettingsTag.getByte(key) != 0);
        }

        CompoundTag intSettingsTag = tag.getCompound("intSettings");
        for (String key: intSettingsTag.getAllKeys()) {
            sd.settings.ints().put(WeatherSettings.IntSettings.valueOf(key), intSettingsTag.getInt(key));
        }

        CompoundTag doubleSettingsTag = tag.getCompound("doubleSettings");
        for (String key: doubleSettingsTag.getAllKeys()) {
            sd.settings.doubles().put(WeatherSettings.DoubleSettings.valueOf(key), doubleSettingsTag.getDouble(key));
        }

        return sd;
    }

    public static WeatherSettingsSD from(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(FACTORY, FILENAME);
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
