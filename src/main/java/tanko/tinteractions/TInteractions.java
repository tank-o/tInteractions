package tanko.tinteractions;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.plugin.java.JavaPlugin;
import tanko.tinteractions.api.InteractionRegistry;
import tanko.tinteractions.core.DefaultRegistry;
import tanko.tinteractions.core.commands.interaction.InteractionCommand;
import tanko.tinteractions.core.commands.interaction.sc.*;
import tanko.tinteractions.core.commands.requirement.RequirementCommand;
import tanko.tinteractions.core.persistence.InteractionsFile;
import tanko.tinteractions.core.traits.MenuInteraction;
import tanko.tinteractions.core.traits.SequentialInteraction;

public final class TInteractions extends JavaPlugin {
    private static InteractionRegistry interactionRegistry;

    @Override
    public void onEnable() {
        // Register Command
        interactionRegistry = new DefaultRegistry();

        // Setup Data Files
        InteractionsFile.setup(this);

        registerCommands();
        registerTraits();
    }

    @Override
    public void onDisable() {
        interactionRegistry.shutdown();
    }

    private void registerTraits(){
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(SequentialInteraction.class).withName("seq-interaction"));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(MenuInteraction.class).withName("menu-interaction"));
    }

    private void registerCommands() {
        this.getCommand("interactions").setExecutor(new InteractionCommand());
        InteractionCommand.register("add", new AddCommand());
        InteractionCommand.register("remove", new RemoveCommand());
        InteractionCommand.register("view", new ViewCommand());
        InteractionCommand.register("select", new SelectCommand());
        InteractionCommand.register("reset", new ResetCommand());
        this.getCommand("requirements").setExecutor(new RequirementCommand());
        RequirementCommand.register("add", new tanko.tinteractions.core.commands.requirement.sc.AddCommand());
        RequirementCommand.register("remove", new tanko.tinteractions.core.commands.requirement.sc.RemoveCommand());
        RequirementCommand.register("view", new tanko.tinteractions.core.commands.requirement.sc.ViewCommand());
        RequirementCommand.register("select", new tanko.tinteractions.core.commands.requirement.sc.SelectCommand());
    }

    public static InteractionRegistry getInteractionRegistry(){
        return interactionRegistry;
    }
}
