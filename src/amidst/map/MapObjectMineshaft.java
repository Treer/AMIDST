package amidst.map;

/** Used mainly to be override its toString method for use in choices
 */
public class MapObjectMineshaft extends MapObject {
	public MapObjectMineshaft(int x, int y) {
		super(MapMarkers.MINESHAFT, x, y);
	}

	@Override
	public String toString() {
		return "Mineshaft at (" + x + ", " + y + ")";
	}
}