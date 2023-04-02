package tanko.tinteractions.core.commands.requirement.sc;

import org.bukkit.entity.Player;
import tanko.tinteractions.TInteractions;
import tanko.tinteractions.api.Interaction;
import tanko.tinteractions.core.commands.SubCommand;

public class RemoveCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        Interaction interaction = TInteractions.getInteractionRegistry().getSelectedInteraction(player);
        if (!(args.length > 0)) return;
        String name = args[0];
        interaction.removeRequirement(name);
        player.sendMessage("§aRemoved " + name + " from " + interaction.getID());
    }
}
