package tanko.tinteractions.core.traits;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.Persist;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import tanko.tinteractions.api.Interaction;
import tanko.tinteractions.core.persistence.ConfigReader;
import tanko.tinteractions.core.persistence.ConfigWriter;

import java.util.*;

public class SequentialInteraction extends InteractionTrait {
    @Persist private final Map<UUID,Integer> playerPositions = new HashMap<>();
    @Persist private final List<String> defaultMessages = new ArrayList<>();
    @Persist private boolean isRepeatable = false;

    public SequentialInteraction() {
        super("seq-interaction");
    }

    @EventHandler
    private void click(NPCRightClickEvent event){
        if (event.getNPC() == this.getNPC()){
            // Trait logic
            Player player = event.getClicker();
            Integer position = playerPositions.getOrDefault(player.getUniqueId(),0);
            if (position >= interactions.size()){
                if (isRepeatable){
                    playerPositions.put(player.getUniqueId(),0);
                } else {
                    // Send a random default message
                    if (defaultMessages.isEmpty()) return;
                    Random random = new Random();
                    int index = random.nextInt(defaultMessages.size());
                    player.sendMessage(defaultMessages.get(index));
                    return;
                }
            }
            // Perform the interaction and its internal logic
            Interaction interaction = (Interaction) interactions.values().toArray()[position];
            if (interaction != null){
                interaction.performInteractionSequence(player, this.getNPC());
                if (interaction.hasCompleted(player)){
                    playerPositions.put(player.getUniqueId(),position + 1);
                }
            }
        }
    }

    public void addDefaultMessage(String message){
        defaultMessages.add(message);
    }

    public void removeDefaultMessage(int index){
        defaultMessages.remove(index);
    }

    @Override
    public void onAttach() {
        Bukkit.getLogger().info("Attaching interaction trait to npc " + getNPC().getId());
        String npcID = String.valueOf(getNPC().getId());
        // Read all the interactions within the config file for this npc
        interactions = ConfigReader.readInteractions(npcID);
    }

    @Override
    public void onRemove() {
        String npcID = String.valueOf(getNPC().getId());
        ConfigWriter.clear(npcID);
    }

    public void clearDefaultMessages(){
        defaultMessages.clear();
    }

    public List<String> getDefaultMessages(){
        return defaultMessages;
    }

    public boolean isRepeatable(){
        return isRepeatable;
    }

    public void setRepeatable(boolean repeatable){
        isRepeatable = repeatable;
    }

    public void resetPlayerPosition(Player player){
        playerPositions.put(player.getUniqueId(),0);
    }
}
