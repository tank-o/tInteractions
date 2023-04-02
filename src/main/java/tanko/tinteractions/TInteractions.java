package tanko.tinteractions;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tanko.tinteractions.commands.InteractionCommand;
import tanko.tinteractions.commands.RequirementCommand;
import tanko.tinteractions.persistence.ConfigWriter;
import tanko.tinteractions.persistence.InteractionsFile;
import tanko.tinteractions.traits.MenuInteraction;
import tanko.tinteractions.traits.SequentialInteraction;

public final class TInteractions extends JavaPlugin {

    private static InteractionRegistry interactionRegistry;
    private static TInteractions instance;

    @Override
    public void onEnable() {
        // Register Command
        interactionRegistry = new InteractionRegistry();
        instance = this;

        // Setup Data Files
        InteractionsFile.setup(this);

        // Register Commands
        this.getCommand("interactions").setExecutor(new InteractionCommand());
        this.getCommand("requirements").setExecutor(new RequirementCommand());

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
