package tanko.tinteractions.persistence;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class InteractionsFile {
    private static File interactionsFile;
    private static FileConfiguration interactionsFileConfig;

    //Finds existent interactionsdata file or generates interactionsdata file
    public static void setup(JavaPlugin plugin){
        interactionsFile = new File(plugin.getDataFolder() + "/interactions.yml");
        if (!interactionsFile.exists()){
            try{
                interactionsFile.createNewFile();
            }catch (IOException e){
                //stop in the name of plod
            }
        }
        interactionsFileConfig = YamlConfiguration.loadConfiguration(interactionsFile);
        interactionsFileConfig.options().copyDefaults(true);
    }

    //Get instance of file configuration
    public static FileConfiguration getFile(){
        return interactionsFileConfig;
    }

    //Save file
    public static void save(){
        try{
            interactionsFileConfig.save(interactionsFile);
        }catch (IOException e){
            Bukkit.getLogger().warning("couldn't save file");
        }

    }

    //Reload File
    public static void reload(){
        interactionsFileConfig = YamlConfiguration.loadConfiguration(interactionsFile);
    }
}
