package com.duckies.gdx.ninja;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class OrthogonalTiledMapRendererWithSprites extends OrthogonalTiledMapRenderer {


    public OrthogonalTiledMapRendererWithSprites(TiledMap map, SpriteBatch spriteBatch) {
        super(map);
    }

    @Override
    public void renderObject(MapObject object) {
        if (object instanceof TextureMapObject) {
            TextureMapObject textureObj = (TextureMapObject) object;
            batch.draw(textureObj.getTextureRegion(), textureObj.getX(), textureObj.getY());
        }
    }
}