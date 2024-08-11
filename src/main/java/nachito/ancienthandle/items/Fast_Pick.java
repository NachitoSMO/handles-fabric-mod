package nachito.ancienthandle.items;

import nachito.ancienthandle.ModItems;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class Fast_Pick extends PickaxeItem {
    public Fast_Pick(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.of("Right Click to gain Haste V for 10s."));
        tooltip.add(Text.of("Cooldown: 20s"));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Item item = ModItems.FAST_PICK;
        ItemStack itemStack = user.getStackInHand(hand);

        if (world.isClient) {
            return TypedActionResult.pass(itemStack);
        }

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

        user.getItemCooldownManager().set(item, 400);
        if (hasBoots && hasHelmet && hasChestplate && hasLeggings) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 400, 5));
        } else {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 200, 4));
        }
        return TypedActionResult.pass(itemStack);

    }
}
