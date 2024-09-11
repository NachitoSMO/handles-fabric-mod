package nachito.ancienthandle;

import nachito.ancienthandle.entity.Ignatius;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class IgnatiusRenderer extends MobEntityRenderer<Ignatius, IgnatiusModel<Ignatius>> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/blaze.png");

    public IgnatiusRenderer(EntityRendererFactory.Context context) {
        super(context, new IgnatiusModel<>(context.getPart(EntityModelLayers.BLAZE)), 0.5F);
    }

    protected int getBlockLight(Ignatius ignatius, BlockPos blockPos) {
        return 15;
    }

    public Identifier getTexture(Ignatius ignatius) {
        return TEXTURE;
    }
}
