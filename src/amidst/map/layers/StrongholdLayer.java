package amidst.map.layers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import amidst.Options;
import amidst.map.Fragment;
import amidst.map.IconLayer;
import amidst.map.MapObjectStronghold;
import amidst.minecraft.Biome;
import amidst.minecraft.MinecraftUtil;
import amidst.version.VersionInfo;

public class StrongholdLayer extends IconLayer {
	public static StrongholdLayer instance;
	
	private static final Biome[] biomesDefault = {
		Biome.desert, 
		Biome.forest, 
		Biome.extremeHills,
		Biome.swampland
	};
	private static final Biome[] biomes1_0 = {
		Biome.desert, 
		Biome.forest,
		Biome.extremeHills, 
		Biome.swampland, 
		Biome.taiga, 
		Biome.icePlains, 
		Biome.iceMountains
	};
	private static final Biome[] biomes1_1 = {
		Biome.desert, 
		Biome.forest, 
		Biome.extremeHills, 
		Biome.swampland, 
		Biome.taiga, 
		Biome.icePlains, 
		Biome.iceMountains, 
		Biome.desertHills, 
		Biome.forestHills, 
		Biome.extremeHillsEdge
	};
	private static final Biome[] biomes12w03a = {
		Biome.desert,
		Biome.forest, 
		Biome.extremeHills,
		Biome.swampland, 
		Biome.taiga, 
		Biome.icePlains, 
		Biome.iceMountains, 
		Biome.desertHills, 
		Biome.forestHills,
		Biome.extremeHillsEdge, 
		Biome.jungle, 
		Biome.jungleHills
	};
	
	double option_distance_inChunks = 32;
	int option_structuresOnFirstRing = 3;
	int option_totalStructureCount = 128;	
	private MapObjectStronghold[] _strongholds = null;
	
	public StrongholdLayer() {
		instance = this;
	}
	
	@Override
	public boolean isVisible() {
		return Options.instance.showStrongholds.get();		
	}
	
	@Override
	public void generateMapObjects(Fragment frag) {
		int size = Fragment.SIZE >> 4;
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int chunkX = x + frag.getChunkX();
				int chunkY = y + frag.getChunkY();
				if (checkChunk(chunkX, chunkY)) { // TODO: This does not need a per-chunk test!
					// FIXME: Possible use of checkChunk causing negative icons to be misaligned!
					frag.addObject(new MapObjectStronghold(x << 4, y << 4).setParent(this));
				}
			}
		}
	}
	 
	public void findStrongholds() {
		Random random = new Random();
		random.setSeed(Options.instance.seed);
		
		if (MinecraftUtil.getVersion().isAtLeast(VersionInfo.V15w43c)) {
			// Number of strongholds was increased to 128
			option_totalStructureCount = 128;
		} else {
			option_totalStructureCount = 3;
		}
		_strongholds = new MapObjectStronghold[option_totalStructureCount];
		
		// TODO: Replace this system!
		Biome[] validBiomes = biomesDefault;
		if (MinecraftUtil.getVersion() == VersionInfo.V1_9pre6 || MinecraftUtil.getVersion() == VersionInfo.V1_0)
			validBiomes = biomes1_0;
		if (MinecraftUtil.getVersion() == VersionInfo.V1_1)
			validBiomes = biomes1_1;
		if (MinecraftUtil.getVersion().isAtLeast(VersionInfo.V12w03a))
			validBiomes = biomes12w03a;

		List<Biome> biomeArrayList = Arrays.asList(validBiomes);
		
		if (MinecraftUtil.getVersion().isAtLeast(VersionInfo.V13w36a)) {
			biomeArrayList = new ArrayList<Biome>();
			for (int i = 0; i < Biome.biomes.length; i++) {
				if ((Biome.biomes[i] != null) && (Biome.biomes[i].type.value1 > 0f)) {
					biomeArrayList.add(Biome.biomes[i]);
				}
			}
		}

		// Issue MC-92289 was fixed in 16w06a (https://bugs.mojang.com/browse/MC-92289)
		// giving a new stronghold location algorithm.
		// (V1_9_pre2 is the closest we can get to recognising snapshot 16w06a)
		boolean emulateBug_MC92289 = !MinecraftUtil.getVersion().isAtLeast(VersionInfo.V1_9_pre2);
		int ring = emulateBug_MC92289 ? 1 : 0;

		int structuresPerRing = option_structuresOnFirstRing;
		int currentRingStructureCount = 0;
		double angle = random.nextDouble() * 3.141592653589793D * 2.0D;
		for (int i = 0; i < option_totalStructureCount; i++) {
			
			double distance;
			if (emulateBug_MC92289) {
				// ring starts at 1
				distance = ((1.25D * ring) + random.nextDouble()) * (option_distance_inChunks * ring);
			} else {
				// ring starts at 0
				distance = (4.0 * option_distance_inChunks) + (6.0 * ring * option_distance_inChunks) + (random.nextDouble() - 0.5) * (option_distance_inChunks * 2.5);				
			}
			
			int x = (int)Math.round(Math.cos(angle) * distance);
			int y = (int)Math.round(Math.sin(angle) * distance);
			
			Point strongholdLocation = MinecraftUtil.findValidLocation((x << 4) + 8, (y << 4) + 8, 112, biomeArrayList, random);
			if (strongholdLocation != null) {
				x = strongholdLocation.x >> 4;
				y = strongholdLocation.y >> 4;
			}
			_strongholds[i] = new MapObjectStronghold((x << 4) + 8, (y << 4) + 8);

			if (emulateBug_MC92289) {			
				angle += 6.283185307179586D * ring / structuresPerRing;			
				// 15w44b algorithm
				if (++currentRingStructureCount == structuresPerRing) {
					ring++;
					currentRingStructureCount = 0;
					structuresPerRing += structuresPerRing + random.nextInt(structuresPerRing);
				}
				/* 15w31c algorithm
				if (i == structuresPerRing) {
					ring += 2 + random.nextInt(5);
					structuresPerRing += 1 + random.nextInt(2);				
				}
				*/
			} else {
				angle += 6.283185307179586D / structuresPerRing;			
				if (++currentRingStructureCount == structuresPerRing) {
					ring++;
					currentRingStructureCount = 0;
					structuresPerRing += 2 * structuresPerRing / (ring + 1);
					structuresPerRing = Math.min(structuresPerRing, option_totalStructureCount - i);
					angle += random.nextDouble() * 3.141592653589793 * 2.0;
				}
			}
		}
	}

	public boolean checkChunk(int chunkX, int chunkY) {
		
		MapObjectStronghold[] strongholds = getStrongholds();
		for (int i = 0; i < strongholds.length; i++) {
			int strongholdChunkX = strongholds[i].x >> 4;
			int strongholdChunkY = strongholds[i].y >> 4;
			if ((strongholdChunkX == chunkX) && (strongholdChunkY == chunkY))
				return true;
		}
		return false;
	}
	
	public MapObjectStronghold[] getStrongholds() {
		if (_strongholds == null) findStrongholds();
		return _strongholds;
	}
	
	@Override
	public void reload() {
		findStrongholds();
	}
}
