package com.duckies.gdx.ninja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * Display data on screen to help debug
 */
public class DebugTile {

	private final TiledMap tiledMap;
	private boolean enable;
	private BitmapFont font;

	private float textX;
	private float textY;

	private int layerWidth;
	private int layerHeight;

	public DebugTile(TiledMap tiledMap) {
		font = new BitmapFont(Gdx.files.internal("cascadia.fnt"), false);

		this.textX = Gdx.graphics.getWidth() / 2f;
		this.textY = Gdx.graphics.getHeight() / 2f;

		this.tiledMap = tiledMap;

		this.enable = true;

		for (int i = 0; i < tiledMap.getLayers().getCount(); i++) {
			if (!(tiledMap.getLayers().get(i) instanceof TiledMapTileLayer)) {
				continue;
			}
			TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(i);
			layerHeight = layer.getTileHeight();
			layerWidth = layer.getTileWidth();
			break;
		}
	}


	public void draw(SpriteBatch batch) {

		if (!enable) {
			return;
		}




		StringBuilder debugDescription = new StringBuilder("(" + (textX/layerHeight) + "," + (textY/layerWidth) + ") " +
				"\n");

		for (int i = 0; i < tiledMap.getLayers().getCount(); i++) {
			if (!(tiledMap.getLayers().get(i) instanceof TiledMapTileLayer)) {
				continue;
			}

			TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(i);
			int x = (int) ((textX / layer.getTileHeight()));
			int y = (int) ((textY / layer.getTileWidth()));
			TiledMapTileLayer.Cell cell = layer.getCell(x, y);
			debugDescription.append("Layer '").append(layer.getName()).append("' : ");
			if (cell != null && cell.getTile() != null) {
				debugDescription.append(cell.getTile().getId()).append("\n");
			} else {
				debugDescription.append("null\n");
			}
		}
		font.draw(batch, debugDescription.toString(), textX - Gdx.graphics.getWidth() / 2f + 50,
				textY + Gdx.graphics.getHeight() / 2f - 50);

	}

	public void translate(float speedX, float speedY) {
		this.textX += speedX;
		this.textY += speedY;
	}

	public void switchVisibility() {
		enable = !enable;
	}
}
