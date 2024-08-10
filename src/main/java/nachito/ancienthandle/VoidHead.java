package nachito.ancienthandle;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoidHead extends FlyingEntity implements Monster {
    private int atkCd = 0;
    private static final TrackedData<Integer> stareTime = DataTracker.registerData(VoidHead.class, TrackedDataHandlerRegistry.INTEGER);
    private int ticksChange = 0;
    private int ticksSet = random.nextBetween(10, 20);

    protected VoidHead(EntityType<? extends FlyingEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1F)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 100F)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1000F);
    }

    public void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(stareTime, 0);
    }


    boolean isLooking(PlayerEntity user) {
        Vec3d startPos = user.getEyePos();

        Vec3d direction = user.getRotationVec(1.0f);

        Vec3d endPos = startPos.add(direction.multiply(1000));

        EntityHitResult result = ProjectileUtil.raycast(user, startPos, endPos,
                user.getBoundingBox().stretch(direction.multiply(1000)).expand(1.0, 1.0, 1.0),
                (entity) -> true, 1000);

        if (result != null) {
            Entity hitEntity = result.getEntity();
            return hitEntity == this;
        }
        return false;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {

        super.initialize(world, difficulty, spawnReason, entityData);
        return entityData;
    }



    @Override
    public void tick() {

        World world = this.getWorld();

        if (!world.isClient) {
            atkCd++;
            ticksChange++;
        }

        DamageSource damageSource = new DamageSource(
                world.getRegistryManager()
                        .get(RegistryKeys.DAMAGE_TYPE)
                        .entryOf(AncientHandleInit.NACHITO_DAMAGE));

        List<? extends PlayerEntity> players = world.getPlayers();

        for (PlayerEntity player : players) {
            if (player.isDead() && !world.isClient && player.distanceTo(this) < 50) {
                this.kill();
            }
        }

            for (PlayerEntity player : players) {
                if (player != null && this.distanceTo(player) < 30) {
                    if (isLooking(player)) {
                        this.dataTracker.set(stareTime, this.dataTracker.get(stareTime) + 1);
                        if (!this.isDead()) {
                            player.playSoundToPlayer(SoundEvents.ENTITY_ENDERMAN_HURT, SoundCategory.HOSTILE, 1F, (float) this.dataTracker.get(stareTime) / 10);
                        }
                        if (this.dataTracker.get(stareTime) >= 7 && !world.isClient) {
                            this.damage(damageSource, 2000);
                            this.dataTracker.set(stareTime, 0);
                        }
                    }
                    if (atkCd >= 20 && !world.isClient && !this.isDead()) {
                        player.damage(damageSource, 35);
                    }

                    if (ticksChange >= ticksSet && !world.isClient) {
                        int telePosX = random.nextBetween(-2, 2);
                        int telePosZ = random.nextBetween(-2, 2);
                        BlockPos nextBlockPos = new BlockPos(this.getBlockX() + telePosX, this.getBlockY(), this.getBlockZ() + telePosZ);
                        ticksChange = 0;
                        ticksSet = random.nextBetween(10, 20);
                        if (world.getBlockState(nextBlockPos).isAir()) {
                            this.setPosition(this.getBlockX() + telePosX, this.getBlockY(), this.getBlockZ() + telePosZ);
                        }
                    }

                }
            }
            if (atkCd >= 20 && !world.isClient) {
                atkCd = 0;
            }

        super.tick();

    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putInt("AtkCD", atkCd);
        tag.putInt("StareTime", this.dataTracker.get(stareTime));
        tag.putInt("TicksChange", ticksChange);
        tag.putInt("setTicks", ticksSet);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        atkCd = tag.getInt("AtkCd");
        ticksChange = tag.getInt("TicksChange");
        ticksSet = tag.getInt("setTicks");
        this.dataTracker.set(stareTime, tag.getInt("StareTime"));
    }

}

