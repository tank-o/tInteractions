package tanko.tinteractions.requirements;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tanko.tinteractions.system.Requirement;
import tanko.tinteractions.utils.Messaging;

public class ItemRequirement extends Requirement {
    private ItemStack item = null;
    private int amount = 0;
    private boolean takeItem = false;

    public ItemRequirement(String ID) {
        super(ID);
    }

    @Override
    public void writeConfig(ConfigurationSection section) {
        section.set("item", item);
        section.set("amount", amount);
        section.set("takeItem", takeItem);
    }

    @Override
    public void readConfig(ConfigurationSection section) {
        item = section.getItemStack("item");
        amount = section.getInt("amount");
        takeItem = section.getBoolean("takeItem");
    }

    @Override
    public boolean checkSatisfied(Player player, NPC npc) {
        if (item == null || item.getType() == Material.AIR) return true;
        if (amount == 0) return true;
        return player.getInventory().containsAtLeast(item, amount);
    }

    @Override
    public void requirementSatisfiedAction(Player player, NPC npc) {
        if (takeItem) {
            player.getInventory().removeItem(new ItemStack(item.getType(), amount));
        }
    }

    @Override
    public void handleCommand(Player player, String[] args) {
        String sub = args[0];
        if ("set".equals(sub)) {
            if (args.length < 2) return;
            String var = args[1];
            switch (var) {
                case "item" -> {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (item.getType() == Material.AIR) {
                        player.sendMessage("§cYou must be holding an item to set");
                        return;
                    }
                    setItem(item);
                    player.sendMessage("§aItem set to " + item.getType());
                }
                case "amount" -> {
                    if (args.length < 3) return;
                    try {
                        int amount = Integer.parseInt(args[2]);
                        if (amount < 0) {
                            player.sendMessage("§cAmount must be positive");
                            return;
                        }
                        setAmount(amount);
                        player.sendMessage("§aAmount set to " + amount);
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cInvalid number");
                    }
                }
                case "take" -> {
                    if (args.length < 3) return;
                    String takeItem = args[2];
                    if ("true".equals(takeItem)) {
                        setTakeItem(true);
                        player.sendMessage("§aTake item set to true");
                    } else if ("false".equals(takeItem)) {
                        setTakeItem(false);
                        player.sendMessage("§aTake item set to false");
                    } else {
                        player.sendMessage("§cInvalid boolean");
                    }
                }
                default -> player.sendMessage("§cInvalid variable");
            }
        } else if (sub.equals("view")){
            player.sendMessage("§aItem: " + item.getType());
            player.sendMessage("§aAmount: " + amount);
            player.sendMessage("§aTake item: " + takeItem);
        } else {
            player.sendMessage("§cInvalid subcommand");
        }
    }

    public void setItem(ItemStack item) {
        item.setAmount(1);
        this.item = item;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public void setTakeItem(boolean takeItem) {
        this.takeItem = takeItem;
    }

    public boolean getTakeItem() {
        return takeItem;
    }
}
