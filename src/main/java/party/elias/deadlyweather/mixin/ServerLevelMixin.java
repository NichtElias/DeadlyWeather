package party.elias.deadlyweather.mixin;

import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import party.elias.deadlyweather.Config;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @ModifyArg(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"), index = 0)
    private int modifyLightningChance(int i) {
        if (Config.Thunder.enable) {
            return Config.Thunder.chance;
        }
        return i;
    }
}
