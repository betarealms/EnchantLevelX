package cz.pmolek.enchantlevelx;

import javax.annotation.Nullable;
import org.bukkit.Material;
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
   * @param  item            the ItemStack to check
   * @param  customModelData the custom model data to compare against
   * @return                 true if the ItemStack has the custom model data, false otherwise
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
   * @param  item                  the item to check
   * @param  minCustomModelData    the minimum custom model data
   * @param  maxCustomModelData    the maximum custom model data
   * @return                       true if the item has a custom model data within the range,
   *                               false otherwise
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
}
