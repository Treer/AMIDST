package MoF; // What does MoF stand for??

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;

import amidst.Options;
import amidst.logging.Log;
import amidst.map.FragmentManager;
import amidst.map.FragmentManagerListener;
import amidst.map.IconLayer;
import amidst.map.ImageLayer;
import amidst.map.LayerVisibilityController;
import amidst.map.LiveLayer;
import amidst.map.Map;
import amidst.map.MapMarkers;
import amidst.map.MapObject;
import amidst.map.WorldDimensionType;
import amidst.map.layers.BiomeIconLayer;
import amidst.map.layers.EndCityLayer;
import amidst.map.layers.NetherFortressLayer;
import amidst.map.layers.OceanMaskLayer;
import amidst.map.layers.OceanMonumentLayer;
import amidst.map.layers.SpawnLayer;
import amidst.map.layers.StrongholdLayer;
import amidst.map.layers.TempleLayer;
import amidst.map.layers.VillageLayer;

/**
 *  Generates a massive area of the map centered at 0,0 and exports it in a format
 *  suitable for use by the online map viewing system at http://buildingwithblocks.info
 */
public class MapExporter implements FragmentManagerListener {

	public static int cExpectedBlocksPerPixel = 16;
	public static int cExpectedOceamMaskSize = 1250;
	
	private ConcurrentLinkedQueue<ExportRequest> requestQueue = new ConcurrentLinkedQueue<ExportRequest>();
	private boolean mapGenerationInitiated = false;
	private boolean mapGenerationComplete = false;
	private boolean processingRequestsInProgress = false;

	/** access this via getFragmentManager() */
	private FragmentManager _fragmentManager = null;
	/** access this via getExportMap() */
	private Map _map = null;
	private OceanMaskLayer oceanLayer = new OceanMaskLayer();
	private String seed;
	private IconLayer[] iconLayers;
	
	
	enum RequestType {
		OceanMap,
		EndMap,
		OverworldLocationList,
		NetherLocationList,
		EndLocationList
	}	
	class ExportRequest {		
		public RequestType exportType = RequestType.OceanMap;
		public File exportFile = null;
		
		public ExportRequest(RequestType exportType, File exportFile) {
			this.exportType = exportType;
			this.exportFile = exportFile;			
		}
	}
	
	private void addRequest(ExportRequest request) {
		
		requestQueue.offer(request);
		
		if (!mapGenerationInitiated) { 
			startMapGeneration();
		} else {
			if (mapGenerationComplete) {		
				processExportRequests();
			} else {
				// We don't have to do anything, as the map is currently	
				// generating, and the ExportRequest will be processed when it's
				// finished.
			}
		}
	}

	private FragmentManager getFragmentManager() {
		if (_fragmentManager == null) _fragmentManager = CreateFragmentManager(WorldDimensionType.OVERWORLD);
		return _fragmentManager;
	}
	
	private Map getMap() {
		// Defer creation of the _map until needed, because it's resource 
		// intensive and this class rarely gets used (but is always created).
		if (_map == null) _map = new Map(getFragmentManager());
		return _map;
	}
		
	/** Starts the fragmentManager creating all the map fragments required */ 
	private void startMapGeneration() {
		
		// startMapGeneration should only be called once, as it's a memory and
		// CPU intensive task, and the position/size of this map will always be the 
		// same.
		assert !mapGenerationInitiated;
		mapGenerationInitiated = true;
				
		Map map = getMap();
		
		map.setZoom(1.0 / cExpectedBlocksPerPixel);
		map.width  = cExpectedOceamMaskSize;
		map.height = cExpectedOceamMaskSize;		

		map.centerOn(0, 0);
		map.requestFragments();
		getFragmentManager().addListener(this); // ToDo: to procrastinate - slight race condition here, in theory anyway		
	}
	
	/** Listener for when fragmentManager finishes generating the map */
	@Override
	public void FragmentsLoaded() {

		mapGenerationComplete = true;
		Log.i("ocean map generation complete");
		processExportRequests();
	}	
	
	private void processExportRequests() {
		// Warning, this can be called from different threads at the same time,
		// keep it threadsafe.
		
		ExportRequest request = requestQueue.poll();
		while(request != null) {
			processingRequestsInProgress = true;
			
			try {
				if (request.exportType == RequestType.OverworldLocationList || request.exportType == RequestType.NetherLocationList || request.exportType == RequestType.EndLocationList) {
					SaveLocationList(request.exportFile, request.exportType);
				} else if (request.exportType == RequestType.OceanMap) {
					SaveOceanMask(request.exportFile);
				} else {
					assert false;		
				}							
			} catch(Exception e) {
				Log.e("Export request failed: " + e.getMessage());			
			}
			request = requestQueue.poll();						
		}
		processingRequestsInProgress = false;
	}

