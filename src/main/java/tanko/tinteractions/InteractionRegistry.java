package tanko.tinteractions;

import org.bukkit.entity.Player;
import tanko.tinteractions.interactions.TextInteraction;
import tanko.tinteractions.requirements.ItemRequirement;
import tanko.tinteractions.system.Interaction;
import tanko.tinteractions.system.Requirement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InteractionRegistry {
    private final Map<String,Class<? extends Interaction>> interactionTypes = new HashMap<>();
    private final Map<String,Class<? extends Requirement>> requirementTypes = new HashMap<>();
    private final Map<UUID,Interaction> selectedInteractions = new HashMap<>();
    private final Map<UUID,Requirement> selectedRequirements = new HashMap<>();

    public InteractionRegistry(){
        // Register default interactions
        registerInteraction("text", TextInteraction.class);
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
}
