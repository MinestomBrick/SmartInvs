![SmartInvs Logo](http://minuskube.fr/img/smart-invs/smart_invs.png)

# SmartInvs
Advanced Inventory API for your Minestom.

## Features
* Inventories of any type (workbench, chest, furnace, ...)
* Customizable size when possible (chest, ...)
* Custom titles
* Allows to prevent the player from closing its inventory
* Custom listeners for the event related to the inventory
* Iterator for inventory slots
* Page system
* Util methods to fill an inventory's row/column/borders/...
* Actions when player clicks on an item
* Update methods to edit the content of the inventory every tick

## API

### Maven setup
```
repositories {
    maven { url "https://repo.jorisg.com/snapshots" }
}
```

```
dependencies {
    compileOnly 'fr.minuskube.inv:SmartInvs:1.0-SNAPSHOT'
}
```

### Usage

* [Official docs on Gitbook](https://minuskube.gitbook.io/smartinvs/)
* [javadocs](https://minestombrick.github.io/SmartInvs/)