	/** Used in SaveLocationList() to store extra info about a MapObject type */
	private class LocationTypeInfo {
		public String locationTypeName;
		public String caption = null;
		public String url = null;
		/** -1 if no iconIndex is needed */
		public int iconIndex;
		public boolean isSpoilerLocation;

		/** Creates a text entry for use in the online map system.
		 *  Pass the amidst.Map if you want village types to be resolved. */
		public String LocationFileEntry(MapObject location, Map map) {
			
			String entryType = locationTypeName;
			
			if (location.type == MapMarkers.VILLAGE && map != null) {
				// Try to determine which kind of village
				String biome = map.getBiomeNameAt(new Point(location.rx, location.ry));
			
				if (biome.contains("Desert")) {
					entryType = "DesertVillage";
				} else if (biome.contains("Savanna")) {
					entryType = "SavannahVillage";
				}			
			}
			
			Point naturalCoords = location.getNaturalCoordinates();			
			String result = String.format("%1$-16s %2$5d, %3$5d", entryType + ",", naturalCoords.x, naturalCoords.y);  
			
			int column = 3; // entryType, x, and z were the first 3 columns.
			if (caption != null) {
				// caption goes in the 4th column
				while (column < 4) { column++; result += ", "; }
				result += caption;
			}
			if (url != null) {
				// url goes in the 6th column
				while (column < 6) { column++; result += ", "; }
				result += url;
			}			
			if (iconIndex >= 0) {
				// iconIndex goes in the 7th column
				while (column < 7) { column++; result += ", "; }  
				result += iconIndex;
			}

			return result;
		}
		
		public LocationTypeInfo(String locationTypeName, boolean isSpoilerLocation) {
			this.locationTypeName = locationTypeName;
			this.iconIndex = -1;
			this.isSpoilerLocation = isSpoilerLocation;
		}
		
		public LocationTypeInfo(String locationTypeName, int iconIndex, boolean isSpoilerLocation) {
			this.locationTypeName = locationTypeName;
			this.iconIndex = iconIndex;
			this.isSpoilerLocation = isSpoilerLocation;
		}
	}
	
