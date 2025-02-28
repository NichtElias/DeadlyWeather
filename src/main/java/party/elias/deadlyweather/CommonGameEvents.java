package party.elias.deadlyweather;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = DeadlyWeather.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CommonGameEvents {

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {

        Entity entity = event.getEntity();
        BlockPos blockPos = BlockPos.containing(entity.getPosition(1));
        RegistryAccess registryAccess = entity.registryAccess();

        if (entity.level() instanceof ServerLevel level && entity instanceof ServerPlayer player) {
            WeatherSettingsSD settings = WeatherSettingsSD.from(level);

            if (settings.getBool(WeatherSettingsSD.BoolSettings.SUNNY_ENABLE)) {
                if (level.getGameTime() % settings.getInt(WeatherSettingsSD.IntSettings.SUNNY_DAMAGE_INTERVAL) == 0
                        && !level.isRaining() && level.isDay() && level.canSeeSky(Utils.getRelevantBlockPos(player)))
                {
                    player.hurt(new DamageSource(registryAccess.holderOrThrow(DamageTypes.IN_FIRE)), (float) settings.getDouble(WeatherSettingsSD.DoubleSettings.SUNNY_DAMAGE));
                }
            }

            if (settings.getBool(WeatherSettingsSD.BoolSettings.THUNDER_ENABLE)
                    && settings.getBool(WeatherSettingsSD.BoolSettings.THUNDER_PLAYER_SEEKING_ENABLE)) {
                if (level.getGameTime() % settings.getInt(WeatherSettingsSD.IntSettings.THUNDER_PLAYER_SEEKING_INTERVAL) == 0
                        && level.isThundering() && level.canSeeSky(Utils.getRelevantBlockPos(player)))
                {
                    Utils.strikeLightningAt(level, Utils.getRelevantBlockPos(player));
                }
            }

            if (settings.getBool(WeatherSettingsSD.BoolSettings.RAINY_ENABLE)) {
                if (level.getGameTime() % settings.getInt(WeatherSettingsSD.IntSettings.RAINY_DAMAGE_INTERVAL) == 0
                        && level.isRaining() && level.getBiome(blockPos).value().warmEnoughToRain(blockPos)
                        && level.canSeeSky(Utils.getRelevantBlockPos(player)))
                {
                    player.hurt(new DamageSource(registryAccess.holderOrThrow(DeadlyWeather.ACID_DAMAGE_KEY)), (float) settings.getDouble(WeatherSettingsSD.DoubleSettings.RAINY_DAMAGE));
                }
            }
        }

    }


    @SubscribeEvent
    public static void onRegisterCommandsEvent(RegisterCommandsEvent event) {
        DeadlyWeatherCommand.register(event.getDispatcher());
    }

}
