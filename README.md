## Android Widgets

What is this? Another set of Android libraries with widgets and utility classes. 
You probably don't need it, because you already have something like this in your codebase, 
but just in case you are interested:

### Swap Layout

SwapLayout always displays a single child view, but it can temporarily hold more views when they are switching. 
The most important thing is that the switching happens automatically and the layout adds a nice set of
animations to it, so you can easily use it in places where you need to swap content but don't have time to
think of clever animations.

### Swap Image Button

Based on SwapLayout, but adapted to a more specific purpose - multi-purpose buttons. The advantage is that 
SwapImageButton has parcelable state and therefore can be managed much more easily. Instead of swaping views, you 
just swap foreground/background drawable resources.
