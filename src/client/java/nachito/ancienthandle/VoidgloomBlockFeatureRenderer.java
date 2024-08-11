package nachito.ancienthandle;

import nachito.ancienthandle.entity.Voidgloom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class VoidgloomBlockFeatureRenderer extends FeatureRenderer<Voidgloom, VoidgloomEntityModel<Voidgloom>> {

    private final BlockRenderManager blockRenderManager;

    public VoidgloomBlockFeatureRenderer(FeatureRendererContext<Voidgloom, VoidgloomEntityModel<Voidgloom>> context, BlockRenderManager blockRenderManager) {
        super(context);
        this.blockRenderManager = blockRenderManager;
    }

    public void render(
                MatrixStack matrixStack,
                VertexConsumerProvider vertexConsumerProvider,
                int i,
                Voidgloom voidgloom,
                float f,
                float g,
                float h,
                float j,
                float k,
                float l
        ) {
            BlockState blockState = Blocks.BEACON.getDefaultState();
            if (voidgloom.getDataTracker().get(Voidgloom.carryingBlock)) {
                matrixStack.push();
                matrixStack.translate(0.0F, 0.6875F, -0.75F);
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(20.0F));
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45.0F));
                matrixStack.translate(0.25F, 0.1875F, 0.25F);
                float m = 0.5F;
                matrixStack.scale(-0.5F, -0.5F, 0.5F);
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
                this.blockRenderManager.renderBlockAsEntity(blockState, matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV);
                matrixStack.pop();
            }
        }
    }

