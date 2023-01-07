// LiquidX Development by PrahXZ and Haflin with FDP Base modified. v2.0 R1
package net.ccbluex.liquidbounce.event

open class Event

open class CancellableEvent : Event() {

    /**
     * Let you know if the event is cancelled
     *
     * @return state of cancel
     */
    var isCancelled: Boolean = false
        private set

    /**
     * Allows you to cancel a event
     */
    fun cancelEvent() {
        isCancelled = true
    }
}

enum class EventState(val stateName: String) {
    PRE("PRE"), POST("POST")
}
