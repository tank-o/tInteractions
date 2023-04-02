package tanko.tinteractions.core.commands.interaction.sc;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import tanko.tinteractions.TInteractions;
import tanko.tinteractions.api.Interaction;
import tanko.tinteractions.core.commands.SubCommand;
import tanko.tinteractions.core.traits.InteractionTrait;
import tanko.tinteractions.core.traits.MenuInteraction;
import tanko.tinteractions.core.traits.SequentialInteraction;

import java.util.Objects;

public class AddCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(player);
        if (!(args.length > 0)) return;
        String type = args[0].toLowerCase();
        String name = args[1];
        try {
            InteractionTrait trait = Objects.requireNonNullElse(npc.getTraitNullable(SequentialInteraction.class), npc.getTraitNullable(MenuInteraction.class));
            Class<? extends Interaction> interactionClass = TInteractions.getInteractionRegistry().getInteraction(type);
            // Create an instance of the interaction and cast it to the correct type
            Interaction interaction = interactionClass.getConstructor(String.class).newInstance(name);
            interactionClass.cast(interaction);
            trait.addInteraction(interaction);
            player.sendMessage("§aAdded " + interaction.getID() + " to " + npc.getName());
            TInteractions.getInteractionRegistry().selectInteraction(player, interaction);
        } catch (NullPointerException npe) {
            player.sendMessage("§cNPC does not have an interaction trait");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
