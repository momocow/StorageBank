package me.momocow.storagebank;

import java.io.File;

import me.momocow.general.util.LogHelper;
import me.momocow.general.util.MailBox;
import me.momocow.storagebank.config.Config;
import me.momocow.storagebank.init.ModBlocks;
import me.momocow.storagebank.init.ModEntities;
import me.momocow.storagebank.init.ModEvents;
import me.momocow.storagebank.init.ModItems;
import me.momocow.storagebank.init.ModRecipes;
import me.momocow.storagebank.proxy.CommonProxy;
import me.momocow.storagebank.reference.Reference;
import me.momocow.storagebank.server.BankingController;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class StorageBank 
{
	//mod instance
	@Mod.Instance(Reference.MOD_ID)
    public static StorageBank instance;
	
	//proxy for client/server event
	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
	public static CommonProxy proxy;
	
	public static Config config;	//mod config
	public static BankingController controller;	//Bank instance to control the interaction with StorageBank
    public static MailBox mailbox;
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) throws Exception
    {
    	LogHelper.info("Stage: Pre-Init");
    	
    	if(e.getSide() == Side.SERVER) mailbox = new MailBox(new File(e.getModConfigurationDirectory().getPath() + File.separator + ".." + File.separator + "mods"));
    
    	config = new Config(e.getModConfigurationDirectory());
    	config.read();
    	
    	ModItems.preinit();
		ModBlocks.preinit();
		ModEntities.preInit();
		
		proxy.createController();
    	proxy.registerRender();
    	proxy.registerChannel();
    }
        
    @EventHandler
    public void init(FMLInitializationEvent e) throws Exception
    {
    	LogHelper.info("Stage: Init");
    	
    	ModBlocks.init();
    	ModRecipes.init();
    	ModEvents.init();
    	
    	proxy.registerGuiHandler();
    }
        
    @EventHandler
    public void postInit(FMLPostInitializationEvent e) 
    {
    	LogHelper.info("Stage: Post-Init");
    	
    	config.save();
    }
}
