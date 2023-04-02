package tanko.tinteractions.api;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface InteractionRegistry {
    Map<String,Class<? extends Interaction>> interactionTypes = new HashMap<>();
    Map<String,Class<? extends Requirement>> requirementTypes = new HashMap<>();
    Map<UUID, Interaction> selectedInteractions = new HashMap<>();
    Map<UUID,Requirement> selectedRequirements = new HashMap<>();

    void selectInteraction(Player player, Interaction interaction);

    Interaction getSelectedInteraction(Player player);

    Class<? extends Interaction> getInteraction(String type);

    void registerInteraction(String type, Class<? extends Interaction> interactionClass);

    String getInteractionName(Class<? extends Interaction> interactionClass);

    Class<? extends Requirement> getRequirement(String type);

    void selectRequirement(Player player, Requirement requirement);

    Requirement getSelectedRequirement(Player player);

    void registerRequirement(String type, Class<? extends Requirement> requirementClass);

    String getRequirementName(Class<? extends Requirement> requirementClass);
}
