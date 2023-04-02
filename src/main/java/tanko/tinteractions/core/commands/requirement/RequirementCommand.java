package tanko.tinteractions.core.commands.requirement;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tanko.tinteractions.TInteractions;
import tanko.tinteractions.api.Interaction;
import tanko.tinteractions.api.Requirement;
import tanko.tinteractions.core.commands.CommandHandler;
import tanko.tinteractions.core.traits.SequentialInteraction;

import java.util.Arrays;

public class RequirementCommand extends CommandHandler implements CommandExecutor {

    /**
     * Function that runs when the ml command is executed
     * @param sender The sender of the command (should be a player)
     * @param command The command that was executed
     * @param label The label of the command
     * @param args The arguments for the command
     */
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) return true; //Check that the sender of the command was a player
        Player player = (Player) sender;
        // Get the currently selected NPC from Citizens
        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(player);
        if (npc == null) {
            player.sendMessage("§cNo NPC selected");
            return false;
        }
        if (!(npc.hasTrait(SequentialInteraction.class))) {
            player.sendMessage("§cNPC does not have the Interaction trait");
            return false;
        }
        Interaction interaction = TInteractions.getInteractionRegistry().getSelectedInteraction(player);
        if (interaction == null) {
            player.sendMessage("§cNo interaction selected, you must select an interaction dealing with requirements");
            return false;
        }

        try {
            if (args.length < 1) return false;
            String action = args[0];
            if (commands.containsKey(action)) {
                commands.get(action).execute(player,Arrays.copyOfRange(args, 1, args.length));
            } else {
                Requirement requirement = interaction.getRequirement(action);
                if (requirement == null) {
                    player.sendMessage("§cRequirement command not found");
                    return false;
                }
                requirement.handleCommand(player, Arrays.copyOfRange(args, 1, args.length));
            }
        } catch (CommandException e) {
            player.sendMessage("§c" + e.getMessage());
            return false;
        }

        return true;
    }
}
