package cz.pmolek.enchantlevelx;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public enum CharmModelData {
    CHARM_I(11200011, 1, ChatColor.YELLOW, true),
    CHARM_II(11200012, 2, ChatColor.AQUA, true),
    CHARM_III(11200013, 3, ChatColor.LIGHT_PURPLE, true),
    CHARMED_BOOK_I(11200021, 1, ChatColor.GOLD, false),
    CHARMED_BOOK_II(11200022, 2, ChatColor.DARK_AQUA, false),
    CHARMED_BOOK_III(11200023, 3, ChatColor.DARK_PURPLE, false);

    public final int modelData;
    public final ChatColor color;
    public final int level;
    public final boolean isCharm;

    CharmModelData(int modelData, int level, ChatColor color, boolean isCharm)
    {
        this.modelData = modelData;
        this.color = color;
        this.level = level;
        this.isCharm = isCharm;
    }

    public static CharmModelData fromModelData(int value)
    {
        for (CharmModelData charmModelData : CharmModelData.values())
        {
            if (charmModelData.modelData == value)
            {
                return charmModelData;
            }
        }
        throw new IllegalArgumentException("Invalid CharmModelData value: " + value);
    }

    public static CharmModelData fromLevel(int value)
    {
        for (CharmModelData charmModelData : CharmModelData.values())
        {
            if (charmModelData.level == value)
            {
                return charmModelData;
            }
        }
        throw new IllegalArgumentException("Invalid CharmModelData level: " + value);
    }

    @Nullable
    public static CharmModelData tryParse(String value)
    {
        for (CharmModelData charmModelData : CharmModelData.values())
        {
            if (charmModelData.name().equalsIgnoreCase(value))
            {
                return charmModelData;
            }
        }
        return null;
    }

    public static boolean isCharmItem(@Nullable ItemStack item, CharmModelData specific)
    {
        return item != null
                &&item.getType() == Material.ENCHANTED_BOOK
                && ItemUtils.hasCustomModelData(item, specific.modelData);
    }

    public static boolean isCharmedBook(@Nullable ItemStack item)
    {
        return item != null
                && item.getType() == Material.ENCHANTED_BOOK
                && ItemUtils.hasCustomModelDataInRange(item, CHARMED_BOOK_I.modelData, CHARMED_BOOK_III.modelData);
    }
}
