package amidst.map;

public class MapObjectDesertTemple extends MapObject {
	public MapObjectDesertTemple(int eX, int eY) {
		super(MapMarkers.DESERT, eX, eY);
	}
	
	@Override
	public String getName() { return "Desert temple"; }
}
