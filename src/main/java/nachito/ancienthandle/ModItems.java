package nachito.ancienthandle;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public final class ModItems {

    public static Item register(Item item, String id) {
        Identifier itemID = Identifier.of("nachito", id);

        return Registry.register(Registries.ITEM, itemID, item);

    }

    public static final Item ANCIENT_HANDLE = register(new ancient_handle_item(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)), "ancient_handle");

    public static final Item EFFICIENT_HANDLE = register(new efficient_handle_item(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)), "efficient_handle");


    public static final Item FAST_MACE = register(
            new fast_mace(
                        new Item.Settings()
                                .rarity(Rarity.EPIC)
                                .maxDamage(500)
                                .component(DataComponentTypes.TOOL, fast_mace.createToolComponent())
                                .attributeModifiers(fast_mace.createAttributeModifiers())
                ), "fast_mace"
        );

    public static final Item SHARP_MACE = register(
            new Sharp_Mace(
                    new Item.Settings()
                            .rarity(Rarity.EPIC)
                            .maxDamage(500)
                            .component(DataComponentTypes.TOOL, Sharp_Mace.createToolComponent())
                            .attributeModifiers(Sharp_Mace.createAttributeModifiers())
            ), "sharp_mace"
    );


    public static final Item SHARP_SWORD = register(
            new Sharp_Sword(
                        ToolMaterials.NETHERITE, new Item.Settings().rarity(Rarity.EPIC).fireproof().attributeModifiers(Sharp_Sword.createAttributeModifiers(ToolMaterials.NETHERITE, 5, -2.4F))
                ), "sharp_sword"
        );


    public static final Item FAST_SWORD = register(
            new Fast_Sword(
                    ToolMaterials.NETHERITE, new Item.Settings().fireproof().rarity(Rarity.EPIC).attributeModifiers(Fast_Sword.createAttributeModifiers(ToolMaterials.NETHERITE, 3, 96F))
            ), "fast_sword"
    );


    public static final Item SHARP_AXE = register(
            new Sharp_Axe(
                    ToolMaterials.NETHERITE, new Item.Settings().fireproof().rarity(Rarity.EPIC).attributeModifiers(Sharp_Axe.createAttributeModifiers(ToolMaterials.NETHERITE, 7, -3F))
            ), "sharp_axe"
    );

    public static final Item FAST_AXE = register(
            new Fast_Axe(
                    ToolMaterials.NETHERITE, new Item.Settings().fireproof().rarity(Rarity.EPIC).attributeModifiers(Fast_Axe.createAttributeModifiers(ToolMaterials.NETHERITE, 5, -2.6F))
            ), "fast_axe"
    );

    public static final Item FAST_PICK = register(
            new Fast_Pick(
                    ToolMaterials.NETHERITE, new Item.Settings().fireproof().rarity(Rarity.EPIC).attributeModifiers(Fast_Pick.createAttributeModifiers(ToolMaterials.NETHERITE, 1, -2.4F))
            ), "fast_pick"
    );

    public static final BlockEntityType<DeathBeaconEntity> DEATH_BEACON_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of("nachito", "death_beacon_entity"),
            BlockEntityType.Builder.create(DeathBeaconEntity::new, AncientHandleInit.DEATH_BEACON).build()
    );

    public static final Item KB_HELMET = register(new ArmorItem(NachoMaterial.KB_NETHERITE, ArmorItem.Type.HELMET, new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(37))), "nacho_helmet");
    public static final Item KB_CHESTPLATE = register(new ArmorItem(NachoMaterial.KB_NETHERITE, ArmorItem.Type.CHESTPLATE, new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(37))), "nacho_chestplate");
    public static final Item KB_LEGGINGS = register(new ArmorItem(NachoMaterial.KB_NETHERITE, ArmorItem.Type.LEGGINGS, new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(37))), "nacho_leggings");
    public static final Item KB_BOOTS = register(new ArmorItem(NachoMaterial.KB_NETHERITE, ArmorItem.Type.BOOTS, new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(37))), "nacho_boots");


    public static void initialize() {
    }
}


