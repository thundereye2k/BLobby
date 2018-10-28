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

package me.bradleysteele.lobby.util;

import me.bradleysteele.commons.itemstack.ItemStackBuilder;
import me.bradleysteele.commons.itemstack.ItemStacks;
import me.bradleysteele.commons.resource.ResourceSection;
import me.bradleysteele.lobby.inventory.ItemType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Bradley Steele
 */
public class Items {

    public final static String NBT_KEY_TYPE = "blobby_item_type";
    public final static String NBT_KEY_SERVER = "blobby_server";

    private Items() {}

    public static ItemStack loadStack(ResourceSection section) {
        ItemStackBuilder builder = loadBuilder(section);
        return builder != null ? builder.build() : null;
    }

    public static ItemStackBuilder loadBuilder(ResourceSection section) {
        Material material = Material.matchMaterial(section.getString("material", "AIR"));

        if (material == null || material == Material.AIR) {
            return null;
        }

        int amount = section.getInt("amount", 1);

        return ItemStacks.builder(material)
                .withDurability(section.getShort("damage"))
                .withAmount(amount < 1 ? 1 : amount > 64 ? 64 : amount)
                .withDisplayNameColoured(section.getString("name"))
                .withLoreColoured(section.getStringList("lore"))
                .withNBTString(NBT_KEY_TYPE, ItemType.of(section.getString("type", "DEFAULT")).name());
    }
}