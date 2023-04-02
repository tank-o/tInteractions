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
import tanko.tinteractions.TInteractions;
import tanko.tinteractions.api.Interaction;
import tanko.tinteractions.core.commands.SubCommand;
import tanko.tinteractions.core.traits.InteractionTrait;
import tanko.tinteractions.core.traits.MenuInteraction;
import tanko.tinteractions.core.traits.SequentialInteraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InteractionCommand implements CommandExecutor {

    private static final Map<String, SubCommand> commands = new HashMap<>();

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
            if (args.length > 0) {
                String action = args[0];
                if (commands.containsKey(action)) {
                    commands.get(action).execute(player,Arrays.copyOfRange(args, 1, args.length));
                } else {
                    player.sendMessage("§cInvalid command");
                    return true;
                }
            } else {
                player.sendMessage("§cInvalid command");
            }
        } catch (CommandException e) {
            player.sendMessage("§c" + e.getMessage());
        }

        if (player.hasPermission(Objects.requireNonNull(command.getPermission()))) {
            if (args.length > 0){
                String action = args[0];
                switch (action) {
                    case "reset" -> resetInteractions(player, npc);
                    case "defaultMessage" -> defaultMessage(player, Arrays.copyOfRange(args, 1, args.length), npc);
                    default -> {
                        try {
                            InteractionTrait trait = Objects.requireNonNullElse(npc.getTraitNullable(SequentialInteraction.class), npc.getTraitNullable(MenuInteraction.class));
                            Interaction interaction = trait.getInteraction(action);
                            if (interaction == null) {
                                player.sendMessage("§cInteraction command not found");
                                return false;
                            }
                            interaction.command(player, Arrays.copyOfRange(args, 1, args.length));
                        } catch (NullPointerException npe) {
                            player.sendMessage("§cNPC does not have an interaction trait");
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Gets the SubCommand represnted by a specific Command
     *
     * @param cmd The name of the command to get
     * @return The SubCommand of the command
     * @throws CommandException when the command was not found. Should be caught.
     */
    SubCommand getCommand(String cmd) throws CommandException {
        if (commands.containsKey(cmd)) {
            return commands.get(cmd);
        } else {
            throw new CommandException("This command was not found.");
        }
    }

    /**
     * Registers a command
     *
     * @param cmd The command to register
     * @param clazz The class to register the command to. Must implement
     *        SubCommand.
     */
    public static void register(String cmd, SubCommand clazz) {
        try {
            Class.forName(clazz.getClass().getName());
        } catch (ClassNotFoundException e) {
            return;
        }
        commands.put(cmd, clazz);
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
