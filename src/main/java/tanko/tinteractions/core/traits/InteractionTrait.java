package tanko.tinteractions.core.traits;

import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Bukkit;
import tanko.tinteractions.api.Interaction;
import tanko.tinteractions.core.persistence.ConfigReader;
import tanko.tinteractions.core.persistence.ConfigWriter;

import java.util.HashMap;
import java.util.Map;

public class InteractionTrait extends Trait {
    Map<String, Interaction> interactions = new HashMap<>();

    protected InteractionTrait(String name) {
        super(name);
    }

    @Override
    public void onAttach() {
        Bukkit.getLogger().info("Attaching interaction trait to npc " + getNPC().getId());
        String npcID = String.valueOf(getNPC().getId());
        // Read all the interactions within the config file for this npc
        interactions.putAll(ConfigReader.readInteractions(npcID));
    }

    @Override
    public void onRemove() {
        String npcID = String.valueOf(getNPC().getId());
        ConfigWriter.clear(npcID);
    }

    public Map<String, Interaction> getInteractions() {
        return interactions;
    }

    public void addInteraction(Interaction interaction){
        interactions.put(interaction.getID(), interaction);
    }

    public Interaction getInteraction(String ID){
        return interactions.get(ID);
    }

    public void removeInteraction(String ID){
        interactions.remove(ID);
    }
}
