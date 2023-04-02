package tanko.tinteractions.core.commands.interaction.sc;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import tanko.tinteractions.core.commands.SubCommand;
import tanko.tinteractions.core.traits.InteractionTrait;
import tanko.tinteractions.core.traits.MenuInteraction;
import tanko.tinteractions.core.traits.SequentialInteraction;

import java.util.Objects;

public class RemoveCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!(args.length > 0)) return;
        String name = args[0];
        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(player);
        try {
            InteractionTrait trait = Objects.requireNonNullElse(npc.getTraitNullable(SequentialInteraction.class),
                                                                npc.getTraitNullable(MenuInteraction.class));
            trait.removeInteraction(name);
            player.sendMessage("§aRemoved " + name + " from " + npc.getName());
        } catch (NullPointerException npe) {
            player.sendMessage("§cNPC does not have an interaction trait");
        }
    }
}
