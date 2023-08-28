package cz.pmolek.enchantlevelx;

import java.util.Map;
import javax.annotation.Nullable;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * Utilities for item manipulation.
 */
public final class ItemUtils {
  private ItemUtils() {
  }

  /**
   * Check if the given item has specific custom model data.
   *
   * @param item            the ItemStack to check
   * @param customModelData the custom model data to compare against
   * @return true if the ItemStack has the custom model data, false otherwise
   */
  public static boolean hasCustomModelData(@Nullable ItemStack item, int customModelData) {
    return item != null
        && item.hasItemMeta()
        && item.getItemMeta().hasCustomModelData()
        && item.getItemMeta().getCustomModelData() == customModelData;
  }

  /**
   * Checks if the given item has a custom model data within the specified range.
   *
   * @param item               the item to check
   * @param minCustomModelData the minimum custom model data
   * @param maxCustomModelData the maximum custom model data
   * @return true if the item has a custom model data within the range,
   *          false otherwise
   */
  public static boolean hasCustomModelDataInRange(@Nullable ItemStack item, int minCustomModelData,
                                                  int maxCustomModelData) {
    return item != null
        && item.hasItemMeta()
        && item.getItemMeta().hasCustomModelData()
        && item.getItemMeta().getCustomModelData() >= minCustomModelData
        && item.getItemMeta().getCustomModelData() <= maxCustomModelData;
  }

  /**
   * Checks if the given item is an enchanted book, is not a charm and is not charmed.
   *
   * @param item the ItemStack to check
   * @return true if the given item is an enchanted book, is not a charm and is not charmed,
   *          false otherwise.
   */
  public static boolean isEnchantedBook(@Nullable ItemStack item) {
    return item != null
        && item.getType() == Material.ENCHANTED_BOOK
        && !hasCustomModelDataInRange(item, CharmModelData.CHARM_I.modelData,
        CharmModelData.CHARM_III.modelData)
        && !hasCustomModelDataInRange(item, CharmModelData.CHARMED_BOOK_I.modelData,
        CharmModelData.CHARMED_BOOK_III.modelData);
  }

  /**
   * Checks if the given item is of the specified material type.
   *
   * @param item     the item to check
   * @param material the material type to compare against
   * @return true if the item is of the specified material type, false otherwise
   */
  public static boolean isType(@Nullable ItemStack item, Material material) {
    return item != null && item.getType() == material;
  }

  /**
   * Returns a string representation of the given item, or an empty string if the item is null.
   *
   * @param  item  the ItemStack to convert to a string
   * @return       a string representation of the item, or an empty string if the item is null
   */
  public static String toStringSafe(@Nullable ItemStack item) {
    return item == null ? "" : item.toString();
  }

  /**
   * Determines if two items are equal by their values.
   *
   * @implNote Compares type, amount and enchantments.
   *
   * @param  item1 the first ItemStack object
   * @param  item2 the second ItemStack object
   * @return true if the two ItemStack objects have the same values, false otherwise
   */
  public static boolean areEqualByValues(@Nullable ItemStack item1, @Nullable ItemStack item2) {
    if (item1 == null || item2 == null) {
      return item1 == item2;
    }

    if (item1.getType() != item2.getType()) {
      return false;
    }
    if (item1.getAmount() != item2.getAmount()) {
      return false;
    }
    if (!compareEnchants(EnchantmentUtils.getEnchantmentsUnsafe(item1),
        EnchantmentUtils.getEnchantmentsUnsafe(item2))) {
      return false;
    }
    return true;
  }

  /**
   * Compares two maps of enchantments and their corresponding levels.
   *
   * @param a the first map of enchantments and levels to compare
   * @param b the second map of enchantments and levels to compare
   * @return true if the maps are equal, false otherwise
   */
  public static boolean compareEnchants(Map<Enchantment, Integer> a, Map<Enchantment, Integer> b) {
    for (Map.Entry<Enchantment, Integer> entry : a.entrySet()) {
      if (!b.containsKey(entry.getKey())) {
        return false;
      }
      if (!entry.getValue().equals(b.get(entry.getKey()))) {
        return false;
      }
    }
    return true;
  }
}
