package tanko.tinteractions.commands;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tanko.tinteractions.TInteractions;
import tanko.tinteractions.system.Interaction;
import tanko.tinteractions.traits.SequentialInteraction;

import java.util.Arrays;
import java.util.Objects;

public class InteractionCommand implements CommandExecutor {

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

        if (player.hasPermission(Objects.requireNonNull(command.getPermission()))) {
            if (args.length > 0){
                String action = args[0];
                switch (action) {
                    case "add" -> addInteraction(player, Arrays.copyOfRange(args, 1, args.length), npc);
                    case "remove" -> removeInteraction(player, Arrays.copyOfRange(args, 1, args.length), npc);
                    case "view" -> viewInteractions(player, npc);
                    case "select" -> selectInteraction(player, Arrays.copyOfRange(args, 1, args.length), npc);
                    case "defaultMessage" -> defaultMessage(player, Arrays.copyOfRange(args, 1, args.length), npc);
                    default -> {
                        SequentialInteraction trait = npc.getTraitNullable(SequentialInteraction.class);
                        if (trait == null) {
                            player.sendMessage("§cInteraction command not found");
                            return false;
                        }
                        Interaction interaction = trait.getInteraction(action);
                        if (interaction == null) {
                            player.sendMessage("§cInteraction command not found");
                            return false;
                        }
                        interaction.handleCommand(player, Arrays.copyOfRange(args, 1, args.length));
                    }
                }
            }
        }
        return true;
    }

    private void addInteraction(Player player, String[] args, NPC npc){
        if (!(args.length > 0)) return;
        String type = args[0].toLowerCase();
        String name = args[1];
        SequentialInteraction trait = npc.getOrAddTrait(SequentialInteraction.class);
        try {
            Class<? extends Interaction> interactionClass = TInteractions.getInteractionRegistry().getInteraction(type);
            // Create an instance of the interaction and cast it to the correct type
            Interaction interaction = interactionClass.getConstructor(String.class).newInstance(name);
            interactionClass.cast(interaction);
            trait.addInteraction(interaction);
            player.sendMessage("§aAdded " + interaction.getID() + " to " + npc.getName());
            TInteractions.getInteractionRegistry().selectInteraction(player, interaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeInteraction(Player player, String[] args, NPC npc){
        if (!(args.length > 0)) return;
        String name = args[0];
        SequentialInteraction trait = npc.getOrAddTrait(SequentialInteraction.class);
        trait.removeInteraction(name);
        player.sendMessage("§aRemoved " + name + " from " + npc.getName());
    }

    private void viewInteractions(Player player, NPC npc){
        SequentialInteraction trait = npc.getOrAddTrait(SequentialInteraction.class);
        if (trait.getInteractions().size() == 0){
            player.sendMessage("§cNo interactions found");
            return;
        }
        player.sendMessage(ChatColor.GREEN + "Showing all interactions:");
        for (Interaction i : trait.getInteractions()){
            player.sendMessage(ChatColor.GREEN + i.getID() + " : " + i.getClass().getSimpleName());
        }
    }

    private void selectInteraction(Player player, String[] args, NPC npc){
        if (!(args.length > 0)) return;
        String name = args[0];
        SequentialInteraction trait = npc.getOrAddTrait(SequentialInteraction.class);
        for (Interaction i : trait.getInteractions()){
            if (i.getID().equals(name)){
                TInteractions.getInteractionRegistry().selectInteraction(player, i);
                player.sendMessage("§aSelected interaction: " + i.getID());
                return;
            }
        }
        player.sendMessage("§cInteraction not found within " + npc.getName());
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
}