	private void SaveLocationList(File exportLocationsFile, RequestType worldType) {
		
		java.util.Map<MapMarkers, LocationTypeInfo> locationTable = new EnumMap<MapMarkers, LocationTypeInfo>(MapMarkers.class);

		if (worldType == RequestType.NetherLocationList) {
			Log.i("Saving Nether locations...");
			locationTable.put(MapMarkers.NETHER_FORTRESS,   new LocationTypeInfo("NetherFortress", false));
		} else if (worldType == RequestType.EndLocationList) {
			Log.i("Saving End locations...");
			locationTable.put(MapMarkers.END_CITY,          new LocationTypeInfo("EndCity", false));
		} else {
			Log.i("Saving locations...");
			locationTable.put(MapMarkers.STRONGHOLD,        new LocationTypeInfo("Dragon",         true));
			locationTable.get(MapMarkers.STRONGHOLD).caption = "\"~Stronghold~\"";
			locationTable.get(MapMarkers.STRONGHOLD).url = "http://minecraft.gamepedia.com/Stronghold";

			locationTable.put(MapMarkers.RAREBIOME_ICE_PLAINS_SPIKES, new LocationTypeInfo("IcePlainsSpikes", true));				
			locationTable.put(MapMarkers.RAREBIOME_MUSHROOM_ISLAND,   new LocationTypeInfo("MushroomIsland",  true));				
			locationTable.put(MapMarkers.RAREBIOME_FLOWER_FOREST,     new LocationTypeInfo("FlowerForest",    false));				
			locationTable.put(MapMarkers.RAREBIOME_MESA,              new LocationTypeInfo("Mesa",            true));				
			
			locationTable.put(MapMarkers.SPAWN,             new LocationTypeInfo("Spawn",           false));
			locationTable.put(MapMarkers.JUNGLE,            new LocationTypeInfo("JungleTemple",    false));	
			locationTable.put(MapMarkers.DESERT,            new LocationTypeInfo("DesertTemple",    false));	
			locationTable.put(MapMarkers.VILLAGE,           new LocationTypeInfo("Village",         false));	
			locationTable.put(MapMarkers.OCEAN_MONUMENT,    new LocationTypeInfo("SeaMonster",      true));	
			locationTable.put(MapMarkers.WITCH,             new LocationTypeInfo("WitchHut",        false));
		}
				
		
		// Now that we have complete biome information we can remove the excess 
		// biome icons from all the fragments.
		Map map = getMap();
		for(IconLayer layer : iconLayers) {
			if (layer instanceof BiomeIconLayer) {
				BiomeIconLayer biomeIconLayer = (BiomeIconLayer)layer;				
				biomeIconLayer.combineNearbyCandidates();
			}			
		}
		map.repaintFragments();
		
		
		// Sort all the map objects so they will be listed in clumps of the same type. 
		List<MapObject> mapObjectsUnsorted = map.getMapObjects();
		List<MapObject> mapObjects = new ArrayList<MapObject>();		
		for(MapMarkers objectType : MapMarkers.values()) {
			for(MapObject location : mapObjectsUnsorted) {	
				if (location.type == objectType) mapObjects.add(location);				
			}
		}
		
		try {
			FileWriter file = new FileWriter(exportLocationsFile, false);
			
			PrintWriter writer = new PrintWriter(file);
			
			// Todo: fix up the project/options code so the text seed is available.
			writer.print("// This list of ");
			if (worldType == RequestType.NetherLocationList) writer.print("Nether ");
			if (worldType == RequestType.EndLocationList) writer.print("End ");
			writer.print("locations was generated for the seed ");
			writer.println(Options.instance.seedText == null ? seed : "\"" + Options.instance.seedText + "\" (" + seed + ")");			
			writer.println("// It can be used with the online map system provided at http://buildingwithblocks.info");
			writer.println("// and was generated by a version of AMIDST modified for this purpose.");
			if (worldType == RequestType.OverworldLocationList) {
				writer.println("//");
				writer.println("// Note, you may find about 5% of the villages that");
				writer.println("// are predicted here don't exist in the actual game. This happens because");
				writer.println("// Minecraft eliminates structures in later stages of the terrain generation");
				writer.println("// process if it finds their surrounds to be unsuitable.");
			}
			if (worldType == RequestType.EndLocationList) {
				writer.println("//");
				writer.println("// Note, you may find about 20% of the End Cities that");
				writer.println("// are predicted here don't exist in the actual game. This happens because");
				writer.println("// Minecraft eliminates structures in later stages of the terrain generation");
				writer.println("// process if it finds their islands to be unsuitable.");
			}

			
			writer.println("");
			
			if (worldType == RequestType.OverworldLocationList) {
				// Insert the spoiler locations 
				writer.println("");
				writer.println("// The following locations are spoilers, which you may wish to remove!");

				for(MapObject location : mapObjects) {				
					LocationTypeInfo info = locationTable.get(location.type);
					if (info != null && info.isSpoilerLocation) {
						writer.println(info.LocationFileEntry(location, map));
						
						if (location.type == MapMarkers.RAREBIOME_MUSHROOM_ISLAND) {
							// Include an island overlay on top of the mushroom location
							LocationTypeInfo overlay = new LocationTypeInfo("IslandOverlay", true);
							writer.println(overlay.LocationFileEntry(location, map));							
						}
					}
				}
			}

			int oceanMaskRange = (cExpectedOceamMaskSize * cExpectedBlocksPerPixel) / 2;
			int range = 1600;
			int lastRange = -1;
			if (worldType == RequestType.NetherLocationList) {
				// the oceanMaskRange will only cover an 8th of the area in the Nether
				range /= 8;
				oceanMaskRange /= 8;
			}
			
			do {
				// Insert the remaining locations in clumps based on their range 
				
				int rangeLimit = Math.min(range, oceanMaskRange);

				writer.println("");
				writer.println("// The following locations fall within a map of range " + rangeLimit);

				if (lastRange <= 0) {
					writer.println("Label, 0, 0, \"\\nOrigin\"");
				}
				
				for(MapObject location : mapObjects) {				
					LocationTypeInfo info = locationTable.get(location.type);
					if (info != null && !info.isSpoilerLocation) {
						Point naturalCoords = location.getNaturalCoordinates();
						int x = Math.abs(naturalCoords.x);
						int y = Math.abs(naturalCoords.y);
						if (x <= rangeLimit && y <= rangeLimit && (x > lastRange || y > lastRange)) {						
							writer.println(info.LocationFileEntry(location, map));
						}
					}
				}
				
				lastRange = range;
				range *= 2;
			} while (lastRange <= oceanMaskRange); 
			
			writer.close();
			Log.i("Saved locations.");  	
			
			
		} catch (IOException e) {
			Log.w("Couldn't save locations to file");
		}
	}
	
