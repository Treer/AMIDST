package amidst.map.layers;

import amidst.Util;
import amidst.logging.Log;
import amidst.map.Fragment;
import amidst.minecraft.Biome;

public class OceanMaskLayer extends BiomeLayer {

	public static int oceanColor = Util.makeColor(0, 0, 0);
	public static int landColor = Util.makeColor(255, 255, 255);
	public boolean visible;

	static boolean[] waterBiome = new boolean[Biome.biomes.length];
	
	public OceanMaskLayer() {
		super();
		visible = false;
		
		// Initialize the waterBiome filter array
		for (int i = 0; i < waterBiome.length; i++) waterBiome[i] = false;
		includeOceans(true);
		includeRivers(false);
	}

	private void includeOceans(boolean include) {
		waterBiome[Biome.ocean.index]        = include;
		waterBiome[Biome.oceanM.index]       = include;
		waterBiome[Biome.frozenOcean.index]  = include;
		waterBiome[Biome.frozenOceanM.index] = include;
		waterBiome[Biome.deepOcean.index]    = include;
		waterBiome[Biome.deepOceanM.index]   = include;
	}
	
	private void includeRivers(boolean include) {
		waterBiome[Biome.river.index]        = include;
		waterBiome[Biome.riverM.index]       = include;
		waterBiome[Biome.frozenRiver.index]  = include;
		waterBiome[Biome.frozenRiverM.index] = include;
	}
		
	@Override
	public boolean isVisible() {
		return visible;
	}	
	
	@Override
	public float getAlpha() {
		return 1.0f;
	}	
	
	@Override
	public void drawToCache(Fragment fragment) {
		int[] dataCache = Fragment.getIntArray();

		for (int i = 0; i < size*size; i++) {
			dataCache[i] = waterBiome[fragment.biomeData[i]] ? oceanColor : landColor;
		}
		
		fragment.setImageData(layerId, dataCache);
	}	
}
