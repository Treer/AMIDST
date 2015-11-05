package amidst.map.layers;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import amidst.Options;
import amidst.map.EndIsland;
import amidst.map.Fragment;
import amidst.map.IconLayer;
import amidst.map.MapObjectEndCity;
import amidst.map.MapObjectOceanMonument;
import amidst.minecraft.Biome;
import amidst.minecraft.MinecraftUtil;
import amidst.preferences.BooleanPrefModel;
import amidst.version.VersionInfo;

public class EndCityLayer extends IconLayer {
	
	/**
	 * Set this to true if you want to show End City locations in 
	 * maps running on older versions of MineCraft (pre 1.9)
	 * (End City icons will be disabled in the Layers menu if this is false)
	 * 
	 * I have this set to true because I instruct people to export their 
	 * map information using the MineCraft version that first created the map,
	 * even though it might now be running in MineCraft 1.9+ which will have 
	 * retroactively added End Cities.
	 */
	public static final boolean cAllVersionsCanShowEndCities = true;
	
	private Random random = new Random();
	
	enum ChunkProbability {
		None,
		Possible,
		Likely				
	}
			
	public EndCityLayer() {
	}
	
	/**
	 * Adds optional polish to user interface.
	 * Invoke this once, after the Minecraft profile or version has been selected.
	 * 
	 * Selects and enables/disables the layer option based on whether Ocean Monuments
	 * are supported.
	 * @param oceanMonumentPreference
	 */
	public static void InitializeUIOptions(BooleanPrefModel endCitiesPrefModel) {

		if (!MinecraftVersionSupportsEndCities()) {
			// Current Minecraft doesn't support End Cities

			// Deselect the Ocean Monuments Layers option for this session.
			endCitiesPrefModel.setSelected(false);
			
			if (!cAllVersionsCanShowEndCities) {
				// Disable the option, to indicate to the user that 
				// there are no Ocean Monuments to show.
				endCitiesPrefModel.setEnabled(false);
			}
		}
	}
	
	/** @return true if Minecraft is v1.9 or greater */
	public static boolean MinecraftVersionSupportsEndCities() {
		return MinecraftUtil.getVersion().isAtLeast(VersionInfo.V15w31c);
	}
	
	@Override
	public boolean isVisible() {
		return Options.instance.showEndCities.get();		
	}
	
	@Override
	public void generateMapObjects(Fragment frag) {
		
		if (cAllVersionsCanShowEndCities || MinecraftVersionSupportsEndCities()) {
		
			int size = Fragment.SIZE >> 4;
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					int chunkX = x + frag.getChunkX();
					int chunkY = y + frag.getChunkY();
					
					ChunkProbability cityProbability = checkChunk(frag, chunkX, chunkY);
					if (cityProbability != ChunkProbability.None) {
						frag.addObject(
							new MapObjectEndCity(
								(x << 4) + 8, 
								(y << 4) + 8,
								cityProbability == ChunkProbability.Likely
							).setParent(this)
						);
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
	

	public ChunkProbability checkChunk(Fragment frag, int chunkX, int chunkY) {
		
		ChunkProbability result = ChunkProbability.None;
		
		byte maxDistanceBetweenScatteredFeatures = 20;
		byte minDistanceBetweenScatteredFeatures = 11;
		int structureMagicNumber = 10387313; // 10387313 is the magic salt for ocean monuments and end cities
		
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
			if ((chunkX * chunkX + chunkY * chunkY) > 4096) {
				result = isIslandCore(frag, chunkX, chunkY);
			}
		}
		return result;
	}
	
	protected ChunkProbability isIslandCore(Fragment frag, int chunkX, int chunkZ) {
		
		// requiredInfluence is a value between 0 and 80 that I'm finding by
		// trial and error. If the island influence is 0 or higher then an 
		// End City can spawn, but they don't spawn unless all of the ground
		// under then is at a higher y value than 60. Since we don't want to generate
		// the land to discover the high areas, I'm using the island influence
		// as proxy for how high the land might be.
		float requiredInfluence = 60;
		
		List<EndIsland> islands = frag.getEndIslands();
		for(EndIsland island: islands) {
			
			float influence = island.influenceAtChunk(chunkX, chunkZ);
			
			if (influence >= 0.0) {
				// The influence is greater or equal to zero so Minecraft WILL attempt to
				// build an End City, however if the ground at any of the corners of the end 
				// city is below height 60 then the End City will be aborted.
				
				// TODO: Use Amidst's ability to hook into the minecraft .jar file to get Minecraft
				// to build just this single chunk so we can tell for certain whether an End City
				// builds here. (If that's feasible)
				
				// In the meantime, fall back on the requiredInfluence heuristic				
				if (influence >= requiredInfluence) {
					return ChunkProbability.Likely;				
				} else {
					return ChunkProbability.Possible;									
				}
			}
		}		
		return ChunkProbability.None;
	}
}
