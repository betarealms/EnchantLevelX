package cz.pmolek.enchantlevelx.enchantdefinitions;

import cz.pmolek.enchantlevelx.CharmModelData;
import cz.pmolek.enchantlevelx.EnchantmentUtils;
import java.util.List;
import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Enchant definition for an anvil enchant.
 */
public class ItemUpgradeWithCharmedBookEnchant extends CharmEnchantBase {
  @Override
  public String getId() {
    return ItemUpgradeWithCharmedBookEnchant.class.getName();
  }

  @Override
  public boolean validateInput(ItemStack left, ItemStack right, ItemStack result) {
    return result != null
        && left != null
        && CharmModelData.isCharmedBook(right);
  }

  @Override
  public ItemStack enchant(ItemStack left, ItemStack right, PrepareAnvilEvent eventData) {
    ItemStack result = eventData.getResult();
    for (Map.Entry<Enchantment, Integer> entry : EnchantmentUtils.getEnchantmentsUnsafe(left)
        .entrySet()) {
      EnchantmentUtils.addEnchantmentUnsafe(result, entry.getKey(), entry.getValue());
    }
    for (Map.Entry<Enchantment, Integer> entry : EnchantmentUtils.getEnchantmentsUnsafe(right)
        .entrySet()) {
      EnchantmentUtils.addEnchantmentUnsafe(result, entry.getKey(), entry.getValue());
    }

    // Remove the Charm of Enchanting lore from the result
    ItemMeta resultMeta = result.getItemMeta();
    if (resultMeta != null && resultMeta.hasLore()) {
      List<String> lore = resultMeta.getLore();
      lore.removeIf(line -> line.contains("Charm of Enchanting"));
      resultMeta.setLore(lore);
      result.setItemMeta(resultMeta);
    }
    return result;
  }

  @Override
  public Integer getEnchantmentCost(ItemStack left, ItemStack right) {
    return CharmModelData.fromModelData(right.getItemMeta().getCustomModelData()).level * 10;
  }
}
