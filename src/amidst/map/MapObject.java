package amidst.map;
import amidst.Options;
import amidst.map.MapMarkers;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class MapObject extends Point {
	public MapMarkers type;
	public int rx, ry;
	public double localScale = 1.0;
	@Deprecated
	public double tempDist = 0;
	public IconLayer parentLayer;
	
	public MapObject(MapMarkers eType, int x, int y) {
		super(x, y);
		type = eType;
	}
	
	public String getName() {
		
		char[] charArray = type.toString().replace('_', ' ').toLowerCase().toCharArray(); 
        charArray[0] = Character.toUpperCase(charArray[0]); 		
		
		return new String(charArray);
	}
	
	public int getWidth() {
		return (int)(type.image.getWidth() * localScale);
	}
	public int getHeight() {
		return (int)(type.image.getHeight() * localScale);
	}
	
	public BufferedImage getImage() {
		return type.image;
	}
	
	public boolean isSelectable() {
		return Options.instance.showVillages.isSelected();
	}
	
	public boolean isLocatedInNether() {
		return false;
	}

	public boolean isNetherCoordinates() {
		return false;
	}
	
	public Point getNetherCoordinates() {
		if (isNetherCoordinates()) {		
			return new Point(rx, ry);
		} else {
			return new Point(rx >> 3, ry >> 3);		
		}
	}

	public Point getOverworldCoordinates() {
		if (isNetherCoordinates()) {		
			return new Point(rx << 3, ry << 3);
		} else {
			return new Point(rx, ry);		
		}
	}
	
	/** returns Nether coords for Nether objects and Overworld coords for Overworld objects */
	public Point getNaturalCoordinates() {
		if (isLocatedInNether()) {		
			return getNetherCoordinates();
		} else {
			return getOverworldCoordinates();		
		}
	}
	
	
	public MapObject setParent(IconLayer layer) {
		parentLayer = layer;
		return this;
	}
}
