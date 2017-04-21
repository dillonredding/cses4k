package com.github.dillonredding.events

open class EventArgs

typealias Event<T> = (Any, T) -> Unit
typealias NoArgsEvent = (Any) -> Unit

abstract class AbstractEventHandler<T> {
    protected val events = mutableListOf<T>()

    /**
     * Adds an event to this handler.
     * When adding a reference to a companion object method, Companion must be specified (e.g., event += Class.Companion::method).
     * Use caution when adding top-level functions as these cannot be removed via -= (minusAssign).
     */
    operator fun plusAssign(event: T) {
        events.add(event)
    }

    /**
     * Removes a single instance of the specified event from this handler, if it is present.
     * This does not work with top-level functions.
     */
    operator fun minusAssign(event: T) {
        events.remove(event)
    }
}

class EventHandler<T : EventArgs> : AbstractEventHandler<Event<T>>() {
    /**
     * Fires the events associated to this handler.
     */
    operator fun invoke(sender: Any, args: T) {
        events.forEach { it(sender, args) }
    }
}

class NoArgsEventHandler : AbstractEventHandler<NoArgsEvent>() {
    /**
     * Fires the events associated to this handler.
     */
    operator fun invoke(sender: Any) {
        events.forEach { it(sender) }
    }
}