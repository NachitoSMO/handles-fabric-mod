package nachito.ancienthandle;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class Fast_Axe extends AxeItem {

    public Fast_Axe(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.of("Right Click to gain Resistance III for 15s."));
        tooltip.add(Text.of("Clears all other effects."));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack itemStack = user.getStackInHand(hand);
        Item item = ModItems.FAST_AXE;

        boolean hasHelmet = false;
        boolean hasChestplate = false;
        boolean hasLeggings = false;
        boolean hasBoots = false;

        for (ItemStack stack : user.getArmorItems()) {
            if (stack.getItem().equals(ModItems.KB_HELMET)) {
                hasHelmet = true;
            }
            if (stack.getItem().equals(ModItems.KB_CHESTPLATE)) {
                hasChestplate = true;
            }
            if (stack.getItem().equals(ModItems.KB_LEGGINGS)) {
                hasLeggings = true;
            }
            if (stack.getItem().equals(ModItems.KB_BOOTS)) {
                hasBoots = true;
            }
        }

        if (world.isClient) {
            return TypedActionResult.pass(itemStack);
        }

        if (hasBoots && hasHelmet && hasChestplate && hasLeggings) {
            user.getItemCooldownManager().set(item, 75);
            user.clearStatusEffects();
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 300, 3));
        } else {
            user.getItemCooldownManager().set(item, 150);
            user.clearStatusEffects();
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 300, 2));
        }
        return TypedActionResult.pass(itemStack);

    }

}
