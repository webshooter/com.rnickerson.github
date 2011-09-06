package com.rnickerson.foldersync.core;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Main {
	
	public static boolean debug = true;
	
	public static enum filterType { BLACK, WHITE };

	public static Logger logger = Logger.getLogger(Main.class.getName());
	public static String propsFile;
	public static Properties props;
	public static ArrayList<String> copyToTarget;
	public static ArrayList<String> copyToBackup;
	public static ArrayList<String> filterList;
	public static filterType filtertype;
	
	// FROM PROPERTIES FILE //
	private static String logfile;
	private static String targetfolder;
	private static String backupfolder;
	private static String filter;
	private static String filterlist;
	private static String syncemptyfolders;
	private static String syncbothways;
	private static String warnpercent;
	private static String abortpercent;
	private static String alertlevel;
	private static String alertemail;
	private static String emailhost;
	private static String emailacct;
	private static String emailpass;
	//////////////////////////
	
	// ADJUSTED PROPERTIES VALUES //
	private static ArrayList<String> filterValues;
	private static File targetFolder;
	private static File backupFolder;
	private static boolean syncEmptyFolders;
	private static boolean syncBothWays;
	private static double warnPercent;
	private static double abortPercent;
	////////////////////////////////
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// START CREATE/LOAD PROPERTIES ///////////////////////////////////////
		propsFile = "fs.properties";
		if (args.length > 0) {
			propsFile = args[0];
		}
		if (!(new File(propsFile).exists())) {
			goExit("Invalid properties file specified: " + propsFile);
		}
		props = new Properties();
		try {
			props.load(new FileInputStream(propsFile));
		} catch (Exception e) {
			handleException(e, "Error loading properties file: " + propsFile);
		}
		// END CREATE/LOAD PROPERTIES /////////////////////////////////////////
		
		// START SETUP LOG4J LOG(S) ///////////////////////////////////////////
		Properties log4jProperties = new Properties();
		try {
			log4jProperties.load(Main.class.getClass().getResourceAsStream("/log4j.properties"));
			log4jProperties.setProperty("log4j.appender.file.file", props.getProperty("logfile"));
		} catch (Exception e) {
			handleException(e, "Error loading log4j properties");
		}
		PropertyConfigurator.configure(log4jProperties);
		// END SETUP LOG4J LOG(S) /////////////////////////////////////////////
		
		// START ASSIGN PROPERTIES VALUES /////////////////////////////////////
		logfile = props.getProperty("logfile");
		targetfolder = props.getProperty("targetfolder");
		backupfolder = props.getProperty("backupfolder");
		filter = props.getProperty("filtertype");
		filterlist = props.getProperty("filterlist");
		syncemptyfolders = props.getProperty("syncemptyfolders");
		syncbothways = props.getProperty("syncbothways");
		warnpercent = props.getProperty("warnpercent");
		abortpercent = props.getProperty("abortpercent");
		alertlevel = props.getProperty("alertlevel");
		alertemail = props.getProperty("alertemail");
		emailhost = props.getProperty("emailhost");
		emailacct = props.getProperty("emailacct");
		emailpass = props.getProperty("emailpass");
		////
		filtertype = (filter.equalsIgnoreCase("white"))?filterType.WHITE:filterType.BLACK;
		targetFolder = new File(targetfolder);
		backupFolder = new File(backupfolder);
		syncEmptyFolders = (syncemptyfolders.equalsIgnoreCase("true"))?true:false;
		syncBothWays = (syncbothways.equalsIgnoreCase("true"))?true:false;
		warnPercent = (double)(Integer.valueOf(warnpercent)/100);
		abortPercent = (double)(Integer.valueOf(abortpercent)/100);
		// END ASSIGN PROPERTIES VALUES ///////////////////////////////////////
		
	}
	
	public static String getProp(String propertyKey) {
		if (props.containsKey(propertyKey)) {
			return props.getProperty(propertyKey);
		}
		return null;
	}
	
	public static void handleException(Exception e, String msg) {
		logger.warn("EXCEPTION THROWN:");
		if (msg != null) {
			logger.warn(msg);
		}
		logger.warn(e.toString());
		if (debug) {
			e.printStackTrace();
		}
		goExit(null);
	}
	
	public static void goExit(String msg) {
		if (msg != null) {
			out(msg);
		}
		System.exit(0);
	}
	
	private static void out(String text) {
		System.out.println(text);
	}

}