	private void SaveOceanMask(File exportImageFile) {
		
		// oceanMap must already be generated to export it to an image.
		assert mapGenerationComplete;
		
		Map map = getMap();
		
		// Create a black&white 1-bit-per-pixel BufferedImage
		int width = map.width;
		int height = map.height;
		int bytesPerRow = (int)Math.ceil(width / 8.0);  
	    
		byte[] rasterData = new byte[height * bytesPerRow];  			      
	    for (int i = 0; i < rasterData.length; i++) rasterData[i] = (byte)170;  
		   
		byte[] bw = {
			(byte) 0xff, 
			(byte) 0
		};
		
		IndexColorModel blackAndWhite = new IndexColorModel(  
			1,  // One bit per pixel  
			2,  // Two values in the component arrays  
			bw, // Red Components  
			bw, // Green Components  
			bw  // Blue Components
		);  
		   
		DataBuffer buffer = new DataBufferByte( rasterData, rasterData.length);  		
		 // Get the writable raster so that data can be changed.  
	    WritableRaster raster = Raster.createPackedRaster(buffer, width, height, 1, null);  
	      
	    // Create the BufferedImage  
	    BufferedImage image = new BufferedImage(blackAndWhite, raster, true, null);  
		Graphics2D g2d = image.createGraphics();
				
		Log.i("Rendering ocean...");
		
		// Turn off all the layers except the ocean layer before drawing the map
		// (unfortunately these settings are global)
		LayerVisibilityController layerVisibilityController = new LayerVisibilityController();
		layerVisibilityController.StoreState();		
		try {
			layerVisibilityController.SetAll(false);
			map.draw(g2d, 1);
		} finally {
			layerVisibilityController.RestoreState();
		}
				
		Log.i("Saving ocean, w: " + image.getWidth() + ", h: " + image.getHeight() + "...");  		
		try {
			ImageIO.write(image, "png", exportImageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("Saved");  		
		
		g2d.dispose();
		image.flush();				
	}
	
	public boolean getMapGenerationInProgress() {
		return mapGenerationInitiated && !mapGenerationComplete;
	}
	public boolean getExportRequestInProgress() {
		return processingRequestsInProgress;
	}
		
	public void saveOceanToFile(File f) {		
		addRequest(new ExportRequest(RequestType.OceanMap, f));
	}

	public void saveOverworldLocationsToFile(File f) {		
		addRequest(new ExportRequest(RequestType.OverworldLocationList, f));
	}

	public void saveNetherLocationsToFile(File f) {		
		addRequest(new ExportRequest(RequestType.NetherLocationList, f));
	}

	public void saveEndLocationsToFile(File f) {		
		addRequest(new ExportRequest(RequestType.EndLocationList, f));
	}
		
	public void dispose() {
		_fragmentManager = null;
		if (_map != null) {
			_map.dispose(); // this will also clean up the fragmentManager (passed to it during construction)
			_map = null;
		}
	}
		
	private FragmentManager CreateFragmentManager(WorldDimensionType dimensionType) {
		
		assert dimensionType == WorldDimensionType.OVERWORLD; // Exporting of The End not implemented yet
		
		iconLayers = new IconLayer[] {
			new VillageLayer(),
			new OceanMonumentLayer(),
			new StrongholdLayer(),
			new TempleLayer(),
			new SpawnLayer(),
			new NetherFortressLayer(),
			new EndCityLayer(),
			new BiomeIconLayer(MapMarkers.RAREBIOME_MUSHROOM_ISLAND, false),
			new BiomeIconLayer(MapMarkers.RAREBIOME_ICE_PLAINS_SPIKES, false),
			new BiomeIconLayer(MapMarkers.RAREBIOME_FLOWER_FOREST, false),
			new BiomeIconLayer(MapMarkers.RAREBIOME_MESA, false)
		};		
		
		return new FragmentManager(
			dimensionType,
			new ImageLayer[] { oceanLayer },
			new LiveLayer[] { },
			iconLayers
		);						
	}	
	
	public MapExporter(long seed) {
		
		this.seed = String.format("%d", seed);
		oceanLayer.visible = true;
	}
}
