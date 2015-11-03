package amidst.map.layers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import amidst.Options;
import amidst.Util;
import amidst.map.EndIsland;
import amidst.map.Fragment;
import amidst.map.ImageLayer;
import amidst.resources.ResourceLoader;

public class EndIslandsLayer extends ImageLayer {
	private static int size = Fragment.SIZE >> 2; // Shift right by 2 because Layer size is 4 blocks per pixel
	
	protected BufferedImage textures;
	
	
	public EndIslandsLayer() {
		super(size);
				
		textures = ResourceLoader.getImage("endtextures.png");
	}
    
	@Override
	public boolean isVisible() {
		return Options.instance.showEndChunks.get();
	}
	
	@Override
	public void drawToCache(Fragment fragment) {
		int[] dataCache = Fragment.getIntArray();
		
		List<EndIsland> islands = fragment.getEndIslands();
		
		int texX;
		int texY;
		int texSize = textures.getWidth();
				
		for (int y = 0; y < size; y++) {
			texY = y % texSize;
			for (int x = 0; x < size; x++) {
				
				int blockX = (fragment.getChunkX() << 4) + (x << 2); // Shift left by 2 because Layer size is 4 blocks per pixel
				int blockY = (fragment.getChunkY() << 4) + (y << 2); // Shift left by 2 because Layer size is 4 blocks per pixel
				
				float maxInfluence = -100.0f;
				for(EndIsland island: islands) {
					float influence = island.influenceAt(blockX, blockY);
					if (influence > maxInfluence) maxInfluence = influence;
				}		

				texX = x % texSize;
				int pixY = texY;

				if (maxInfluence <= -100) {
					pixY += texSize + texSize;
				} else if (maxInfluence < 0) { // ToDo: tune this value. (adjusts where the islands end and the rocky shores start)					
					pixY += texSize;
				}
				dataCache[y * size + x] = textures.getRGB(texX,  pixY);
			}
		}
		
		fragment.setImageData(layerId, dataCache);
	}
	
}
