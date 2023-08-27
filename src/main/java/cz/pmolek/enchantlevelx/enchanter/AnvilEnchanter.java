package cz.pmolek.enchantlevelx.enchanter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Class for managing enchantments on anvils.
 */
public class AnvilEnchanter implements Listener {
  private final Map<String, AnvilEnchantDefinition> enchants = new HashMap<>();

  /**
   * Adds an AnvilEnchantDefinition to the enchants map.
   *
   * @param  enchant  the AnvilEnchantDefinition to be added
   */
  public void addEnchant(AnvilEnchantDefinition enchant) {
    enchants.put(enchant.getId(), enchant);
  }

  /**
   * Retrieves the collection of AnvilEnchantDefinition objects.
   *
   * @return          the collection of AnvilEnchantDefinition objects
   */
  public Collection<AnvilEnchantDefinition> getEnchants() {
    return enchants.values();
  }

  /**
   * Removes an enchantment from the collection by id.
   *
   * @param  id  the id of the enchantment to be removed
   */
  public void removeEnchantById(String id) {
    enchants.remove(id);
  }

  /**
   * This function is an event handler that is called when the PrepareAnvilEvent is triggered.
   * It retrieves the items in the anvil, finds the appropriate enchantment to apply,
   * and performs the enchantment on the items.
   *
   * @param  event  The PrepareAnvilEvent object that triggered the function.
   */
  @EventHandler
  public void onPrepareAnvil(PrepareAnvilEvent event) {
    // Get items in anvil
    ItemStack left = event.getInventory().getItem(0);
    ItemStack right = event.getInventory().getItem(1);

    AnvilEnchantDefinition enchant = findEnchant(event);

    if (enchant == null) {
      return;
    }

    ItemStack result = enchant.enchant(left, right, event);

    event.setResult(result);
    Integer repairCost = enchant.getEnchantmentCost(left, right);
    if (repairCost != null) {
      if (enchant.isEnchantmentCostInLevels()) {
        event.getInventory().setRepairCost(repairCost);
      } else {
        event.getInventory().setRepairCostAmount(repairCost);
      }
    }

    //Hack for disabling max repair cost
    event.getInventory().setMaximumRepairCost(9999);

    if (enchant.requiresInventoryUpdate()) {
      ((Player) event.getViewers().get(0)).updateInventory();
    }
  }

  @Nullable
  private AnvilEnchantDefinition findEnchant(PrepareAnvilEvent event) {
    // Get items in anvil
    ItemStack target = event.getInventory().getItem(0);
    ItemStack addition = event.getInventory().getItem(1);
    ItemStack result = event.getResult();

    for (AnvilEnchantDefinition enchant : enchants.values()) {
      if (enchant.validateInput(target, addition, result)) {
        return enchant;
      }
    }
    return null;
  }
}
