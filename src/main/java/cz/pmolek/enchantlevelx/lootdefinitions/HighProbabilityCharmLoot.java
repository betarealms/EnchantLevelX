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

public class HighProbabilityCharmLoot extends LootTableAdderLoot {
  public HighProbabilityCharmLoot() {
    super(createAcceptedLootTables(), createItems(), 1);
  }

  private static Collection<NamespacedKey> createAcceptedLootTables() {
    return List.of(new NamespacedKey[] {
        LootTables.ANCIENT_CITY_ICE_BOX.getKey(),
        LootTables.END_CITY_TREASURE.getKey(),
        LootTables.STRONGHOLD_LIBRARY.getKey(),
        LootTables.WOODLAND_MANSION.getKey()
    });
  }

  private static Collection<Tuple<Double, Supplier<ItemStack>>> createItems() {
    return List.of(new Tuple[] {
        new Tuple<>(0.8,
            (Supplier<ItemStack>) () -> CharmFactory.createBook(CharmModelData.CHARM_III)),
        new Tuple<>(0.9,
            (Supplier<ItemStack>) () -> CharmFactory.createBook(CharmModelData.CHARM_II)),
        new Tuple<>(1.0,
            (Supplier<ItemStack>) () -> CharmFactory.createBook(CharmModelData.CHARM_I))
    });
  }
}
