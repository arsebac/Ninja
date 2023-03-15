package com.duckies.gdx.ninja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class PositionHelpers {
	/**
	 * Compute a Vector between the middle of the screen and
	 * @param screenX
	 * @param screenY
	 * @return
	 */
	public static Vector2 getClickPosition(int screenX, int screenY) {

		// Compute difference between middle of the screen and touch point
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		return new Vector2(screenX - w / 2, (h - screenY) - h / 2);
	}

	public static Vector2 getClickedCase(Vector2 click, Vector2 camera, Vector2 player, Vector2 mapTileSize) {
		Vector2 diffInCase = new Vector2( - click.x / mapTileSize.x, - click.y / mapTileSize.y);

		if (player.x != camera.x || player.y != camera.y) {

			diffInCase.x += (player.x - camera.x) / mapTileSize.x;
			diffInCase.y += (player.y - camera.y) / mapTileSize.y;
		}

		float newX = player.x / mapTileSize.x - diffInCase.x;
		float newY = player.y / mapTileSize.y  - diffInCase.y;

		return new Vector2(newX, newY);
	}

	public static Vector2 getCameraPosition(OrthographicCamera camera) {
		return new Vector2(camera.position.x, camera.position.y);
	}

	public static void updateCameraTranslationForBorders(Vector2 translation, Vector2 camera, Vector2 player,
														 Vector2 mapTileSize, Vector2 mapTileCellSize) {

		int halfScreenWidth = Gdx.graphics.getWidth() / 2;


		boolean lowerBoundTouchedInX = camera.x + translation.x < halfScreenWidth;
		boolean upperBoundTouchedInX = camera.x + translation.x > mapTileSize.x * mapTileCellSize.x - halfScreenWidth;

		int halfScreenHeight = Gdx.graphics.getHeight() / 2;

		boolean lowerBoundTouchedInY = camera.y + translation.y < halfScreenHeight;
		boolean upperBoundTouchedInY = camera.y + translation.y > mapTileSize.y * mapTileCellSize.y - halfScreenHeight;

		// There is a bound touched

		if (lowerBoundTouchedInX && translation.x < 0) {
			translation.x = 0;
		} else if (upperBoundTouchedInX && translation.x > 0) {
			translation.x = 0;
		} else if (lowerBoundTouchedInY && translation.y < 0) {
			translation.y = 0;
		} else if (upperBoundTouchedInY && translation.y > 0) {
			translation.y = 0;
		}


		// Check if camera need to stay here and wait for player to go further
		if (Math.abs(camera.x + translation.x - player.x) > 0.1) {
			translation.x = 0;
		}

		if (Math.abs(camera.y + translation.y - player.y) > 0.1) {
			translation.y = 0;
		}
	}
}
