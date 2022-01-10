package fr.minuskube.inv;

import net.minestom.server.extensions.Extension;

public class SmartInvsPlugin extends Extension {

    private static SmartInvsPlugin instance;
    private static InventoryManager invManager;

    public static InventoryManager getInvManager() {
        return invManager;
    }

    public static InventoryManager manager() {
        return invManager;
    }

    public static SmartInvsPlugin instance() {
        return instance;
    }

    @Override
    public void initialize() {
        instance = this;

        invManager = new InventoryManager(this);
        invManager.init();
    }

    @Override
    public void terminate() {

    }
}
