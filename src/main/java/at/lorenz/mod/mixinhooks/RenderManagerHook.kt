package at.lorenz.mod.mixinhooks

import at.lorenz.mod.events.CheckRenderEntityEvent
import net.minecraft.client.renderer.culling.ICamera
import net.minecraft.entity.Entity
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

fun shouldRender(
    entityIn: Entity,
    camera: ICamera,
    camX: Double,
    camY: Double,
    camZ: Double,
    cir: CallbackInfoReturnable<Boolean>
) {
    if (
        CheckRenderEntityEvent(
            entityIn,
            camera,
            camX,
            camY,
            camZ
        ).postAndCatch()
    ) cir.returnValue = false
}