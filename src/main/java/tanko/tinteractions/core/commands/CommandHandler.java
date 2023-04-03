package tanko.tinteractions.core.commands;

import org.bukkit.command.CommandException;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    protected final Map<String, SubCommand> commands = new HashMap<>();

    /**
     * Gets the SubCommand represnted by a specific Command
     *
     * @param cmd The name of the command to get
     * @return The SubCommand of the command
     * @throws CommandException when the command was not found. Should be caught.
     */
    protected SubCommand getCommand(String cmd) throws CommandException {
        if (commands.containsKey(cmd)) {
            return commands.get(cmd);
        } else {
            throw new CommandException("This command was not found.");
        }
    }

    /**
     * Registers a command
     *
     * @param cmd The command to register
     * @param clazz The class to register the command to. Must implement
     *        SubCommand.
     */
    public void register(String cmd, SubCommand clazz) {
        try {
            Class.forName(clazz.getClass().getName());
        } catch (ClassNotFoundException e) {
            return;
        }
        commands.put(cmd, clazz);
    }
}
