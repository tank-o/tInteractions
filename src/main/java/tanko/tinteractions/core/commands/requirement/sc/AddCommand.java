package tanko.tinteractions.core.commands.requirement.sc;

import org.bukkit.entity.Player;
import tanko.tinteractions.TInteractions;
import tanko.tinteractions.api.Interaction;
import tanko.tinteractions.api.Requirement;
import tanko.tinteractions.core.commands.SubCommand;

public class AddCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!(args.length > 1)) return;
        Interaction interaction = TInteractions.getInteractionRegistry().getSelectedInteraction(player);
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
}
