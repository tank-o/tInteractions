package tanko.tinteractions;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.plugin.java.JavaPlugin;
import tanko.tinteractions.api.InteractionRegistry;
import tanko.tinteractions.core.DefaultRegistry;
import tanko.tinteractions.core.commands.interaction.InteractionCommand;
import tanko.tinteractions.core.commands.interaction.sc.*;
import tanko.tinteractions.core.commands.requirement.RequirementCommand;
import tanko.tinteractions.core.persistence.ConfigReader;
import tanko.tinteractions.core.persistence.InteractionsFile;
import tanko.tinteractions.core.traits.InteractionTrait;
import tanko.tinteractions.core.traits.MenuInteraction;
import tanko.tinteractions.core.traits.SequentialInteraction;

import java.util.Objects;

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
        InteractionCommand interactionCommand = new InteractionCommand();
        this.getCommand("interactions").setExecutor(interactionCommand);
        interactionCommand.register("add", new AddCommand());
        interactionCommand.register("remove", new RemoveCommand());
        interactionCommand.register("view", new ViewCommand());
        interactionCommand.register("select", new SelectCommand());
        interactionCommand.register("reset", new ResetCommand());
        RequirementCommand requirementCommand = new RequirementCommand();
        this.getCommand("requirements").setExecutor(requirementCommand);
        requirementCommand.register("add", new tanko.tinteractions.core.commands.requirement.sc.AddCommand());
        requirementCommand.register("remove", new tanko.tinteractions.core.commands.requirement.sc.RemoveCommand());
        requirementCommand.register("view", new tanko.tinteractions.core.commands.requirement.sc.ViewCommand());
        requirementCommand.register("select", new tanko.tinteractions.core.commands.requirement.sc.SelectCommand());
    }

    public static InteractionRegistry getInteractionRegistry(){
        return interactionRegistry;
    }

    public static void reloadNPCs(){
        // Go through each of the NPCs and reload their interactions
        for (NPC npc : CitizensAPI.getNPCRegistry()){
            try {
                InteractionTrait trait = Objects.requireNonNullElse(npc.getTraitNullable(SequentialInteraction.class), npc.getTraitNullable(MenuInteraction.class));
                trait.reloadInteractions();
            } catch (IllegalArgumentException ignored){ }
        }
    }
}
