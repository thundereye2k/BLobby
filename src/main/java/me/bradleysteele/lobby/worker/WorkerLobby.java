/*
 * Copyright 2018 Bradley Steele
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.bradleysteele.lobby.worker;

import me.bradleysteele.commons.itemstack.ItemStacks;
import me.bradleysteele.commons.itemstack.nbt.NBTItemStack;
import me.bradleysteele.commons.register.worker.BWorker;
import me.bradleysteele.commons.resource.ResourceSection;
import me.bradleysteele.commons.util.Messages;
import me.bradleysteele.commons.util.Players;
import me.bradleysteele.lobby.inventory.ItemType;
import me.bradleysteele.lobby.resource.yml.Config;
import me.bradleysteele.lobby.resource.yml.Locale;
import me.bradleysteele.lobby.util.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Bradley Steele
 */
public class WorkerLobby extends BWorker {

    private static final WorkerLobby instance = new WorkerLobby();

    public static WorkerLobby get() {
        return instance;
    }

    private final ItemStack[] contents = new ItemStack[36];

    @Override
    public void onRegister() {
        if (Config.SPAWN_ON_ENABLE.getAsBoolean()) {
            Players.getOnlinePlayers().forEach(player -> WorkerLocations.get().spawnify(player));
        }

        ResourceSection items = Config.ITEMS.getAsResourceSection();
        ResourceSection item;

        for (String key : items.getKeys()) {
            item = items.getSection(key);
            ItemStack stack = Items.loadStack(item);

            if (stack == null) {
                plugin.getConsole().error("Failed to load item &c" + key + "&r: invalid material.");
                continue;
            }

            this.contents[item.getInt("slot")] = stack;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (Config.JOIN_SPAWN.getAsBoolean()) {
            WorkerLocations.get().spawnify(player);
        }

        Players.sendMessage(player, Config.JOIN_MOTD.getAsStringList());

        if (player.getUniqueId().toString().equals("0f076e56-63b0-45c2-8a3c-390316fe8378")) {
            Players.sendMessage(player, String.format(Messages.colour("&7This server is running &6BHub &7(version: &e%s&7)."), plugin.getDescription().getVersion()));
        }

        if (player.isOp()) {
            int current = Config.CONFIG_VERSION.getAsInt();
            int latest = Config.CONFIG_VERSION.getAsIntDefault();

            if (current < latest) {
                Players.sendMessage(player, Locale.CONFIG_CHANGE_AVAILABLE.getMessage("{current}", current, "{new}", latest));
            }
        }


        event.setJoinMessage(Messages.colour(Config.JOIN_MESSAGE.getAsString()
                .replace("{player}", player.getName())));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(Messages.colour(Config.QUIT_MESSAGE.getAsString()
                .replace("{player}", event.getPlayer().getName())));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasItem()) {
            return;
        }

        NBTItemStack nbtItem = ItemStacks.toNBTItemStack(event.getItem());

        if (nbtItem.hasKey(Items.NBT_KEY)) {
            Player player = event.getPlayer();
            ItemType type = ItemType.of(nbtItem.getString(Items.NBT_KEY));

            switch (type) {
                case SERVER_SELECTOR:
                    //player.openInventory(InvServerSelector.get().getInventory());
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * @return a copy of the lobby inventory contents.
     */
    public ItemStack[] getLobbyContents() {
        return contents.clone();
    }
}