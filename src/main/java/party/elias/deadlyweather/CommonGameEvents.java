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
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = DeadlyWeather.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CommonGameEvents {

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {

        Entity entity = event.getEntity();
        BlockPos blockPos = BlockPos.containing(entity.getPosition(1));
        RegistryAccess registryAccess = entity.registryAccess();

        if (entity.level() instanceof ServerLevel level && entity instanceof ServerPlayer player) {
            if (Config.Sunny.enable) {
                if (level.getGameTime() % Config.Sunny.damageInterval == 0
                        && !level.isRaining() && level.isDay() && level.canSeeSky(Utils.getRelevantBlockPos(player)))
                {
                    player.hurt(new DamageSource(registryAccess.holderOrThrow(DamageTypes.IN_FIRE)), (float) Config.Sunny.damage);
                }
            }

            if (Config.Thunder.enable && Config.Thunder.PlayerSeeking.enable) {
                if (level.getGameTime() % Config.Thunder.PlayerSeeking.interval == 0
                        && level.isThundering() && level.canSeeSky(Utils.getRelevantBlockPos(player)))
                {
                    Utils.strikeLightningAt(level, Utils.getRelevantBlockPos(player));
                }
            }
        }

    }
}
