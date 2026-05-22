package dev.tpplugin.commands;

import dev.tpplugin.TeleportPlugin;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * /tpl (tplocation) <x> <y> <z> — телепортация себя по координатам.
 * Алиас: tplocation
 * Пермишн: tpplugin.tp.coords
 */
public class TplCommand implements CommandExecutor, TabCompleter {

    private static final String PERMISSION = "tpplugin.tp.coords";

    private final TeleportPlugin plugin;

    public TplCommand(TeleportPlugin plugin) {
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
            player.sendMessage("§cУ вас нет прав для телепортации по координатам!");
            return true;
        }

        if (args.length != 3) {
            player.sendMessage("§eИспользование: §f/tpl <x> <y> <z>");
            return true;
        }

        double x, y, z;
        try {
            x = parseCoord(args[0], player.getLocation().getX());
            y = parseCoord(args[1], player.getLocation().getY());
            z = parseCoord(args[2], player.getLocation().getZ());
        } catch (NumberFormatException e) {
            player.sendMessage("§cОшибка: координаты должны быть числами (или ~N для относительных)!");
            return true;
        }

        if (y < player.getWorld().getMinHeight() || y > player.getWorld().getMaxHeight()) {
            player.sendMessage("§cКоордината Y §f" + (int) y +
                    " §cвне допустимого диапазона (§f" + player.getWorld().getMinHeight() +
                    " §c– §f" + player.getWorld().getMaxHeight() + "§c)!");
            return true;
        }

        Location dest = new Location(
                player.getWorld(), x, y, z,
                player.getLocation().getYaw(),
                player.getLocation().getPitch()
        );
        player.teleport(dest);
        player.sendMessage("§aТелепортирован к §f" +
                formatCoord(x) + " " + formatCoord(y) + " " + formatCoord(z) + "§a.");
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

        return switch (args.length) {
            case 1 -> List.of(String.valueOf((int) player.getLocation().getX()), "~");
            case 2 -> List.of(String.valueOf((int) player.getLocation().getY()), "~");
            case 3 -> List.of(String.valueOf((int) player.getLocation().getZ()), "~");
            default -> Collections.emptyList();
        };
    }

    private double parseCoord(String input, double current) throws NumberFormatException {
        if (input.startsWith("~")) {
            String rel = input.substring(1);
            return rel.isEmpty() ? current : current + Double.parseDouble(rel);
        }
        return Double.parseDouble(input);
    }

    private String formatCoord(double coord) {
        if (coord == Math.floor(coord)) return String.valueOf((int) coord);
        return String.format("%.2f", coord);
    }
}
