package tanko.tinteractions.system;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class Interaction {
    protected String ID;
    protected Material iconMaterial = Material.DIRT;
    protected String interactionFinishMessage = null;
    protected LinkedHashMap<String,Requirement> requirements = new LinkedHashMap<>();

    public Interaction(String ID) {
        this.ID = ID;
    }

    public abstract void action(Player player, NPC npc);

    public abstract void handleCommand(Player player,String[] args);

    public abstract boolean completeChecks(Player player, NPC npc);

    public void viewInteractionInfo(Player player){
        player.sendMessage("§aName: " + ID);

        if (requirements.size() == 0) {
            player.sendMessage("§aRequirements: §cNone");
        }

        for (Requirement r : requirements.values()){
            player.sendMessage(ChatColor.GREEN + r.getID() + " : " + r.getClass().getSimpleName());
        }
    }

    public abstract void readConfig(ConfigurationSection section);

    public abstract void writeConfig(ConfigurationSection section);

    public boolean performInteractionSequence(Player player, NPC npc){
        action(player, npc);
        return interactionComplete(player, npc);
    }

    public boolean interactionComplete(Player player, NPC npc) {
        if (!completeChecks(player, npc)) return false;
        for (Requirement requirement : getRequirements()) {
            if (!requirement.checkSatisfied(player, npc)){
                return false;
            }
        }
        for (Requirement requirement : getRequirements()) {
            requirement.requirementSatisfiedAction(player, npc);
        }
        if (interactionFinishMessage != null) {
            player.sendMessage(interactionFinishMessage);
        }
        return true;
    }

    public void addRequirement(Requirement requirement){
        requirements.put(requirement.getID(),requirement);
    }

    public void removeRequirement(String ID){
        requirements.remove(ID);
    }

    public Requirement getRequirement(String ID){
        return requirements.get(ID);
    }

    public void setRequirements(LinkedHashMap<String,Requirement> requirements) {
        this.requirements = requirements;
    }

    public String getID() {
        return ID;
    }

    public List<Requirement> getRequirements() {
        return new ArrayList<>(requirements.values());
    }

    public Material getIconMaterial() {
        return iconMaterial;
    }
}
