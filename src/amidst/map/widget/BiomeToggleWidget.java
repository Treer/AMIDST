package amidst.map.widget;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import amidst.logging.Log;
import amidst.map.FragmentManager;
import amidst.map.ImageLayer;
import amidst.map.Map;
import amidst.map.WorldDimensionType;
import amidst.map.layers.BiomeLayer;
import amidst.resources.ResourceLoader;
import MoF.MapViewer;

public class BiomeToggleWidget extends PanelWidget {
	private static BufferedImage highlighterIcon = ResourceLoader.getImage("highlighter.png");
	public static boolean isBiomeWidgetVisible = false;
	
	public BiomeToggleWidget(MapViewer mapViewer) {
		super(mapViewer);
		setDimensions(36, 36);
	}

	/** can return null! */
	private BiomeLayer getBiomeLayer() {
		BiomeLayer result = (BiomeLayer) mapViewer.getFragmentManager().getLayer(BiomeLayer.class);
		if (result == null) {
			// Perhaps mapViewer is showing The End?
			if (!targetVisibility) {
				// The map probably changed to The End (which has no biome layer), 
				// and the widget is still fading out.
			} else {
				Log.e("BiomeWidget: BiomeLayer is missing!"); 				
			}
		}
		return result;
	}
		
	@Override
	protected boolean onVisibilityCheck() {
		// getBiomeLayer() will fail if the mapViewer isn't showing the Overworld. 	
		return mapViewer.getFragmentManager().worldDimensionType == WorldDimensionType.OVERWORLD;
	}		
	
	@Override
	public void draw(Graphics2D g2d, float time) {
		super.draw(g2d, time);
		g2d.drawImage(highlighterIcon, x, y, 36, 36, null);
	}

	@Override
	public boolean onMousePressed(int x, int y) {
		isBiomeWidgetVisible = !isBiomeWidgetVisible;
		
		BiomeLayer biomeLayer = getBiomeLayer();
		if (biomeLayer != null) biomeLayer.setHighlightMode(isBiomeWidgetVisible);
		
		(new Thread(new Runnable() {
			@Override
			public void run() {
				BiomeLayer biomeLayer = getBiomeLayer();
				if (biomeLayer != null) mapViewer.getMap().repaintImageLayer(biomeLayer.getLayerId());
			}
		})).start();
		return true;
	}
}
