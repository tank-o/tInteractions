package tanko.tinteractions.core.menus;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import tanko.tinteractions.api.Interaction;
import tanko.tinteractions.core.menus.icons.Border;
import tanko.tinteractions.core.menus.icons.InteractionIcon;
import tanko.tinteractions.core.traits.MenuInteraction;

import java.util.Collection;
import java.util.List;

public class InteractionMenu {
    protected GUI menu;
    protected String title = "Select Interaction";
    protected Player player;
    List<Integer> interactionSlots = List.of(20, 22, 21, 13, 31);

    public InteractionMenu(Player player, NPC npc){
        this.player = player;
        menu = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(
                        "# # # # # # # # #",
                        "# . . . . . . . #",
                        "# . . . . . . . #",
                        "# . . . . . . . #",
                        "# # # # # # # # #")
                .addIngredient('#', Border::new)
                .build();

        title = npc.getName() + " - " + title;

        MenuInteraction menuInteraction = npc.getTraitNullable(MenuInteraction.class);
        if (menuInteraction == null){
            player.sendMessage("§cNPC does not have a menu interaction, but you got to this menu somehow, contact an admin");
            return;
        }
        Collection<Interaction> interactions = menuInteraction.getInteractions().values();
        interactions.removeIf(interaction -> interaction.hasCompleted(player));
        for (int i = 0; i < interactions.size(); i++) {
            Interaction interaction = interactions.toArray(new Interaction[0])[i];
            menu.setItem(interactionSlots.get(i), new InteractionIcon(player, interaction, menuInteraction));
        }
    }

    public void open() {
        if (menu == null){
            player.sendMessage("Menu not initialized, Contact an admin");
            return;
        }
        new SimpleWindow(player, title, menu).show();
    }
}
