package tanko.tinteractions.api;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class Interaction {
    protected String ID;
    protected Material iconMaterial = Material.DIRT;
    protected String displayName = null;
    protected LinkedHashMap<String,Requirement> requirements = new LinkedHashMap<>();
    protected Map<String,Boolean> completed = new HashMap<>();
    protected boolean isRepeatable = false;

    public Interaction(String ID) {
        this.ID = ID;
    }

    /*************************************************/
    /*               Command Methods                 */
    /*************************************************/

    public abstract void handleOtherCommand(Player player, String[] args);

    public abstract void handleSetCommand(Player player,String[] args);

    public void command(Player player,String[] args){
        if (args.length < 1) {
            player.sendMessage("§cYou must specify a command");
            return;
        }
        String var = args[0];
        if (var.equalsIgnoreCase("view")) {
            viewInteractionInfo(player);
        } else if (var.equalsIgnoreCase("set")) {
            handleDefaultSetCommand(player, Arrays.copyOfRange(args, 1, args.length));
        } else {
            handleOtherCommand(player,args);
        }
    }

    public void handleDefaultSetCommand(Player player, String[] args){
        if (args.length < 1) {
            player.sendMessage("§cYou must specify a variable to set");
            return;
        }
        String var = args[0];
        if (var.equalsIgnoreCase("repeatable")) {
            if (args.length < 2) {
                player.sendMessage("§cYou must specify true or false");
                return;
            }
            String bool = args[1];
            if (bool.equalsIgnoreCase("true")) {
                isRepeatable = true;
                player.sendMessage("§aRepeatable set to true");
            } else if (bool.equalsIgnoreCase("false")) {
                isRepeatable = false;
                player.sendMessage("§aRepeatable set to false");
            } else {
                player.sendMessage("§cInvalid argument");
            }
        } else if (var.equalsIgnoreCase("icon")) {
            // Get the material that the players hand is holding
            Material material = player.getInventory().getItemInMainHand().getType();
            if (material == Material.AIR) {
                player.sendMessage("§cYou must be holding an item");
                return;
            }
            iconMaterial = material;
        } else if (var.equalsIgnoreCase("name")) {
            if (args.length < 2) {
                player.sendMessage("§cYou must specify a display name");
                return;
            }
            displayName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            player.sendMessage("§aDisplay name set to: " + displayName);
        } else {
            handleSetCommand(player,Arrays.copyOfRange(args, 1, args.length));
        }
    }

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

    /*************************************************/
    /*               Config Methods                  */
    /*************************************************/

    public abstract void readConfig(ConfigurationSection section);

    public abstract void writeConfig(ConfigurationSection section);

    /*************************************************/
    /*                 Action Methods                */
    /*************************************************/

    public abstract void action(Player player, NPC npc);

    public void performInteractionSequence(Player player, NPC npc){
        if ((!isRepeatable) && hasCompleted(player)) return;
        if (requirementsSatisfied(player, npc)) {
            action(player, npc);
            if (completeChecks(player, npc)) {
                addCompleted(player);
            }
        }
    }

    public boolean requirementsSatisfied(Player player, NPC npc) {
        for (Requirement requirement : getRequirements()) {
            if (!requirement.checkSatisfied(player, npc)){
                return false;
            }
        }
        for (Requirement requirement : getRequirements()) {
            requirement.requirementSatisfiedAction(player, npc);
        }
        return true;
    }

    /*************************************************/
    /*               Getters/Setters                 */
    /*************************************************/

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

    public void setIconMaterial(Material iconMaterial) {
        this.iconMaterial = iconMaterial;
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public void setRepeatable(boolean repeatable) {
        isRepeatable = repeatable;
    }

    public void setCompleted(Map<String, Boolean> completed) {
        this.completed = completed;
    }

    public void addCompleted(Player player){
        completed.put(player.getUniqueId().toString(),true);
    }

    public boolean hasCompleted(Player player){
        return completed.getOrDefault(player.getUniqueId().toString(),false);
    }

    public Map<String,Boolean> getCompleted(){
        return completed;
    }

    public String getDisplayName(){
        return Objects.requireNonNullElse(displayName,ID);
    }

    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    public Material getIcon(){
        return iconMaterial;
    }
}
