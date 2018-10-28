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

package me.bradleysteele.lobby.inventory;

import me.bradleysteele.commons.inventory.BInventory;
import me.bradleysteele.commons.itemstack.ItemStackBuilder;
import me.bradleysteele.commons.resource.ResourceSection;
import me.bradleysteele.commons.util.Messages;
import me.bradleysteele.commons.util.logging.StaticLog;
import me.bradleysteele.lobby.resource.ResourceType;
import me.bradleysteele.lobby.resource.Resources;
import me.bradleysteele.lobby.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Bradley Steele
 */
public class InvServerSelector implements BInventory {

    private static final InvServerSelector instance = new InvServerSelector();

    public static InvServerSelector get() {
        return instance;
    }

    private final Inventory inventory;

    private InvServerSelector() {
        ResourceSection menu = Resources.get().getResource(ResourceType.SERVER_SELECTOR).getSection("menu");

        this.inventory = Bukkit.createInventory(this, menu.getInt("size"), Messages.colour(menu.getString("title")));

        ResourceSection items = menu.getSection("items");
        ResourceSection item;

        for (String key : items.getKeys()) {
            item = items.getSection(key);
            ItemStackBuilder builder = Items.loadBuilder(item.getSection("item"));

            if (builder == null) {
                StaticLog.error("Failed to load item &c" + key + "&r: invalid material.");
                continue;
            }

            builder.withNBTString(Items.NBT_KEY_SERVER, item.getString("server"));
            inventory.setItem(item.getInt("slot"), builder.build());
        }
    }

    @Override
    public void onClick(InventoryClickEvent event, Player clicker, ItemStack stack) {
        event.setCancelled(true);
    }

    @Override
    public void onDrag(InventoryDragEvent event, Player clicker, ItemStack stack) {
        event.setCancelled(true);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}