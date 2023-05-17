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
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.MathHelper
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import java.awt.Color

@ModuleInfo(name = "TargetStrafe",  category = ModuleCategory.MOVEMENT)
class TargetStrafe : Module() {
    val range = FloatValue("Range", 2.0f, 0.1f, 6.0f)
    private val moveSpeed = FloatValue("MoveSpeed", 0.5f, 0.1f, 0.40f)
    private val modeValue = ListValue("KeyMode", arrayOf("Jump", "None"), "Jump")
    private val safewalk = BoolValue("SafeWalk", true)
    val behind = BoolValue("Behind", false)
  //  val CircleRange = BoolValue("CircleRange", false)
    val thirdPerson = BoolValue("ThirdPerson", false)
    /*
    val killAura = LiquidBounce.moduleManager.getModule(KillAura::class.java)
    val speed = LiquidBounce.moduleManager.getModule(Speed::class.java)
    val longJump = LiquidBounce.moduleManager.getModule(LongJump::class.java)

   */
    val flight = LiquidBounce.moduleManager.getModule(Fly::class.java)
    var direction = 1
    var lastView = 0
    var hasChangedThirdPerson = true

    override fun onEnable() {
        hasChangedThirdPerson = true
        lastView = mc.gameSettings.thirdPersonView
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        /*
        val target = getTarget()
        if (target != null) {
            if (CircleRange.get()) {
                RenderUtils.drawMarkCircleTargetStrafe(target, Color.white)

            }
        }

         */
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {

        if (thirdPerson.get()) { // smart change back lol
            if (getTarget() != null) {
                if (hasChangedThirdPerson) lastView = mc.gameSettings.thirdPersonView
                mc.gameSettings.thirdPersonView = 1
                hasChangedThirdPerson = false
            } else if (!hasChangedThirdPerson) {
                mc.gameSettings.thirdPersonView = lastView
                hasChangedThirdPerson = true
            }
        }

        if (event.eventState == EventState.PRE) {
            if (mc.thePlayer.isCollidedHorizontally || safewalk.get() && checkVoid() && !flight!!.state)
                this.direction = -this.direction
        }
    }

    @EventTarget
    fun onMove(event: MoveEvent) {

        if (getTarget() != null) {
            //     ClientUtils.logError("Debug " + getTarget())
            //     strafe(event, MovementUtils.getSpeed().toInt())
            //    ClientUtils.logError("Debug " + strafe(event, MovementUtils.getSpeed().toInt()))
            val target = getTarget()

            val rotYaw = RotationUtils.getRotationsEntity(target!!).yaw

            if (mc.thePlayer.getDistanceToEntity(target) <= 1.5)
                MovementUtils.setSpeed(event, moveSpeed.get().toDouble(), rotYaw, direction.toDouble(), 0.0)
            else
                MovementUtils.setSpeed(event, moveSpeed.get().toDouble(), rotYaw, direction.toDouble(), 1.0)

            if (behind.get()) {
                val xPos: Double = target.posX + -Math.sin(Math.toRadians(target.rotationYaw.toDouble())) * -2
                val zPos: Double = target.posZ + Math.cos(Math.toRadians(target.rotationYaw.toDouble())) * -2
                event.x = ((3 * -MathHelper.sin(
                        Math.toRadians(RotationUtils.getRotations1(xPos, target.posY, zPos)[0].toDouble())
                                .toFloat()
                )).toDouble())
                event.z = ((3 * MathHelper.cos(
                        Math.toRadians(RotationUtils.getRotations1(xPos, target.posY, zPos)[0].toDouble())
                                .toFloat()
                )).toDouble())
            } else {
                if (mc.thePlayer.getDistanceToEntity(target) <= range.get())
                    MovementUtils.setSpeed(event, moveSpeed.get().toDouble(), rotYaw, direction.toDouble(), 0.0)
                else
                    MovementUtils.setSpeed(event, moveSpeed.get().toDouble(), rotYaw, direction.toDouble(), 1.0)
            }
            if (safewalk.get() && checkVoid() && !flight!!.state)
                event.isSafeWalk = true
        }
        /*
        if(LiquidBounce.moduleManager[KillAura::class.java]!!.state) {
            ClientUtils.displayChatMessage("Debug funcionas mierda?")
        }
        */


    }

    private fun getTarget(): EntityLivingBase? {
        var target: EntityLivingBase? = null
        var distance = Float.MAX_VALUE
        if(LiquidBounce.moduleManager[KillAura::class.java]!!.hitable) {
            for (entity in mc.theWorld.loadedEntityList) {
                if (entity is EntityLivingBase && entity != mc.thePlayer && entity.isEntityAlive && !entity.isInvisible) {
                    val currentDistance = mc.thePlayer.getDistanceToEntity(entity)
                    if (currentDistance < distance && currentDistance <= range.get()) {
                        target = entity
                        distance = currentDistance
                    }
                }
            }
        }




        return target
    }

    val keyMode: Boolean
        get() = when (modeValue.get()) {
            "Jump" -> Keyboard.isKeyDown(Keyboard.KEY_SPACE)
            "None" -> mc.thePlayer.movementInput.moveStrafe != 0f || mc.thePlayer.movementInput.moveForward != 0f
            else -> false
        }



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

    private fun isVoid(X: Int, Z: Int): Boolean {
        if (mc.thePlayer.posY < 0.0) {
            return true
        }
        var off = 0
        while (off < mc.thePlayer.posY.toInt() + 2) {
            val bb: AxisAlignedBB = mc.thePlayer.entityBoundingBox.offset(X.toDouble(), (-off).toDouble(), Z.toDouble())
            if (mc.theWorld!!.getCollidingBoundingBoxes(mc.thePlayer as Entity, bb).isEmpty()) {
                off += 2
                continue
            }
            return false
            off += 2
        }
        return true
    }
}