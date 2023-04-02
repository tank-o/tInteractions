package tanko.tinteractions.traits;

import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import tanko.tinteractions.menus.InteractionMenu;
import tanko.tinteractions.persistence.ConfigReader;
import tanko.tinteractions.persistence.ConfigWriter;
import tanko.tinteractions.system.Interaction;

import java.util.HashMap;
import java.util.Map;

public class MenuInteraction extends InteractionTrait {
    @Persist HashMap<String,String> selectedInteractions = new HashMap<>();
    public MenuInteraction() {
        super("menu-interaction");
    }

    /**
     * Performs the interaction when the player right clicks the NPC if the player has selected an interaction
     * otherwise it will open the menu to select an interaction
     * @param event The event that triggered the interaction
     **/
    @EventHandler
    private void rightClick(NPCRightClickEvent event){
        if (event.getNPC() == this.getNPC()){
            Player player = event.getClicker();
            String selectedInteraction = getSelectedInteraction(player);
            if (selectedInteraction == null){
                new InteractionMenu(player, this.getNPC()).open();
            } else {
                interactions.get(selectedInteraction).performInteractionSequence(player, this.getNPC());
            }
        }
    }

    /**
     * Open the menu to select an interaction
     * @param event The event that triggered the interaction
     **/
    @EventHandler
    private void leftClick(NPCLeftClickEvent event){
        if (event.getNPC() == this.getNPC()){
            Player player = event.getClicker();
            new InteractionMenu(player, this.getNPC()).open();
        }
    }

    public String getSelectedInteraction(Player player){
        return selectedInteractions.get(player.getUniqueId().toString());
    }

    public void setSelectedInteraction(Player player, String interaction){
        selectedInteractions.put(player.getUniqueId().toString(), interaction);
    }

}
