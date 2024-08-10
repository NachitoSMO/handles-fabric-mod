package nachito.ancienthandle.util;

import nachito.ancienthandle.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.*;
import net.minecraft.util.Identifier;

public class ModLootTableModifiers {

    private static final Identifier ANCIENT_CITY_ID = Identifier.of("minecraft", "chests/ancient_city");

    private static void addToLootTable(LootTable.Builder tableBuilder, Item item, float chance) {
        tableBuilder.pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(RandomChanceLootCondition.builder(chance))
                .with(ItemEntry.builder(item))
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 1)).build())
                .build()
        );
    }

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            Identifier id = key.getValue();
            if (ANCIENT_CITY_ID.equals(id)) {
                addToLootTable(tableBuilder, ModItems.ANCIENT_HANDLE, 0.02f);
                addToLootTable(tableBuilder, ModItems.EFFICIENT_HANDLE, 0.025f);
            }
        });
    }
}
