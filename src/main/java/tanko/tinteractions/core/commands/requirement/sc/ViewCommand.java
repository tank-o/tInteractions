package tanko.tinteractions.core.commands.requirement.sc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tanko.tinteractions.TInteractions;
import tanko.tinteractions.api.Interaction;
import tanko.tinteractions.api.Requirement;
import tanko.tinteractions.core.commands.SubCommand;

import java.util.List;

public class ViewCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        Interaction interaction = TInteractions.getInteractionRegistry().getSelectedInteraction(player);

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
}
