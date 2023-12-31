package cz.pmolek.enchantlevelx.loot;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;

/**
 * Generator for loot.
 */
public class LootGenerator implements Listener {
  private final List<LootDefinition> lootDefinitions = new ArrayList<>();

  public List<LootDefinition> getLootDefinitions() {
    return lootDefinitions;
  }

  public void addLootDefinition(LootDefinition lootDefinition) {
    lootDefinitions.add(lootDefinition);
  }

  public void removeLootDefinition(LootDefinition lootDefinition) {
    lootDefinitions.remove(lootDefinition);
  }

  /**
   * Handles the event when loot is generated.
   *
   * @param  event  the LootGenerateEvent object representing the event
   */
  @EventHandler
  public void onLootGenerate(LootGenerateEvent event) {
    for (LootDefinition lootDefinition : lootDefinitions) {
      if (lootDefinition.canSpawn(event)) {
        lootDefinition.generate(event);
      }
    }
  }
}
