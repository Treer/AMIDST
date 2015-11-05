package amidst.map;

public class MapObjectIgloo extends MapObject {
	public MapObjectIgloo(int eX, int eY) {
		super(MapMarkers.IGLOO, eX, eY);
	}
	
	@Override
	public String getName() { return "Igloo"; }	
}
