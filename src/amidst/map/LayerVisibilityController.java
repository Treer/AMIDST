package amidst.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import amidst.Options;
import amidst.preferences.BooleanPrefModel;

/** Provides simple mechanisms for hiding and showing all the layers */ 
public class LayerVisibilityController {

	List<BooleanPrefModel> layerList = new ArrayList<BooleanPrefModel>();
	java.util.Map<BooleanPrefModel,Boolean> storedState = new HashMap<BooleanPrefModel, Boolean>();
	
	/** Stores the current value of all the options being controlled */  
	public void StoreState() {
		for(BooleanPrefModel layer : layerList) {
			storedState.put(layer, layer.get());
		}
	}

	/** Retrieves the stored values of all the options being controlled */  
	public void RestoreState() {
		for(BooleanPrefModel layer : layerList) {
			storedState.get(layer);
			layer.set(storedState.containsKey(layer) ? storedState.get(layer) : layer.get());
		}
	}
	
	/** Assigns the given value to all the options being controlled */  
	public void SetAll(boolean visible) {		
		for(BooleanPrefModel layer : layerList) layer.set(visible);
	}
		
	public LayerVisibilityController() {
		// Until there is more than one use for this class we can hardcode it
		// to automatically include the visibility options for all the layers.
		layerList.add(Options.instance.showGrid);
		layerList.add(Options.instance.showSlimeChunks);
		layerList.add(Options.instance.showEndChunks);
		layerList.add(Options.instance.showVillages);
		layerList.add(Options.instance.showOceanMonuments);
		layerList.add(Options.instance.showTemples);
		layerList.add(Options.instance.showEndCities);
		layerList.add(Options.instance.showStrongholds);
		layerList.add(Options.instance.showPlayers);
		layerList.add(Options.instance.showNetherFortresses);
		layerList.add(Options.instance.showSpawn);				
	}
	
}
