package cz.pmolek.enchantlevelx.enchanter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AnvilEnchanter implements Listener {
    private final Map<String, AnvilEnchantDefinition> enchants = new HashMap<>();

    public void addEnchant(AnvilEnchantDefinition enchant) {
        enchants.put(enchant.getId(), enchant);
    }

    public Collection<AnvilEnchantDefinition> getEnchants() {
        return enchants.values();
    }

    public void removeEnchantById(String id) {
        enchants.remove(id);
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event)
    {
        // Get items in anvil
        ItemStack left = event.getInventory().getItem(0);
        ItemStack right = event.getInventory().getItem(1);

        AnvilEnchantDefinition enchant = findEnchant(event);

        if (enchant == null)
            return;

        ItemStack result = enchant.enchant(left, right, event);

        event.setResult(result);
        Integer repairCost = enchant.getEnchantmentCost(left, right);
        if (repairCost != null) {
            if (enchant.isEnchantmentCostInLevels())
                event.getInventory().setRepairCost(repairCost);
            else
                event.getInventory().setRepairCostAmount(repairCost);
        }

        //Hack for disabling max repair cost
        event.getInventory().setMaximumRepairCost(9999);

        if (enchant.requiresInventoryUpdate())
        {
            ((Player) event.getViewers().get(0)).updateInventory();
        }
    }

    @Nullable
    private AnvilEnchantDefinition findEnchant(PrepareAnvilEvent event)
    {
        // Get items in anvil
        ItemStack target = event.getInventory().getItem(0);
        ItemStack addition = event.getInventory().getItem(1);
        ItemStack result = event.getResult();

        for (AnvilEnchantDefinition enchant : enchants.values())
        {
            if (enchant.validateInput(target, addition, result))
            {
                return enchant;
            }
        }
        return null;
    }
}
