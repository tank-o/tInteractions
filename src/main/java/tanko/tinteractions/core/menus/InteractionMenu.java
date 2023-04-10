package tanko.tinteractions.core.menus;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import tanko.tinteractions.api.Interaction;
import tanko.tinteractions.core.menus.icons.Border;
import tanko.tinteractions.core.menus.icons.InteractionIcon;
import tanko.tinteractions.core.traits.MenuInteraction;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

import java.util.Collection;
import java.util.List;

public class InteractionMenu {
    protected Gui menu;
    protected String title = "Select Interaction";
    protected Player player;
    List<Integer> interactionSlots = List.of(22, 20, 24, 21, 23);

    public InteractionMenu(Player player, NPC npc){
        this.player = player;
        menu = Gui.normal()
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
        Window window = Window.single()
                .setViewer(player)
                .setGui(menu)
                .setTitle(title)
                .build();
        window.open();
    }
}
