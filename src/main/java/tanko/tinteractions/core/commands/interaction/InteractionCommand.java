package tanko.tinteractions.core.commands.interaction;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tanko.tinteractions.api.Interaction;
import tanko.tinteractions.core.commands.CommandHandler;
import tanko.tinteractions.core.traits.InteractionTrait;
import tanko.tinteractions.core.traits.MenuInteraction;
import tanko.tinteractions.core.traits.SequentialInteraction;

import java.util.Arrays;

import java.util.Objects;

public class InteractionCommand extends CommandHandler implements CommandExecutor {

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

        try {
            if (args.length < 1) return false;
            String action = args[0];
            if (commands.containsKey(action)) {
                commands.get(action).execute(player,Arrays.copyOfRange(args, 1, args.length));
            } else {
                // If the command is not one of the set subcommand, check if they are trying to run an interaction command
                // and let the implementation handle it
                InteractionTrait trait = Objects.requireNonNullElse(npc.getTraitNullable(SequentialInteraction.class), npc.getTraitNullable(MenuInteraction.class));
                Interaction interaction = trait.getInteraction(action);
                if (interaction == null) {
                    player.sendMessage("§cInteraction command not found");
                    return false;
                }
                interaction.command(player, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
        } catch (CommandException e) {
            player.sendMessage("§c" + e.getMessage());
        }  catch (NullPointerException npe) {
            player.sendMessage("§cNPC does not have an interaction trait");
        }
        return true;
    }

    private void defaultMessage(Player player, String[] args, NPC npc) {
        if (!(args.length > 0)) return;
        String command = args[0];
        if (!npc.hasTrait(SequentialInteraction.class)){
            player.sendMessage("§cNo interaction trait found");
            return;
        }
        SequentialInteraction trait = npc.getOrAddTrait(SequentialInteraction.class);
        switch (command){
            case "add" -> {
                if (!(args.length > 1)) return;
                String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                trait.addDefaultMessage(message);
                player.sendMessage("§aAdded default message to " + npc.getName());
            }
            case "remove" -> {
                int index = Integer.parseInt(args[1]);
                trait.removeDefaultMessage(index);
                player.sendMessage("§aRemoved default message from " + npc.getName());
            }
            case "clear" -> {
                trait.clearDefaultMessages();
                player.sendMessage("§aCleared default messages from " + npc.getName());
            }
            case "view" -> {
                if (trait.getDefaultMessages().size() == 0){
                    player.sendMessage("§cNo default messages found");
                    return;
                }
                player.sendMessage(ChatColor.GREEN + "Showing all default messages:");
                for (String s : trait.getDefaultMessages()){
                    player.sendMessage(ChatColor.GREEN + s);
                }
            }
        }
    }

    private void resetInteractions(Player player, NPC npc){

    }
}
