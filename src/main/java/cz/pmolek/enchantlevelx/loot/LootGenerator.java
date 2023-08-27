package cz.pmolek.enchantlevelx.loot;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;

import java.util.*;

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

    @EventHandler
    public void onLootGenerate(LootGenerateEvent event) {
        for (LootDefinition lootDefinition : lootDefinitions) {
            if (lootDefinition.canSpawn(event))
            {
                lootDefinition.generate(event);
            }
        }
    }
}
