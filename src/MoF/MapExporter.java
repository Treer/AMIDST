package MoF; // What does MoF stand for??

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
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
import amidst.map.layers.NetherFortressLayer;
import amidst.map.layers.OceanMaskLayer;
import amidst.map.layers.SpawnLayer;
import amidst.map.layers.StrongholdLayer;
import amidst.map.layers.TempleLayer;
import amidst.map.layers.VillageLayer;
import amidst.map.widget.Widget;

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

	private FragmentManager fragmentManager;
	private Map oceanMap;
	private OceanMaskLayer oceanLayer = new OceanMaskLayer();
	
	
	enum RequestType {
		OceanMap,
		LocationList
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

	/** Starts the fragmentManager creating all the map fragments required */ 
	private void startMapGeneration() {
		
		// startMapGeneration should only be called once, as it's a memory and
		// CPU intensive task, and the position/size of this map will always be the 
		// same.
		assert !mapGenerationInitiated;
		mapGenerationInitiated = true;
				
		oceanMap.setZoom(1.0 / cExpectedBlocksPerPixel);
		oceanMap.width  = cExpectedOceamMaskSize;
		oceanMap.height = cExpectedOceamMaskSize;		

		oceanMap.centerOn(0, 0);
		oceanMap.requestFragments();
		fragmentManager.addListener(this); // to procrastinate: slight race condition here, in theory anyway		
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
			
			if (request.exportType == RequestType.LocationList) {
				SaveLocationList(request.exportFile);
			} else if (request.exportType == RequestType.OceanMap) {
				SaveOceanMask(request.exportFile);
			} else {
				assert false;		
			}			
			request = requestQueue.poll();
		}
	}

	private void SaveLocationList(File exportImageFile) {
		Log.i("Saving locations...");  		
	
		/*
		for (Widget widget : widgets) {
			
		};
		 */
		Log.i("Not saved - not implemented.");  		
	}
	
	private void SaveOceanMask(File exportImageFile) {
		
		// oceanMap must already be generated to export it to an image.
		assert mapGenerationComplete;
		
		// Create a black&white 1-bit-per-pixel BufferedImage
		int width = oceanMap.width;
		int height = oceanMap.height;
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
				
		System.out.println("Rendering ocean");
		
		LayerVisibilityController layerVisibilityController = new LayerVisibilityController();
		layerVisibilityController.StoreState();		
		try {
			layerVisibilityController.SetAll(false);
			oceanMap.draw(g2d, 1);
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
	
		
	public void saveOceanToFile(File f) {		
		addRequest(new ExportRequest(RequestType.OceanMap, f));
	}

	public void saveLocationsToFile(File f) {		
		addRequest(new ExportRequest(RequestType.LocationList, f));
	}
	
	public MapExporter() {
		
		oceanLayer.visible = true;
		
		fragmentManager = new FragmentManager(
			new ImageLayer[] { oceanLayer },
			new LiveLayer[] { },
			new IconLayer[] {
				new VillageLayer(),
				new StrongholdLayer(),
				new TempleLayer(),
				new SpawnLayer(),
				new NetherFortressLayer()
			}
		);				
		
		oceanMap = new Map(fragmentManager);
	}
}
