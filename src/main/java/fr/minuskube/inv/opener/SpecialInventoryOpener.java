package fr.minuskube.inv.opener;

import com.google.common.collect.ImmutableList;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import java.util.List;

public class SpecialInventoryOpener implements InventoryOpener {

    private static final List<InventoryType> SUPPORTED = ImmutableList.of(
            InventoryType.FURNACE,
            InventoryType.BREWING_STAND,
            InventoryType.ANVIL,
            InventoryType.BEACON,
            InventoryType.HOPPER,
            InventoryType.CHEST_1_ROW,
            InventoryType.CHEST_2_ROW,
            InventoryType.CHEST_3_ROW,
            InventoryType.CHEST_4_ROW,
            InventoryType.CHEST_5_ROW,
            InventoryType.CHEST_6_ROW
    );

    @Override
    public Inventory open(SmartInventory inv, Player player) {
        InventoryManager manager = inv.getManager();
        Inventory handle = new Inventory(inv.getType(), inv.getTitle());

        fill(handle, manager.getContents(player).get());

        player.openInventory(handle);
        return handle;
    }

    @Override
    public boolean supports(InventoryType type) {
        return SUPPORTED.contains(type);
    }

}
