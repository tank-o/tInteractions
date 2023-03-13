package tanko.tinteractions.system;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public abstract class Requirement {
    protected String ID;

    public Requirement(String ID){
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public abstract void writeConfig(ConfigurationSection section);

    public abstract void readConfig(ConfigurationSection section);

    public abstract boolean checkSatisfied(Player player, NPC npc);

    public abstract void requirementSatisfiedAction(Player player, NPC npc);

    public abstract void handleCommand(Player player, String[] args);
}