package me.momocow.general.util;

import org.apache.logging.log4j.Level;

import me.momocow.storagebank.reference.Reference;
import net.minecraftforge.fml.common.FMLLog;

public class LogHelper
{
	
	private static void createLog(Level logLevel, Object object){
		FMLLog.log(Reference.MOD_NAME,logLevel,String.valueOf(object));
	}
	
	public static void off(Object object) { createLog(Level.OFF,object); }
	
	public static void fatal(Object object) { createLog(Level.FATAL,object); }
	
	public static void error(Object object) { createLog(Level.ERROR,object); }
	
	public static void warn(Object object) { createLog(Level.WARN,object); }
	
	public static void info(Object object) { createLog(Level.INFO,object); }
	
	public static void infoDebugMode(Object object){
//		if(ConfigHandler.debugMode) 
		createLog(Level.INFO,object);
	}
	
	public static void debug(Object object){ 
//		if(ConfigHandler.debugMode) 
		createLog(Level.DEBUG,object);
	}
	
	public static void trace(Object object) { createLog(Level.TRACE,object); }
	
	public static void all(Object object) { createLog(Level.ALL,object); }
	
	
}