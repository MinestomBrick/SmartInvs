package fr.minuskube.inv.button;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.Pagination;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.PlayerHeadMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class NextButton {

    public static ClickableItem get(@NotNull SmartInventory inventory, @NotNull Pagination pagination) {
        if (pagination.isLast()) return ClickableItem.empty(ItemStack.AIR);

        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD)
                .withDisplayName(Component.text("Next", NamedTextColor.YELLOW))
                .withMeta(PlayerHeadMeta.class, b -> {
                    b.skullOwner(UUID.randomUUID());
                    b.playerSkin(new PlayerSkin(
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMyY2E2NjA1NmI3Mjg2M2U5OGY3ZjMyYmQ3ZDk0YzdhMGQ3OTZhZjY5MWM5YWMzYTkxMzYzMzEzNTIyODhmOSJ9fX0=",
                            ""
                    ));
                });
        return ClickableItem.of(itemStack, (e) -> {
            inventory.open(e.getPlayer(), pagination.next().getPage());
        });
    }
}
