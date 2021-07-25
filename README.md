# mavy

## What is it?

Keyboard interface for manipulating items, NEI and crafting. If you've ever used avy.el or vim-easymotion, this is very similar. Currently targeting 1.7.10 and GTNH. Using this mod in this state might unbind all your keys, bewarned. Also it's my first ever minecraft mod, so I don't know what I'm doing.

All keys can be customised in the forge mod config screen.

## Keys/Actions

Default keys are notated as follows [Default|One handed default].

Some keys have the ability to be prefixed with a specified number before enabling, which can modify their behavior. Clear this prefix by using the Reload/Clear prefix button.

### Master [;|w]
Access all other keys by pressing this first. Will attempt to disable other keybindings.

### Abort [e|e]
Stop the current mavy action and enable normal keybinds.

### Move [m|a]
Select the item you wish to move, then select an empty spot or free stack to put it on.

Prefix: Move this many items.

Prefix craft: Target this many items to craft.

### Recipe/Craft [c|c]
Select any avaliable item from NEI or an inventory to check its recipe. Also works inside a NEI recipe screen.

### Usage [u|v]
Select any avaliable item from NEI or an inventory to check its usage. Also works inside a NEI usage screen.

### Reload/Clear prefix [r|r]
If in sticky mode with the follwing one button modes: Shift/Drop, reload the tree to be able to select new items again.

Will also clear the current prefix number.

### Swap [s|s]
Select the item you wish to swap, then select another item to swap it with.

### Sticky [t|q]
This toggles sticky mode, which prevents mavy from exiting a mode after a successful action, which can be useful when you want to perform actions in bulk.

### Drop [x|z]
Drop a selected item stack.

### Shift [f|x]
Shift click a specified slot

### Distribute [d|d]
Select an item to distribute, then choose multiple slots to attempt to evenly distribute the item. If the stack cannot be evenly distributed, it will place the remainder back in the original slot.

Prefix: Instead of evenly distirbuting, it will just place the amount specified in each slot.
