package tanko.tinteractions.persistence;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import tanko.tinteractions.TInteractions;
import tanko.tinteractions.system.Interaction;
import tanko.tinteractions.system.Requirement;
import tanko.tinteractions.traits.SequentialInteraction;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public final class ConfigWriter {

    public static void writeInteractions(SequentialInteraction trait){
        NPC npc = trait.getNPC();
        Collection<Interaction> interactions = trait.getInteractions();
        ConfigurationSection npcSection = InteractionsFile.getFile().getConfigurationSection(String.valueOf(npc.getId()));
        if (npcSection == null) npcSection = InteractionsFile.getFile().createSection(String.valueOf(npc.getId()));
        ConfigurationSection interactionsSection = npcSection.getConfigurationSection("interactions");
        if (interactionsSection == null) interactionsSection = npcSection.createSection("interactions");
        for (Interaction interaction : interactions){
            ConfigurationSection interactionSection = interactionsSection.getConfigurationSection(interaction.getID());
            if (interactionSection == null) interactionSection = interactionsSection.createSection(interaction.getID());
            writeInteraction(interactionSection, interaction);
        }
        InteractionsFile.save();
    }

    private static void writeInteraction(ConfigurationSection section,Interaction interaction) {
        String type = TInteractions.getInteractionRegistry().getInteractionName(interaction.getClass());
        if (type == null) {
            Bukkit.getLogger().warning("Interaction not registered" + interaction.getID());
        }
        section.set("type", TInteractions.getInteractionRegistry().getInteractionName(interaction.getClass()));
        interaction.writeConfig(section);
        ConfigurationSection requirementsSection = section.getConfigurationSection("requirements");
        if (requirementsSection == null) requirementsSection = section.createSection("requirements");
        for (Requirement requirement : interaction.getRequirements()){
             ConfigurationSection reqSection = requirementsSection.getConfigurationSection(requirement.getID());
             if (reqSection == null) reqSection = requirementsSection.createSection(requirement.getID());
             reqSection.set("type", TInteractions.getInteractionRegistry().getRequirementName(requirement.getClass()));
             requirement.writeConfig(reqSection);
        }
    }

    public static void writePlayerPositions(ConfigurationSection section, Map<UUID,Integer> playerPositions){
        for (UUID uuid : playerPositions.keySet()){
            section.set(uuid.toString(), playerPositions.get(uuid));
        }
    }

    public static void clear(String npcID){
        InteractionsFile.getFile().set(npcID, null);
        InteractionsFile.save();
    }
}
