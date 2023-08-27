package cz.pmolek.enchantlevelx.enchantdefinitions;

import cz.pmolek.enchantlevelx.CharmModelData;
import cz.pmolek.enchantlevelx.EnchantmentUtils;
import cz.pmolek.enchantlevelx.ItemUtils;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public class EnchantedBookUpgradeEnchantT1 extends CharmEnchantBase {
    @Override
    public String getId() {
        return EnchantedBookUpgradeEnchantT1.class.getName();
    }

    @Override
    public boolean validateInput(ItemStack left, ItemStack right, ItemStack result) {
        return result == null
                && ItemUtils.isEnchantedBook(left)
                && CharmModelData.isCharmItem(right, CharmModelData.CHARM_I)
                && EnchantmentUtils.getEnchantmentsThat(left, this::isValidEnchantment).size() == 1;
    }

    @Override
    public ItemStack enchant(ItemStack left, ItemStack right, PrepareAnvilEvent eventData) {
        ItemStack output = left.clone();

        applyItemMeta(output, CharmModelData.CHARMED_BOOK_I, eventData.getInventory());

        // Add new enchant level
        enhanceEnchant(output);
        return output;
    }

    @Override
    public Integer getEnchantmentCost(ItemStack left, ItemStack right) {
        return 10;
    }
}
