package party.elias.deadlyweather;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import java.util.HashMap;

public record WeatherSettings (HashMap<BoolSettings, Boolean> bools, HashMap<IntSettings, Integer> ints,
                               HashMap<DoubleSettings, Double> doubles) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<WeatherSettings> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DeadlyWeather.MODID, "weather_settings"));

    public static final StreamCodec<ByteBuf, WeatherSettings> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    HashMap<BoolSettings, Boolean>::new,
                    ByteBufCodecs.fromCodec(StringRepresentable.fromEnum(BoolSettings::values)),
                    ByteBufCodecs.BOOL
            ),
            WeatherSettings::bools,
            ByteBufCodecs.map(
                    HashMap<IntSettings, Integer>::new,
                    ByteBufCodecs.fromCodec(StringRepresentable.fromEnum(IntSettings::values)),
                    ByteBufCodecs.INT
            ),
            WeatherSettings::ints,
            ByteBufCodecs.map(
                    HashMap<DoubleSettings, Double>::new,
                    ByteBufCodecs.fromCodec(StringRepresentable.fromEnum(DoubleSettings::values)),
                    ByteBufCodecs.DOUBLE
            ),
            WeatherSettings::doubles,
            WeatherSettings::new
    );

    public static WeatherSettings fromConfig() {
        WeatherSettings settings = new WeatherSettings(new HashMap<>(), new HashMap<>(), new HashMap<>());
        settings.initFromConfig();
        return settings;
    }

    private void initFromConfig() {
        bools.put(WeatherSettings.BoolSettings.SUNNY_ENABLE, Config.Sunny.enable);
        bools.put(WeatherSettings.BoolSettings.THUNDER_ENABLE, Config.Thunder.enable);
        bools.put(WeatherSettings.BoolSettings.THUNDER_PLAYER_SEEKING_ENABLE, Config.Thunder.PlayerSeeking.enable);
        bools.put(WeatherSettings.BoolSettings.SNOWY_ENABLE, Config.Snowy.enable);
        bools.put(WeatherSettings.BoolSettings.RAINY_ENABLE, Config.Rainy.enable);

        ints.put(WeatherSettings.IntSettings.SUNNY_DAMAGE_INTERVAL, Config.Sunny.damageInterval);
        ints.put(WeatherSettings.IntSettings.THUNDER_CHANCE, Config.Thunder.chance);
        ints.put(WeatherSettings.IntSettings.THUNDER_PLAYER_SEEKING_INTERVAL, Config.Thunder.PlayerSeeking.interval);
        ints.put(WeatherSettings.IntSettings.RAINY_DAMAGE_INTERVAL, Config.Rainy.damageInterval);

        doubles.put(WeatherSettings.DoubleSettings.SUNNY_DAMAGE, Config.Sunny.damage);
        doubles.put(WeatherSettings.DoubleSettings.RAINY_DAMAGE, Config.Rainy.damage);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum BoolSettings implements StringRepresentable {
        SUNNY_ENABLE,
        THUNDER_ENABLE,
        THUNDER_PLAYER_SEEKING_ENABLE,
        SNOWY_ENABLE,
        RAINY_ENABLE;

        @Override
        public String getSerializedName() {
            return name();
        }
    }

    public enum IntSettings implements StringRepresentable {
        SUNNY_DAMAGE_INTERVAL,
        THUNDER_CHANCE,
        THUNDER_PLAYER_SEEKING_INTERVAL,
        RAINY_DAMAGE_INTERVAL;

        @Override
        public String getSerializedName() {
            return name();
        }
    }

    public enum DoubleSettings implements StringRepresentable {
        SUNNY_DAMAGE,
        RAINY_DAMAGE;

        @Override
        public String getSerializedName() {
            return name();
        }
    }
}
