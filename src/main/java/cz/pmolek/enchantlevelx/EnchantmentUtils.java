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

public final class EnchantmentUtils {
  private EnchantmentUtils() {
  }

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

  public static List<Tuple<Enchantment, Integer>> getEnchantmentsThat(ItemStack item,
                                                                      BiPredicate<Enchantment, Integer> predicate) {
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
