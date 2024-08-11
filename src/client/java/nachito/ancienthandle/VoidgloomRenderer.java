package nachito.ancienthandle;

import nachito.ancienthandle.entity.Voidgloom;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;


public class VoidgloomRenderer extends MobEntityRenderer<Voidgloom, VoidgloomEntityModel<Voidgloom>> {

    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/enderman/enderman.png");

    public VoidgloomRenderer(EntityRendererFactory.Context context) {
        super(context, new VoidgloomEntityModel<>(context.getPart(EntityModelLayers.ENDERMAN)), 0.5F);
        this.addFeature(new VoidgloomBlockFeatureRenderer(this, context.getBlockRenderManager()));
    }

    public void render(Voidgloom voidgloom, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        VoidgloomEntityModel<Voidgloom> voidgloomEntityModel = this.getModel();
        voidgloomEntityModel.carryingBlock = voidgloom.getDataTracker().get(Voidgloom.carryingBlock);
        voidgloomEntityModel.angry = voidgloom.getDataTracker().get(Voidgloom.angry);
        super.render(voidgloom, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(Voidgloom voidgloom) {
        return TEXTURE;
    }
}