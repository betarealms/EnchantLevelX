package cz.pmolek.enchantlevelx;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootTables;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import java.util.*;

import static java.lang.Integer.parseInt;

public final class EnchantLevelX extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {

    // On plugin enable
    @Override
    public void onEnable() {
        // Register events
        getServer().getPluginManager().registerEvents(new AnvilListener(), this);
        getServer().getPluginManager().registerEvents(new LootListener(), this);

        // Register commands
        getServer().getPluginCommand("elx").setExecutor(this);
        getServer().getPluginCommand("elx").setTabCompleter(this);

        getLogger().info("Enabled");
    }

    // On plugin disable
    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }

    // Anvil listener
    public static class AnvilListener implements Listener {
        @EventHandler
        public void onPrepareAnvil(PrepareAnvilEvent event) {

            // Get items in anvil
            ItemStack target = event.getInventory().getItem(0);
            ItemStack sacrifice = event.getInventory().getItem(1);
            ItemStack result = event.getResult();

            // Check if target and sacrifice items are not empty and sacrifice item is a Charm of Enchanting or an Charmed Enchanted Book
            if (target != null && sacrifice != null && sacrifice.getItemMeta().hasCustomModelData() && ((sacrifice.getItemMeta().getCustomModelData() > 11200010 && sacrifice.getItemMeta().getCustomModelData() < 11200014) || (sacrifice.getItemMeta().getCustomModelData() > 11200020 && sacrifice.getItemMeta().getCustomModelData() < 11200024))) {

                // Case 1: Target = Enchanted Book, Sacrifice = Charm of Enchanting
                if (target.getType() == Material.ENCHANTED_BOOK && sacrifice.getItemMeta().hasCustomModelData() && sacrifice.getItemMeta().getCustomModelData() > 11200010 && sacrifice.getItemMeta().getCustomModelData() < 11200014) {

                    // Check if target is a valid Enchanted Book with only one enchantment at its maximum level or higher
                    Map<Enchantment, Integer> targetEnchantments = getEnchantmentsUnsafe(target);
                    Map<Enchantment, Integer> validEnchantments = new HashMap<>();
                    for (Map.Entry<Enchantment, Integer> enchantment : targetEnchantments.entrySet()) {
                        if (enchantment.getValue() != 1 && enchantment.getValue() >= enchantment.getKey().getMaxLevel()) {
                            validEnchantments.put(enchantment.getKey(), enchantment.getValue());
                        }
                    }
                    if (validEnchantments.size() == 1) {

                        // Case 1.1: Target = Base Enchanted Book, Sacrifice = Charm of Enchanting I
                        if (!target.getItemMeta().hasCustomModelData() && sacrifice.getItemMeta().getCustomModelData() == 11200011) {

                            // Clone the target item to be the result
                            result = target.clone();

                            // Add lore
                            ItemMeta meta = result.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            lore.add(ChatColor.WHITE + "Charmed with " + ChatColor.YELLOW + "Charm of Enchanting I" + ChatColor.WHITE + ".");
                            meta.setLore(lore);

                            // Add CustomModelData
                            meta.setCustomModelData(11200021);

                            // Set item name
                            meta.setDisplayName(event.getInventory().getRenameText());

                            // Save meta
                            result.setItemMeta(meta);

                            // Add new enchant level
                            Map.Entry<Enchantment, Integer> resultEnchantment = validEnchantments.entrySet().iterator().next();
                            addEnchantmentUnsafe(result, resultEnchantment.getKey(), resultEnchantment.getValue() + 1);

                            // Set cost
                            event.getInventory().setRepairCost(15);
                        }
                        // Case 1.2: Target = Charmed Enchanted Book, Sacrifice = Charm of Enchanting II/III
                        else if (target.getItemMeta().hasCustomModelData() && target.getItemMeta().getCustomModelData() > 11200020 && target.getItemMeta().getCustomModelData() < 11200023 && sacrifice.getItemMeta().getCustomModelData() > 11200011 && target.getItemMeta().getCustomModelData() - sacrifice.getItemMeta().getCustomModelData() == 9) {

                            // Clone the target item to be the result
                            result = target.clone();

                            // Add lore and cost
                            ItemMeta meta = result.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            switch (target.getItemMeta().getCustomModelData()) {
                                case 11200021:
                                    lore.add(ChatColor.WHITE + "Charmed with " + ChatColor.AQUA + "Charm of Enchanting II" + ChatColor.WHITE + ".");
                                    event.getInventory().setRepairCost(25);
                                    break;
                                case 11200022:
                                    lore.add(ChatColor.WHITE + "Charmed with " + ChatColor.LIGHT_PURPLE + "Charm of Enchanting III" + ChatColor.WHITE + ".");
                                    event.getInventory().setRepairCost(35);
                                    break;
                                default:
                                    break;
                            }
                            meta.setLore(lore);

                            // Add CustomModelData
                            meta.setCustomModelData(sacrifice.getItemMeta().getCustomModelData() + 10);

                            // Set item name
                            meta.setDisplayName(event.getInventory().getRenameText());

                            // Save meta
                            result.setItemMeta(meta);

                            // Add new enchant level
                            Map.Entry<Enchantment, Integer> resultEnchantment = validEnchantments.entrySet().iterator().next();
                            addEnchantmentUnsafe(result, resultEnchantment.getKey(), resultEnchantment.getValue() + 1);
                        }
                    }
                }
            }
            // Case 2: Target = Item or Enchanted Book, Sacrifice = Charmed Enchanted Book
            if (result != null && (((target.getType() == Material.ENCHANTED_BOOK && target.getItemMeta().hasCustomModelData() && target.getItemMeta().getCustomModelData() > 11200020 && target.getItemMeta().getCustomModelData() < 11200024)) || (sacrifice.getType() == Material.ENCHANTED_BOOK && sacrifice.getItemMeta().hasCustomModelData() && sacrifice.getItemMeta().getCustomModelData() > 11200020 && sacrifice.getItemMeta().getCustomModelData() < 11200024))) {

                // Add enchantments from both Target and Sacrifice to results
                for (Map.Entry<Enchantment, Integer> entry : getEnchantmentsUnsafe(target).entrySet()) {
                    addEnchantmentUnsafe(result, entry.getKey(), entry.getValue());
                }
                for (Map.Entry<Enchantment, Integer> entry : getEnchantmentsUnsafe(sacrifice).entrySet()) {
                    addEnchantmentUnsafe(result, entry.getKey(), entry.getValue());
                }

                // Check if both Target and Sacrifice are Enchanted Books
                if (target.getType() == Material.ENCHANTED_BOOK && sacrifice.getType() == Material.ENCHANTED_BOOK) {

                    // Get meta
                    ItemMeta targetMeta = target.getItemMeta();
                    ItemMeta sacrificeMeta = sacrifice.getItemMeta();
                    ItemMeta resultMeta = result.getItemMeta();

                    // Check if target or sacrifice are Charmed books, if so, add its CustomModelData and lore to result
                    List<String> lore = new ArrayList<>();
                    if (targetMeta.hasCustomModelData() && targetMeta.getCustomModelData() > 11200020 && targetMeta.getCustomModelData() < 11200024) {
                        resultMeta.setCustomModelData(targetMeta.getCustomModelData());
                        lore.addAll(targetMeta.getLore());
                    }
                    if (sacrificeMeta.hasCustomModelData() && sacrificeMeta.getCustomModelData() > 11200020 && sacrificeMeta.getCustomModelData() < 11200024) {
                        resultMeta.setCustomModelData(sacrificeMeta.getCustomModelData());
                        lore.addAll(sacrificeMeta.getLore());
                    }
                    resultMeta.setLore(lore);

                    // Set item meta
                    result.setItemMeta(resultMeta);
                }
            }

            // Set result item
            event.setResult(result);

            // Update inventory
            ((Player) event.getViewers().get(0)).updateInventory();
        }
    }

    // Enchantment reading
    private static Map<Enchantment, Integer> getEnchantmentsUnsafe(ItemStack item) {
        if (item.getType() == Material.ENCHANTED_BOOK) {
            return ((org.bukkit.inventory.meta.EnchantmentStorageMeta) item.getItemMeta()).getStoredEnchants();
        } else {
            return item.getEnchantments();
        }
    }

    // Enchantment saving
    private static void addEnchantmentUnsafe(ItemStack item, Enchantment enchantment, int level) {
        if (item.getType() == Material.ENCHANTED_BOOK) {
            org.bukkit.inventory.meta.EnchantmentStorageMeta meta = (org.bukkit.inventory.meta.EnchantmentStorageMeta) item.getItemMeta();
            meta.addStoredEnchant(enchantment, level, true);
            item.setItemMeta(meta);
        } else {
            item.addUnsafeEnchantment(enchantment, level);
        }
    }

    // Loot tables - generate Charms of Enchanting in dungeons
    public static class LootListener implements Listener {
        @EventHandler
        public void onLootGenerate(LootGenerateEvent event) {

            // Get loot
            List<ItemStack> loot = new ArrayList<>(event.getLoot());
            NamespacedKey lootTableKey = event.getLootTable().getKey();

            // Create books
            ItemStack book1 = createBook(1);
            ItemStack book2 = createBook(2);
            ItemStack book3 = createBook(3);

            // Generate random number in the interval [0,1]
            Random rand = new Random();
            double chance = rand.nextDouble();

            // High chance - Ancient City Ice Box, End City Treasure, Stronghold Library, Woodland Mansion
            if (lootTableKey.equals(LootTables.ANCIENT_CITY_ICE_BOX.getKey())
                    || lootTableKey.equals(LootTables.END_CITY_TREASURE.getKey())
                    || lootTableKey.equals(LootTables.STRONGHOLD_LIBRARY.getKey())
                    || lootTableKey.equals(LootTables.WOODLAND_MANSION.getKey()) ) {

                // Check for the random number but in a way so that each loot only has up to one book
                if (chance < 0.8) {
                    loot.add(book3);
                } else if (chance < 0.9) {
                    loot.add(book2);
                } else { // A "High chance" loot table has a 100 % chance of a book generating
                    loot.add(book1);
                }
            }

            // Low chance - Ancient City, Simple Dungeon, Abandoned Mineshaft, Bastion Treasure, Buried Treasure, Desert Pyramid, Jungle Temple, Nether Bridge, Stronghold Corridor, Stronghold Crossing
            if (lootTableKey.equals(LootTables.ANCIENT_CITY.getKey())
                    || lootTableKey.equals(LootTables.SIMPLE_DUNGEON.getKey())
                    || lootTableKey.equals(LootTables.ABANDONED_MINESHAFT.getKey())
                    || lootTableKey.equals(LootTables.BASTION_TREASURE.getKey())
                    || lootTableKey.equals(LootTables.BURIED_TREASURE.getKey())
                    || lootTableKey.equals(LootTables.DESERT_PYRAMID.getKey())
                    || lootTableKey.equals(LootTables.JUNGLE_TEMPLE.getKey())
                    || lootTableKey.equals(LootTables.NETHER_BRIDGE.getKey())
                    || lootTableKey.equals(LootTables.STRONGHOLD_CORRIDOR.getKey())
                    || lootTableKey.equals(LootTables.STRONGHOLD_CROSSING.getKey()) ) {

                // Check for the random number but in a way so that each loot only has up to one book
                if (chance < 0.13) {
                    loot.add(book3);
                } else if (chance < 0.36) {
                    loot.add(book2);
                } else if (chance < 0.55) {
                    loot.add(book1);
                }
            }

            // Save loot
            event.setLoot(loot);
        }
    }

    // Create Charm of Enchanting
    public static ItemStack createBook(int level) {
        // Create new item
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();

        // Set custom properties
        // Item name and lore
        List<String> lore = new ArrayList<>();
        switch (level) {
            case 1:
                meta.setDisplayName(ChatColor.YELLOW + "Charm of Enchanting I");
                lore.add(ChatColor.WHITE + "Combine with a " + ChatColor.GRAY + "max-level" + ChatColor.WHITE + " Enchanted Book.");
                break;
            case 2:
                meta.setDisplayName(ChatColor.AQUA + "Charm of Enchanting II");
                lore.add(ChatColor.WHITE + "Combine with an Enchanted Book charmed by " + ChatColor.YELLOW + "Charm I" + ChatColor.WHITE + ".");
                break;
            case 3:
                meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Charm of Enchanting III");
                lore.add(ChatColor.WHITE + "Combine with an Enchanted Book charmed by " + ChatColor.AQUA + "Charm II" + ChatColor.WHITE + ".");
                break;
            default:
                meta.setDisplayName(ChatColor.WHITE + "Charm of Enchanting Debug Item");
                break;

        }
        lore.add(ChatColor.GRAY + "Raises enchantment by one level.");
        lore.add(ChatColor.GRAY + "Ignores vanilla enchantments limits.");
        lore.add(ChatColor.DARK_GRAY + "Use in an anvil.");

        // CustomModelData for Resource Pack purposes
        // 112000 is the "prefix" for this plugin, 1 is item ID for Charm of Enchanting, then level is added
        meta.setCustomModelData(parseInt("1120001" + level));

        // Make it appear enchanted
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        // Save the metadata
        meta.setLore(lore);
        book.setItemMeta(meta);

        return book;
    }

    // Command executor
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Check if the command is executed by a player
        if (sender instanceof Player == false) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return true;
        }

        // Check if the command has exactly one argument and is a number
        if (args == null || args.length == 0 || args.length > 1 || safeParseInt(args[0]) == null || parseInt(args[0]) < 1 || parseInt(args[0]) > 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /elx <level>");
            return true;
        }

        // Give the book
        Player player = (Player) sender;
        ItemStack book = createBook(parseInt(args[0]));
        player.getInventory().addItem(book);

        sender.sendMessage(ChatColor.WHITE + "You have been given Charm of Enchanting");

        return true;
    }

    // Tab completer
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> options = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("elx")) {
            if (args.length == 1) {
                options.add("1");
                options.add("2");
                options.add("3");
            }
        }

        return options;
    }

    // Safe parseInt
    public static Integer safeParseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
