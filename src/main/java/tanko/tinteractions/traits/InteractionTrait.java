package tanko.tinteractions.traits;

import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tanko.tinteractions.persistence.ConfigReader;
import tanko.tinteractions.persistence.ConfigWriter;
import tanko.tinteractions.system.Interaction;

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
