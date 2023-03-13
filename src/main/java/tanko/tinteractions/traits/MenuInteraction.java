package tanko.tinteractions.traits;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.event.EventHandler;

public class MenuInteraction extends Trait {
    protected MenuInteraction() {
        super("menuinteraction");
    }

    @EventHandler
    private void click(NPCRightClickEvent event){
        if (event.getNPC() == this.getNPC()){

        }
    }
}
