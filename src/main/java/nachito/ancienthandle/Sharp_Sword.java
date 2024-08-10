package nachito.ancienthandle;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class Sharp_Sword extends SwordItem {

    public Sharp_Sword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.of("Right Click to gain Strength II for 15s."));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        Item item = ModItems.SHARP_SWORD;
        ItemStack itemStack = user.getStackInHand(hand);

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

        if (hasHelmet && hasLeggings && hasBoots && hasChestplate) {
            user.getItemCooldownManager().set(item, 150);
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 400, 2));
        } else {
            user.getItemCooldownManager().set(item, 300);
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 300, 1));
        }
        return TypedActionResult.pass(itemStack);

    }
}
