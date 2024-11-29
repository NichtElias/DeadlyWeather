package party.elias.deadlyweather;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = DeadlyWeather.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final String TL_KEY = DeadlyWeather.MODID + ".configuration.";

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue SUNNY_ENABLE;
    private static final ModConfigSpec.DoubleValue SUNNY_DAMAGE;
    private static final ModConfigSpec.IntValue SUNNY_DAMAGE_INTERVAL;

    private static final ModConfigSpec.BooleanValue THUNDER_ENABLE;
    private static final ModConfigSpec.IntValue THUNDER_CHANCE;

    private static final ModConfigSpec.BooleanValue THUNDER_PLAYER_SEEKING_ENABLE;
    private static final ModConfigSpec.IntValue THUNDER_PLAYER_SEEKING_INTERVAL;

    private static final ModConfigSpec.BooleanValue SNOWY_ENABLE;

    static final ModConfigSpec SPEC;

    static {
        BUILDER.push("sunny");

        SUNNY_ENABLE = BUILDER.comment("Enable deadly weather for sunny weather")
                .translation(TL_KEY + "sunny.enable")
                .define("enable", true);
        SUNNY_DAMAGE = BUILDER.comment("Damage dealt when exposed to sunlight")
                .translation(TL_KEY + "sunny.damage")
                .defineInRange("damage", 1, 0, Double.MAX_VALUE);
        SUNNY_DAMAGE_INTERVAL = BUILDER.comment("Time in ticks between sun damage")
                .translation(TL_KEY + "sunny.damage_interval")
                .defineInRange("damageInterval", 20, 1, Integer.MAX_VALUE);

        BUILDER.pop();

        BUILDER.push("thunder");

        THUNDER_ENABLE = BUILDER.comment("Enable deadly weather for thunderstorms")
                .translation(TL_KEY + "thunder.enable")
                .define("enable", true);
        THUNDER_CHANCE = BUILDER.comment("Chance for a lightning strike to spawn for each chunk each tick is given by 1/chance")
                .translation(TL_KEY + "thunder.chance")
                .defineInRange("chance", 2000, 1, Integer.MAX_VALUE);

        BUILDER.push("playerSeeking");

        THUNDER_PLAYER_SEEKING_ENABLE = BUILDER.comment("Enable spawning extra lightning aimed at players")
                .translation(TL_KEY + "thunder.player_seeking.enable")
                .define("enable", true);
        THUNDER_PLAYER_SEEKING_INTERVAL = BUILDER.comment("Interval between player seeking lightning strikes in ticks")
                .translation(TL_KEY + "thunder.player_seeking.interval")
                .defineInRange("interval", 200, 1, Integer.MAX_VALUE);

        BUILDER.pop();

        BUILDER.pop();

        BUILDER.push("snowy");

        SNOWY_ENABLE = BUILDER.comment("Enable deadly weather for snowy weather")
                .translation(TL_KEY + "snowy.enable")
                .define("enable", true);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static class Sunny {
        public static boolean enable;
        public static double damage;
        public static int damageInterval;
    }

    public static class Thunder {
        public static boolean enable;
        public static int chance;

        public static class PlayerSeeking {
            public static boolean enable;
            public static int interval;
        }
    }

    public static class Snowy {
        public static boolean enable;
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        Sunny.enable = SUNNY_ENABLE.get();
        Sunny.damage = SUNNY_DAMAGE.get();
        Sunny.damageInterval = SUNNY_DAMAGE_INTERVAL.get();

        Thunder.enable = THUNDER_ENABLE.get();
        Thunder.chance = THUNDER_CHANCE.get();

        Thunder.PlayerSeeking.enable = THUNDER_PLAYER_SEEKING_ENABLE.get();
        Thunder.PlayerSeeking.interval = THUNDER_PLAYER_SEEKING_INTERVAL.get();

        Snowy.enable = SNOWY_ENABLE.get();
    }
}
