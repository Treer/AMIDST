package amidst.map;


public class IconLayer extends Layer {
	public IconLayer() {
	}
	
	public void generateMapObjects(Fragment frag) {
		
	}
	
	public void clearMapObjects(Fragment frag) {
		frag.objectsLength = 0;
	}
	
	/** Override this in a subclass if the icon objects might change after the 
	 * fragment was loaded. Use it to update Fragment.objects (and Fragment.objectsLength) 
	 */
	public void refresh(Fragment frag) {
		// Do nothing - most icon layers get their MapObjects right the first time!
	}
}
