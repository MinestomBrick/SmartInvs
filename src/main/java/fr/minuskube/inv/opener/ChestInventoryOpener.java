package fr.minuskube.inv.opener;

import com.google.common.base.Preconditions;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;


public class ChestInventoryOpener implements InventoryOpener {

    @Override
    public Inventory open(SmartInventory inv, Player player) {
        Preconditions.checkArgument(inv.getColumns() == 9,
                "The column count for the chest inventory must be 9, found: %s.", inv.getColumns());
        Preconditions.checkArgument(inv.getRows() >= 1 && inv.getRows() <= 6,
                "The row count for the chest inventory must be between 1 and 6, found: %s", inv.getRows());

        InventoryManager manager = inv.getManager();

        InventoryType type = null;
        System.out.println(inv.getRows() * inv.getColumns());

        switch (inv.getRows() * inv.getColumns()) {
            case 9 -> type = InventoryType.CHEST_1_ROW;
            case 18 -> type = InventoryType.CHEST_2_ROW;
            case 27 -> type = InventoryType.CHEST_3_ROW;
            case 36 -> type = InventoryType.CHEST_4_ROW;
            case 45 -> type = InventoryType.CHEST_5_ROW;
            case 54 -> type = InventoryType.CHEST_6_ROW;
        }

        Inventory handle = new Inventory(type, inv.getTitle());

        fill(handle, manager.getContents(player).get());

        player.openInventory(handle);
        return handle;
    }

    @Override
    public boolean supports(InventoryType type) {
        return type == InventoryType.CHEST_1_ROW || type == InventoryType.CHEST_2_ROW ||
                type == InventoryType.CHEST_3_ROW || type == InventoryType.CHEST_4_ROW ||
                type == InventoryType.CHEST_5_ROW || type == InventoryType.CHEST_6_ROW;
    }

}
