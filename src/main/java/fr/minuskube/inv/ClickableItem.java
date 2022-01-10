package fr.minuskube.inv;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;

import java.util.function.Consumer;

public class ClickableItem {

    private ItemStack item;
    private Consumer<InventoryPreClickEvent> consumer;

    private ClickableItem(ItemStack item, Consumer<InventoryPreClickEvent> consumer) {
        this.item = item;
        this.consumer = consumer;
    }

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
