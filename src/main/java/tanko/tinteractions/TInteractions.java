package tanko.tinteractions;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tanko.tinteractions.api.InteractionRegistry;
import tanko.tinteractions.core.DefaultRegistry;
import tanko.tinteractions.core.commands.interaction.InteractionCommand;
import tanko.tinteractions.core.commands.requirement.RequirementCommand;
import tanko.tinteractions.core.commands.interaction.sc.*;
import tanko.tinteractions.core.persistence.ConfigWriter;
import tanko.tinteractions.core.persistence.InteractionsFile;
import tanko.tinteractions.core.traits.MenuInteraction;
import tanko.tinteractions.core.traits.SequentialInteraction;

public final class TInteractions extends JavaPlugin {

    private static InteractionRegistry interactionRegistry;
    private static TInteractions instance;

    @Override
    public void onEnable() {
        // Register Command
        interactionRegistry = new DefaultRegistry();
        instance = this;

        // Setup Data Files
        InteractionsFile.setup(this);

        // Register Commands
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



        // Register Traits
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(SequentialInteraction.class).withName("seq-interaction"));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(MenuInteraction.class).withName("menu-interaction"));
    }

    @Override
    public void onDisable() {
        // Loop through all of the NPC's
        // If they have the InteractionTrait
        // Save their interactions
        for (NPC npc : CitizensAPI.getNPCRegistry()){
            if (npc.hasTrait(SequentialInteraction.class)){
                Bukkit.getLogger().info("SAVING INTERACTIONS FOR NPC: " + npc.getId());
                ConfigWriter.writeInteractions(npc.getOrAddTrait(SequentialInteraction.class));
            }
        }
    }

    public static InteractionRegistry getInteractionRegistry(){
        return interactionRegistry;
    }

    public static TInteractions getInstance(){
        return instance;
    }
}
