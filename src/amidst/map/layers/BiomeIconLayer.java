package amidst.map.layers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import amidst.logging.Log;
import amidst.map.Fragment;
import amidst.map.IconLayer;
import amidst.map.MapMarkers;
import amidst.map.MapObject;
import amidst.minecraft.Biome;
import amidst.version.VersionInfo;

public class BiomeIconLayer extends IconLayer {
	
	static final int cBiomeScale = Fragment.SIZE / Fragment.BIOME_SIZE;	
	static final int cSearchResolution = 16; // should be a multiple of cBiomeScale (normally 4)  
	
	protected static int size = Fragment.BIOME_SIZE;
	
	MapMarkers biomeType;
	Biome[] desiredBiomes;
	VersionInfo minimumVersion; 
	boolean[] isDesiredBiome = new boolean[Biome.biomes.length];
	
	/** Manages a bunch of points which we know belong to a desired biome type, so we can
	 * group points together that belong to the same continuous biome, and determine a single
	 * set of coordinates roughly where its center is. 
	 */
	class BiomePointCloud {
		
		static final int cJoinDistance = 20;
		
		private Set<Point> points = new HashSet<Point>();
		private Point topLeft;
		private Point bottomRight;
		
		public BiomePointCloud(Point firstPoint) {
			
			topLeft     = new Point(firstPoint.x - cJoinDistance, firstPoint.y - cJoinDistance);
			bottomRight = new Point(firstPoint.x + cJoinDistance, firstPoint.y + cJoinDistance);
			points.add(firstPoint);			
		}

		public Point getTopLeft() { return topLeft; }

		public Point getBottomRight() { return bottomRight; }
		
		public Point getCenter() {
			// Perhaps do something more advanced than an average?			
			int sum_x = 0;
			int sum_y = 0;
			for(Point point : points) {
				sum_x += point.x;
				sum_y += point.y;				
			}
			
			return new Point(sum_x / points.size(), sum_y / points.size());
		}
		
		public boolean belongs(Point point) {
			
			return 
				point.x >= topLeft.x &&
				point.x <= bottomRight.x &&
				point.y >= topLeft.y &&
				point.y <= bottomRight.y;
		}

		public boolean belongs(BiomePointCloud pointCloud) {
			
			return 
				bottomRight.x - cJoinDistance >= pointCloud.getTopLeft().x &&  
				bottomRight.y - cJoinDistance >= pointCloud.getTopLeft().y &&  
				topLeft.x + cJoinDistance <= pointCloud.getBottomRight().x &&  
				topLeft.y + cJoinDistance <= pointCloud.getBottomRight().y;  
		}
		
		public boolean add(Point newPoint) {

			boolean changed = false;
			
			if (points.add(newPoint)) {
				changed = true;

				if (newPoint.x - cJoinDistance < topLeft.x) {
					topLeft.x = newPoint.x - cJoinDistance;
				} else if (newPoint.x + cJoinDistance > bottomRight.x) {
					bottomRight.x = newPoint.x + cJoinDistance;
				}
				
				if (newPoint.y - cJoinDistance < topLeft.y) {
					topLeft.y = newPoint.y - cJoinDistance;
				} else if (newPoint.y + cJoinDistance > bottomRight.y) {
					bottomRight.y = newPoint.y + cJoinDistance;
				}
			}
			return changed;
		}
		
		public void add(BiomePointCloud pointCloud) {
			
			if (pointCloud.getTopLeft().x < topLeft.x) {
				topLeft.x = pointCloud.getTopLeft().x;
			} else if (pointCloud.getBottomRight().x > bottomRight.x) {
				bottomRight.x = pointCloud.getBottomRight().x;
			}
			
			if (pointCloud.getTopLeft().y < topLeft.y) {
				topLeft.y = pointCloud.getTopLeft().y;
			} else if (pointCloud.getBottomRight().y > bottomRight.y) {
				bottomRight.y = pointCloud.getBottomRight().y;
			}
			points.addAll(pointCloud.points);			
		}
	}
	
	List<BiomePointCloud> candidates = new ArrayList<BiomePointCloud>();
	
