package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.specials

import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.features.value.BoolValue
import net.ccbluex.liquidbounce.features.value.FloatValue
import net.ccbluex.liquidbounce.features.value.IntegerValue
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.MovementUtils.isMoving
import net.minecraft.potion.Potion

class UniversocraftSpeed : SpeedMode("Universocraft") {


    // Custom
    private val customSpeed = BoolValue("UniversoCraft-CustomSpeed", false)
    private val custommovementfactorPOT = FloatValue("UniversoCraft-JumpMovementFactorWithPotion", 0.02f, 0.01f, 0.04f).displayable { customSpeed.get() }
    private val custommovementfactorNOPOT = FloatValue("UniversoCraft-JumpMovementFactorWithoutPotion", 0.02f, 0.01f, 0.04f).displayable { customSpeed.get() }
    private val customstrafe = FloatValue("UniversoCraft-FrictionWithPotion", 0.48f, 0.1f, 2f).displayable { customSpeed.get() }
    private val customNOstrafe = FloatValue("UniversoCraft-FrictionWithoutPotion", 0.48f, 0.1f, 2f).displayable { customSpeed.get() }
    private val customSpeedValue = FloatValue("UniversoCraft-SpeedWithPotion", 2.8f, 1f, 4f).displayable { customSpeed.get() }
    private val customSpeedNoValue = FloatValue("UniversoCraft-SpeedWhithoutPotion", 2.0f, 1f, 4f).displayable { customSpeed.get() }


    // YPort and Boost
    private val yport = BoolValue("Universocraft-YPort", false)
    private val YMotion = FloatValue("YPort-MotionY", 0.1f, 0.0f, 2.0f).displayable { yport.get() }
    private val MotionTicks = IntegerValue("YPort-Ticks", 4, 1, 10).displayable { yport.get() }
    private val boost = BoolValue("Universocraft-Damage-Boost", false)
    private val boostvalue = FloatValue("UniversoCraft-Boost-Speed", 1f, 0.1f,9f).displayable { boost.get() }

    private var ticks = 0

    override fun onEnable() {
        ticks = 0
    }

    override fun onUpdate() {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed) && isMoving()) {
            ++ticks
            mc.thePlayer.jumpMovementFactor = if (customSpeed.get()) { custommovementfactorPOT.get() } else { 0.02f }
            mc.thePlayer.speedInAir = if (customSpeed.get()) { customSpeedValue.get() / 100 } else { 0.028f }
            mc.gameSettings.keyBindJump.pressed = false
            if (boost.get() && mc.thePlayer.hurtTime == 9) {
                MovementUtils.strafe(boostvalue.get())
            }
            if (!mc.thePlayer.onGround && ticks > MotionTicks.get() && mc.thePlayer.motionY > 0 && yport.get()) {
                mc.thePlayer.motionY = -YMotion.get().toDouble()
            }
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump()
                ticks = 0
                mc.thePlayer.motionY = 0.41999998688697815
                MovementUtils.strafe(if (customSpeed.get()) { customstrafe.get() } else { 0.48f })
            }
            MovementUtils.strafe()
        } else {
            mc.thePlayer.jumpMovementFactor = if (customSpeed.get()) { custommovementfactorNOPOT.get() } else { 0.02f }
            if (boost.get() && mc.thePlayer.hurtTime == 9) {
                MovementUtils.strafe(boostvalue.get());
            }
            mc.thePlayer.speedInAir = if (customSpeed.get()) { customSpeedNoValue.get() / 100 } else { 0.02f }
            mc.gameSettings.keyBindJump.pressed = false
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump()
                mc.thePlayer.motionY = 0.41999998688697815
                MovementUtils.strafe(if (customSpeed.get()) { customNOstrafe.get() } else { 0.48f })
            }
            MovementUtils.strafe()
        }
    }
    override fun onDisable() {
        mc.thePlayer.speedInAir = 0.02f
    }

    override fun onMove(event: MoveEvent) {
    }
}

