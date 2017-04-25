package com.github.dillonredding.events

open class EventArgs

typealias Handler<T> = (Any, T) -> Unit
typealias NoArgsHandler = (Any) -> Unit

abstract class AbstractEventHandler<T> {
    protected val handlers = mutableListOf<T>()

    /**
     * Adds a handler to this event handler.
     * When adding a reference to a companion object method, Companion must be specified (e.g., event += Class.Companion::method).
     * Use caution when adding top-level functions as these cannot be removed via -= (minusAssign).
     */
    operator fun plusAssign(handler: T) {
        handlers.add(handler)
    }

    /**
     * Removes a single instance of the specified event from this handler, if it is present.
     * This does not work with top-level functions.
     */
    operator fun minusAssign(handler: T) {
        handlers.remove(handler)
    }
}

class EventHandler<T : EventArgs> : AbstractEventHandler<Handler<T>>() {
    /**
     * Fires the event and notifies the associated handlers.
     */
    operator fun invoke(sender: Any, args: T) {
        handlers.forEach { it(sender, args) }
    }
}

class NoArgsEventHandler : AbstractEventHandler<NoArgsHandler>() {
    /**
     * Fires the event and notifies the associated handlers.
     */
    operator fun invoke(sender: Any) {
        handlers.forEach { it(sender) }
    }
}

inline fun <reified T : EventArgs> eventHandler(): EventHandler<T> {
    return EventHandler()
}

fun eventHandler(): NoArgsEventHandler {
    return NoArgsEventHandler()
}
