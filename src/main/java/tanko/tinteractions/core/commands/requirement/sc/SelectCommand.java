package tanko.tinteractions.core.commands.requirement.sc;

import org.bukkit.entity.Player;
import tanko.tinteractions.TInteractions;
import tanko.tinteractions.api.Interaction;
import tanko.tinteractions.api.Requirement;
import tanko.tinteractions.core.commands.SubCommand;

import java.util.List;

public class SelectCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!(args.length > 0)) return;
        Interaction interaction = TInteractions.getInteractionRegistry().getSelectedInteraction(player);
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
