package fr.minuskube.inv.button;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.PlayerHeadMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BackButton {

    public static ClickableItem get(@NotNull SmartInventory inventory) {
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD)
                .withDisplayName(Component.text("Back", NamedTextColor.GRAY))
                .withMeta(PlayerHeadMeta.class, b -> {
                    b.skullOwner(UUID.randomUUID());
                    b.playerSkin(new PlayerSkin(
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTQyZmRlOGI4MmU4YzFiOGMyMmIyMjY3OTk4M2ZlMzVjYjc2YTc5Nzc4NDI5YmRhZGFiYzM5N2ZkMTUwNjEifX19",
                            ""
                    ));
                });
        return ClickableItem.of(itemStack, (e) -> {
            inventory.open(e.getPlayer());
        });
    }

}
