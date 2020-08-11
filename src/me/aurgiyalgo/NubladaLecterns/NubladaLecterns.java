package me.aurgiyalgo.NubladaLecterns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Lectern;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class NubladaLecterns extends JavaPlugin implements Listener, CommandExecutor {

    @Override
    public void onEnable() {
        getCommand("nubladalecterns").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Comando no válido. Usa: /nubladalecterns get (destino)");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only a player can do that");
            return true;
        }
        Player p = (Player) sender;
        switch (args[0]) {
            case "get":
                if (args.length < 8) {
                    sender.sendMessage("Not enough arguments (X, Y, Z, Yaw, Pitch, World, Destination)");
                }
                ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta meta = (BookMeta) book.getItemMeta();
                List<String> lore = new ArrayList<String>();
                lore.add(String.valueOf(p.getLocation().getX()));
                lore.add(String.valueOf(p.getLocation().getY()));
                lore.add(String.valueOf(p.getLocation().getZ()));
                lore.add(String.valueOf(p.getLocation().getYaw()));
                lore.add(String.valueOf(p.getLocation().getPitch()));
                lore.add(p.getLocation().getWorld().getName());
                meta.setLore(lore);
                meta.setAuthor("Nublada Servidor");
                meta.setTitle("Portal:" + args[1]);
                List<String> pages = new ArrayList<String>();
                pages.add("Pulsa \n\n\"Recoger Libro\"\n\n para teletransportarte a\n\n " + ChatColor.BOLD + args[1]);
                meta.setPages(pages);
                book.setItemMeta(meta);
                p.getInventory().addItem(book);
                return true;
            default:
                sender.sendMessage(ChatColor.RED + "Comando no válido. Usa: /nubladalecterns get (destino)");
                return true;
        }
    }

    @EventHandler
    public void onPlayerTakeLecternBook(PlayerTakeLecternBookEvent event) {
        ItemStack book = event.getLectern().getInventory().getItem(0);
        if (book == null) return;
        BookMeta meta = (BookMeta) book.getItemMeta();
        if (!meta.getAuthor().equals("Nublada Servidor")) return;
        double x, y, z;
        String destination;
        float yaw, pitch;
        x = Double.valueOf(ChatColor.stripColor(meta.getLore().get(0)));
        y = Double.valueOf(ChatColor.stripColor(meta.getLore().get(1)));
        z = Double.valueOf(ChatColor.stripColor(meta.getLore().get(2)));
        yaw = Float.valueOf(ChatColor.stripColor(meta.getLore().get(3)));
        pitch = Float.valueOf(ChatColor.stripColor(meta.getLore().get(4)));
        destination = meta.getTitle().replace("Portal:", "");
        World world = Bukkit.getWorld(ChatColor.stripColor(meta.getLore().get(5)));
        event.getPlayer().teleport(new Location(world, x, y, z, yaw, pitch));
        event.getPlayer().sendTitle("", ChatColor.translateAlternateColorCodes('&', "&fTe has teletransportado a &6&l" + destination), 10, 50, 10);
        event.setCancelled(true);
    }

    @EventHandler
    public void onLecternBreak(BlockBreakEvent event) {
        if (!(event.getBlock() instanceof Lectern)) return;
        Lectern lectern = (Lectern) event.getBlock();
        ItemStack book = lectern.getInventory().getItem(0);
        if (book == null) return;
        BookMeta meta = (BookMeta) book.getItemMeta();
        if (!meta.getAuthor().equals("Nublada Servidor")) return;
        if (!event.getPlayer().hasPermission("nubladalecterns.use")) event.setCancelled(true);
    }

}
