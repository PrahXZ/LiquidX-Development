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

object CustomRPC {
    val discordRPCModule = LiquidBounce.moduleManager[DiscordRPCModule::class.java]!!
    private val ipcClient = IPCClient(discordRPCModule.customipc.get().toLong())
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
        builder.setStartTimestamp(timestamp)
        builder.setLargeImage("${discordRPCModule.customlargeimage.get()}")
        builder.setSmallImage("${discordRPCModule.customsmallimage.get()}")
        builder.setDetails("${discordRPCModule.customdetails.get()}")
        builder.setState("${discordRPCModule.customstate.get()}")

        // Check ipc client is connected and send rpc
        if (ipcClient.status == PipeStatus.CONNECTED) ipcClient.sendRichPresence(builder.build())
    }

    fun stop() {
        if (ipcClient.status == PipeStatus.CONNECTED) ipcClient.close()
    }
}