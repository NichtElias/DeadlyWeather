package party.elias.deadlyweather;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;

public class WeatherSettingsSD extends SavedData {

    private static final Factory<WeatherSettingsSD> FACTORY = new Factory<>(WeatherSettingsSD::new, WeatherSettingsSD::load);
    private static final String FILENAME = "deadlyweather_settings";

    private final HashMap<BoolSettings, Boolean> boolSettings;
    private final HashMap<IntSettings, Integer> intSettings;
    private final HashMap<DoubleSettings, Double> doubleSettings;

    public WeatherSettingsSD() {
        this.boolSettings = new HashMap<>();
        this.intSettings = new HashMap<>();
        this.doubleSettings = new HashMap<>();

        initFromConfig();
    }

    public enum BoolSettings {
        SUNNY_ENABLE,
        THUNDER_ENABLE,
        THUNDER_PLAYER_SEEKING_ENABLE,
        SNOWY_ENABLE,
        RAINY_ENABLE
    }

    public enum IntSettings {
        SUNNY_DAMAGE_INTERVAL,
        THUNDER_CHANCE,
        THUNDER_PLAYER_SEEKING_INTERVAL,
        RAINY_DAMAGE_INTERVAL
    }

    public enum DoubleSettings {
        SUNNY_DAMAGE,
        RAINY_DAMAGE
    }

    private void initFromConfig() {
        boolSettings.put(BoolSettings.SUNNY_ENABLE, Config.Sunny.enable);
        boolSettings.put(BoolSettings.THUNDER_ENABLE, Config.Thunder.enable);
        boolSettings.put(BoolSettings.THUNDER_PLAYER_SEEKING_ENABLE, Config.Thunder.PlayerSeeking.enable);
        boolSettings.put(BoolSettings.SNOWY_ENABLE, Config.Snowy.enable);
        boolSettings.put(BoolSettings.RAINY_ENABLE, Config.Rainy.enable);

        intSettings.put(IntSettings.SUNNY_DAMAGE_INTERVAL, Config.Sunny.damageInterval);
        intSettings.put(IntSettings.THUNDER_CHANCE, Config.Thunder.chance);
        intSettings.put(IntSettings.THUNDER_PLAYER_SEEKING_INTERVAL, Config.Thunder.PlayerSeeking.interval);
        intSettings.put(IntSettings.RAINY_DAMAGE_INTERVAL, Config.Rainy.damageInterval);

        doubleSettings.put(DoubleSettings.SUNNY_DAMAGE, Config.Sunny.damage);
        doubleSettings.put(DoubleSettings.RAINY_DAMAGE, Config.Rainy.damage);
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {

        CompoundTag boolSettingsTag = new CompoundTag();
        for (BoolSettings key: boolSettings.keySet()) {
            boolSettingsTag.put(key.toString(), ByteTag.valueOf(boolSettings.get(key)));
        }

        compoundTag.put("boolSettings", boolSettingsTag);

        CompoundTag intSettingsTag = new CompoundTag();
        for (IntSettings key: intSettings.keySet()) {
            intSettingsTag.put(key.toString(), IntTag.valueOf(intSettings.get(key)));
        }

        compoundTag.put("intSettings", intSettingsTag);

        CompoundTag doubleSettingsTag = new CompoundTag();
        for (DoubleSettings key: doubleSettings.keySet()) {
            doubleSettingsTag.put(key.toString(), DoubleTag.valueOf(doubleSettings.get(key)));
        }

        compoundTag.put("doubleSettings", doubleSettingsTag);

        return compoundTag;
    }

    private static WeatherSettingsSD load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        WeatherSettingsSD sd = new WeatherSettingsSD();

        CompoundTag boolSettingsTag = tag.getCompound("boolSettings");
        for (String key: boolSettingsTag.getAllKeys()) {
            sd.boolSettings.put(BoolSettings.valueOf(key), boolSettingsTag.getByte(key) != 0);
        }

        CompoundTag intSettingsTag = tag.getCompound("intSettings");
        for (String key: intSettingsTag.getAllKeys()) {
            sd.intSettings.put(IntSettings.valueOf(key), intSettingsTag.getInt(key));
        }

        CompoundTag doubleSettingsTag = tag.getCompound("doubleSettings");
        for (String key: doubleSettingsTag.getAllKeys()) {
            sd.doubleSettings.put(DoubleSettings.valueOf(key), doubleSettingsTag.getDouble(key));
        }

        return sd;
    }

    public static WeatherSettingsSD from(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(FACTORY, FILENAME);
    }

    public boolean getBool(BoolSettings key) {
        return boolSettings.get(key);
    }

    public int getInt(IntSettings key) {
        return intSettings.get(key);
    }

    public double getDouble(DoubleSettings key) {
        return doubleSettings.get(key);
    }

    public void setBool(BoolSettings key, boolean value) {
        boolSettings.put(key, value);
    }

    public void setInt(IntSettings key, int value) {
        intSettings.put(key, value);
    }

    public void setDouble(DoubleSettings key, double value) {
        doubleSettings.put(key, value);
    }
}
