package amidst.map.layers;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import amidst.Options;
import amidst.map.Fragment;
import amidst.map.IconLayer;
import amidst.map.MapObjectOceanMonument;
import amidst.minecraft.Biome;
import amidst.minecraft.MinecraftUtil;
import amidst.preferences.BooleanPrefModel;
import amidst.version.VersionInfo;

public class OceanMonumentLayer extends IconLayer {
	
	/**
	 * Set this to true if you want to show OceanMonument locations in 
	 * maps running on older versions of MineCraft (pre 1.8)
	 * (Ocean Monument icons will be disabled in the Layers menu if this is false)
	 * 
	 * I have this set to true because I instruct people to export their 
	 * map information using the MineCraft version that first created the map,
	 * even though it might now be running in MineCraft 1.8+ which will have 
	 * retroactively added Ocean Monuments.
	 * 
	 * Note: Ocean Monuments are not retroactively added anywhere "that 
	 * has been seen by a player by more than a couple of minutes" 
	 * https://www.reddit.com/r/Minecraft/comments/28sbtm/a_minecraft_experiment_just_how_does_the_new_18/cidzxpi?context=1
	 */
	public static final boolean cAllVersionsCanShowOceanMonuments = true;
	private boolean useFixedAlogirithm = true;	
	
	public static List<Biome> validBiomes = Arrays.asList(
		new Biome[] { 
			Biome.deepOcean, 			
			Biome.deepOceanM, // Not sure if the extended biomes count
		}
	);
	public static List<Biome> validSurroundingBiomes = Arrays.asList(
			new Biome[] { 
				Biome.ocean, 
				Biome.deepOcean, 
				Biome.frozenOcean,
				Biome.river,
				Biome.frozenRiver,
				// Not sure if the extended biomes count. I think they do because Minecraft has
				// a MutatedBiome class (or ModifiedBiome - I don't know what it's called in 
				// Mojang's code) that defers equality checks to its base biome, and biome list
				// checks in Minecraft are done with List<>.contains(), which will use that
				// base-biome equality check. But this stuff has been refactored a lot so it's
				// hard to know what the story is. 
				// Right now it doesn't matter because these mutated biomes are never created 
				// in the game.
				Biome.oceanM, 
				Biome.deepOceanM, 
				Biome.frozenOceanM,
				Biome.riverM,
				Biome.frozenRiverM,
			}
		);
	
	private Random random = new Random();
	
	public OceanMonumentLayer() {
		
		if (MinecraftUtil.getVersion().isAtLeast(VersionInfo.V15w46a)) {			
			// Use the current ocean monuments algorithm
			
			// Bug https://bugs.mojang.com/browse/MC-65214 was fixed in 15w46a,
			// and the fix changes where Ocean Monuments can appear.
			// This class implements the LocationChecker for after the fix was 
			// implemented.
			//  
			// The fix is described here: https://bugs.mojang.com/browse/MC-65214?focusedCommentId=228462&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-228462
			// That description is quoted below:
			// 
			// ----
			//    "The issue lies in that the server is calculating DEEP_BIOME based on 
			//    the 1.8 World Generator and NOT the worlds ACTUAL biome from previous 
			//    versions.
			//
			//    I had a report of a biome in OCEAN (Not Deep), and when I generated a 
			//    1.8 world with exact same seed, sure enough that location was 
			//    DEEP_OCEAN in that seed.
			//
			//    The Monument Structure check uses 2 different Biome lookup methods, and
			//    the one that does DEEP_BIOME check uses the World Gen calculations, and
			//    then the followup check for "Surrounding biomes" uses the actual worlds 
			//    data.
			//
			//    I temp fixed for my server with the following change:
			//        - if (this.c.getWorldChunkManager().getBiome(new BlockPosition(k * 16 + 8, 64, l * 16 + 8), (BiomeBase) null) != BiomeBase.DEEP_OCEAN) {
			//        + if (!this.c.getWorldChunkManager().a(k * 16 + 8, l * 16 + 8, 16, Arrays.asList(BiomeBase.DEEP_OCEAN))) {
			//        
			//    This issue has more flaws than this report states as it also causes 
			//    monuments to spawn in the middle of Rivers, Frozen Rivers and Frozen 
			//    Oceans, which is quite odd to stroll through the mountains and find 
			//    a monument."
			// ----
			// 
			// Examples:
			// In seed -3189899870270394863, the monuments at (808, 1224) and (-856, 184)
			// are eliminated by this fix.			
			useFixedAlogirithm = true;
		} else {
			useFixedAlogirithm = false;
		}
		
		
	}
	
