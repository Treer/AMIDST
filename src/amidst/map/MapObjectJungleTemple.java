package amidst.map;

public class MapObjectJungleTemple extends MapObject {
	public MapObjectJungleTemple(int eX, int eY) {
		super(MapMarkers.JUNGLE, eX, eY);
	}
	
	@Override
	public String getName() { return "Jungle temple"; }	
}
