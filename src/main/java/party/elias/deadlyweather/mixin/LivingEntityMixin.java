package party.elias.deadlyweather.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import party.elias.deadlyweather.Config;
import party.elias.deadlyweather.Utils;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @WrapOperation(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;isInPowderSnow:Z"))
    private boolean wrapIsInPowderSnow(LivingEntity instance, Operation<Boolean> original) {
        if (Config.Snowy.enable && instance instanceof Player) {
            return original.call(instance) || Utils.shouldFreeze(instance);
        }
        return original.call(instance);
    }

    @WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canFreeze()Z"))
    private boolean wrapCanFreeze(LivingEntity instance, Operation<Boolean> original) {
        if (Config.Snowy.enable && instance instanceof Player) {
            return original.call(instance) || (Utils.shouldFreeze(instance) && !Utils.isVeryFrostResistant(instance));
        }
        return original.call(instance);
    }

}
