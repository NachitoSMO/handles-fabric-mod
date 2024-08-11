package nachito.ancienthandle.blocks;

import nachito.ancienthandle.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DeathBeaconEntity extends BlockEntity {
    private int killTimer = 0;
    private boolean hasKilled = false;
    private boolean playedSound = false;

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public DeathBeaconEntity(BlockPos pos, BlockState state) {
        super(ModItems.DEATH_BEACON_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, DeathBeaconEntity be) {


            if (!world.isClient) {
                DamageSource lightningDmg = new DamageSource(
                        world.getRegistryManager()
                                .get(RegistryKeys.DAMAGE_TYPE)
                                .entryOf(DamageTypes.MAGIC));

                List<? extends PlayerEntity> players = world.getPlayers();

                if (!be.hasKilled && players != null) {
                    LightningEntity spawnLightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                    be.killTimer += 1;
                    float sound = be.killTimer;
                    if (be.killTimer > 120) {
                        be.killTimer = 0;
                        be.hasKilled = true;
                    }
                    for (PlayerEntity player : players) {
                        if (pos.isWithinDistance(player.getPos(), 50)) {
                            world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.5F, sound / 100);
                        }
                        if (!be.playedSound && pos.isWithinDistance(player.getPos(), 50)) {
                            player.playSoundToPlayer(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 10F, 10F);
                        }
                        be.markDirty();
                    }
                    if (!be.playedSound) {
                        be.playedSound = true;
                    }
                    for (PlayerEntity player : players) {
                        if (be.killTimer >= 120) {

                            if (pos.isWithinDistance(player.getPos(), 50)) {
                                spawnLightning.setPos(player.getX(), player.getY(), player.getZ());
                                world.spawnEntity(spawnLightning);
                                player.damage(lightningDmg, 1000);
                            }

                        }
                    }
                    if (be.killTimer >= 120) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
                }
            }
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        nbt.putInt("timer", killTimer);
        nbt.putBoolean("killed", hasKilled);
        nbt.putBoolean("playedSound", playedSound);
        super.writeNbt(nbt, wrapper);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        super.readNbt(nbt, wrapper);
        killTimer = nbt.getInt("timer");
        hasKilled = nbt.getBoolean("killed");
        playedSound = nbt.getBoolean("playedSound");
    }

}
