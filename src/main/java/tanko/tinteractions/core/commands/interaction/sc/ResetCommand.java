package tanko.tinteractions.core.commands.interaction.sc;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import tanko.tinteractions.core.commands.SubCommand;
import tanko.tinteractions.core.traits.MenuInteraction;
import tanko.tinteractions.core.traits.SequentialInteraction;

public class ResetCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(player);
        if (npc.hasTrait(SequentialInteraction.class)){
            SequentialInteraction trait = npc.getOrAddTrait(SequentialInteraction.class);
            trait.resetPlayerPosition(player);
            player.sendMessage("§aReset interaction position from " + npc.getName());
        } else if (npc.hasTrait(MenuInteraction.class)){
            MenuInteraction trait = npc.getOrAddTrait(MenuInteraction.class);
            trait.getInteractions().values().forEach(interaction -> interaction.getCompleted().remove(player.getUniqueId().toString()));
            player.sendMessage("§aReset interaction position from " + npc.getName());
        }
    }
}
