package amidst.map.layers;

import java.util.Random;
import amidst.Options;
import amidst.map.Fragment;
import amidst.map.IconLayer;
import amidst.map.MapObjectNether;

public class NetherFortressLayer extends IconLayer {
	private Random random = new Random();
	/** set true for Overworld fragments, false for Nether fragments. Determines if Fortress
	 * is located at nether coordinates or overworld coordinates. 
	 */
	private static boolean correctCoordsForOverworld = true; // As Amidst only ever displays an Overworld map, we'll set this true.
	
	public NetherFortressLayer() {
	}
	
	@Override
	public boolean isVisible() {
		return Options.instance.showNetherFortresses.get();
	}
	@Override
	public void generateMapObjects(Fragment frag) {
		int size = Fragment.SIZE >> 4;
		
		int chunkXOffset = frag.getChunkX();
		int chunkYOffset = frag.getChunkY();
		
		if (correctCoordsForOverworld) {
			size >>= 3;
			chunkXOffset >>= 3;
			chunkYOffset >>= 3;
		}
		
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int chunkX = x + chunkXOffset;
				int chunkY = y + chunkYOffset;
				if (checkChunk(chunkX, chunkY)) {
					// Found that the center of the junction is +11, +11 from the chunk 
					// coords empirically (using v1.8.4), so I don't know why this is.
					// (tho suspect it'll be because a corner of the junction starts at the 
					// center of the chunk, which is +8, +8)					
					int mapX = (x << 4) + 11;
					int mapY = (y << 4) + 11;
					if (correctCoordsForOverworld) {
						mapX <<= 3;
						mapY <<= 3;
					}					
					frag.addObject(new MapObjectNether(mapX, mapY, !correctCoordsForOverworld).setParent(this));
				}
			}
		}
	}
	 

	public boolean checkChunk(int chunkX, int chunkY) {
		int i = chunkX >> 4;
		int j = chunkY >> 4;

		random.setSeed(i ^ j << 4 ^ Options.instance.seed);
		random.nextInt();

		if (random.nextInt(3) != 0) {
		  return false;
		}
		if (chunkX != (i << 4) + 4 + random.nextInt(8)) {
		  return false;
		}

		return chunkY == (j << 4) + 4 + random.nextInt(8);
	}
}
