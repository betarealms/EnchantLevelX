package cz.pmolek.enchantlevelx.loot;

import cz.pmolek.enchantlevelx.Tuple;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import org.bukkit.NamespacedKey;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Loot definition for simple loot addition to existing loot tables.
 */
public class LootTableAdderLoot implements LootDefinition {
  protected final Set<NamespacedKey> acceptedLootTables;
  protected final ChanceList<Supplier<ItemStack>> items;
  protected final int itemsToGenerate;

  /**
   * Create a new loot definition.
   *
   * @param acceptedLootTables  the accepted loot tables.
   *
   * @param items               the items to generate.
   *
   * @param itemsToGenerate     the number of items to generate.
   */
  public LootTableAdderLoot(Collection<NamespacedKey> acceptedLootTables,
                            Collection<Tuple<Double, Supplier<ItemStack>>> items,
                            int itemsToGenerate) {

    this.items = new ChanceList<>(items);
    this.acceptedLootTables = new HashSet<>(acceptedLootTables);
    this.itemsToGenerate = itemsToGenerate;
  }

  @Override
  public boolean canSpawn(LootGenerateEvent event) {
    return acceptedLootTables.contains(event.getLootTable().getKey());
  }

  @Override
  public List<ItemStack> generate(LootGenerateEvent event) {
    List<ItemStack> loot = event.getLoot();
    for (int i = 0; i < itemsToGenerate; i++) {
      Supplier<ItemStack> supplier = items.getWeightedRandom(ThreadLocalRandom.current());
      if (supplier != null) {
        loot.add(supplier.get());
      }
    }
    return loot;
  }
}
