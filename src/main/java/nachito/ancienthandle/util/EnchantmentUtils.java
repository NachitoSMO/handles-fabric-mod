package nachito.ancienthandle.util;

import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

public class EnchantmentUtils {
    public static void transferEnchants(ItemStack from, ItemStack to) {
        ItemEnchantmentsComponent fromEnchants = EnchantmentHelper.getEnchantments(from);
        EnchantmentHelper.set(to, fromEnchants);
    }
}
