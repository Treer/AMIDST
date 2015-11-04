package amidst.map;

public class MapObjectEndCity extends MapObject {
	
	boolean _likelyExists;
	
	public MapObjectEndCity(int eX, int eY, boolean likelyExists) {		
		super(likelyExists ? MapMarkers.END_CITY : MapMarkers.POSSIBLE_END_CITY, eX, eY);		
		_likelyExists = likelyExists;
	}
	
	public String getName() {
		return _likelyExists ? "Likely End City" : "Possible End City";		
	}
}
