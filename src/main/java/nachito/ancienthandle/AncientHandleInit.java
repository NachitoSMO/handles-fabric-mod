package nachito.ancienthandle;

import nachito.ancienthandle.blocks.DeathBeacon;
import nachito.ancienthandle.entity.Ignatius;
import nachito.ancienthandle.entity.VoidHead;
import nachito.ancienthandle.entity.Voidgloom;
import nachito.ancienthandle.items.NachoMaterial;
import nachito.ancienthandle.util.ModLootTableModifiers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AncientHandleInit implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("ancienthandle");

	public static final EntityType<Voidgloom> VOIDGLOOM = Registry.register(
			Registries.ENTITY_TYPE, Identifier.of("nachito", "voidgloom"), EntityType.Builder.create(Voidgloom::new, SpawnGroup.MISC).dimensions(0.75F, 3F).build());

	public static final EntityType<Ignatius> IGNATIUS = Registry.register(
			Registries.ENTITY_TYPE, Identifier.of("nachito", "ignatius"), EntityType.Builder.create(Ignatius::new, SpawnGroup.MISC).dimensions(1F, 2F).build());


	public static final EntityType<VoidHead> VOIDHEAD = Registry.register(Registries.ENTITY_TYPE, Identifier.of("nachito", "voidhead"),
			EntityType.Builder.create(VoidHead::new, SpawnGroup.MISC).dimensions(0.75f, 0.6f).build());

	public static final Block DEATH_BEACON = new DeathBeacon(Block.Settings.create().requiresTool().strength(100.0F, 1400.0F).nonOpaque());

	public static final RegistryKey<DamageType> NACHITO_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("nachito", "nachito_damage"));

	public static final RegistryKey<DamageType> NACHITO_BLAZE_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("nachito", "nachito_blaze_damage"));

	@Override
	public void onInitialize() {

		LOGGER.info("Hello Chat");

		ModItems.initialize();
		NachoMaterial.initialize();
		Registry.register(Registries.BLOCK, Identifier.of("nachito", "death_beacon"), DEATH_BEACON);
		ModLootTableModifiers.modifyLootTables();
		FabricDefaultAttributeRegistry.register(VOIDGLOOM, Voidgloom.createMobAttributes());
		FabricDefaultAttributeRegistry.register(VOIDHEAD, VoidHead.createMobAttributes());
		FabricDefaultAttributeRegistry.register(IGNATIUS, Ignatius.createMobAttributes());
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
				.register((itemGroup) -> itemGroup.add(ModItems.ANCIENT_HANDLE));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
				.register((itemGroup) -> itemGroup.add(ModItems.EFFICIENT_HANDLE));
	}
}