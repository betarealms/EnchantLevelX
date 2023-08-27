package cz.pmolek.enchantlevelx;

import static java.lang.Integer.parseInt;

import cz.pmolek.enchantlevelx.enchantdefinitions.CharmedBookUpgradeEnchantT2;
import cz.pmolek.enchantlevelx.enchantdefinitions.CharmedBookUpgradeEnchantT3;
import cz.pmolek.enchantlevelx.enchantdefinitions.EnchantedBookUpgradeEnchantT1;
import cz.pmolek.enchantlevelx.enchantdefinitions.ItemUpgradeWithCharmedBookEnchant;
import cz.pmolek.enchantlevelx.enchantdefinitions.OverclockedItemUpgradeWithNormalBook;
import cz.pmolek.enchantlevelx.enchanter.AnvilEnchanter;
import cz.pmolek.enchantlevelx.loot.LootGenerator;
import cz.pmolek.enchantlevelx.lootdefinitions.CharmFactory;
import cz.pmolek.enchantlevelx.lootdefinitions.HighProbabilityCharmLoot;
import cz.pmolek.enchantlevelx.lootdefinitions.LowProbabilityCharmLoot;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Core plugin class.
 */
public final class EnchantLevelX extends JavaPlugin
    implements Listener, CommandExecutor, TabCompleter {

  /**
   * Parses the given string into an Integer and returns it. If the string cannot be
   * parsed into an Integer, returns null.
   *
   * @param  string  the string to be parsed
   * @return         the parsed Integer value, or null if the string cannot be parsed
   */
  public static Integer safeParseInt(String string) {
    try {
      return Integer.parseInt(string);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Override
  public void onEnable() {
    // Register events
    getServer().getPluginManager().registerEvents(createAnvilEnchantListener(), this);
    getServer().getPluginManager().registerEvents(createLootGenerator(), this);

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

  private Listener createAnvilEnchantListener() {
    AnvilEnchanter enchanter = new AnvilEnchanter();
    enchanter.addEnchant(new EnchantedBookUpgradeEnchantT1());
    enchanter.addEnchant(new CharmedBookUpgradeEnchantT2());
    enchanter.addEnchant(new CharmedBookUpgradeEnchantT3());
    enchanter.addEnchant(new ItemUpgradeWithCharmedBookEnchant());
    enchanter.addEnchant(new OverclockedItemUpgradeWithNormalBook());
    return enchanter;
  }

  private Listener createLootGenerator() {
    LootGenerator lootGenerator = new LootGenerator();
    lootGenerator.addLootDefinition(new LowProbabilityCharmLoot());
    lootGenerator.addLootDefinition(new HighProbabilityCharmLoot());
    return lootGenerator;
  }

  // Command executor
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    // Check if the command is executed by a player
    if (!(sender instanceof Player)) {
      sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
      return true;
    }

    // Check if the command has exactly one argument and is a number
    if (args == null
        || args.length != 1
        || safeParseInt(args[0]) == null
        || parseInt(args[0]) < 1 || parseInt(args[0]) > 3) {
      sender.sendMessage(ChatColor.RED + "Usage: /elx <level>");
      return true;
    }

    // Give the book
    Player player = (Player) sender;
    ItemStack book = CharmFactory.createBook(CharmModelData.fromLevel(parseInt(args[0])));
    player.getInventory().addItem(book);

    sender.sendMessage(ChatColor.WHITE + "You have been given Charm of Enchanting");

    return true;
  }

  // Tab completer
  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias,
                                    String[] args) {
    List<String> options = new ArrayList<>();

    if (command.getName().equalsIgnoreCase("elx") && (args.length == 1)) {
      options.add("1");
      options.add("2");
      options.add("3");
    }

    return options;
  }

}
