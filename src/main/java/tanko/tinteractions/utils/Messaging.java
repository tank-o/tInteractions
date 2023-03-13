package tanko.tinteractions.utils;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class Messaging {

    public static void messageFromNPC(Player player, NPC npc, String message){
        String npcMessage = ChatColor.YELLOW + npc.getName() + ChatColor.WHITE + " -> " + ChatColor.AQUA + message;
        player.sendMessage(npcMessage);
    }
}
