package cz.pmolek.enchantlevelx.enchantdefinitions;

import cz.pmolek.enchantlevelx.CharmModelData;
import cz.pmolek.enchantlevelx.EnchantmentUtils;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Enchant definition for an anvil enchant.
 */
public class CharmedBookUpgradeEnchantT3 extends CharmEnchantBase {
  @Override
  public String getId() {
    return CharmedBookUpgradeEnchantT3.class.getName();
  }

  @Override
  public boolean validateInput(ItemStack left, ItemStack right, ItemStack result) {
    return result == null
        && CharmModelData.isCharmItem(left, CharmModelData.CHARMED_BOOK_II)
        && CharmModelData.isCharmItem(right, CharmModelData.CHARM_III)
        && EnchantmentUtils.getEnchantmentsThat(left, this::isValidEnchantment).size() == 1;
  }

  @Override
  public ItemStack enchant(ItemStack left, ItemStack right, PrepareAnvilEvent eventData) {
    ItemStack output = left.clone();

    applyItemMeta(output, CharmModelData.CHARMED_BOOK_III, eventData.getInventory());

    enhanceEnchant(output);
    return output;
  }

  @Override
  public Integer getEnchantmentCost(ItemStack left, ItemStack right) {
    return 30;
  }
}
