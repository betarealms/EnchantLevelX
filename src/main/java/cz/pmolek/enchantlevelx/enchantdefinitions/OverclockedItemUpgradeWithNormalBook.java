package cz.pmolek.enchantlevelx.enchantdefinitions;

import cz.pmolek.enchantlevelx.EnchantmentUtils;
import cz.pmolek.enchantlevelx.ItemUtils;
import cz.pmolek.enchantlevelx.Tuple;
import cz.pmolek.enchantlevelx.enchanter.AnvilEnchantDefinition;
import javax.annotation.Nullable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Enchant definition for an anvil enchant.
 */
public class OverclockedItemUpgradeWithNormalBook implements AnvilEnchantDefinition {
  @Override
  public String getId() {
    return OverclockedItemUpgradeWithNormalBook.class.getName();
  }

  @Override
  public boolean validateInput(@Nullable ItemStack left, @Nullable ItemStack right,
                               @Nullable ItemStack result) {
    return result != null
        && ItemUtils.isEnchantedBook(right)
        && !EnchantmentUtils.getEnchantmentsThat(left, (ench, level) -> level > ench.getMaxLevel())
        .isEmpty();
  }

  @Override
  public ItemStack enchant(ItemStack left, ItemStack right, PrepareAnvilEvent eventData) {
    ItemStack result = eventData.getResult();
    for (Tuple<Enchantment, Integer> entry : EnchantmentUtils.getEnchantmentsThat(left,
        (ench, level) -> level > ench.getMaxLevel())) {
      EnchantmentUtils.addEnchantmentUnsafe(result, entry.getValueA(), entry.getValueB());
    }
    return result;
  }

  @Override
  public boolean isEnchantmentCostInLevels() {
    return false;
  }

  @Override
  @Nullable
  public Integer getEnchantmentCost(ItemStack left, ItemStack right) {
    return null;
  }

  @Override
  public boolean requiresInventoryUpdate() {
    return true;
  }
}
