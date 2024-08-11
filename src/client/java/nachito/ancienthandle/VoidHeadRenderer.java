package nachito.ancienthandle;

import nachito.ancienthandle.entity.VoidHead;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;


public class VoidHeadRenderer extends MobEntityRenderer<VoidHead, VoidHeadEntityModel<VoidHead>> {

    private static final Identifier TEXTURE = Identifier.of("nachito", "textures/entity/voidhead.png");

    public VoidHeadRenderer(EntityRendererFactory.Context context) {
        super(context, new VoidHeadEntityModel<>(context.getPart(NachitoModelLayers.VOIDHEAD_LAYER)), 0.2f);
    }

    @Override
    public Identifier getTexture(VoidHead voidhead) {
        return TEXTURE;
    }
}