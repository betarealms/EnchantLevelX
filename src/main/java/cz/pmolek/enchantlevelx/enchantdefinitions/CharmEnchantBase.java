package cz.pmolek.enchantlevelx.enchantdefinitions;

import cz.pmolek.enchantlevelx.CharmModelData;
import cz.pmolek.enchantlevelx.EnchantmentUtils;
import cz.pmolek.enchantlevelx.Tuple;
import cz.pmolek.enchantlevelx.enchanter.AnvilEnchantDefinition;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Base class for Charm enchantments.
 */
public abstract class CharmEnchantBase implements AnvilEnchantDefinition {
  protected boolean isValidEnchantment(Enchantment enchantment, int level) {
    return level > 1 && level >= enchantment.getMaxLevel();
  }

  protected void enhanceEnchant(ItemStack output) {
    Tuple<Enchantment, Integer> enchantment =
        EnchantmentUtils.getEnchantmentsThat(output, this::isValidEnchantment).get(0);
    EnchantmentUtils
        .addEnchantmentUnsafe(output, enchantment.getValueA(), enchantment.getValueB() + 1);
  }

  protected ItemMeta createLore(ItemMeta meta, CharmModelData charmData) {
    List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
    lore.clear();
    lore.add(ChatColor.WHITE + "Charmed with " + charmData.color + "Charm of Enchanting "
        + "I".repeat(charmData.level) + ChatColor.WHITE + ".");
    meta.setLore(lore);
    return meta;
  }

  protected void applyItemMeta(ItemStack output, CharmModelData charmData,
                               AnvilInventory eventData) {
    ItemMeta meta = output.getItemMeta();

    meta = createLore(meta, charmData);

    meta.setCustomModelData(charmData.modelData);

    meta.setDisplayName(eventData.getRenameText());

    output.setItemMeta(meta);
  }

  @Override
  public boolean isEnchantmentCostInLevels() {
    return true;
  }

  @Override
  public boolean requiresInventoryUpdate() {
    return true;
  }
}
