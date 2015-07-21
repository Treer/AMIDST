package amidst.map;

import java.awt.Point;

public class MapObjectNether extends MapObject {
	
	private boolean isNetherCoordinates;
	
	public MapObjectNether(int eX, int eY, boolean eIsNetherCoordinates) {

		super(MapMarkers.NETHER_FORTRESS, eX, eY);
		isNetherCoordinates = eIsNetherCoordinates;		
	}
	
	public boolean isNetherCoordinates() {
		return isNetherCoordinates;
	}

	public boolean isLocatedInNether() {
		return true;
	}	
}
