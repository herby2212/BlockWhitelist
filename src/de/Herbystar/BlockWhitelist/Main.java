package de.Herbystar.BlockWhitelist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener {
	
	public String prefix = "§7[§cBWL§7] ";
	public boolean UpdateAviable;
	
	public void onEnable() {
		
		loadConfig();
		
		getServer().getPluginManager().registerEvents(this, this);
		
		Server server = Bukkit.getServer();
	    ConsoleCommandSender console = server.getConsoleSender();
	    console.sendMessage("§7[§cBlockWhitelList§7] " + ChatColor.AQUA + "Version: " + getDescription().getVersion() + " §aby " + "§c" + getDescription().getAuthors() + ChatColor.GREEN + " enabled!");
		
	}
		
	@EventHandler
	public void onJoinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(p.isOp()) {
			if(this.UpdateAviable == true) {
//				p.sendMessage("§7[§cBlockWhitelList§7] §a-=> Update is available! <=-");
			}
		}
	}

	private void loadConfig() {
		this.getConfig().options().copyDefaults(true);
		saveConfig();		
	}
	
	public void onDisable() {
		
		Server server = Bukkit.getServer();
	    ConsoleCommandSender console = server.getConsoleSender();
	    console.sendMessage("§7[§cBlockWhitelist§7] " + ChatColor.AQUA + "Version: " + getDescription().getVersion() + " §aby " + "§c" + getDescription().getAuthors() + ChatColor.RED + " disabled!");

	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = null;
		if(sender instanceof Player) {
			p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("BlockWhitelist") | (cmd.getName().equalsIgnoreCase("BWL"))) {
				if(args.length == 0) {
					p.sendMessage(this.prefix + "§7use §c/BWL Toggle §7to §aenable §7or §cdisable §7the Plugin!");
					return true;
				}
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("Toggle")) {
						if(this.getConfig().getBoolean("BlockWhitelist.Enabled") == true) {
							this.getConfig().set("BlockWhitelist.Enabled", false);
							saveConfig();
							p.sendMessage(this.prefix + "§7Anti Block Breaking §cdisabled!");
							return true;
						} else {
							this.getConfig().set("BlockWhitelist.Enabled", true);
							saveConfig();
							p.sendMessage(this.prefix + "§7Anti Block Breaking §aenabled!");
							return true;
						}
					}
				}				
			}
		}							
		return false;			
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockbreak(BlockBreakEvent e) {
		
		Material m = e.getBlock().getType();
		if(this.getConfig().getBoolean("BlockWhitelist.Enabled") == true) {
			for(String IID : this.getConfig().getStringList("BlockWhitelist.BlockWhitelist"))
				try {
					if(m.getId() == Integer.parseInt(IID))
						e.setCancelled(true);
				} catch (NumberFormatException exe) {
					Material IM = Material.getMaterial(IID);
					if((IM != null) && (IM == m))
						e.setCancelled(true);
				}
		}		
	}
}
