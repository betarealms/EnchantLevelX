package cz.pmolek.enchantlevelx.lootdefinitions;

import cz.pmolek.enchantlevelx.CharmModelData;
import cz.pmolek.enchantlevelx.Tuple;
import cz.pmolek.enchantlevelx.loot.LootTableAdderLoot;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTables;

/**
 * Loot tables for Charm books in low quality chests.
 */
public class LowProbabilityCharmLoot extends LootTableAdderLoot {
  public LowProbabilityCharmLoot() {
    super(createAcceptedLootTables(), createItems(), 1);
  }

  private static Collection<NamespacedKey> createAcceptedLootTables() {
    return List.of(new NamespacedKey[] {
        LootTables.ANCIENT_CITY.getKey(),
        LootTables.SIMPLE_DUNGEON.getKey(),
        LootTables.ABANDONED_MINESHAFT.getKey(),
        LootTables.BASTION_TREASURE.getKey(),
        LootTables.BURIED_TREASURE.getKey(),
        LootTables.DESERT_PYRAMID.getKey(),
        LootTables.JUNGLE_TEMPLE.getKey(),
        LootTables.NETHER_BRIDGE.getKey(),
        LootTables.STRONGHOLD_CORRIDOR.getKey(),
        LootTables.STRONGHOLD_CROSSING.getKey()
    });
  }

  private static List<Tuple<Double, Supplier<ItemStack>>> createItems() {
    return List.of(new Tuple[] {
        new Tuple<>(0.13,
            (Supplier<ItemStack>) () -> CharmFactory.createBook(CharmModelData.CHARM_III)),
        new Tuple<>(0.36,
            (Supplier<ItemStack>) () -> CharmFactory.createBook(CharmModelData.CHARM_II)),
        new Tuple<>(0.55,
            (Supplier<ItemStack>) () -> CharmFactory.createBook(CharmModelData.CHARM_I))
    });
  }
}
