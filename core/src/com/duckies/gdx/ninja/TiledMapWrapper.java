package com.duckies.gdx.ninja;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.duckies.gdx.ninja.pojo.Warp;

public class TiledMapWrapper {
	private TiledMap tiledMap;
	private final OrthogonalTiledMapRendererWithSprites tiledMapRenderer;
	private final List<Warp> warps;

	public TiledMapWrapper(String mapName) {
		tiledMap = new TmxMapLoader().load("farm/" + mapName);
		tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(tiledMap);

		this.warps = buildExits(mapName);
	}

	private List<Warp> buildExits(String mapName) {

		List<Warp> warps = new ArrayList<>();

		String warp = tiledMap.getProperties().get("Warp", String.class);

		String[] s = warp.split(" ");

		int currentId = 0;

		Map<Vector2, Map<String, Vector2>> vecByOrigin = new HashMap<>();

		while (currentId < s.length) {
			// TODO: build exit;

			Vector2 departure = new Vector2(Integer.parseInt(s[currentId]), Integer.parseInt(s[currentId + 1]));

			Map<String, Vector2> stringVector2Map = vecByOrigin.computeIfAbsent(departure, vector2 -> new HashMap<>());
			String newMapName = s[currentId + 2];
			Vector2 arrival = new Vector2(Integer.parseInt(s[currentId + 3]), Integer.parseInt(s[currentId + 4]));
			stringVector2Map.put(mapName, arrival);

			warps.add(new Warp(departure, mapName.substring(0,mapName.indexOf(".")), arrival, newMapName));

			currentId += 5;
		}

		return warps;


	}

	public TiledMapTileLayer getPathsLayer() {
		return (TiledMapTileLayer) tiledMap.getLayers().get("Paths");
	}

	public TiledMapTileLayer getBackLayer() {
		return (TiledMapTileLayer) tiledMap.getLayers().get("Back");
	}


	public MapObjects getObjectLayerObjects() {
		MapLayers layers = tiledMap.getLayers();
		for (int i = 0; i < layers.getCount(); i++) {
			MapLayer mapLayer = layers.get(i);
			if (!(mapLayer instanceof TiledMapTileLayer) && "Back".equals(mapLayer.getName())) {
				mapLayer.setVisible(true);
				return mapLayer.getObjects();
			}
		}
		return tiledMap.getLayers().get(5).getObjects();
	}

	public int getMaxTileX() {
		return getPathsLayer().getWidth();
	}

	public int getMaxTileY() {
		return getPathsLayer().getHeight();
	}

	public int getTileHeight() {
		return getPathsLayer().getTileHeight();
	}

	public int getTileWidth() {
		return getPathsLayer().getTileWidth();
	}

	public Map<String, TiledMapTileLayer.Cell> getCellsMatchingCoordinates(int x, int y) {
		Map<String, TiledMapTileLayer.Cell> cellByLayer = new HashMap<>();
		for (int i = 0; i < tiledMap.getLayers().getCount(); i++) {
			if (!(tiledMap.getLayers().get(i) instanceof TiledMapTileLayer layer)) {
				continue;
			}

			cellByLayer.put(layer.getName(), layer.getCell(x, y));
		}

		return cellByLayer;
	}

	public MapRenderer getTiledMapRenderer() {
		return tiledMapRenderer;
	}

	public void dispose() {
		tiledMap.dispose();
		tiledMapRenderer.dispose();
	}

	public void removeCellTile(int x, int y) {
		for (MapLayer layer : tiledMap.getLayers()) {
			if (!(layer instanceof TiledMapTileLayer tiledLayer) || "Back".equals(layer.getName())) {
				continue;
			}

			TiledMapTileLayer.Cell cell = tiledLayer.getCell(x, y);
			if (cell != null) {
				cell.setTile(null);
			}
		}
	}

	public Warp getWarp(int tileCellX, int tileCellY) {
		return warps.stream()
				.filter(warp -> tileCellX == warp.getDepartureCoords().x && tileCellY == warp.getDepartureCoords().y)
				.findFirst()
				.orElse(null);
	}

	public void doWarpMap(Warp warp) {

		String path = "farm/" + warp.getArrivalMap() + ".tmx";

		tiledMap = new TmxMapLoader().load(path);

		System.out.println(warp);
		tiledMapRenderer.setMap(tiledMap);

	}
}
