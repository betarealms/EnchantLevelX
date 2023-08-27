package cz.pmolek.enchantlevelx;

import javax.annotation.Nullable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class ItemUtils {
  private ItemUtils() {
  }

  public static boolean hasCustomModelData(@Nullable ItemStack item, int customModelData) {
    return item != null
        && item.hasItemMeta()
        && item.getItemMeta().hasCustomModelData()
        && item.getItemMeta().getCustomModelData() == customModelData;
  }

  public static boolean hasCustomModelDataInRange(@Nullable ItemStack item, int minCustomModelData,
                                                  int maxCustomModelData) {
    return item != null
        && item.hasItemMeta()
        && item.getItemMeta().hasCustomModelData()
        && item.getItemMeta().getCustomModelData() >= minCustomModelData
        && item.getItemMeta().getCustomModelData() <= maxCustomModelData;
  }

  /**
   * Checks if the given ItemStack is an enchanted book, is not a charm and is not charmed.
   *
   * @param item the ItemStack to check
   * @return true if the item is an enchanted book
   *          and does not have a custom model data within the given range, false otherwise.
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