	/**
	 * Adds optional polish to user interface.
	 * Invoke this once, after the Minecraft profile or version has been selected.
	 * 
	 * Selects and enables/disables the layer option based on whether Ocean Monuments
	 * are supported.
	 * @param oceanMonumentPreference
	 */
	public static void InitializeUIOptions(BooleanPrefModel oceanMonumentPrefModel) {

		if (!MinecraftVersionSupportsOceanMoments()) {
			// Current Minecraft doesn't support Ocean Monuments

			// Deselect the Ocean Monuments Layers option for this session.
			oceanMonumentPrefModel.setSelected(false);
			
			if (!cAllVersionsCanShowOceanMonuments) {
				// Disable the option, to indicate to the user that 
				// there are no Ocean Monuments to show.
				oceanMonumentPrefModel.setEnabled(false);
			}
		}
	}
	
	/** @return true if Minecraft is v1.8 or greater */
	public static boolean MinecraftVersionSupportsOceanMoments() {
		return MinecraftUtil.getVersion().isAtLeast(VersionInfo.V1_8);
	}
	
	@Override
	public boolean isVisible() {
		return Options.instance.showOceanMonuments.get();		
	}
	
	@Override
	public void generateMapObjects(Fragment frag) {
		
		if (cAllVersionsCanShowOceanMonuments || MinecraftVersionSupportsOceanMoments()) {
		
			int size = Fragment.SIZE >> 4;
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					int chunkX = x + frag.getChunkX();
					int chunkY = y + frag.getChunkY();
					if (checkChunk(chunkX, chunkY)) {
						frag.addObject(new MapObjectOceanMonument((x << 4) + 8, (y << 4) + 8).setParent(this));
					}
				}
			}
		}
	}
	 
    /** puts the World Random seed to a specific state dependent on the inputs */
    public void setRandomSeed(int a, int b, int structure_magic_number)
    {
        long positionSeed = (long)a * 341873128712L + (long)b * 132897987541L + Options.instance.seed + (long)structure_magic_number;
        random.setSeed(positionSeed);
    }
	

	public boolean checkChunk(int chunkX, int chunkY) {
		
		boolean result = false;
		
		byte maxDistanceBetweenScatteredFeatures = 32;
		byte minDistanceBetweenScatteredFeatures = 5;
		int structureSize = 29;
		int structureCenterSize = 16;
		int structureMagicNumber = 10387313; // 10387313 is the magic salt for ocean monuments
		
		int chunkXadj = chunkX;
		int chunkYadj = chunkY;
		if (chunkXadj < 0) chunkXadj -= maxDistanceBetweenScatteredFeatures - 1;
		if (chunkYadj < 0) chunkYadj -= maxDistanceBetweenScatteredFeatures - 1;
		
		int a = chunkXadj / maxDistanceBetweenScatteredFeatures;
		int b = chunkYadj / maxDistanceBetweenScatteredFeatures;
		
		setRandomSeed(a, b, structureMagicNumber);		

		int distanceRange = maxDistanceBetweenScatteredFeatures - minDistanceBetweenScatteredFeatures;
		a *= maxDistanceBetweenScatteredFeatures;
		b *= maxDistanceBetweenScatteredFeatures;
		a += (random.nextInt(distanceRange) + random.nextInt(distanceRange)) / 2;
		b += (random.nextInt(distanceRange) + random.nextInt(distanceRange)) / 2;
		
		if ((chunkX == a) && (chunkY == b)) {

			if (useFixedAlogirithm) {
				result = 
					MinecraftUtil.isValidBiome(chunkX * 16 + 8, chunkY * 16 + 8, structureCenterSize, validBiomes) &&
					MinecraftUtil.isValidBiome(chunkX * 16 + 8, chunkY * 16 + 8, structureSize, validSurroundingBiomes);
			} else {
				// Note that getBiomeAt() is full-resolution biome data, while isValidBiome() is calculated using
				// quarter-resolution biome data. This is identical to how Minecraft calculates monuments.
				// I think getBiomeAt() is also using the world generatOR, whereas isValidBiome() is
				// using the biomes of the generatED world - these can differ if the world was generated in an
				// earlier version to what the client is currently running.
				result = 
					MinecraftUtil.getBiomeAt(  chunkX * 16 + 8, chunkY * 16 + 8) == Biome.deepOcean &&
					MinecraftUtil.isValidBiome(chunkX * 16 + 8, chunkY * 16 + 8, structureSize, validSurroundingBiomes);
			}
		}
		return result;
	}
}
