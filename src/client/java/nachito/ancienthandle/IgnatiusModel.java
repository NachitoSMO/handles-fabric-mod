//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nachito.ancienthandle;

import java.util.Arrays;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class IgnatiusModel<T extends HostileEntity> extends SinglePartEntityModel<T> {
    private final ModelPart root;
    private final ModelPart[] rods;
    private final ModelPart head;

    public IgnatiusModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.rods = new ModelPart[12];
        Arrays.setAll(this.rods, (index) -> {
            return root.getChild(getRodName(index));
        });
    }

    private static String getRodName(int index) {
        return "part" + index;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), ModelTransform.NONE);
        float f = 0.0F;
        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(0, 16).cuboid(0.0F, 0.0F, 0.0F, 2.0F, 8.0F, 2.0F);

        int i;
        float g;
        float h;
        float j;
        for(i = 0; i < 4; ++i) {
            g = MathHelper.cos(f) * 9.0F;
            h = -2.0F + MathHelper.cos((float)(i * 2) * 0.25F);
            j = MathHelper.sin(f) * 9.0F;
            modelPartData.addChild(getRodName(i), modelPartBuilder, ModelTransform.pivot(g, h, j));
            ++f;
        }

        f = 0.7853982F;

        for(i = 4; i < 8; ++i) {
            g = MathHelper.cos(f) * 7.0F;
            h = 2.0F + MathHelper.cos((float)(i * 2) * 0.25F);
            j = MathHelper.sin(f) * 7.0F;
            modelPartData.addChild(getRodName(i), modelPartBuilder, ModelTransform.pivot(g, h, j));
            ++f;
        }

        f = 0.47123894F;

        for(i = 8; i < 12; ++i) {
            g = MathHelper.cos(f) * 5.0F;
            h = 11.0F + MathHelper.cos((float)i * 1.5F * 0.5F);
            j = MathHelper.sin(f) * 5.0F;
            modelPartData.addChild(getRodName(i), modelPartBuilder, ModelTransform.pivot(g, h, j));
            ++f;
        }

        return TexturedModelData.of(modelData, 64, 32);
    }

    public ModelPart getPart() {
        return this.root;
    }

    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float f = animationProgress * 3.1415927F * -0.1F;

        int i;
        for(i = 0; i < 4; ++i) {
            this.rods[i].pivotY = -2.0F + MathHelper.cos(((float)(i * 2) + animationProgress) * 0.25F);
            this.rods[i].pivotX = MathHelper.cos(f) * 9.0F;
            this.rods[i].pivotZ = MathHelper.sin(f) * 9.0F;
            ++f;
        }

        f = 0.7853982F + animationProgress * 3.1415927F * 0.03F;

        for(i = 4; i < 8; ++i) {
            this.rods[i].pivotY = 2.0F + MathHelper.cos(((float)(i * 2) + animationProgress) * 0.25F);
            this.rods[i].pivotX = MathHelper.cos(f) * 7.0F;
            this.rods[i].pivotZ = MathHelper.sin(f) * 7.0F;
            ++f;
        }

        f = 0.47123894F + animationProgress * 3.1415927F * -0.05F;

        for(i = 8; i < 12; ++i) {
            this.rods[i].pivotY = 11.0F + MathHelper.cos(((float)i * 1.5F + animationProgress) * 0.5F);
            this.rods[i].pivotX = MathHelper.cos(f) * 5.0F;
            this.rods[i].pivotZ = MathHelper.sin(f) * 5.0F;
            ++f;
        }

        this.head.yaw = headYaw * 0.017453292F;
        this.head.pitch = headPitch * 0.017453292F;
    }
}

