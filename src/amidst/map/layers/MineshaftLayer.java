package amidst.map.layers;

import java.util.Random;

import amidst.Options;
import amidst.map.Fragment;
import amidst.map.IconLayer;
import amidst.map.MapObjectMineshaft;
import amidst.minecraft.MinecraftUtil;
import amidst.version.VersionInfo;

public class MineshaftLayer extends IconLayer {
	private final static double chancePerChunk_v1_7 = 0.004D;
	private final static double chancePerChunk_v1_6 = 0.01D;
	
	private Random  random = new Random();
	private double  chancePerChunk;
	private boolean useOriginalAlogirithm = false;

	public MineshaftLayer() {
		
		if (MinecraftUtil.getVersion().isAtLeast(VersionInfo.V1_4_2)) {
			// Use the current mineshaft algorithm
				
			if (MinecraftUtil.getVersion().isAtLeast(VersionInfo.V1_7_2)) {
				// Mineshafts became less common from version 1.7.2 onward 
				chancePerChunk = chancePerChunk_v1_7;			
			} else {
				chancePerChunk = chancePerChunk_v1_6;			
			}
		} else {
			// Use the original mineshaft algorithm (retired in v1.4.2).
			useOriginalAlogirithm = true;
		}
	}

	@Override
	public boolean isVisible() {
		return Options.instance.showMineshafts.get();
	}
	@Override
	public void generateMapObjects(Fragment frag) {
		
		int size = Fragment.SIZE >> 4;
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int chunkX = x + frag.getChunkX();
				int chunkY = y + frag.getChunkY();
				if (checkChunk(chunkX, chunkY)) {
					frag.addObject(new MapObjectMineshaft((x << 4) + 8, (y << 4) + 8).setParent(this));
				}
			}
		}
	}

	public boolean checkChunk(int chunkX, int chunkY) {
        random.setSeed(Options.instance.seed);
        long var7 = random.nextLong();
        long var9 = random.nextLong();

		long var13 = (long)chunkX * var7;
		long var15 = (long)chunkY * var9;
		random.setSeed(var13 ^ var15 ^ Options.instance.seed);
		random.nextInt();

		if (useOriginalAlogirithm) {
			// Empirical testing suggests this version of the algorithm works all the way back to b1.8,  
			// and the Minecraft Wiki says b1.8 was when mineshafts were introduced, but since Amidst 
			// versions only go back as far as VersionInfo.Vbeta_1_8_1 I won't bother to check for 
			// pre-mineshaft versions.
			//
			// I've not decompiled the very early Minecraft versions, and TheMasterCaver points out that "for older 
			// versions it is possible that the second part of this code, nextInt(80) < (max of absolute value 
			// of chunk coordinates), may not have been present, resulting in mineshafts being equally as common 
			// near the origin as they currently are 80 or more chunks away."
			// I've included TheMasterCaver's comment because my empirical testing that Amidst mineshafts do 
			// appear in the game can't tell us whether the very early versions have fewer mineshafts near the origin. 
			return (random.nextInt(100) == 0) && (random.nextInt(80) < Math.max(Math.abs(chunkX), Math.abs(chunkY)));
		} else {
			// As of v1.4.2 Minecraft switched to this version of the algorithm
			return (random.nextDouble() < chancePerChunk) && random.nextInt(80) < Math.max(Math.abs(chunkX), Math.abs(chunkY));
		}
	}
}
