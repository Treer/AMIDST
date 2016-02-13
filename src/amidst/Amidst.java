package amidst;

import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.google.gson.Gson;

import MoF.FinderWindow;
import MoF.Google;
import amidst.gui.version.VersionSelectWindow;
import amidst.logging.FileLogger;
import amidst.logging.Log;
import amidst.minecraft.Minecraft;
import amidst.minecraft.MinecraftUtil;
import amidst.preferences.BiomeColorProfile;
import amidst.resources.ResourceLoader;

public class Amidst {	
	public final static int version_major = 3;
	public final static int version_minor = 7;
	public final static String versionOffset = "";
	public final static int exporter_version_major = 1;
	public final static int exporter_version_minor = 44;
	public final static String exporter_versionOffset = "";
	public static List<Image> icons = new ArrayList<Image>();
	public static final Gson gson = new Gson();
	
	static {
		icons.add(ResourceLoader.getImage("icon16.png"));
		icons.add(ResourceLoader.getImage("icon32.png"));
		icons.add(ResourceLoader.getImage("icon64.png"));
	}
	
	
	public static void main(String args[]) {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable e) {
				Log.crash(e, "Amidst has encounted an uncaught exception on thread: " + thread);
			}
		});
		CmdLineParser parser = new CmdLineParser(Options.instance); 
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			Log.w("There was an issue parsing command line options.");
			e.printStackTrace();
		}
		Util.setMinecraftDirectory();
		Util.setMinecraftLibraries();
		
		if (Options.instance.logPath != null)
			Log.addListener("file", new FileLogger(new File(Options.instance.logPath)));
		
		
		if (!isOSX()) { Util.setLookAndFeel(); }
		Google.startTracking();
		Google.track("Run");
		System.setProperty("sun.java2d.opengl","True");
		System.setProperty("sun.java2d.accthreshold", "0");
		BiomeColorProfile.scan();
		
		if (Options.instance.minecraftJar != null)
		{
			try {
				Util.setProfileDirectory(Options.instance.minecraftPath);
				MinecraftUtil.setBiomeInterface(new Minecraft(new File(Options.instance.minecraftJar)).createInterface());
				new FinderWindow();
			} catch (MalformedURLException e) {
				Log.crash(e, "MalformedURLException on Minecraft load.");
			}
		}
		else
		{
			new VersionSelectWindow();
		}
	}
	
	public static boolean isOSX() {
		String osName = System.getProperty("os.name");
		return osName.contains("OS X");
	}

	/**
	 * Gets the version of AMIDST Exporter, which can increment independently of the 
	 * version of AMIDST it is build on. 
	 */
	public static String exporterVersion() {
		return exporter_version_major + "." + exporter_version_minor + exporter_versionOffset;
	}
	
	public static String version() {
		if (MinecraftUtil.hasInterface())
			return version_major + "." + version_minor + versionOffset + " [recognized Minecraft version: " + MinecraftUtil.getVersion() + "]";
		return version_major + "." + version_minor + versionOffset;
	}
	
}
