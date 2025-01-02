package party.elias.deadlyweather.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import party.elias.deadlyweather.Config;
import party.elias.deadlyweather.DeadlyWeather;

@Mixin(WeatherEffectRenderer.class)
public class WeatherEffectRendererMixin {

    private static final ResourceLocation RAIN_LOCATION = ResourceLocation.withDefaultNamespace("textures/environment/rain.png");
    private static final ResourceLocation ACID_RAIN_LOCATION = ResourceLocation.fromNamespaceAndPath(DeadlyWeather.MODID, "textures/environment/acid_rain.png");

    @ModifyArg(method = "render(Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/phys/Vec3;IFLjava/util/List;Ljava/util/List;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;weather(Lnet/minecraft/resources/ResourceLocation;Z)Lnet/minecraft/client/renderer/RenderType;"), index = 0)
    private ResourceLocation modifyShaderTexture(ResourceLocation resourceLocation) {
        if (Config.Rainy.enable && resourceLocation.equals(RAIN_LOCATION)) {
            return ACID_RAIN_LOCATION;
        }
        return resourceLocation;
    }

    @WrapOperation(method = "tickRainParticles", at = @At(value = "FIELD", target = "Lnet/minecraft/core/particles/ParticleTypes;RAIN:Lnet/minecraft/core/particles/SimpleParticleType;"))
    private SimpleParticleType wrapParticleTypesRain(Operation<SimpleParticleType> original) {
        if (Config.Rainy.enable) {
            return DeadlyWeather.ACID_RAIN_PARTICLE.get();
        }
        return ParticleTypes.RAIN;
    }

}
