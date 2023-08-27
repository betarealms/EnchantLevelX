package cz.pmolek.enchantlevelx.loot;

import java.util.List;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

public interface LootDefinition {
  public boolean canSpawn(LootGenerateEvent event);

  public List<ItemStack> generate(LootGenerateEvent event);
}
