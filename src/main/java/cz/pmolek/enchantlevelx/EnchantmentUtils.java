package cz.pmolek.enchantlevelx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

/**
 * Utility class for enchantment manipulation.
 */
public final class EnchantmentUtils {
  private EnchantmentUtils() {
  }

  /**
   * Retrieves the enchantments applied to the given item.
   *
   * @param item the item to retrieve the enchantments from
   * @return a map containing the enchantments and their levels
   */
  public static Map<Enchantment, Integer> getEnchantmentsUnsafe(ItemStack item) {
    if (item.getType() == Material.ENCHANTED_BOOK) {
      EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
      if (meta == null) {
        return new HashMap<>();
      }
      return meta.getStoredEnchants();
    } else {
      return item.getEnchantments();
    }
  }

  /**
   * Adds an enchantment to an item, ignoring max enchantment levels.
   *
   * @param item        the item
   * @param enchantment the enchantment
   * @param level       the level
   */
  // Enchantment saving
  public static void addEnchantmentUnsafe(ItemStack item, Enchantment enchantment, int level) {
    if (item.getType() == Material.ENCHANTED_BOOK) {
      EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
      meta.addStoredEnchant(enchantment, level, true);
      item.setItemMeta(meta);
    } else {
      item.addUnsafeEnchantment(enchantment, level);
    }
  }

  /**
   * Removes the specified enchantment from the given item.
   *
   * @param  item         the item from which to remove the enchantment
   * @param  enchantment  the enchantment to be removed
   */
  public static void removeEnchantment(ItemStack item, Enchantment enchantment) {
    if (item.getType() == Material.ENCHANTED_BOOK) {
      EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
      meta.removeStoredEnchant(enchantment);
      item.setItemMeta(meta);
    } else {
      item.removeEnchantment(enchantment);
    }
  }

  /**
   * Retrieves a list of enchantments from an item that satisfy a given predicate.
   *
   * @param item      the item to retrieve enchantments from
   * @param predicate a predicate that determines which enchantments to include in the result
   * @return a list of tuples representing the enchantments and their corresponding levels
   */
  public static List<Tuple<Enchantment, Integer>>
      getEnchantmentsThat(ItemStack item, BiPredicate<Enchantment, Integer> predicate) {
    List<Tuple<Enchantment, Integer>> result = new ArrayList<>();
    for (Map.Entry<Enchantment, Integer> enchantment : getEnchantmentsUnsafe(item).entrySet()) {
      Tuple<Enchantment, Integer> tuple = Tuple.of(enchantment);
      if (predicate.test(tuple.getValueA(), tuple.getValueB())) {
        result.add(tuple);
      }
    }
    return result;
  }
}
