package com.hotmail.idonno;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;


public class WaterDamage extends JavaPlugin implements Listener {
	
	
	Logger log = Logger.getLogger("Minecraft");

	private HashMap<UUID,World> effectedWorlds;
	private HashMap<UUID,long[]> timeInWater;
	private int damageAmount;
	private int initialDelay;
	private int consecutiveDelay;
	private boolean multiverseEnabled;
	
	public void onDisable()
	{
		log.info("WaterDamage plugin disabled");
		this.saveConfig();
	}
	
	public void onEnable()
	{
		this.getServer().getPluginManager().registerEvents(this,this);
		effectedWorlds = new HashMap<UUID,World>();
		timeInWater = new HashMap<UUID, long[]>();
		getCommand("wdadd").setExecutor(new WorldAddCommand(this));
		getCommand("wdremove").setExecutor(new WorldRemoveCommand(this));
		getCommand("wdlist").setExecutor(new WorldListCommand(this));
		getCommand("wdsetdamage").setExecutor(new SetDamageAmountCommand(this));
		getCommand("wdsetinitdelay").setExecutor(new SetInitialDelayCommand(this));
		getCommand("wdsetconsecdelay").setExecutor(new SetConsecDelayCommand(this));
		getCommand("wdgetdamageinfo").setExecutor(new GetDamageInfoCommand(this));
		for(String worldName : getConfig().getStringList("worlds"))
		{
			addWorld(worldName);
		}
		
		multiverseEnabled = this.getServer().getPluginManager().isPluginEnabled("Multiverse-Core");
		damageAmount = getConfig().getInt("damage.damageAmount", 2);
		initialDelay = getConfig().getInt("damage.initialDelay", 3 * 1000);
		consecutiveDelay = getConfig().getInt("damage.consecutiveDelay", 1 * 1000);
		
		log.info("" + this.getServer().getPluginManager().isPluginEnabled("Multiverse-Core"));
	}
	//GETTERS AND SETTERS
	//Fairly self explanatory (probably?)
	public void setInitialDelay(int initDelay)
	{
		initialDelay = initDelay;
		getConfig().set("damage.initialDelay", initialDelay);
	}
	
	public void setConsecutiveDelay(int consecDelay)
	{
		consecutiveDelay = consecDelay;
		getConfig().set("damage.consecutiveDelay", consecutiveDelay);
	}
	
	public void setDamageAmount(int damage)
	{
		damageAmount = damage;
		getConfig().set("damage.damageAmount", damageAmount);
	}
	
	public int getInitialDelay()
	{
		return initialDelay;
	}
	
	public int getConsecutiveDelay()
	{
		return consecutiveDelay;
	}
	
