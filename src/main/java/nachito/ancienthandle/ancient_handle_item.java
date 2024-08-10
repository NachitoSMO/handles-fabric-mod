package nachito.ancienthandle;

import nachito.ancienthandle.util.EnchantmentUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class ancient_handle_item extends Item {

    public ancient_handle_item(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.of("Upgrades a Netherite item's §cSHARPNESS."));
        tooltip.add(Text.of("Requires Sharpness 1 or above on target item (except Mace)."));
        tooltip.add(Text.of("Put the item in your offhand to use."));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack itemStack = user.getStackInHand(hand);
        ItemStack itemStackMain = user.getStackInHand(Hand.MAIN_HAND);
        ItemStack itemStackOff = user.getStackInHand(Hand.OFF_HAND);
        ItemStack maceStack = new ItemStack(ModItems.SHARP_MACE);
        ItemStack swordStack = new ItemStack(ModItems.SHARP_SWORD);
        ItemStack axeStack = new ItemStack(ModItems.SHARP_AXE);

        Voidgloom voidgloom = new Voidgloom(AncientHandleInit.VOIDGLOOM, world);

        DynamicRegistryManager drm = world.getRegistryManager();
        Registry<Enchantment> reg = drm.get(RegistryKeys.ENCHANTMENT);

        Optional<RegistryEntry.Reference<Enchantment>> optional = reg.getEntry(Enchantments.SHARPNESS);
        RegistryEntry<Enchantment> registryEntry2 = optional.orElseThrow();

        if (world.isClient) {
            return TypedActionResult.pass(itemStack);
        }

        if(itemStackOff.isOf(ModItems.ANCIENT_HANDLE) && !itemStackMain.isEmpty()) {
            if (itemStackMain.isOf(ModItems.EFFICIENT_HANDLE)) {
                itemStackOff.decrement(1);
                itemStackMain.decrement(1);
                voidgloom.setPos(user.getX(), user.getY(), user.getZ());
                world.spawnEntity(voidgloom);
                world.createExplosion(voidgloom, voidgloom.getX(), voidgloom.getY(), voidgloom.getZ(), 0.1F, false, World.ExplosionSourceType.MOB);
                voidgloom.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN);
            }
            if (itemStackMain.isOf(Items.NETHERITE_SWORD) && itemStackMain.getEnchantments().getLevel(registryEntry2) >= 1) {
                EnchantmentUtils.transferEnchants(itemStackMain, swordStack);
                swordStack.addEnchantment(registryEntry2, 6);
                itemStackOff.decrement(1);
                itemStackMain.decrement(1);
                user.getInventory().offerOrDrop(swordStack);
                user.spawnSweepAttackParticles();
                user.playSoundToPlayer(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1, 1);
                user.sendMessage(Text.of("You have harnessed the Handle's §cSHARPNESS"));

            } if (itemStackMain.isOf(Items.MACE)) {
                EnchantmentUtils.transferEnchants(itemStackMain, maceStack);
                maceStack.addEnchantment(registryEntry2, 3);
                itemStackOff.decrement(1);
                itemStackMain.decrement(1);
                user.getInventory().offerOrDrop(maceStack);
                user.spawnSweepAttackParticles();
                user.playSoundToPlayer(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1, 1);
                user.sendMessage(Text.of("You have harnessed the Handle's §cSHARPNESS"));
            }

            if (itemStackMain.isOf(Items.NETHERITE_AXE) && itemStackMain.getEnchantments().getLevel(registryEntry2) >= 1) {
                EnchantmentUtils.transferEnchants(itemStackMain, axeStack);
                axeStack.addEnchantment(registryEntry2, 6);
                itemStackOff.decrement(1);
                itemStackMain.decrement(1);
                user.getInventory().offerOrDrop(axeStack);
                user.spawnSweepAttackParticles();
                user.playSoundToPlayer(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1, 1);
                user.sendMessage(Text.of("You have harnessed the Handle's §cSHARPNESS"));
            }

        }
        return TypedActionResult.fail(itemStack);

    }

}
