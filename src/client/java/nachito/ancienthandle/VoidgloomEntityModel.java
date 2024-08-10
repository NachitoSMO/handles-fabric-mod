package nachito.ancienthandle;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.mob.PathAwareEntity;

public class VoidgloomEntityModel<V extends PathAwareEntity> extends BipedEntityModel<V> {

    public boolean carryingBlock;
    public boolean angry;
    public VoidgloomEntityModel(ModelPart root) {
        super(root);
    }

    public void setAngles(V livingEntity, float f, float g, float h, float i, float j) {
        super.setAngles(livingEntity, f, g, h, i, j);
        this.head.visible = true;
        boolean k = true;
        this.body.pitch = 0.0F;
        this.body.pivotY = -14.0F;
        this.body.pivotZ = -0.0F;
        ModelPart var10000 = this.rightLeg;
        var10000.pitch -= 0.0F;
        var10000 = this.leftLeg;
        var10000.pitch -= 0.0F;
        var10000 = this.rightArm;
        var10000.pitch *= 0.5F;
        var10000 = this.leftArm;
        var10000.pitch *= 0.5F;
        var10000 = this.rightLeg;
        var10000.pitch *= 0.5F;
        var10000 = this.leftLeg;
        var10000.pitch *= 0.5F;
        float l = 0.4F;
        if (this.rightArm.pitch > 0.4F) {
            this.rightArm.pitch = 0.4F;
        }

        if (this.leftArm.pitch > 0.4F) {
            this.leftArm.pitch = 0.4F;
        }

        if (this.rightArm.pitch < -0.4F) {
            this.rightArm.pitch = -0.4F;
        }

        if (this.leftArm.pitch < -0.4F) {
            this.leftArm.pitch = -0.4F;
        }

        if (this.rightLeg.pitch > 0.4F) {
            this.rightLeg.pitch = 0.4F;
        }

        if (this.leftLeg.pitch > 0.4F) {
            this.leftLeg.pitch = 0.4F;
        }

        if (this.rightLeg.pitch < -0.4F) {
            this.rightLeg.pitch = -0.4F;
        }

        if (this.leftLeg.pitch < -0.4F) {
            this.leftLeg.pitch = -0.4F;
        }

        if (this.carryingBlock) {
            this.rightArm.pitch = -0.5F;
            this.leftArm.pitch = -0.5F;
            this.rightArm.roll = 0.05F;
            this.leftArm.roll = -0.05F;
        }

        this.rightLeg.pivotZ = 0.0F;
        this.leftLeg.pivotZ = 0.0F;
        this.rightLeg.pivotY = -5.0F;
        this.leftLeg.pivotY = -5.0F;
        this.head.pivotZ = -0.0F;
        this.head.pivotY = -13.0F;
        this.hat.pivotX = this.head.pivotX;
        this.hat.pivotY = this.head.pivotY;
        this.hat.pivotZ = this.head.pivotZ;
        this.hat.pitch = this.head.pitch;
        this.hat.yaw = this.head.yaw;
        this.hat.roll = this.head.roll;

        boolean n = true;
        this.rightArm.setPivot(-5.0F, -12.0F, 0.0F);
        this.leftArm.setPivot(5.0F, -12.0F, 0.0F);
        if (this.angry) {
            float m = 1.0F;
            var10000 = this.head;
            var10000.pivotY -= 5.0F;
        }
    }
}
