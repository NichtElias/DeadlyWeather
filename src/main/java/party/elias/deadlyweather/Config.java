package party.elias.deadlyweather;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = DeadlyWeather.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final String TL_KEY = DeadlyWeather.MODID + ".configuration.";

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue CLEAR_ENABLE;

    static final ModConfigSpec SPEC;

    static {
        BUILDER.comment("Config for clear weather").push("clear");

        CLEAR_ENABLE = BUILDER.comment("Enable deadly weather for clear weather")
                .translation(TL_KEY + "clear.enable")
                .define("enable", true);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static class Clear {
        public static boolean enable;
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        Clear.enable = CLEAR_ENABLE.get();
    }
}
