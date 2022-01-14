package fr.minuskube.inv;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;

import java.util.function.Consumer;

public record ClickableItem(ItemStack item,
                            Consumer<InventoryPreClickEvent> consumer) {

    public static ClickableItem empty(ItemStack item) {
        return of(item, e -> {
        });
    }

    public static ClickableItem of(ItemStack item, Consumer<InventoryPreClickEvent> consumer) {
        return new ClickableItem(item, consumer);
    }

    public void run(InventoryPreClickEvent e) {
        consumer.accept(e);
    }

    public ItemStack getItem() {
        return item;
    }

}
