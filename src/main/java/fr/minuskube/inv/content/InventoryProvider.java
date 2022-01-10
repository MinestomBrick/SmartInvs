package fr.minuskube.inv.content;

import net.minestom.server.entity.Player;

public interface InventoryProvider {

    void init(Player player, InventoryContents contents);
    default void update(Player player, InventoryContents contents) {}

}
