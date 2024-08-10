package nachito.ancienthandle;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class AncientHandleClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(AncientHandleInit.VOIDGLOOM, VoidgloomRenderer::new);
		EntityRendererRegistry.register(AncientHandleInit.VOIDHEAD, VoidHeadRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(NachitoModelLayers.VOIDHEAD_LAYER, VoidHeadEntityModel::getTexturedModelData);
		BlockRenderLayerMap.INSTANCE.putBlock(AncientHandleInit.DEATH_BEACON, RenderLayer.getCutout());
	}
}