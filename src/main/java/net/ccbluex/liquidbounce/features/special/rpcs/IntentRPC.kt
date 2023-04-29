package net.ccbluex.liquidbounce.features.special.rpcs

import com.jagrosh.discordipc.IPCClient
import com.jagrosh.discordipc.IPCListener
import com.jagrosh.discordipc.entities.RichPresence
import com.jagrosh.discordipc.entities.pipe.PipeStatus
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.client.DiscordRPCModule
import net.ccbluex.liquidbounce.utils.*
import org.json.JSONObject
import java.time.OffsetDateTime
import kotlin.concurrent.thread

object IntentRPC {
    private val ipcClient = IPCClient(863062476882313226)
    private val timestamp = OffsetDateTime.now()
    private var running = false


    fun run() {
        ipcClient.setListener(object : IPCListener {
            override fun onReady(client: IPCClient?) {
                running = true
                thread {
                    while (running) {
                        update()
                        try {
                            Thread.sleep(1000L)
                        } catch (ignored: InterruptedException) {
                        }
                    }
                }
            }

            override fun onClose(client: IPCClient?, json: JSONObject?) {
                running = false
            }
        })
        try {
            ipcClient.connect()
        } catch (e: Exception) {
            ClientUtils.logError("DiscordRPC failed to start")
        } catch (e: RuntimeException) {
            ClientUtils.logError("DiscordRPC failed to start")
        }
    }

    private fun update() {
        val builder = RichPresence.Builder()
        val discordRPCModule = LiquidBounce.moduleManager[DiscordRPCModule::class.java]!!
        builder.setStartTimestamp(timestamp)
        builder.setLargeImage("intentbig")
        builder.setSmallImage("intentlight")
        builder.setDetails("Version V12.5")
        ServerUtils.getRemoteIp().also {
            val str = (if(discordRPCModule.intentclient.equals("Rise")) "Playing Rise" else "\n") + (if(discordRPCModule.intentclient.equals("ZeroDay")) "Playing ZeroDay" else "\n") + (if(discordRPCModule.intentclient.equals("Tenacity")) "Playing Tenacity" else "\n") + (if(discordRPCModule.intentclient.equals("Juul")) "Playing Juul" else "\n")
            builder.setState(str)
        }

        // Check ipc client is connected and send rpc
        if (ipcClient.status == PipeStatus.CONNECTED) ipcClient.sendRichPresence(builder.build())
    }

    fun stop() {
        if (ipcClient.status == PipeStatus.CONNECTED) ipcClient.close()
    }
}