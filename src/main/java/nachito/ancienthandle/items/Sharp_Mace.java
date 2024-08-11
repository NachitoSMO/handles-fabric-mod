package nachito.ancienthandle.items;

import nachito.ancienthandle.ModItems;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class Sharp_Mace extends MaceItem {
    public Sharp_Mace(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.of("Right Click to gain Strength IV for 3s."));
        tooltip.add(Text.of("Cooldown: 60s"));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        Item item = ModItems.SHARP_MACE;
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

        if (hasHelmet && hasBoots && hasChestplate && hasLeggings) {
            user.getItemCooldownManager().set(item, 600);
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100, 4));
        } else {
            user.getItemCooldownManager().set(item, 1200);
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 60, 3));
        }
        return TypedActionResult.pass(itemStack);

    }

    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 6.0, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, -3.4F, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .build();
    }
}
