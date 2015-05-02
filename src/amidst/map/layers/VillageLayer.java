package amidst.map.layers;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import amidst.Options;
import amidst.map.Fragment;
import amidst.map.IconLayer;
import amidst.map.MapObjectVillage;
import amidst.minecraft.Biome;
import amidst.minecraft.MinecraftUtil;

public class VillageLayer extends IconLayer {
	public static List<Biome> validBiomes = Arrays.asList(new Biome[] { Biome.plains, Biome.desert, Biome.savanna});
	private Random random = new Random();
	
	public VillageLayer() {
	}
	
	@Override
	public boolean isVisible() {
		return Options.instance.showVillages.get();		
	}
	
	@Override
	public void generateMapObjects(Fragment frag) {
		int size = Fragment.SIZE >> 4;
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int chunkX = x + frag.getChunkX();
				int chunkY = y + frag.getChunkY();
				if (checkChunk(chunkX, chunkY)) {
					frag.addObject(new MapObjectVillage(x << 4, y << 4).setParent(this));
				}
			}
		}
	}
	 
    /**
     * puts the World Random seed to a specific state dependant on the inputs
     */
    public void setRandomSeed(int a, int b, int salt)
    {
        long positionSeed = (long)a * 341873128712L + (long)b * 132897987541L + Options.instance.seed + (long)salt;
        random.setSeed(positionSeed);
    }
	

	public boolean checkChunk(int chunkX, int chunkY) {
		byte maxDistanceBetweenScatteredFeatures = 32;
		byte minDistanceBetweenScatteredFeatures = 8;
		int structureSize = 0;
		int structureMagicNumber = 10387312; // 10387312 is the magic number for villages	
		
		
		int k = chunkX;
		int m = chunkY;
		if (chunkX < 0) chunkX -= maxDistanceBetweenScatteredFeatures - 1;
		if (chunkY < 0) chunkY -= maxDistanceBetweenScatteredFeatures - 1;
		
		int n = chunkX / maxDistanceBetweenScatteredFeatures;
		int i1 = chunkY / maxDistanceBetweenScatteredFeatures;
		
		setRandomSeed(n, i1, structureMagicNumber); 	
		
		n *= maxDistanceBetweenScatteredFeatures;
		i1 *= maxDistanceBetweenScatteredFeatures;
		n += random.nextInt(maxDistanceBetweenScatteredFeatures - minDistanceBetweenScatteredFeatures);
		i1 += random.nextInt(maxDistanceBetweenScatteredFeatures - minDistanceBetweenScatteredFeatures);
		chunkX = k;
		chunkY = m;
		if ((chunkX == n) && (chunkY == i1)) {
			// This is a potential feature biome			
			
			int chunkCenterBlockX = chunkX * 16 + 8;
			int chunkCenterBlockY = chunkY * 16 + 8;
			
			boolean canSpawnStructureAtCoords = MinecraftUtil.isValidBiome(chunkCenterBlockX, chunkCenterBlockY, structureSize, validBiomes);
			
			if (canSpawnStructureAtCoords) {
				// Villages start will a well, size 6 x 6, extending to the right and down from 
				// the village spawn coordinates. The well starts the village bounding box at 6 x 6.
				//
				// My hypothesis was as follows:
				//   "If a river or non-valid biome cuts through this area then none of the paths extending from
				//   the well will generate (as I think they each retest the bounding box against valid biomes as they 
				//   extend it). If none of the paths generate then I think the Village fails as a certain number
				//   of buildings are required - it even looks like more than 2 roads may be required."
				//
				// Village at [-5008, -8000] and [-6352, -8144] on seed -1364077613 disprove the hypothesis, yet 
				// strangely it still appears to be a fantastic improvement to village accuracy - eliminating 18% of 
				// all villages from the 20km test area with those two being the only false negative I've found (having 
				// checked half of the eliminated villages). So I will keep this code in AmidstExporter, but leave 
				// it up to other maintainers to decide if it goes in their versions.
				//
				// The village paths must be only biome-testing their own bounding box, rather than the villages, so 
				// beats me why this works so well, just a strong tendency of villages near boundaries to fail I guess.
				int wellSize = 6;		
				int x1 = chunkX * 16 + 2; // For some reason MapGenVillage.Start.Start() adds only 2 to the multiplied coord
				int y1 = chunkY * 16 + 2;
				int x2 = x1 + wellSize - 1; 
				int y2 = y1 + wellSize - 1;
				int wellX = (x1 + x2) / 2;
				int wellY = (y1 + y2) / 2;
				// It looked like there was an arbitraryConstant of 4 in the Minecraft source that's added to bounding 
				// box sizes to get the "structureSize" for areBiomesViable() (e.g. see func_176069_e()), but using 4 
				// eliminates several villages confirmed to exist. The source is hard to read and I've obviously missed a 
				// lot that's going on, so trying a different tact... 
				// Setting arbitraryConstant to 1 means a wellStructureSize of 3 -> the area 
				// passed to isValidBiome() will exactly match the visual footprint of the well. Setting arbitraryConstant 
				// to 2 will mean a wellStructureSize of 4 which corresponds to a 1-block lip around the well also being
				// checked for bad biomes (at 1/4th the block resolution, too). 
				// Testing all villages within 20km of the seed -1364077613, I see that the difference between arbitraryConstant 
				// of 1 vs 2 is that a value of 2 correctly eliminates a further 8 villages and none of those 8 villages exist 
				// in the game, however values above 2 will eliminate more villages that do exist (e.g. [2672, -5424] and 
				// [3376, -3504]).
				// So in lieu of a larger data set, lets set arbitraryConstant to 2 ;)
				int arbitraryConstant = 0;
				int wellStructureSize = (x2 - x1) / 2 + arbitraryConstant;
							
				boolean canSpawnWellAtCoords = MinecraftUtil.isValidBiome(wellX, wellY, wellStructureSize, validBiomes);
				
				// Checking that the well is able to build eliminates most of the false positives, however
				// a few remain. Here are some villages unable to build sufficiently beyond the
				// well (i.e. I have confirmed they do not exist in the game), which are not eliminated by
				// canSpawnWellAtCoords. Found during testing on seed -1364077613:
				//  *  2912,  2080
				//  * -5888,  4784
				//  * -7952, -9424
				//  *  -272, -7872
				return canSpawnWellAtCoords;
			}
		}
	
		return false;
	}
}
