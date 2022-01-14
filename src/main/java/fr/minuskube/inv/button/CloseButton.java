package fr.minuskube.inv.button;

import fr.minuskube.inv.ClickableItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.PlayerHeadMeta;

import java.util.UUID;

public class CloseButton {

    public static ClickableItem get() {
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD)
                .withDisplayName(Component.text("Close", NamedTextColor.RED))
                .withMeta(PlayerHeadMeta.class, b -> {
                    b.skullOwner(UUID.randomUUID());
                    b.playerSkin(new PlayerSkin(
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==",
                            ""
                    ));
                });

        return ClickableItem.of(itemStack, event -> {
            event.getPlayer().closeInventory();
        });
    }

}
