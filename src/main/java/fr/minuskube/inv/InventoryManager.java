package fr.minuskube.inv;

import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.opener.ChestInventoryOpener;
import fr.minuskube.inv.opener.InventoryOpener;
import fr.minuskube.inv.opener.SpecialInventoryOpener;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.extensions.Extension;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class InventoryManager {

    private final Extension extension;
    private final CommandManager pluginManager;

    private final Map<UUID, SmartInventory> inventories;
    private final Map<UUID, InventoryContents> contents;

    private final List<InventoryOpener> defaultOpeners;
    private final List<InventoryOpener> openers;

    public InventoryManager(Extension extension) {
        this.extension = extension;
        this.pluginManager = MinecraftServer.getCommandManager();

        this.inventories = new HashMap<>();
        this.contents = new HashMap<>();

        this.defaultOpeners = Arrays.asList(
                new ChestInventoryOpener(),
                new SpecialInventoryOpener()
        );

        this.openers = new ArrayList<>();
    }

    public void init(EventNode<InventoryEvent> eventNode) {
        eventNode
                .addListener(new InvListenerClose())
                .addListener(new InvListenerClick())
                .addListener(new InvListenerOpen());

        MinecraftServer.getSchedulerManager().submitTask(() -> {
            new HashMap<>(inventories).forEach((uuid, inv) -> {
                Player player = MinecraftServer.getConnectionManager().getPlayer(uuid);

                try {
                    inv.getProvider().update(player, contents.get(uuid));
                } catch (Exception e) {
                    handleInventoryUpdateError(inv, player, e);
                }
            });

            return TaskSchedule.tick(1);
        });

    }

    public Optional<InventoryOpener> findOpener(InventoryType type) {
        Optional<InventoryOpener> opInv = this.openers.stream()
                .filter(opener -> opener.supports(type))
                .findAny();

        if (!opInv.isPresent()) {
            opInv = this.defaultOpeners.stream()
                    .filter(opener -> opener.supports(type))
                    .findAny();
        }

        return opInv;
    }

    public void registerOpeners(InventoryOpener... openers) {
        this.openers.addAll(Arrays.asList(openers));
    }

    public List<Player> getOpenedPlayers(SmartInventory inv) {
        List<Player> list = new ArrayList<>();

        this.inventories.forEach((player, playerInv) -> {
            if (inv.equals(playerInv))
                list.add(MinecraftServer.getConnectionManager().getPlayer(player));
        });

        return list;
    }

    public Optional<SmartInventory> getInventory(Player p) {
        return Optional.ofNullable(this.inventories.get(p.getUuid()));
    }

    protected void setInventory(Player p, SmartInventory inv) {
        if (inv == null)
            this.inventories.remove(p.getUuid());
        else
            this.inventories.put(p.getUuid(), inv);
    }

    public Optional<InventoryContents> getContents(Player p) {
        return Optional.ofNullable(this.contents.get(p.getUuid()));
    }

    protected void setContents(Player p, InventoryContents contents) {
        if (contents == null)
            this.contents.remove(p.getUuid());
        else
            this.contents.put(p.getUuid(), contents);
    }

    public void handleInventoryOpenError(SmartInventory inventory, Player player, Exception exception) {
        inventory.close(player);


        extension.getLogger().error("Error while opening SmartInventory:", exception);
    }

    public void handleInventoryUpdateError(SmartInventory inventory, Player player, Exception exception) {
        inventory.close(player);

        extension.getLogger().error("Error while updating SmartInventory:", exception);
    }

    @SuppressWarnings("unchecked")
    class InvListenerClick implements EventListener<InventoryPreClickEvent> {

        @Override
        public @NotNull Class<InventoryPreClickEvent > eventType() {
            return InventoryPreClickEvent .class;
        }

        @Override
        public @NotNull Result run(@NotNull InventoryPreClickEvent event) {
            Player p = event.getPlayer();
            if (!inventories.containsKey(p.getUuid()))
                return Result.SUCCESS;

            // Restrict putting items from the bottom inventory into the top inventory
            Inventory clickedInventory = event.getInventory();
            if (clickedInventory == p.getOpenInventory()) {
                if (event.getClickType() == ClickType.DOUBLE_CLICK || event.getClickType() == ClickType.SHIFT_CLICK) {
                    event.setCancelled(true);
                    return Result.INVALID;
                }
            }

            if (clickedInventory == p.getOpenInventory()) {
                event.setCancelled(true);
                int row = event.getSlot() / 9;
                int column = event.getSlot() % 9;

                if (row < 0 || column < 0)
                    return Result.INVALID;

                SmartInventory inv = inventories.get(p.getUuid());

                if (row >= inv.getRows() || column >= inv.getColumns())
                    return Result.INVALID;

                inv.getListeners().stream()
                        .filter(listener -> listener.getType() == InventoryClickEvent.class)
                        .forEach(listener -> ((InventoryListener<InventoryPreClickEvent>) listener).accept(event));

                contents.get(p.getUuid()).get(row, column).ifPresent(item -> item.run(event));

                p.getInventory().update();

            }
            return Result.SUCCESS;
        }
    }

    class InvListenerOpen implements EventListener<InventoryOpenEvent> {


        @Override
        public @NotNull Class<InventoryOpenEvent> eventType() {
            return InventoryOpenEvent.class;
        }

        @Override
        public @NotNull Result run(@NotNull InventoryOpenEvent event) {
            Player p = event.getPlayer();

            if (!inventories.containsKey(p.getUuid()))
                return Result.INVALID;

            SmartInventory inv = inventories.get(p.getUuid());

            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == InventoryOpenEvent.class)
                    .forEach(listener -> ((InventoryListener<InventoryOpenEvent>) listener).accept(event));
            return Result.SUCCESS;
        }

    }

    class InvListenerClose implements EventListener<InventoryCloseEvent> {

        @Override
        public @NotNull Class<InventoryCloseEvent> eventType() {
            return InventoryCloseEvent.class;
        }

        @Override
        public @NotNull Result run(@NotNull InventoryCloseEvent event) {
            Player p = event.getPlayer();

            if (!inventories.containsKey(p.getUuid()))
                return Result.INVALID;

            SmartInventory inv = inventories.get(p.getUuid());

            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == InventoryCloseEvent.class)
                    .forEach(listener -> ((InventoryListener<InventoryCloseEvent>) listener).accept(event));

            if (inv.isCloseable()) {
                event.getInventory().clear();

                inventories.remove(p.getUuid());
                contents.remove(p.getUuid());
            } else
                MinecraftServer.getSchedulerManager()
                        .scheduleNextTick(() -> {
                            p.openInventory(event.getInventory());
                        });
            return Result.SUCCESS;
        }
    }
}

