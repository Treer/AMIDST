package amidst.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import amidst.Options;
import amidst.minecraft.SimplexNoise;

public class EndIsland {
	
	public int ChunkX, ChunkZ;
	public float ErosionFactor;
	
	public static final double cIslandDensityThreshold = -0.8999999761581421;
	
	private static SimplexNoise _cachedNoiseFunction = null;
	private static long _cachedNoiseFunctionSeed;
	
	/** Returns the noise function using the current seed */
	protected static SimplexNoise NoiseFunction() {
		
		if (_cachedNoiseFunctionSeed != Options.instance.seed || _cachedNoiseFunction == null) {
			
			_cachedNoiseFunctionSeed = Options.instance.seed;
			Random random = new Random(_cachedNoiseFunctionSeed);
			
			Perlin3dOctaves_FakeConstructor(random, 16);
			Perlin3dOctaves_FakeConstructor(random, 16);
			Perlin3dOctaves_FakeConstructor(random, 8);
			Perlin3dOctaves_FakeConstructor(random, 10);
			Perlin3dOctaves_FakeConstructor(random, 16);			
			_cachedNoiseFunction = new SimplexNoise(random);
		}
		return _cachedNoiseFunction;
	}
	
	/** Mimics the side-effects to the random number generator caused by constructing a Perlin3dOctaves instance */
	private static void Perlin3dOctaves_FakeConstructor(Random rand, int octaveCount) {
		
		for(int i = 0; i < octaveCount; i++) {
			Perlin3d_FakeConstructor(rand);		
		}
	}
	
	/** Mimics the side-effects to the random number generator caused by constructing a Perlin3d instance */	
	private static void Perlin3d_FakeConstructor(Random rand) {
		
		rand.nextDouble();
		rand.nextDouble();
		rand.nextDouble();

		for(int i = 0; i < 256; i++) {
			rand.nextInt(256 - i);			
		}		
	}
	
	
	protected EndIsland(int chunkX, int chunkZ, float erosion) {
		this.ChunkX = chunkX;
		this.ChunkZ = chunkZ;
		this.ErosionFactor = erosion;
	}
	
	/** Returns an EndIsland if one has 'grown out' from the chunk, otherwise null */
	protected static EndIsland checkForIsland(int chunkX, int chunkZ, SimplexNoise noiseFunction) {
		
		EndIsland result = null;
		
		if (chunkX == 0 && chunkZ == 0) {
			// The main island grows from the origin, with a hard-coded erosion factor of 8
			result = new EndIsland(chunkX, chunkZ, 8.0f);
			
		} else if (chunkX * chunkX + chunkZ * chunkZ > 4096) {
			// The chunk is in the outer-islands band (1024 blocks from the origin)
						
			if (noiseFunction.noise(chunkX, chunkZ) < cIslandDensityThreshold) {
				// An island (or part of an island) grows out from this chunk,
				// with an erosion factor between 9 and 21 (i.e. they will be 
				// smaller than the main island).
				float erosionFactor = (Math.abs(chunkX) * 3439 + Math.abs(chunkZ) * 147) % 13 + 9;		
				
				result = new EndIsland(chunkX, chunkZ, erosionFactor);
			}
		}
		return result;
	}
	
	/**
	 * Returns a list of all islands that might be touching a chunk-area. 
	 * ("Touching" includes the rocky-island-shore that extends from each island)
	 */
	public static List<EndIsland> findSurroundingIslands(int chunkX, int chunkY, int chunksWide, int chunksDeep) {
	
		List<EndIsland> result = new ArrayList<EndIsland>();
		
		SimplexNoise noiseFunction = NoiseFunction();
		
		// Minecraft checks 12 chunks either side of a chunk when assessing
		// island influence. 
		for(int y = -12; y <= chunksDeep + 12; y++) {
			for(int x = -12; x <= chunksWide + 12; x++) {
			
				EndIsland island = checkForIsland(chunkX + x, chunkY + y, noiseFunction);
				if (island != null) result.add(island);				
			}			
		}
		return result;
	}
	
	/**
	 * @return Retuns a value between 80 and -100 which indicates this island's influence 
	 * at the block coordindates given. A non-negative value indicates there will be solid
	 * ground, while a negative value indicates the rocky-island-shore, which might be solid
	 * ground (but that becomes less likely the lower the value).
	 */
	public float influenceAt(int blockX, int blockZ) {
		
		float testChunkX = blockX / 16.0f;
		float testChunkZ = blockZ / 16.0f;

		int adjConstX = 1;
		int adjConstZ = 1;
		
		float adjustedX = (ChunkX - testChunkX) * 2 + adjConstX;
		float adjustedZ = (ChunkZ - testChunkZ) * 2 + adjConstZ;
		
		float result = 100.0f - (float)Math.sqrt(adjustedX * adjustedX + adjustedZ * adjustedZ) * ErosionFactor;
		if (result > 80.0f) result = 80.0f;
		if (result < -100.0f) result = -100.0f;
		
		return result;
	}
	
	/** 
	 * A version of influenceAt() that more exactly adheres to Minecraft's
	 * algorithm, for use in testing for End Cities.
	 */
	public float influenceAtChunk(int testChunkX, int testChunkZ) {
		
		int adjConstX = 1;
		int adjConstZ = 1;
		
		int adjustedX = (testChunkX - ChunkX) * 2 + adjConstX;
		int adjustedZ = (testChunkZ - ChunkZ) * 2 + adjConstZ;
		
		float result = 100.0f - (float)Math.sqrt(adjustedX * adjustedX + adjustedZ * adjustedZ) * ErosionFactor;
		if (result > 80.0f) result = 80.0f;
		if (result < -100.0f) result = -100.0f;
		
		return result;
	}
	
	
}