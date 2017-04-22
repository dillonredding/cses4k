# Kotlin Events
This is a library that allows for C#-style events in Kotlin.

## C# Event Simulation
### Event Arguments
You can define your own event arguments by extending the `EventArgs` class.
```kotlin
class MyEventArgs(val arg1: Int, val arg2: String) : EventArgs()
```

### Event Handlers
There are two types of event handlers: `EventHandler<T>` and `NoArgsEventHandler`.

Unlike .NET, there are distinct names for the args and no-args versions.
This is due to the Java's implementation of generics as syntactic sugar for casting.

Luckily, we can still simulate the usage of .NET events.

```kotlin
class EventDrivenObject {
    val someEvent = EventHandler<MyEventArgs>()

    fun eventFiringMethod() {
        someEvent(this, MyEventArgs(42, "foo"))
    }
}
```

For events with no data, we use the `NoArgsEventHandler`.
```kotlin
class EventDrivenObject {
    val someEvent = NoArgsEventHandler()

    fun eventFiringMethod() {
        someEvent(this)
    }
}
```

#### Adding
We can add handlers to our `EventHandler`  with the `+=` operator.
`Handler<T>` is simply a type alias for the function type `(Any, T) -> Unit` where `T` is a subclass of `EventArgs`.
`NoArgsHandler` represents the function type `(Any) - Unit`. 
As such, we can add any of the following to an event handler:
- **Lambdas**
- **Top-level functions**
  - You *cannot* remove top-level functions they've been added.
- **Instance methods**
- **Companion object methods**
  - When adding a reference to a companion object method, you have to qualify the companion object (see the example below).
- **Object methods**

In the example below, all the various `handleEvent` functions have the same type: `(Any, MyEventArgs) -> Unit`
```kotlin
fun main(args: Array<String>) {
    val edo = EventDrivenObject()
    edo.someEvent += { sender, eventArgs -> /*...*/ } // lambda
    edo.someEvent += ::handleEvent                    // top-level function
    edo.someEvent += Foo()::handleEvent               // instance method
    edo.someEvent += Foo.Companion::handleEvent       // companion object method
    edo.someEvent += Bar::handleEvent                 // object method
    edo.eventFiringMethod()
}
```

Handlers for a `NoArgsEventHandler` need the type `(Any) -> Unit`.

#### Removing
We can remove handlers with the `-=` operator. Note that you cannot remove a top-level function.
```kotlin
fun main(args: Array<String>) {
    val edo = EventDrivenObject()
    
    val foo = Foo()
    edo.someEvent += foo::handleEvent   // someEvent has 1 handler
    edo.someEvent += foo::handleEvent   // someEvent has 2 handlers
    edo.someEvent -= foo::handleEvent   // someEvent has 1 handler
    
    edo.eventFiringMethod()
}
```