package tanko.tinteractions.core.commands.interaction.sc;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tanko.tinteractions.api.Interaction;
import tanko.tinteractions.core.commands.SubCommand;
import tanko.tinteractions.core.traits.InteractionTrait;
import tanko.tinteractions.core.traits.MenuInteraction;
import tanko.tinteractions.core.traits.SequentialInteraction;

import java.util.Objects;

public class ViewCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(player);
        try {
            InteractionTrait trait = Objects.requireNonNullElse(npc.getTraitNullable(SequentialInteraction.class), npc.getTraitNullable(MenuInteraction.class));
            if (trait.getInteractions().size() == 0) {
                player.sendMessage("§cNo interactions found");
                return;
            }
            player.sendMessage(ChatColor.GREEN + "Showing all interactions:");
            for (Interaction i : trait.getInteractions().values()) {
                player.sendMessage(ChatColor.GREEN + i.getID() + " : " + i.getClass().getSimpleName());
            }
        } catch (NullPointerException npe) {
            player.sendMessage("§cNPC does not have an interaction trait");
        }
    }
}
