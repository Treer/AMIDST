package amidst.json;

import java.util.List;


public class InstallInformation {
	/*
	public String name;
	public String lastVersionId = "latest";
	public String gameDir;
	public String javaDir;
	public String javaArgs;
	public String playerUUID;
	public Resolution resolution;
	public String[] allowedReleaseTypes = new String[] { "release" };
	*/
	
	/**
	 * Some Minecraft installations using the legacy launcher have a profile
	 * with the key "(Default)" and no properties in the actual profile object.
	 * 
	 * The JSON looks like this:
	 * "(Default)": {},
	 * 
	 * This profile has the name null. Also, it cannot be deleted from the
	 * launcher. I guess this is a bug in the minecraft launcher. However, the
	 * minecraft launcher displays it with an empty string as name and it uses
	 * the latest stable release for it, so we do the same.
	 */
	private volatile String name = "";
	private volatile String lastVersionId = "latest";
	private volatile String gameDir;
	private volatile List<ReleaseType> allowedReleaseTypes = ProfileType.LATEST_RELEASE.getAllowedReleaseTypes().get();
	
	private volatile ProfileType type = ProfileType.LEGACY;

	//@GsonConstructor
	public InstallInformation() {
	}

	public String getName() {
		return type.getDefaultName().orElse(name);
	}

	public String getLastVersionId() {
		return lastVersionId;
	}

	public String getGameDir() {
		return gameDir;
	}

	public List<ReleaseType> getAllowedReleaseTypes() {
		return type.getAllowedReleaseTypes().orElse(allowedReleaseTypes);
}	
}