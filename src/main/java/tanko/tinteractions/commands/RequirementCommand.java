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
import tanko.tinteractions.system.Requirement;
import tanko.tinteractions.traits.SequentialInteraction;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RequirementCommand implements CommandExecutor {

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

        if (player.hasPermission(Objects.requireNonNull(command.getPermission()))) {
            if (args.length > 0){
                String action = args[0];
                switch (action) {
                    case "add" -> addRequirement(player, Arrays.copyOfRange(args, 1, args.length), interaction);
                    case "remove" -> removeRequirement(player, Arrays.copyOfRange(args, 1, args.length), interaction);
                    case "view" -> viewRequirements(player, interaction);
                    case "select" -> selectRequirement(player, Arrays.copyOfRange(args, 1, args.length), interaction);
                    default -> {
                        Requirement requirement = interaction.getRequirement(action);
                        if (requirement == null){
                            player.sendMessage("§cRequirement command not found");
                            return false;
                        }
                        requirement.handleCommand(player, Arrays.copyOfRange(args, 1, args.length));
                    }
                }
            }
        }
        return true;
    }

    private void addRequirement(Player player, String[] args, Interaction interaction){
        if (!(args.length > 1)) return;
        String type = args[0].toLowerCase();
        String name = args[1];
        try {
            Class<? extends Requirement> requirementClass = TInteractions.getInteractionRegistry().getRequirement(type);
            // Create an instance of the interaction and cast it to the correct type
            Requirement requirement = requirementClass.getConstructor(String.class).newInstance(name);
            requirementClass.cast(requirement);
            interaction.addRequirement(requirement);
            player.sendMessage("§aAdded " + requirementClass.getSimpleName() + " to " + interaction.getID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeRequirement(Player player, String[] args, Interaction interaction){
        if (!(args.length > 0)) return;
        String name = args[0];
        interaction.removeRequirement(name);
        player.sendMessage("§aRemoved " + name + " from " + interaction.getID());
    }

    private void viewRequirements(Player player, Interaction interaction){
        List<Requirement> requirements = interaction.getRequirements();
        if (requirements.size() == 0) {
            player.sendMessage("§aRequirements: §cNone");
        } else {
            player.sendMessage("§aRequirements:");
        }
        for (Requirement r : requirements){
            player.sendMessage(ChatColor.GREEN + r.getID() + " : " + r.getClass().getSimpleName());
        }
    }

    private void selectRequirement(Player player, String[] args, Interaction interaction){
        if (!(args.length > 0)) return;
        String name = args[0];
        List<Requirement> requirements = interaction.getRequirements();
        for (Requirement r : requirements){
            if (r.getID().equals(name)){
                TInteractions.getInteractionRegistry().selectRequirement(player,r);
                player.sendMessage("§aSelected " + r.getID() + " from " + interaction.getID() + " requirements");
                return;
            }
        }
    }
}
