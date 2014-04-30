package com.stalker.places;

import java.util.HashMap;
import java.util.Map;

public class TODOPlaceListMap {
	private Map<TODOItem, PlacesList> todoToPlacesMap;

	public Map<TODOItem, PlacesList> getTodoToPlacesMap() {
		if(todoToPlacesMap==null)
			todoToPlacesMap = new HashMap<TODOItem, PlacesList>();
		return todoToPlacesMap;
	}

	public void setTodoToPlacesMap(Map<TODOItem, PlacesList> todoToPlacesMap) {
		this.todoToPlacesMap = todoToPlacesMap;
	}
	
}