	public BiomeIconLayer(MapMarkers biomeType) {
		
		switch(biomeType) {		
			case MUSHROOM_ISLAND:
				minimumVersion = VersionInfo.V1_0;
				desiredBiomes = new Biome[] { Biome.mushroomIsland, Biome.mushroomIslandM, Biome.mushroomIslandShore, Biome.mushroomIslandShoreM };
				break;
				
			case ICE_PLAINS_SPIKES:
				minimumVersion = VersionInfo.V1_7_2;
				desiredBiomes = new Biome[] { Biome.icePlainsSpikes } ;
				break;
				
			default:
				Log.crash("BiomeIconLayer created with unsupported biome");
				break;
		}		
		
		this.biomeType = biomeType;
		
		for (int i = 0; i < isDesiredBiome.length; i++) isDesiredBiome[i] = false;
		for(Biome biome : desiredBiomes) { isDesiredBiome[biome.index] = true; }		
	}
	
	
	@Override
	public boolean isVisible() {
		// Set to true because the only times this layer should be included in the
		// the main window is when debugging, in which case you probably want to see
		// were the MapObjects are being placed.
		return true;		
	}
	
	/** As more fragments are loaded, the number and position of the MapObjects changes
	 *  so you should call this for every fragment once all the fragments have loaded.
	 */
	@Override
	public void refresh(Fragment frag) {
		
		clearMapObjects(frag);
		addCandidatesToFragment(frag);
	}
	
	/** Override to make it only clear the objects that came from this Layer */
	@Override
	public void clearMapObjects(Fragment frag) {
		
		int newObjectsLength = 0;
		int newPosition = 0;
		
		for (int i = 0; i < frag.objectsLength; i++) {
		
			if (frag.objects[i].type != biomeType) {
				frag.objects[newPosition++] = frag.objects[i];
				newObjectsLength++;
			}
		}
		frag.objectsLength = newObjectsLength;
	}
	
	
	@Override
	public void generateMapObjects(Fragment frag) {
				
		int searchResolution = cSearchResolution / cBiomeScale;
		
		boolean candidatesChanged = false;
		
		for (int biome_y = 0; biome_y < size; biome_y += searchResolution) {
			for (int biome_x = 0; biome_x < size; biome_x += searchResolution) {

				int index = biome_y * size + biome_x;
			
				if (isDesiredBiome[frag.biomeData[index]]) {
					
					Point foundPoint = new Point(frag.blockX + (biome_x * cBiomeScale), frag.blockY + (biome_y * cBiomeScale));
	
					boolean foundExistingBiome = false;
					for(BiomePointCloud candidate: candidates) {
					
						if (candidate.belongs(foundPoint)) {						
							candidatesChanged = candidate.add(foundPoint);
							foundExistingBiome = true;
						}
					}
					if (!foundExistingBiome) {
						candidates.add(new BiomePointCloud(foundPoint));
						candidatesChanged = true;
					}				
				}
			}
		}
		if (candidatesChanged) combineNearbyCandidates();
		
		addCandidatesToFragment(frag);
	}
	
	private void addCandidatesToFragment(Fragment frag) {
		
		for(BiomePointCloud candidate: candidates) {
			
			Point center = candidate.getCenter();
			
			if (center.x >= frag.blockX && center.x < frag.blockX + Fragment.SIZE && center.y >= frag.blockY && center.y < frag.blockY + Fragment.SIZE) {
				MapObject newMapObject = new MapObject(biomeType, center.x - frag.blockX, center.y - frag.blockY);
				newMapObject.parentLayer = this;
				frag.addObject(newMapObject);
			}
			
		}		
	}
	
	public void combineNearbyCandidates() {

		List<BiomePointCloud> condensedCandidates = new ArrayList<BiomePointCloud>();
		
		for(BiomePointCloud candidate: candidates) {

			boolean isUnique = true;
			for(BiomePointCloud condensedCandidate: condensedCandidates) {
				if (condensedCandidate.belongs(candidate)) {
					isUnique = false;
					condensedCandidate.add(candidate);
				}
			}
			if (isUnique) condensedCandidates.add(candidate);
		}
		candidates = condensedCandidates;
		
		/*
		Log.i(biomeType + "Candidates: ");
		for(BiomePointCloud candidate: candidates) {
			Point center = candidate.getCenter();
			Log.i("   (" + center.x + ", " + center.y + "), cloud size: " + candidate.points.size());
		}*/
	}
}
