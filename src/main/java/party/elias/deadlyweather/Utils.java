package party.elias.deadlyweather;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class Utils {
    public static LightningBolt strikeLightningAt(ServerLevel level, BlockPos blockPos) {
        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
        if (lightningBolt != null) {
            lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
            level.addFreshEntity(lightningBolt);
        }
        return lightningBolt;
    }

    public static BlockPos getRelevantBlockPos(LivingEntity entity) {
        return BlockPos.containing(entity.getHitbox().getBottomCenter());
    }

    public static boolean shouldFreeze(LivingEntity entity) {
        Level level = entity.level();
        BlockPos blockPos = getRelevantBlockPos(entity);
        return level.isRaining() && !level.getBiome(blockPos).value().warmEnoughToRain(blockPos)
                && level.canSeeSky(blockPos) && level.getBrightness(LightLayer.BLOCK, blockPos) < 12;
    }

    public static boolean isVeryFrostResistant(LivingEntity entity) {
        if (entity.isSpectator()) {
            return true;
        }
        if (entity.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
            return true;
        }
        return entity.getItemBySlot(EquipmentSlot.BODY).is(ItemTags.FREEZE_IMMUNE_WEARABLES)
                || (entity.getItemBySlot(EquipmentSlot.HEAD).is(ItemTags.FREEZE_IMMUNE_WEARABLES)
                && entity.getItemBySlot(EquipmentSlot.CHEST).is(ItemTags.FREEZE_IMMUNE_WEARABLES)
                && entity.getItemBySlot(EquipmentSlot.LEGS).is(ItemTags.FREEZE_IMMUNE_WEARABLES)
                && entity.getItemBySlot(EquipmentSlot.FEET).is(ItemTags.FREEZE_IMMUNE_WEARABLES));
    }
}
