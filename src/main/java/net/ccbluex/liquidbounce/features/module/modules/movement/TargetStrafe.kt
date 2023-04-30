// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.features.value.*
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.MathHelper
import org.lwjgl.input.Keyboard
import net.minecraft.util.Vec3;

@ModuleInfo(name = "TargetStrafe",  category = ModuleCategory.MOVEMENT)
class TargetStrafe : Module() {
    val strafeRange = FloatValue("Range", 2.0f, 0.1f, 4.0f) // esto es para saber a que distancia del jugador se calculara la direccion
    private val Jump = BoolValue("Jump", true) // Esto es para que no te caigas al vacio
    private val safeWalk = BoolValue("SafeWalk", true) // Esto es para que no te caigas al vacio
    private val moveSpeed = FloatValue("MoveSpeed", 0.5f, 0.1f, 2.0f)
    val killAura = LiquidBounce.moduleManager.getModule(KillAura::class.java)
    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (event.eventState == EventState.PRE) {
            if (Jump.get() && !mc.gameSettings.keyBindJump.isKeyDown()) {
                return
            }
            val target = getTarget() ?: return

            //   val speed = getSpeed(direction)
            if (safeWalk.get() && checkVoid()) {
                return

                // Desactivar todas las teclas a w d s
            }
            if(killAura?.target != null) {
                val angle = Math.toRadians(mc.thePlayer.rotationYaw.toDouble())
                val x = Math.cos(angle) * strafeRange.get() + target.posX
                val z = Math.sin(angle) * strafeRange.get() + target.posZ
                val y = target.posY
                val newTarget = Vec3(x, y, z)
                val newDirection = newTarget.subtract(mc.thePlayer.getPositionEyes(1.0f)).normalize()
                mc.thePlayer.motionX = newDirection.xCoord * moveSpeed.get()
                mc.thePlayer.motionZ = newDirection.zCoord * moveSpeed.get()
            }

        }
    }

    private fun getTarget(): EntityLivingBase? {
        var target: EntityLivingBase? = null
        var distance = Float.MAX_VALUE
        for (entity in mc.theWorld.loadedEntityList) {
            if (entity is EntityLivingBase && entity != mc.thePlayer && entity.isEntityAlive && !entity.isInvisible) {
                val currentDistance = mc.thePlayer.getDistanceToEntity(entity)
                if (currentDistance < distance && currentDistance <= strafeRange.get()) {
                    target = entity
                    distance = currentDistance
                }
            }
        }
        return target
    }

    private fun getDirection(target: EntityLivingBase): Vec3? {
        val x = target.posX - mc.thePlayer.posX
        val z = target.posZ - mc.thePlayer.posZ
        val direction = Vec3(x, 0.0, z).normalize()
        return if (direction.xCoord.isNaN() || direction.zCoord.isNaN()) null else direction
    }
    /*
     private fun getSpeed(direction: Vec3?): Double {
         return if (direction == null) 0.0 else mc.thePlayer.getDistanceToEntity(mc.thePlayer.ridingEntity ?: mc.thePlayer) * 0.15
     }
     */
    private fun checkVoid(): Boolean {
        for (x in -1..0) {
            for (z in -1..0) {
                if (isVoid(x, z)) {
                    return true
                }
            }
        }
        return false
    }

    private fun isVoid(x: Int, z: Int): Boolean {
        if (mc.thePlayer.posY < 0.0) {
            return true
        }
        var off = 0
        while (off < mc.thePlayer.posY.toInt() + 2) {
            val bb = mc.thePlayer.entityBoundingBox.offset(x.toDouble(), (-off).toDouble(), z.toDouble())
            if (mc.theWorld!!.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                off += 2
                continue
            }
            return false
            off += 2
        }
        return true
    }

}


