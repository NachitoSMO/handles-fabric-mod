package nachito.ancienthandle.entity;

import nachito.ancienthandle.AncientHandleInit;
import nachito.ancienthandle.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Ignatius extends HostileEntity {

    private ServerBossBar bossBar;
    private int atkCooldown;
    private int time;
    private int giveUp;
    private int giveUpCarpet;
    private boolean doingAttack;
    private boolean shooting;
    private boolean hasShot;
    private boolean hasSpawnedAnvils;
    private boolean droppedItem;
    private FireballEntity fireball;
    private final ArrayList<BlockPos> blockList = new ArrayList<>();

    public Ignatius(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setCustomName(Text.of("§lIGNATIUS"));
        this.setCustomNameVisible(true);

        if (world instanceof ServerWorld) {
            this.bossBar = new ServerBossBar(Text.of("§lIGNATIUS"), BossBar.Color.YELLOW, BossBar.Style.PROGRESS);
        }
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setCustomName(Text.of("§lIGNATIUS"));
        this.setCustomNameVisible(true);
        return entityData;
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 500.0F)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 100);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!source.isIn(DamageTypeTags.IS_FIRE) && !source.isOf(DamageTypes.FALLING_BLOCK) && !source.isOf(DamageTypes.DRAGON_BREATH)) {
            return super.damage(source, amount);
        }

        return false;
    }

    private void updateBossBar() {
        if (this.bossBar != null) {
            this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
            for (PlayerEntity player : this.getWorld().getPlayers()) {
                if (player instanceof ServerPlayerEntity serverPlayer) {
                    if (this.distanceTo(player) < 20) {
                        this.bossBar.addPlayer(serverPlayer);
                    } else {
                        this.bossBar.removePlayer(serverPlayer);
                    }
                    if (player.isDead()) {
                        this.bossBar.removePlayer(serverPlayer);
                    }
                }
            }
        }
    }

    @Override
    public void updatePostDeath() {
        super.updatePostDeath();
        if (this.bossBar != null) {
            this.bossBar.clearPlayers();
        }
        if (!droppedItem) {
            this.dropItem(ModItems.ANCIENT_HANDLE);
            droppedItem = true;
        }
    }

    public void tick() {
        World world = this.getWorld();
        PlayerEntity closestPlayer = world.getClosestPlayer(this, 50);

        if (!world.isClient) {
            atkCooldown++;
            time++;
        }

        DamageSource damageSource = new DamageSource(
                world.getRegistryManager()
                        .get(RegistryKeys.DAMAGE_TYPE)
                        .entryOf(AncientHandleInit.NACHITO_BLAZE_DAMAGE));

        List<? extends PlayerEntity> players = world.getPlayers();

        if (!world.isClient && closestPlayer != null && !this.isDead()) {

            if (!doingAttack) {
                Vec3d direction = new Vec3d(closestPlayer.getX() - this.getX(), closestPlayer.getY() - this.getY(), closestPlayer.getZ() - this.getZ()).normalize();
                BlockPos blockPosFront = new BlockPos((int) (this.getX() + direction.x), (int) this.getY(), (int) (this.getZ() + direction.z));

                this.moveControl.moveTo(closestPlayer.getX(), closestPlayer.getY(), closestPlayer.getZ(), 0.7);
                if (!world.getBlockState(blockPosFront).isAir() && this.isOnGround()) {
                    this.jump();
                }
            }

            for (PlayerEntity player : players) {
                if (atkCooldown >= 30 && player.distanceTo(this) < 30) {
                    player.damage(damageSource, 5);
                }
            }

            if (atkCooldown >= 30) {
                atkCooldown = 0;
                this.playSound(SoundEvents.ENTITY_BLAZE_BURN);
            }

            if (time >= 200) {
                if (!shooting) {
                    this.playSound(SoundEvents.ENTITY_GHAST_WARN);
                    doingAttack = true;
                    shooting = true;
                    double x = closestPlayer.getX() - (this.getX());
                    double z = closestPlayer.getZ() - (this.getZ());
                    Vec3d vec3d = new Vec3d(x, 0, z).normalize();
                    fireball = new FireballEntity(world, this, vec3d, 5);
                    fireball.setPos(this.getX(), this.getBodyY(0.5) + 0.5, this.getZ());
                    fireball.setVelocity(vec3d.multiply(1.5));
                }


                if (time >= 220 && !hasShot) {
                    doingAttack = false;
                    if (fireball != null) {
                        this.playSound(SoundEvents.ENTITY_GHAST_SHOOT);
                        world.spawnEntity(fireball);
                    }
                    hasShot = true;
                }


            }

            if (time >= 400 && !hasSpawnedAnvils) {
                doingAttack = true;
                this.playSound(SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE);
                for (int i = 0; i < 250; i++) {
                    BlockPos blockPos = new BlockPos(closestPlayer.getBlockX() + random.nextBetween(-9, 9), closestPlayer.getBlockY(), closestPlayer.getBlockZ() + random.nextBetween(-9, 9));

                    while (!world.getBlockState(blockPos).isAir() && giveUpCarpet < 200 || blockList.contains(blockPos) && giveUpCarpet < 200) {
                        blockPos = blockPos.up();
                        blockPos = switch (random.nextBetween(1, 4)) {
                            case 1 -> blockPos.east();
                            case 2 -> blockPos.west();
                            case 3 -> blockPos.south();
                            case 4 -> blockPos.north();
                            default -> blockPos;
                        };
                    }

                    while (world.getBlockState(blockPos.down()).isAir() && giveUpCarpet < 400) {
                        blockPos = blockPos.down();
                        giveUpCarpet++;
                    }


                    if (!world.getBlockState(blockPos.down()).isAir() && world.getBlockState(blockPos).isAir() && !blockList.contains(blockPos) || giveUpCarpet >= 200) {
                        world.setBlockState(blockPos, Blocks.RED_CARPET.getDefaultState());
                        blockList.add(blockPos);
                        for (int n = 0; n < 10; n++) {
                            blockList.add(blockPos.up());
                        }
                    }

                    BlockPos anvilPos = new BlockPos(blockPos.up(20));
                    while (!world.getBlockState(anvilPos).isAir() && giveUp < 20) {
                        anvilPos.down();
                        giveUp++;
                    }
                    if (world.getBlockState(anvilPos).isAir() || giveUp >= 20) {
                        FallingBlockEntity fallingBlock = FallingBlockEntity.spawnFromBlock(world, anvilPos, Blocks.FIRE.getDefaultState());
                        fallingBlock.setHurtEntities(40, 100);
                        giveUp = 0;
                        giveUpCarpet = 0;
                        world.spawnEntity(fallingBlock);
                    }
                }
                hasSpawnedAnvils = true;

            }

            if (time >= 440) {
                doingAttack = false;
                for (BlockPos block : blockList) {
                    world.setBlockState(block, Blocks.AIR.getDefaultState());
                }
                blockList.clear();
            }

            if (time >= 600) {
                this.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL);
                DragonFireballEntity dragonFireball = new DragonFireballEntity(world, this, new Vec3d(0, -10, 0));
                dragonFireball.setPos(closestPlayer.getX(), closestPlayer.getY() + 10, closestPlayer.getZ());
                world.spawnEntity(dragonFireball);
                for (int i = 0; i < 11; i++) {
                    dragonFireball = new DragonFireballEntity(world, this, new Vec3d(0, -10, 0));
                    dragonFireball.setPos(closestPlayer.getX() + random.nextBetween(-10, 10), closestPlayer.getY() + 10, closestPlayer.getZ() + random.nextBetween(-10, 10));
                    world.spawnEntity(dragonFireball);
                }

                time = 0;
                shooting = false;
                hasShot = false;
                hasSpawnedAnvils = false;
            }

        }

        if (this.isDead()) {
            for (BlockPos block : blockList) {
                world.setBlockState(block, Blocks.AIR.getDefaultState());
            }
        }

        if (!world.isClient && bossBar != null) {
            updateBossBar();
        }

        super.tick();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putInt("time", time);
        tag.putInt("atkCooldown", atkCooldown);
        tag.putInt("giveUp", giveUp);
        tag.putInt("giveUpCarpet", giveUpCarpet);
        tag.putBoolean("doingAttack", doingAttack);
        tag.putBoolean("shooting", shooting);
        tag.putBoolean("hasShot", hasShot);
        tag.putBoolean("hasSpawnedAnvils", hasSpawnedAnvils);
        tag.putBoolean("droppedItem", droppedItem);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        time = tag.getInt("time");
        atkCooldown = tag.getInt("atkCooldown");
        doingAttack = tag.getBoolean("doingAttack");
        shooting = tag.getBoolean("shooting");
        hasShot = tag.getBoolean("hasShot");
        hasSpawnedAnvils = tag.getBoolean("hasSpawnedAnvils");
        giveUp = tag.getInt("giveUp");
        giveUpCarpet = tag.getInt("giveUpCarpet");
        droppedItem = tag.getBoolean("droppedItem");
    }

}
