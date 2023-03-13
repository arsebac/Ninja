package com.duckies.gdx.ninja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Display data on screen to help debug
 */
public class DebugTile extends Actor {


	private final TiledMapWrapper tiledMap;
	private final Player player;
	private boolean enable;
	private final BitmapFont font;

	private final float tileX;
	private final float tileY;

	public DebugTile(TiledMapWrapper tileMapWrapper, Player player) {
		font = new BitmapFont(Gdx.files.internal("cascadia.fnt"), false);

		this.tileX = 30;
		this.tileY = Gdx.graphics.getHeight() - 50;

		this.tiledMap = tileMapWrapper;

		this.enable = true;

		this.player = player;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		if (!enable) {
			return;
		}

		StringBuilder debugDescription = new StringBuilder().append("(")
						.append(player.getTileCellX())
						.append(",")
						.append(player.getTileCellY())
						.append(") ")
						.append("\n");

		tiledMap.getCellsMatchingCoordinates(player.getTileCellX(), player.getTileCellY()).forEach((layer, cell) -> {
			debugDescription.append("Layer '").append(layer).append("' : ");

			if (cell != null && cell.getTile() != null) {
				debugDescription.append(cell.getTile().getId()).append("\n");
			} else {
				debugDescription.append("null\n");
			}
		});

		font.draw(batch, debugDescription.toString(), tileX, tileY);
	}


	public void switchVisibility() {
		enable = !enable;
	}
}
