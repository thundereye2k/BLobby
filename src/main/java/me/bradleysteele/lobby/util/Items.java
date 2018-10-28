package me.bradleysteele.lobby.util;

import me.bradleysteele.commons.itemstack.ItemStacks;
import me.bradleysteele.commons.resource.ResourceSection;
import me.bradleysteele.lobby.inventory.ItemType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Bradley Steele
 */
public class Items {

    public final static String NBT_KEY = "blobby_item_type";

    private Items() {}

    public static ItemStack loadStack(ResourceSection section) {
        Material material = Material.matchMaterial(section.getString("material", "AIR"));

        if (material == Material.AIR) {
            return null;
        }

        int amount = section.getInt("amount", 1);

        return ItemStacks.builder(material)
                .withDurability(section.getShort("damage"))
                .withAmount(amount < 1 ? 1 : amount > 64 ? 64 : amount)
                .withDisplayNameColoured(section.getString("name"))
                .withLoreColoured(section.getStringList("lore"))
                .withNBTString(NBT_KEY, ItemType.of(section.getString("type", "DEFAULT")).name())
                .build();
    }
}