package tanko.tinteractions.interactions;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tanko.tinteractions.system.Interaction;

import java.util.Arrays;

public class PlayerCommandInteraction extends Interaction {
    String command = null;

    public PlayerCommandInteraction(String ID) {
        super(ID);
        iconMaterial = Material.COMMAND_BLOCK;
    }

    @Override
    public void action(Player player, NPC npc) {
        command = command.replace("<player>", player.getName());
        if ((command.equals(""))) {
            player.sendMessage("§cCommand not set");
        }
        if (player.performCommand(command)) {
            player.sendMessage("§aCommand executed");
        } else {
            player.sendMessage("§cCommand failed");
        }
    }

    @Override
    public void handleCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cYou must specify a command");
            return;
        }
        String var = args[0];
        if (var.equalsIgnoreCase("command")) {
            // Set commands to the rest of the arguments except the first one
            command = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            player.sendMessage("§aCommand set to: " + command);
        } else {
            player.sendMessage("§cInvalid argument");
        }
    }

    @Override
    public boolean completeChecks(Player player, NPC npc) {
        return true;
    }

    @Override
    public void readConfig(ConfigurationSection section) {

    }

    @Override
    public void writeConfig(ConfigurationSection section) {

    }
}
