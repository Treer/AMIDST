package amidst.map;

import amidst.resources.ResourceLoader;

import java.awt.image.BufferedImage;

/** Contains information about all possible Map Markers.
 * Map objects use either its or their own icon
 * TODO: link to test.amidst.map object class
 */
public enum MapMarkers {
	NETHER_FORTRESS,
	PLAYER,
	SLIME,
	STRONGHOLD,
	JUNGLE,
	DESERT,
	VILLAGE,
	SPAWN,
	WITCH,
	OCEAN_MONUMENT,
	END_CITY,
	MINESHAFT,
	MUSHROOM_ISLAND,
	ICE_PLAINS_SPIKES,
	FLOWER_FOREST;
	
	public final BufferedImage image;
	
	private MapMarkers() {
		String fileName = this.toString().toLowerCase() + ".png";
		image = ResourceLoader.getImage(fileName);
	}
}
