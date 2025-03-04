package party.elias.deadlyweather.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import party.elias.deadlyweather.Utils;
import party.elias.deadlyweather.WeatherSettings;
import party.elias.deadlyweather.WeatherSettingsSD;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @WrapOperation(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;isInPowderSnow:Z"))
    private boolean wrapIsInPowderSnow(LivingEntity instance, Operation<Boolean> original) {
        if (instance instanceof ServerPlayer) { // only gets called on the server, so this is a player check, not a side check
            if (WeatherSettingsSD.from((ServerLevel) instance.level()).get(WeatherSettings.BoolSettings.SNOWY_ENABLE)) {
                return original.call(instance) || Utils.shouldFreeze(instance);
            }
        }
        return original.call(instance);
    }

    @WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canFreeze()Z"))
    private boolean wrapCanFreeze(LivingEntity instance, Operation<Boolean> original) {
        if (instance instanceof ServerPlayer) { // only gets called on the server, so this is a player check, not a side check
            if (WeatherSettingsSD.from((ServerLevel) instance.level()).get(WeatherSettings.BoolSettings.SNOWY_ENABLE)) {
                return original.call(instance) || (Utils.shouldFreeze(instance) && !Utils.isVeryFrostResistant(instance));
            }
        }
        return original.call(instance);
    }

}