	public int getDamageAmount()
	{
		return damageAmount;
	}
	/**
	 * Lists the worlds effected in the form of a string
	 * @return a string containing the names of the effected worlds
	 */
	public String listEffectedWorlds()
	{
		MultiverseCore multiverse = (MultiverseCore) this.getServer().getPluginManager().getPlugin("Multiverse-Core");
		//Technically I should probably put in something about multiverse not being enabled. But... Eh...
		String out = "The following worlds have water damage enabled:\n";
		if(multiverse != null)
		{
			MVWorldManager worldManager = multiverse.getMVWorldManager();
			Set<Entry<UUID, World>> set = effectedWorlds.entrySet();
			for(Entry<UUID,World> entry : set)
			{
				out += worldManager.getMVWorld(entry.getValue()).getName() + "\n";
			}
		}
		return out;
	}
	
	
	/**
	 * Adds the world to the list of effected worlds by its name
	 * @param worldName - name of the world to be added
	 * @return true if adding the world was successful, false otherwise
	 */
	public boolean addWorld(String worldName)
	{
		boolean successfulAdd = false;
		//First get Multiverse and check if it's active
		MultiverseCore multiverse = (MultiverseCore)this.getServer().getPluginManager().getPlugin("Multiverse-Core");
		if(multiverse != null)
		{
			//Get the world from Multiverse by it's name and check if it exists.
			//Note: If this isn't done multiple copies of the same world could be in the list.
			//Don't allow this, save space.
			MVWorld multiWorld =  (MVWorld) multiverse.getMVWorldManager().getMVWorld(worldName);
			if(multiWorld != null)
			{
				//Convert the world to a CraftbukkitWorld and add it to effectedWorlds
				World toAdd = multiWorld.getCBWorld();
				if(effectedWorlds.get(toAdd.getUID()) == null)
					effectedWorlds.put(toAdd.getUID(), toAdd);
				boolean addToConfig = true;
				//Check the config file to see if the world is already in there
				List<String> worldConfigList = getConfig().getStringList("worlds");
				addToConfig = !worldConfigList.contains(worldName);


				//If it isn't don't add it to the config file.
				if(addToConfig)
				{
					worldConfigList.add(worldName);
					getConfig().set("worlds", worldConfigList);
				}
				successfulAdd = true;
			}
		}
		return successfulAdd;
	}
	/**
	 * Removes world from the list of effected worlds by the world's name.
	 * @param worldName - name of the world to be removed
	 * @return true if world remove was successful, false otherwise
	 */
	public boolean removeWorld(String worldName)
	{
		boolean successfulRemove = false;
		//First get the plugin and check if it's active.
		MultiverseCore multiverse = (MultiverseCore)this.getServer().getPluginManager().getPlugin("Multiverse-Core");
		if(multiverse != null)
		{
			//Get the world from Multiverse by it's name and check if it exists
			MVWorld multiWorld = (MVWorld) multiverse.getMVWorldManager().getMVWorld(worldName);
			if(multiWorld != null)
			{
				//Convert the world to a CraftBukkitWorld as they are stored
				World toRemove = multiWorld.getCBWorld();
				//Remove the world from the effectedWorlds Hashmap and set the successfulRemove variable
				//(which returns is set to true if it is successfully removed and false otherwise)
				successfulRemove = effectedWorlds.remove(toRemove.getUID()) == null ? false : true;
				//Remove the world to the list of worlds in the config file
				//and update the config file.
				List<String> worldConfigList = getConfig().getStringList("worlds");
				if(worldConfigList.remove(worldName))
					getConfig().set("worlds", worldConfigList);
				
			}
		}
		return successfulRemove;
	}
	
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		
		Player player = event.getPlayer();
		World playerWorld = player.getWorld();
		//Check if the world the player is in is in the list of worlds
		if(effectedWorlds.get(playerWorld.getUID()) != null || !multiverseEnabled)
		{
			//Figure out the ID of the block the player is in and whether or not they should take damage.
			Location playerLoc = player.getLocation();
			int materialID = playerWorld.getBlockAt(playerLoc).getType().getId();
			//If they are in flowing or stationary water and they aren't in a boat keep going
			if((materialID == Material.WATER.getId() || materialID == Material.STATIONARY_WATER.getId()) && player.getVehicle() == null)
			{
				//Fancy shennanigans
				//First off get the player from the list of players who have 
				//been in the water
				long[] temp = timeInWater.get(player.getUniqueId());
				if(temp == null)
				{
					//If they haven't add them with the time they entered the 
					//water and the time for initial delay (stored in milliseconds)
					timeInWater.put(player.getUniqueId(), new long[]{System.currentTimeMillis(),(long)initialDelay});
				}
				else if(System.currentTimeMillis() - temp[0] >= temp[1])
				{
					//If they have been in the water check if the first part of the array of longs
					//(the time since they entered the water/the time since they last took damage) against
					//the second (the amount of time that should pass until they take damage)
					player.damage(damageAmount);
					//Update the time they took damage, and put consecutive damage in.
					timeInWater.put(player.getUniqueId(), new long[]{System.currentTimeMillis(), (long)consecutiveDelay});
				}
			}
			else if(timeInWater.get(player.getUniqueId()) != null)
			{
				//If the player is still in the list, put null in its place
				//So they stop taking damage, I could remove them from the list.
				//But for my purposes I *think* this is faster and the effect is the same.
				timeInWater.put(player.getUniqueId(), null);
			}
		}
	}
	
}
