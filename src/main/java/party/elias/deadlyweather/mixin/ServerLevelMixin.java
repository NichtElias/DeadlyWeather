package party.elias.deadlyweather.mixin;

import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import party.elias.deadlyweather.WeatherSettings;
import party.elias.deadlyweather.WeatherSettingsSD;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @ModifyArg(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"), index = 0)
    private int modifyLightningChance(int i) {
        WeatherSettingsSD settings = WeatherSettingsSD.from((ServerLevel) (Object) this);
        if (settings.get(WeatherSettings.BoolSettings.THUNDER_ENABLE)) {
            return settings.get(WeatherSettings.IntSettings.THUNDER_CHANCE);
        }
        return i;
    }
}
