package amidst.map.layers;

import java.awt.image.BufferedImage;
import java.util.List;

import amidst.Options;
import amidst.map.EndIsland;
import amidst.map.Fragment;
import amidst.map.Fragment_TheEnd;
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
	
	/** Only draws to Fragment_TheEnd instances */
	@Override
	public void drawToCache(Fragment fragment) {
		
		if (fragment instanceof Fragment_TheEnd) {
			List<EndIsland> islands = ((Fragment_TheEnd)fragment).getEndIslands();
			
			int[] dataCache = Fragment.getIntArray();
				
			float influenceFadeStart   =  0;
			float influenceFadeFinish  = -8; // must be lower than influenceFadeStart, so that islands fades out as influence declines
			float fadeRange = influenceFadeStart - influenceFadeFinish;
			
			int texelX;
			int texelY;
			int textureWidth = textures.getWidth();
			int textureHeight = textures.getHeight() >> 1;
			int textureOffset_rockyShores = textureHeight;
			
			boolean showRockyShores;
			int chunkX, chunkY, blockX, blockY, fragmentChunkX, fragmentChunkY, fragmentBlockX, fragmentBlockY;
			fragmentChunkX = fragment.getChunkX();
			fragmentChunkY = fragment.getChunkY();
			fragmentBlockX = fragment.getBlockX();
			fragmentBlockY = fragment.getBlockY();
	
			for (int y = 0; y < size; y++) {
				
				texelY = y % textureHeight;
				chunkY = fragmentChunkY + (y >> 2); // Shift y right by 2 instead of 4 because Layer size is 4 blocks per pixel
				blockY = fragmentBlockY + (y << 2); // Shift y left  by 2 instead of 4 because Layer size is 4 blocks per pixel
						
				for (int x = 0; x < size; x++) {
	
					texelX = x % textureWidth;
					chunkX = fragmentChunkX + (x >> 2); // Shift x right by 2 instead of 4 because Layer size is 4 blocks per pixel
					blockX = fragmentBlockX + (x << 2); // Shift x left  by 2 instead of 4 because Layer size is 4 blocks per pixel
					
					// Determine if the chunk may contain miniature islands
					// (Unfortunately the "rocky shore" miniature islands are not deterministic
					// from the world seed, like chorus plants they are decorations whose PRNG state
					// depends on the order chunks are created/explored in. This makes me sad :( )
					showRockyShores = (chunkX * chunkX + chunkY * chunkY) > 4096;
					
					// Determine whether this 
					float maxInfluence = -100.0f;
					for(EndIsland island: islands) {
						float influence = island.influenceAt(blockX, blockY);
						if (influence > maxInfluence) maxInfluence = influence;
					}		
	
					int pixel = 0x00000000; // transparent black - shows as void
	
					if (maxInfluence >= influenceFadeStart) {
						// Draw endstone island
						pixel = textures.getRGB(texelX,  texelY);					
					} else {
	
						if (showRockyShores) {
							pixel = textures.getRGB(texelX,  texelY + textureOffset_rockyShores);
						}
	
						if (maxInfluence > influenceFadeFinish) {
							// Fade out the endstone - this is the edge of an island
							int pixelAlpha = pixel >>> 24;
							int fadingIslandAlpha = 255 - (int)(255 * (influenceFadeStart - maxInfluence) / fadeRange);
							
							if (fadingIslandAlpha > pixelAlpha) {
								// favor the island pixel instead of the rocky shores pixel
								// (Should look perfect without needing to blend, because rocky shore is still endstone texture)
								pixel = (textures.getRGB(texelX,  texelY) & 0x00FFFFFF) | (fadingIslandAlpha << 24);
							}
						}
					}
					dataCache[y * size + x] = pixel;
				}
			}
			
			fragment.setImageData(layerId, dataCache);
		}
	}
	
}
