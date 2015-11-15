package amidst.map;

import java.util.List;

import amidst.minecraft.Biome;

/** implements a concrete Fragment class containing The End islands and biomeData */
public class Fragment_TheEnd extends Fragment {

	/** Access this via getEndIslands() method */
	private List<EndIsland> endIslands = null;
	
	public Fragment_TheEnd(ImageLayer[] imageLayers, LiveLayer[] liveLayers, IconLayer[] iconLayers) {
		super(imageLayers, liveLayers, iconLayers);
	}	
	
	@Override
	protected void loadBiomeData() {		
		// The map is displaying The End - it's all one biome		
		fillBiomeData((short)Biome.theEnd.index);
		
		// load() must have been called, which means this fragment 
		// might be being recycled: clear the islands
		endIslands = null;
	}
	
	public List<EndIsland> getEndIslands() {
		if (endIslands == null) {
			endIslands = EndIsland.findSurroundingIslands(
				getChunkX(), 
				getChunkY(), 
				SIZE_IN_CHUNKS,
				SIZE_IN_CHUNKS
			);
		}
		return endIslands;
	}
}
