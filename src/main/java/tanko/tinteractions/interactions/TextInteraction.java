package tanko.tinteractions.interactions;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import tanko.tinteractions.persistence.ConfigReader;
import tanko.tinteractions.persistence.ConfigWriter;
import tanko.tinteractions.system.Interaction;
import tanko.tinteractions.utils.Messaging;
import java.util.*;

public class TextInteraction extends Interaction {
    List<String> messages = new ArrayList<>();
    Map<UUID,Integer> playerPositions = new HashMap<>();

    public TextInteraction(String ID) {
        super(ID);
        iconMaterial = Material.PAPER;
    }

    @Override
    public void action(Player player, NPC npc) {
        Integer playerPos = playerPositions.getOrDefault(player.getUniqueId(),0);
        if (playerPos < messages.size() -1) {
            playerPositions.put(player.getUniqueId(), playerPos + 1);
        }
        Messaging.messageFromNPC(player, npc, messages.get(playerPos));
    }

    @Override
    public boolean completeChecks(Player player, NPC npc) {
        return playerPositions.getOrDefault(player.getUniqueId(),0) <= messages.size();
    }

    @Override
    public void readConfig(ConfigurationSection section) {
        messages = section.getStringList("messages");
        playerPositions = ConfigReader.readPlayerPositions(section.getConfigurationSection("playerPositions"));
    }

    @Override
    public void viewInteractionInfo(Player player){
        super.viewInteractionInfo(player);
        player.sendMessage("Messages: ");
        for (String message : messages){
            player.sendMessage(messages.indexOf(message) + " -> " + message);
        }
    }

    @Override
    public void writeConfig(ConfigurationSection section) {
        section.set("messages", messages);
        ConfigurationSection playerPositionsSection = section.getConfigurationSection("playerPositions");
        if (playerPositionsSection == null) playerPositionsSection = section.createSection("playerPositions");
        ConfigWriter.writePlayerPositions(playerPositionsSection, playerPositions);
    }

    @Override
    public void handleCommand(Player player, String[] args) {
        if (args.length == 0) return;
        String sub = args[0];
        if (sub.equalsIgnoreCase("add")){
            if (args.length < 2) return;
            StringBuilder message = new StringBuilder();
            for (int i = 1; i < args.length; i++){
                message.append(args[i]).append(" ");
            }
            addMessage(message.toString());
            player.sendMessage("§aAdded message: " + message);
        } else if (sub.equalsIgnoreCase("remove")){
            if (args.length < 2) return;
            try {
                int index = Integer.parseInt(args[1]);
                removeMessage(index);
            } catch (NumberFormatException e){
                player.sendMessage("§cInvalid index of message");
            }
        } else if (sub.equalsIgnoreCase("clear")){
            clearMessages();
            player.sendMessage("§aCleared messages");
        } else if (sub.equalsIgnoreCase("list")){
            player.sendMessage("§aMessages: ");
            for (String message : messages){
                player.sendMessage(messages.indexOf(message) + " -> " + message);
            }
        }
    }

    public void addMessage(String message){
        messages.add(message);
    }

    public void removeMessage(int index){
        messages.remove(index);
    }

    public void clearMessages(){
        messages.clear();
    }

    public List<String> getMessages(){
        return messages;
    }

    public Map<UUID, Integer> getPlayerPositions() {
        return playerPositions;
    }
}
