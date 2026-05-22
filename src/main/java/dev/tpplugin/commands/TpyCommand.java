package dev.tpplugin.commands;

import dev.tpplugin.TeleportPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * /tpy (tpyank) <игрок> — притянуть указанного игрока к себе.
 * Алиас: tpyank
 * Пермишн: tpplugin.tp.pull
 */
public class TpyCommand implements CommandExecutor, TabCompleter {

    private static final String PERMISSION = "tpplugin.tp.pull";

    private final TeleportPlugin plugin;

    public TpyCommand(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cЭта команда доступна только для игроков!");
            return true;
        }

        if (!player.hasPermission(PERMISSION)) {
            player.sendMessage("§cУ вас нет прав для притягивания игроков к себе!");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§eИспользование: §f/tpy <игрок>");
            return true;
        }

        String targetName = args[0];

        if (targetName.equalsIgnoreCase(player.getName())) {
            player.sendMessage("§cНельзя притянуть самого себя!");
            return true;
        }

        Player target = Bukkit.getPlayerExact(targetName);

        if (target == null) {
            player.sendMessage("§cИгрок §f" + targetName + " §cне найден или не в сети!");
            return true;
        }

        target.teleport(player.getLocation());
        target.sendMessage("§aВас телепортировали к §f" + player.getName() + "§a.");
        player.sendMessage("§aИгрок §f" + target.getName() + " §aпритянут к вам.");

        return true;
    }

    @Override
    public List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (!(sender instanceof Player player) || !player.hasPermission(PERMISSION)) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(prefix))
                    .filter(n -> !n.equalsIgnoreCase(player.getName()))
                    .toList();
        }

        return Collections.emptyList();
    }
}
