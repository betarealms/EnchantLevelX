package cz.pmolek.enchantlevelx.enchantdefinitions;

import cz.pmolek.enchantlevelx.CharmModelData;
import cz.pmolek.enchantlevelx.EnchantmentUtils;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public class CharmedBookUpgradeEnchantT2 extends CharmEnchantBase {
    @Override
    public String getId() {
        return CharmedBookUpgradeEnchantT2.class.getName();
    }

    @Override
    public boolean validateInput(ItemStack left, ItemStack right, ItemStack result) {
        return result == null
                && CharmModelData.isCharmItem(left, CharmModelData.CHARMED_BOOK_I)
                && CharmModelData.isCharmItem(right, CharmModelData.CHARM_II)
                && EnchantmentUtils.getEnchantmentsThat(left, this::isValidEnchantment).size() == 1;
    }


    @Override
    public ItemStack enchant(ItemStack left, ItemStack right, PrepareAnvilEvent eventData) {
        ItemStack output = left.clone();

        applyItemMeta(output, CharmModelData.CHARMED_BOOK_II, eventData.getInventory());

        enhanceEnchant(output);
        return output;
    }

    @Override
    public Integer getEnchantmentCost(ItemStack left, ItemStack right) {
        return 20;
    }
}
