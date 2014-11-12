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
		if ((chunkX == n) && (chunkY == i1))
			return MinecraftUtil.isValidBiome(chunkX * 16 + 8, chunkY * 16 + 8, structureSize, validBiomes);
		
		return false;
	}
}
