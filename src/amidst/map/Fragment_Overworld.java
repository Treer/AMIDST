package amidst.map;

import amidst.minecraft.MinecraftUtil;

/** implements a concrete Fragment class containing Overworld biomeData */
public class Fragment_Overworld extends Fragment {

	public Fragment_Overworld(ImageLayer[] imageLayers, LiveLayer[] liveLayers, IconLayer[] iconLayers) {
		super(imageLayers, liveLayers, iconLayers);
	}

	@Override
	protected void loadBiomeData() {
		
		int[] data = MinecraftUtil.getBiomeData(blockX >> 2, blockY >> 2, BIOME_SIZE, BIOME_SIZE, true);
		for (int i = 0; i < BIOME_SIZE * BIOME_SIZE; i++) {
			biomeData[i] = (short)data[i];
		}
	}

}
