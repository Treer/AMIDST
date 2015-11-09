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
	IGLOO,
	END_CITY,
	POSSIBLE_END_CITY,
	MINESHAFT,
	RAREBIOME_MUSHROOM_ISLAND,
	RAREBIOME_ICE_PLAINS_SPIKES,
	RAREBIOME_FLOWER_FOREST,
	RAREBIOME_MESA;
	
	public final BufferedImage image;
	
	private MapMarkers() {
		String fileName = this.toString().toLowerCase() + ".png";
		image = ResourceLoader.getImage(fileName);
	}
}
