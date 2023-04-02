package tanko.tinteractions.persistence;

import org.bukkit.configuration.ConfigurationSection;
import tanko.tinteractions.TInteractions;
import tanko.tinteractions.system.Interaction;
import tanko.tinteractions.system.Requirement;

import java.util.*;

public final class ConfigReader {

    public static Map<String, Interaction> readInteractions(String npcId){
        ConfigurationSection npcSection = InteractionsFile.getFile().getConfigurationSection(npcId);
        if (npcSection == null) return new LinkedHashMap<>();
        ConfigurationSection interactionsSection = npcSection.getConfigurationSection("interactions");
        if (interactionsSection == null) return new LinkedHashMap<>();
        LinkedHashMap<String, Interaction> interactions = new LinkedHashMap<>();
        for (String key : interactionsSection.getKeys(false)){
            ConfigurationSection interactionSection = interactionsSection.getConfigurationSection(key);
            assert interactionSection != null;
            Interaction interaction = readInteraction(interactionSection);
            if (interaction != null) interactions.put(key, interaction);
        }
        return interactions;
    }

    private static Interaction readInteraction(ConfigurationSection interactionSection){
        String type = interactionSection.getString("type");
        try {
            Class<? extends Interaction> interactionClass = TInteractions.getInteractionRegistry().getInteraction(type);
            // Create an instance of the interaction and cast it to the correct type
            Interaction interaction = interactionClass.getConstructor(String.class).newInstance(interactionSection.getName());
            interactionClass.cast(interaction);
            interaction.readConfig(interactionSection);
            ConfigurationSection requirementsSection = interactionSection.getConfigurationSection("requirements");
            if (requirementsSection != null) {
                interaction.setRequirements(readRequirements(requirementsSection));
            }
            return interaction;
        } catch (Exception e){
            return null;
        }
    }

    private static LinkedHashMap<String,Requirement> readRequirements(ConfigurationSection section){
        LinkedHashMap<String,Requirement> requirements = new LinkedHashMap<>();
        for (String key : section.getKeys(false)){
            requirements.put(key, readRequirement(section.getConfigurationSection(key)));
        }
        return requirements;
    }

    private static Requirement readRequirement(ConfigurationSection section){
        String type = section.getString("type");
        try {
            Class<? extends Requirement> requirementClass = TInteractions.getInteractionRegistry().getRequirement(type);
            // Create an instance of the requirement and cast it to the correct type
            Requirement requirement = requirementClass.getConstructor(String.class).newInstance(section.getName());
            requirementClass.cast(requirement);
            requirement.readConfig(section);
            return requirement;
        } catch (Exception e){
            return null;
        }
    }

    public static Map<UUID,Integer> readPlayerPositions(ConfigurationSection section){
        if (section == null) return new HashMap<>();
        Map<UUID,Integer> playerPositions = new HashMap<>();
        for (String key : section.getKeys(false)){
            playerPositions.put(UUID.fromString(key), section.getInt(key));
        }
        return playerPositions;
    }
}
