package tanko.tinteractions.core.menus.icons;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import tanko.tinteractions.api.Interaction;
import tanko.tinteractions.core.traits.MenuInteraction;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class InteractionIcon extends AbstractItem {
    private final Interaction interaction;
    private final Player player;
    private final MenuInteraction menuInteraction;

    public InteractionIcon(Player player, Interaction interaction, MenuInteraction menuInteraction) {
        this.interaction = interaction;
        this.player = player;
        this.menuInteraction = menuInteraction;
    }

    @Override
    public ItemProvider getItemProvider() {
        ItemBuilder builder = new ItemBuilder(interaction.getIconMaterial());
        String selectedInteraction = menuInteraction.getSelectedInteraction(player);
        boolean selected = false;
        builder.setDisplayName(ChatColor.YELLOW + interaction.getDisplayName());
        if (selectedInteraction != null) selected = selectedInteraction.equals(interaction.getID());
        if (selected) builder.addLoreLines(" ", ChatColor.GREEN + "§lSelected");
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        menuInteraction.setSelectedInteraction(player, interaction.getID());
        player.closeInventory();
    }
}
