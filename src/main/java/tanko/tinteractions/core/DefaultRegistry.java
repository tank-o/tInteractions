package tanko.tinteractions.core;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tanko.tinteractions.api.Interaction;
import tanko.tinteractions.api.InteractionRegistry;
import tanko.tinteractions.api.Requirement;
import tanko.tinteractions.core.interactions.PlayerCommandInteraction;
import tanko.tinteractions.core.interactions.TextInteraction;
import tanko.tinteractions.core.persistence.ConfigWriter;
import tanko.tinteractions.core.requirements.ItemRequirement;
import tanko.tinteractions.core.traits.InteractionTrait;
import tanko.tinteractions.core.traits.MenuInteraction;
import tanko.tinteractions.core.traits.SequentialInteraction;

import java.util.Map;
import java.util.Objects;

public class DefaultRegistry implements InteractionRegistry {

    public DefaultRegistry(){
        // Register default interactions
        registerInteraction("text", TextInteraction.class);
        registerInteraction("command", PlayerCommandInteraction.class);
        // Register default requirements
        registerRequirement("item", ItemRequirement.class);
    }

    // Interaction Functions

    public void selectInteraction(Player player, Interaction interaction){
        selectedInteractions.put(player.getUniqueId(), interaction);
    }

    public Interaction getSelectedInteraction(Player player){
        return selectedInteractions.get(player.getUniqueId());
    }

    public Class<? extends Interaction> getInteraction(String type){
        return interactionTypes.get(type);
    }

    public void registerInteraction(String type, Class<? extends Interaction> interactionClass){
        interactionTypes.put(type, interactionClass);
    }

    public String getInteractionName(Class<? extends Interaction> interactionClass){
        for (Map.Entry<String,Class<? extends Interaction>> entry : interactionTypes.entrySet()){
            if (entry.getValue().equals(interactionClass)) return entry.getKey();
        }
        return null;
    }

    // Requirement Functions
    public Class<? extends Requirement> getRequirement(String type){
        return requirementTypes.get(type);
    }

    public void selectRequirement(Player player, Requirement requirement){
        selectedRequirements.put(player.getUniqueId(), requirement);
    }

    public Requirement getSelectedRequirement(Player player){
        return selectedRequirements.get(player.getUniqueId());
    }

    public void registerRequirement(String type, Class<? extends Requirement> requirementClass){
        requirementTypes.put(type, requirementClass);
    }

    public String getRequirementName(Class<? extends Requirement> requirementClass){
        for (Map.Entry<String,Class<? extends Requirement>> entry : requirementTypes.entrySet()){
            if (entry.getValue().equals(requirementClass)) return entry.getKey();
        }
        return null;
    }

    @Override
    public void shutdown() {
        for (NPC npc : CitizensAPI.getNPCRegistry()){
            try {
                InteractionTrait trait = Objects.requireNonNullElse(npc.getTraitNullable(SequentialInteraction.class),
                        npc.getTraitNullable(MenuInteraction.class));
                Bukkit.getLogger().info("SAVING INTERACTIONS FOR NPC: " + npc.getId());
                ConfigWriter.writeInteractions(trait);
            } catch (NullPointerException e){
                Bukkit.getLogger().info("NPC " + npc.getId() + " has no interaction trait");
            }
        }
    }
}
