package cz.pmolek.enchantlevelx.lootdefinitions;

import cz.pmolek.enchantlevelx.CharmModelData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public final class CharmFactory {
    private CharmFactory() {}
    public static ItemStack createBook(CharmModelData data) {
        if (!data.isCharm)
            throw new IllegalArgumentException("Item is not a charm");

        // Create new item
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();

        // Set custom properties
        // Item name and lore
        List<String> lore = new ArrayList<>();
        switch (data.level) {
            case 1:
                meta.setDisplayName(data.color + "Charm of Enchanting I");
                lore.add(ChatColor.GRAY + "Combine with a " + ChatColor.WHITE + "max-level" + ChatColor.GRAY + " Enchanted Book.");
                break;
            case 2:
                meta.setDisplayName(data.color + "Charm of Enchanting II");
                lore.add(ChatColor.GRAY + "Combine with an Enchanted Book charmed by " + ChatColor.YELLOW + "Charm I" + ChatColor.GRAY + ".");
                break;
            case 3:
                meta.setDisplayName(data.color + "Charm of Enchanting III");
                lore.add(ChatColor.GRAY + "Combine with an Enchanted Book charmed by " + ChatColor.AQUA + "Charm II" + ChatColor.GRAY + ".");
                break;
            default:
                meta.setDisplayName(ChatColor.WHITE + "Charm of Enchanting Debug Item");
                break;

        }
        lore.add(ChatColor.GRAY + "Raises enchantment by one level.");
        lore.add(ChatColor.GRAY + "Ignores vanilla enchantments limits.");
        lore.add(ChatColor.DARK_GRAY + "Use in an anvil.");

        // CustomModelData for Resource Pack purposes
        // 112000 is the "prefix" for this plugin, 1 is item ID for Charm of Enchanting, then level is added
        meta.setCustomModelData(data.modelData);

        // Make it appear enchanted
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        // Save the metadata
        meta.setLore(lore);
        book.setItemMeta(meta);

        return book;
    }
}
