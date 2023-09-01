package cz.pmolek.enchantlevelx.enchantdefinitions;

import cz.pmolek.enchantlevelx.CharmModelData;
import cz.pmolek.enchantlevelx.EnchantmentUtils;
import cz.pmolek.enchantlevelx.ItemUtils;
import cz.pmolek.enchantlevelx.Tuple;
import cz.pmolek.enchantlevelx.enchanter.AnvilEnchanter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Enchant for splitting enchantments between two books.
 */
public class CharmableEnchantTransferEnchant extends CharmEnchantBase {
  private record State(@Nullable ItemStack source,
                       @Nullable Tuple<Enchantment, Integer> enchant,
                       @Nullable ItemStack result) {
    public static State getEmpty() {
      return new State(null, null, null);
    }
  }

  private final Map<UUID, State> inProgressEnchants = new HashMap<>();
  private final AnvilEnchanter enchanter;

  public CharmableEnchantTransferEnchant(AnvilEnchanter enchanter) {
    this.enchanter = enchanter;
  }

  @Override
  public String getId() {
    return CharmableEnchantTransferEnchant.class.getName();
  }

  @Override
  public boolean validateInput(@Nullable ItemStack left, @Nullable ItemStack right,
                               @Nullable ItemStack result) {
    return left == null
        || (result == null
            && (ItemUtils.isEnchantedBook(left) || CharmModelData.isCharmedBook(left))
            && ItemUtils.isType(right, Material.BOOK)
            && !EnchantmentUtils.getEnchantmentsThat(left, this::isValidEnchantment).isEmpty());
  }

  @Override
  public ItemStack enchant(ItemStack left, ItemStack right, PrepareAnvilEvent eventData) {
    HumanEntity sender = eventData.getViewers().get(0);
    State state = inProgressEnchants.getOrDefault(sender.getUniqueId(), State.getEmpty());

    //Reset
    if (left == null) {
      //Transaction finished, return original book without enchant
      if (state.result != null && state.source != null && state.enchant != null) {
        AnvilInventory inventory = eventData.getInventory();
        ItemStack source = state.source;
        EnchantmentUtils.removeEnchantment(source, state.enchant.getValueA());
        enchanter.ignoreNextEventFrom(sender);
        inventory.setItem(0, source);
      }
      state = State.getEmpty();
      inProgressEnchants.put(sender.getUniqueId(), state);
      return eventData.getResult();
    }
    //Result already evaluated
    if (state.result != null) {
      return state.result;
    }
    //Source already evaluated
    if (ItemUtils.areEqualByValues(state.source, left)) {
      return eventData.getResult();
    } else { //Start new state
      state = new State(left, null, null);
      inProgressEnchants.put(sender.getUniqueId(), state);
    }

    ItemStack result = new ItemStack(Material.ENCHANTED_BOOK, 1);

    ItemMeta meta = result.getItemMeta();
    meta.setDisplayName(eventData.getInventory().getRenameText());
    result.setItemMeta(meta);

    List<Tuple<Enchantment, Integer>> enchants = EnchantmentUtils
        .getEnchantmentsThat(left, this::isValidEnchantment);
    EnchantmentUtils
        .addEnchantmentUnsafe(result, enchants.get(0).getValueA(), enchants.get(0).getValueB());

    state = new State(left, enchants.get(0), result);
    inProgressEnchants.put(sender.getUniqueId(), state);
    return result;
  }

  @Nullable
  @Override
  public Integer getEnchantmentCost(ItemStack left, ItemStack right) {
    return 3;
  }
}
