package amidst.map.layers;

import java.util.Random;

import amidst.Options;
import amidst.map.Fragment;
import amidst.map.IconLayer;
import amidst.map.MapObjectMineshaft;
import amidst.minecraft.MinecraftUtil;
import amidst.version.VersionInfo;

public class MineshaftLayer extends IconLayer {
	private Random random = new Random();

	public MineshaftLayer() {
	}

	@Override
	public boolean isVisible() {
		return Options.instance.showMineshafts.get();
	}
	@Override
	public void generateMapObjects(Fragment frag) {
		
		if (MinecraftVersionIsSupported()) {
		
			int size = Fragment.SIZE >> 4;
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					int chunkX = x + frag.getChunkX();
					int chunkY = y + frag.getChunkY();
					if (checkChunk(chunkX, chunkY)) {
						frag.addObject(new MapObjectMineshaft(x << 4, y << 4).setParent(this));
					}
				}
			}
		}
	}

	/** @return true if Minecraft is v1.4.2 or greater (earlier versions place mineshafts differently) */
	public static boolean MinecraftVersionIsSupported() {
		return MinecraftUtil.getVersion().isAtLeast(VersionInfo.V1_4_2);
	}
	
	
	double _e = 0.004D;

	public boolean checkChunk(int chunkX, int chunkY) {
        random.setSeed(Options.instance.seed);
        long var7 = random.nextLong();
        long var9 = random.nextLong();

		long var13 = (long)chunkX * var7;
		long var15 = (long)chunkY * var9;
		random.setSeed(var13 ^ var15 ^ Options.instance.seed);
		random.nextInt();

		return random.nextDouble() < _e && random.nextInt(80) < Math.max(Math.abs(chunkX), Math.abs(chunkY));
	}
}
