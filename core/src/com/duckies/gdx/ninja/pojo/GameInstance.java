package com.duckies.gdx.ninja.pojo;

import java.util.List;
import java.util.Map;

public class GameInstance {
	private Map<String, List<GameModificationsInstance>> modificationsByMap;


	public Map<String, List<GameModificationsInstance>> getModificationsByMap() {
		return modificationsByMap;
	}

	public void setModificationsByMap(
			Map<String, List<GameModificationsInstance>> modificationsByMap) {
		this.modificationsByMap = modificationsByMap;
	}
}
