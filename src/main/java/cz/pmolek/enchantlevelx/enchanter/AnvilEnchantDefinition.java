package cz.pmolek.enchantlevelx.enchanter;

import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public interface AnvilEnchantDefinition {
    public String getId();
    public boolean validateInput(@Nullable ItemStack left, @Nullable ItemStack right, @Nullable ItemStack result);


    public ItemStack enchant(ItemStack left, ItemStack right, PrepareAnvilEvent eventData);

    public boolean isEnchantmentCostInLevels();

    @Nullable
    public Integer getEnchantmentCost(ItemStack left, ItemStack right);

    public boolean requiresInventoryUpdate();
}
