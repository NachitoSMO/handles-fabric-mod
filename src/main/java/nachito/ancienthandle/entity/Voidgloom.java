package nachito.ancienthandle.entity;

import nachito.ancienthandle.AncientHandleInit;
import nachito.ancienthandle.ModItems;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Voidgloom extends PathAwareEntity {

    private int atkCooldown;
    private ServerBossBar bossBar;
    private boolean phase1;
    private int phaseHits;
    private int trackDmg = 0;
    private boolean phase2 = false;
    private int time = 0;
    private int time2 = 0;
    private int time3 = 0;
    private int tryDown = 0;
    private int giveUp = 0;
    private int bigAttack = 0;
    private boolean droppedItem = false;
    private boolean simonEnd = false;
    private boolean playedSound = false;
    private boolean playedAngrySound = false;
    private boolean giveHealth = false;
    private boolean postPhase2 = false;
    private boolean wentUp = false;
    public static final TrackedData<Boolean> carryingBlock = DataTracker.registerData(Voidgloom.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Boolean> angry = DataTracker.registerData(Voidgloom.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Integer> isLooking = DataTracker.registerData(Voidgloom.class, TrackedDataHandlerRegistry.INTEGER);
    private LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, this.getWorld());
    private int tpCd = 0;
    private boolean rng = random.nextBoolean();

    public Voidgloom(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);

        phaseHits = 100;
        phase1 = true;
        this.setCustomNameVisible(true);

        if (world instanceof ServerWorld) {
            this.bossBar = new ServerBossBar(Text.of("§lVOIDEATH"), BossBar.Color.RED, BossBar.Style.PROGRESS);
        }
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        phaseHits = 100;
        phase1 = true;
        this.setCustomNameVisible(true);
        return entityData;
    }

    public void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(carryingBlock, false);
        builder.add(isLooking, 0);
        builder.add(angry, false);
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1000.0F)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3F)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 100);
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
            switch (random.nextBetween(1, 4)) {
                case 1:
                    this.dropItem(ModItems.KB_BOOTS);
                    droppedItem = true;
                    break;
                case 2:
                    this.dropItem(ModItems.KB_CHESTPLATE);
                    droppedItem = true;
                    break;
                case 3:
                    this.dropItem(ModItems.KB_HELMET);
                    droppedItem = true;
                    break;
                case 4:
                    this.dropItem(ModItems.KB_LEGGINGS);
                    droppedItem = true;
                    break;

            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {

        if (source.isIn(DamageTypeTags.IS_PROJECTILE) && time3 >= 250) {
            time3 = 0;
        }

        if (!source.isIn(DamageTypeTags.IS_PROJECTILE) && !source.isIn(DamageTypeTags.IS_FIRE) && !source.isIn(DamageTypeTags.IS_EXPLOSION) && !this.getWorld().isClient) {
            ExperienceOrbEntity experienceOrb = new ExperienceOrbEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), 10);
            bigAttack = 0;
            this.getWorld().spawnEntity(experienceOrb);
            if (phase1 || phase2) {
                phaseHits -= 1;
                if (this.attackingPlayer != null && this.attackingPlayer.getMainHandStack().isOf(ModItems.FAST_SWORD)) {
                    phaseHits -= 1;
                }
                if (phaseHits <= 0) {
                    if (phase1) {
                        phaseHits = 150;
                    }
                    phase1 = false;
                    phase2 = false;
                }

                if (phase1 && !this.isDead()) {
                    this.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1F, (50F / phaseHits) * 0.5F);
                    this.setHealth(this.getMaxHealth());
                }
                if (phase2 && !this.isDead()) {
                    this.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1F, (100F / phaseHits) * 0.5F);
                    this.setHealth(this.getMaxHealth());
                }
            }

            return super.damage(source, amount);
        }

        return false;
    }

    public void updateName() {
        if (phase1 || phase2) {
            this.setCustomName(Text.of("§lHITS: " + phaseHits));
        } else {
            this.setCustomName(Text.of("§lVOIDEATH"));
        }
    }

    boolean isLooking(PlayerEntity player) {
        Vec3d vec3d = player.getRotationVec(1.0F).normalize();
        Vec3d vec3d2 = new Vec3d(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
        double d = vec3d2.length();
        vec3d2 = vec3d2.normalize();
        double e = vec3d.dotProduct(vec3d2);
        return e > 1.0 - 0.025 / d;
    }

    @Override
    public void tick() {

        World world = this.getWorld();
        PlayerEntity closestPlayer = world.getClosestPlayer(this, 150);

        if (!world.isClient) {
            atkCooldown++;
            tpCd++;
            time3++;

            if (!phase1 && !phase2) {
                time++;
                time2++;
                bigAttack++;
            }
        }


        DamageSource damageSource = new DamageSource(
                world.getRegistryManager()
                        .get(RegistryKeys.DAMAGE_TYPE)
                        .entryOf(AncientHandleInit.NACHITO_DAMAGE));

        DamageSource lightningDmg = new DamageSource(
                world.getRegistryManager()
                        .get(RegistryKeys.DAMAGE_TYPE)
                        .entryOf(DamageTypes.MAGIC));

        List<? extends PlayerEntity> players = world.getPlayers();

        for (PlayerEntity player : players) {
            if (player.isDead() && !world.isClient && player.distanceTo(this) < 50) {
                droppedItem = true;
                this.kill();
            }
        }


            if (!world.isClient && closestPlayer != null && !this.isDead()) {

                updateName();
                this.moveControl.moveTo(closestPlayer.getX(), closestPlayer.getY(), closestPlayer.getZ(), 0.5);

                if (atkCooldown > 20) {
                    atkCooldown = 0;
                }

                if (bigAttack == 60) {
                    this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED);
                }

                if (bigAttack >= 90) {
                    this.playSound(SoundEvents.ENTITY_CREEPER_DEATH);
                    for (PlayerEntity player : players) {
                        if (player.distanceTo(this) < 20) {
                            player.damage(damageSource, 70);
                        }
                    }
                    bigAttack = 0;
                }

                if (this.distanceTo(closestPlayer) > 30) {
                    trackDmg = 0;
                }

                if (phaseHits > 150) {
                    phaseHits = 150;
                }
                if (this.getHealth() < this.getMaxHealth() / 4 && !phase1 && !postPhase2 && !this.isDead()) {
                    phase2 = true;
                    postPhase2 = true;
                    for (PlayerEntity user : players) {
                        if (!playedSound && user.distanceTo(this) < 50) {
                            user.playSoundToPlayer(SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.HOSTILE, 1.0F, 1.0F);
                            playedSound = true;
                        }
                    }
                }
                if (time >= 300 && !phase1 && !phase2) {
                    if (!rng) {
                        time = 0;
                        VoidHead voidHead = new VoidHead(AncientHandleInit.VOIDHEAD, world);
                        voidHead.setPos(this.getX() + getRandom().nextBetween(-5, 5), this.getY() + 2, this.getZ() + getRandom().nextBetween(-5, 5));
                        world.spawnEntity(voidHead);
                        voidHead = new VoidHead(AncientHandleInit.VOIDHEAD, world);
                        voidHead.setPos(this.getX() + getRandom().nextBetween(-5, 5), this.getY() + 2, this.getZ() + getRandom().nextBetween(-5, 5));
                        world.spawnEntity(voidHead);
                        this.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON);
                        rng = random.nextBoolean();
                    } else {
                        if (!playedAngrySound) {
                            this.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL);
                            this.teleport(closestPlayer.getX() + random.nextBetween(-3, 3), closestPlayer.getY(), closestPlayer.getZ() + random.nextBetween(-3, 3), true);
                            this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT);
                            playedAngrySound = true;
                        }
                        this.dataTracker.set(angry, true);
                        for (PlayerEntity player : players) {
                            if (isLooking(player)) {
                                this.dataTracker.set(isLooking, this.dataTracker.get(isLooking) + 1);
                            }
                        }
                        if (time >= 330 && this.dataTracker.get(isLooking) < 3) {

                            if (closestPlayer.distanceTo(this) < 50) {
                                lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                                lightning.setPos(closestPlayer.getX(), closestPlayer.getY(), closestPlayer.getZ());
                                world.spawnEntity(lightning);
                                closestPlayer.damage(lightningDmg, 1000);
                            }
                            this.dataTracker.set(isLooking, 0);
                            this.dataTracker.set(angry, false);
                            time = 0;
                            playedAngrySound = false;
                            rng = random.nextBoolean();
                        } else if (time >= 330) {
                            time = 0;
                            this.dataTracker.set(isLooking, 0);
                            rng = random.nextBoolean();
                            playedAngrySound = false;
                            this.dataTracker.set(angry, false);
                        }

                    }
                }

                if (tpCd >= 80 && time3 < 250 && time2 > -110) {
                    this.teleport(closestPlayer.getX() + random.nextBetween(-3, 3), closestPlayer.getY(), closestPlayer.getZ() + random.nextBetween(-3, 3), true);
                    world.playSound(this, this.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1F, 1F);
                    tpCd = 0;
                }


                if (time2 >= 500 && !simonEnd && this.distanceTo(closestPlayer) < 50) {

                    for (PlayerEntity player : players) {
                        if (player.distanceTo(this) < 50) {
                            player.playSoundToPlayer(SoundEvents.ENTITY_RABBIT_DEATH, SoundCategory.HOSTILE, 1, 1);
                        }
                    }

                    time3 = 0;
                    time = 150;
                    this.dataTracker.set(angry, false);

                    switch (time2) {
                        case 510:
                            lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                            lightning.setPos(closestPlayer.getX(), closestPlayer.getY(), closestPlayer.getZ());
                            break;
                        case 525, 540, 545, 550:
                            world.spawnEntity(lightning);
                            if (closestPlayer.getBlockX() == lightning.getBlockX() && closestPlayer.getBlockZ() == lightning.getBlockZ()) {
                                closestPlayer.damage(lightningDmg, 1000);
                            }
                            lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                            lightning.setPos(closestPlayer.getX(), closestPlayer.getY(), closestPlayer.getZ());
                            break;
                        case 555:
                            world.spawnEntity(lightning);
                            if (closestPlayer.getBlockX() == lightning.getBlockX() && closestPlayer.getBlockZ() == lightning.getBlockZ()) {
                                closestPlayer.damage(lightningDmg, 1000);
                            }
                            simonEnd = true;
                            break;
                    }

                }

                if (time2 >= 750 && !phase1 && !phase2 && !wentUp) {
                    for (PlayerEntity player : players) {
                        if (player.distanceTo(this) < 50) {
                            player.playSoundToPlayer(SoundEvents.ENTITY_SHULKER_BULLET_HURT, SoundCategory.HOSTILE, 1F, 1F);
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 10, 69));
                        }
                    }
                    time = 0;
                    this.dataTracker.set(angry, false);
                    time3 = 0;
                    wentUp = true;
                }

                if (time2 >= 750 && time2 <= 850) {
                    bigAttack = 0;
                }

                if (time2 >= 960 && time2 < 1000 && !phase1 && !phase2) {
                    this.dataTracker.set(carryingBlock, true);
                    time = 100;
                    this.dataTracker.set(angry, false);
                }

                    if (time2 >= 1000 && !phase1 && !phase2) {

                        BlockPos blockPos = new BlockPos(this.getBlockX() + random.nextBetween(-15, 15), this.getBlockY() + 10, this.getBlockZ() + random.nextBetween(-15, 15));

                        while (world.getBlockState(blockPos.down()).isAir() && tryDown < 50) {
                            blockPos = blockPos.down();
                            tryDown++;
                        }

                        if (tryDown >= 50) {
                            blockPos = new BlockPos(this.getBlockX() + random.nextBetween(-15, 15), this.getBlockY() + 10, this.getBlockZ() + random.nextBetween(-15, 15));
                            giveUp++;
                            tryDown = 0;
                        }

                        while (!world.getBlockState(blockPos).isAir()) {
                            blockPos = blockPos.up();
                            blockPos = blockPos.east();
                        }

                        if (!world.getBlockState(blockPos.down()).isAir() && world.getBlockState(blockPos).isAir()) {
                            this.teleport(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ(), true);
                            closestPlayer.playSoundToPlayer(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1F, 1F);
                            world.setBlockState(blockPos, AncientHandleInit.DEATH_BEACON.getDefaultState());
                            this.dataTracker.set(carryingBlock, false);
                            time2 = -120;
                            tpCd = -20;
                            simonEnd = false;
                            wentUp = false;
                            tryDown = 0;
                            giveUp = 0;
                        }

                        if (giveUp >= 10) {
                            time2 = -120;
                            this.dataTracker.set(carryingBlock, false);
                            simonEnd = false;
                            wentUp = false;
                            tryDown = 0;
                            giveUp = 0;
                        }
                    }

                    if (time2 == -110) {
                        this.teleport(closestPlayer.getX() + random.nextBetween(-3, 3) , closestPlayer.getY(), closestPlayer.getZ() + random.nextBetween(-3, 3), true);
                        closestPlayer.playSoundToPlayer(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1F, 1F);
                    }

                    if (time2 < 0) {
                        bigAttack = 0;
                    }

                    for (PlayerEntity user : players) {
                        if (this.distanceTo(user) < 30 && !phase1 && !phase2) {
                            if (atkCooldown >= 20) {
                                user.damage(damageSource, 15);
                                if (this.getHealth() < this.getMaxHealth() && !this.isDead()) {
                                    this.setHealth(this.getHealth() + 5);
                                }
                            }
                        }
                    }

                    if (time3 >= 250 && time2 < 800 && time2 >= 0) {
                        for (PlayerEntity player : players) {
                            if (player.distanceTo(this) < 40) {
                                player.playSoundToPlayer(SoundEvents.ENTITY_DOLPHIN_DEATH, SoundCategory.HOSTILE, 1F, 1F);
                            }
                        }
                        if (this.distanceTo(closestPlayer) < 15 && time3 >= 290) {
                            lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                            lightning.setPos(closestPlayer.getX(), closestPlayer.getY(), closestPlayer.getZ());
                            world.spawnEntity(lightning);
                            if (closestPlayer.getBlockX() == lightning.getBlockX() && closestPlayer.getBlockZ() == lightning.getBlockZ()) {
                                closestPlayer.damage(lightningDmg, 1000);
                            }
                        }

                        if (time3 >= 290) {
                            time3 = 0;
                        }


                    }

                    if (time3 >= 250 && time2 < 0 || time3 >= 250 && time2 >= 800) {
                        time3 = 0;
                    }

                    if (atkCooldown >= 5 && this.distanceTo(closestPlayer) > 30) {
                        phaseHits += 1;
                    }

                    for (PlayerEntity player : players) {
                        if (phase1 && player.distanceTo(this) < 30 || phase2 && player.distanceTo(this) < 30) {
                            if (atkCooldown >= 20) {

                                if (trackDmg < 60) {
                                    player.damage(damageSource, 20);
                                }

                                if (trackDmg > 60 && trackDmg < 100) {
                                    player.damage(damageSource, 25);
                                }

                                if (trackDmg > 100) {
                                    player.damage(damageSource, 30);
                                }
                            }

                        }
                    }


                    if (!phase1 && !phase2) {
                        trackDmg++;
                    }

                    if (phase2 && !giveHealth) {
                        this.setHealth(this.getMaxHealth());
                        giveHealth = true;
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
        tag.putBoolean("Phase1", phase1);
        tag.putBoolean("Phase2", phase2);
        tag.putInt("PhaseHits", phaseHits);
        tag.putInt("TrackDmg", trackDmg);
        tag.putInt("Time", time);
        tag.putInt("Time2", time2);
        tag.putBoolean("simonEnd", simonEnd);
        tag.putBoolean("playedSound", playedSound);
        tag.putBoolean("giveHealth", giveHealth);
        tag.putInt("tpCd", tpCd);
        tag.putInt("time3", time3);
        tag.putInt("tryDown", tryDown);
        tag.putInt("giveUp", giveUp);
        tag.putBoolean("droppedItem", droppedItem);
        tag.putBoolean("carryingBlock", this.dataTracker.get(carryingBlock));
        tag.putBoolean("wentUp", wentUp);
        tag.putInt("isLooking", this.dataTracker.get(isLooking));
        tag.putBoolean("playedAngrySound", playedAngrySound);
        tag.putInt("bigAttack", bigAttack);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        phase1 = tag.getBoolean("Phase1");
        phase2 = tag.getBoolean("Phase2");
        phaseHits = tag.getInt("PhaseHits");
        trackDmg = tag.getInt("TrackDmg");
        time = tag.getInt("Time");
        time2 = tag.getInt("Time2");
        simonEnd = tag.getBoolean("simonEnd");
        playedSound = tag.getBoolean("playedSound");
        giveHealth = tag.getBoolean("giveHealth");
        tpCd = tag.getInt("tpCd");
        time3 = tag.getInt("time3");
        tryDown = tag.getInt("tryDown");
        giveUp = tag.getInt("giveUp");
        droppedItem = tag.getBoolean("droppedItem");
        wentUp = tag.getBoolean("wentUp");
        bigAttack = tag.getInt("bigAttack");
        playedAngrySound = tag.getBoolean("playedAngrySound");
        this.dataTracker.set(carryingBlock, tag.getBoolean("carryingBlock"));
        this.dataTracker.set(isLooking, tag.getInt("isLooking"));
    }

}
