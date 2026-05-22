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
import java.util.stream.Collectors;

/**
 * /tpe (tpeach) <игрок1> <игрок2> — телепортировать первого игрока ко второму.
 * Алиас: tpeach
 * Пермишн: tpplugin.tp.force
 */
public class TpeCommand implements CommandExecutor, TabCompleter {

    private static final String PERMISSION = "tpplugin.tp.force";

    private final TeleportPlugin plugin;

    public TpeCommand(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (!sender.hasPermission(PERMISSION)) {
            sender.sendMessage("§cУ вас нет прав для принудительной телепортации игроков!");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§eИспользование: §f/tpe <игрок1> <игрок2>");
            return true;
        }

        Player from = Bukkit.getPlayerExact(args[0]);
        Player to   = Bukkit.getPlayerExact(args[1]);

        if (from == null) {
            sender.sendMessage("§cИгрок §f" + args[0] + " §cне найден или не в сети!");
            return true;
        }
        if (to == null) {
            sender.sendMessage("§cИгрок §f" + args[1] + " §cне найден или не в сети!");
            return true;
        }
        if (from.equals(to)) {
            sender.sendMessage("§cНельзя телепортировать игрока к самому себе!");
            return true;
        }

        from.teleport(to.getLocation());

        from.sendMessage("§aВас телепортировали к §f" + to.getName() + "§a.");
        to.sendMessage("§7К вам телепортировали §f" + from.getName() + "§7.");
        if (!(sender instanceof Player p) || !p.equals(from)) {
            sender.sendMessage("§aИгрок §f" + from.getName() +
                    " §aтелепортирован к §f" + to.getName() + "§a.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (!sender.hasPermission(PERMISSION)) {
            return Collections.emptyList();
        }

        String prefix = args.length > 0 ? args[args.length - 1].toLowerCase() : "";

        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(prefix))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(prefix))
                    .filter(n -> !n.equalsIgnoreCase(args[0]))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
