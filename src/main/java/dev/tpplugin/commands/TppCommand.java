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
 * /tpp (tpplayer) <игрок> — телепортация себя к указанному игроку.
 * Алиас: tpplayer
 * Пермишн: tpplugin.tp.player
 */
public class TppCommand implements CommandExecutor, TabCompleter {

    private static final String PERMISSION = "tpplugin.tp.player";

    private final TeleportPlugin plugin;

    public TppCommand(TeleportPlugin plugin) {
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
            player.sendMessage("§cУ вас нет прав для использования этой команды!");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§eИспользование: §f/tpp <игрок>");
            return true;
        }

        String targetName = args[0];

        if (targetName.equalsIgnoreCase(player.getName())) {
            player.sendMessage("§cВы не можете телепортироваться к себе!");
            return true;
        }

        Player target = Bukkit.getPlayerExact(targetName);

        if (target == null) {
            player.sendMessage("§cИгрок §f" + targetName + " §cне найден или не в сети!");
            return true;
        }

        player.teleport(target.getLocation());
        player.sendMessage("§aТелепортация к игроку §f" + target.getName() + "§a!");
        target.sendMessage("§7К вам телепортировался §f" + player.getName() + "§7.");

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
                    .filter(name -> name.toLowerCase().startsWith(prefix))
                    .filter(name -> !name.equalsIgnoreCase(player.getName()))
                    .toList();
        }

        return Collections.emptyList();
    }
}
