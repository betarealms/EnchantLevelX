package cz.pmolek.enchantlevelx.loot;

import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface LootDefinition {
    public boolean canSpawn(LootGenerateEvent event);

    public List<ItemStack> generate(LootGenerateEvent event);
}
