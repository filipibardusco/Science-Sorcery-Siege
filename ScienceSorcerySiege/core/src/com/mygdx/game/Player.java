package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import java.security.Key;

public class Player extends Sprite implements InputProcessor {

    /** the movement velocity */
    private Vector2 velocity = new Vector2();

    private float speed = 4;


    private TiledMapTileLayer collisionLayer;



    public Player(Sprite sprite, TiledMapTileLayer c) {
        super(sprite);
        setSize(64, 64);
        collisionLayer = c;
    }

    public void draw(SpriteBatch spriteBatch) {

        super.draw(spriteBatch);
//        update(Gdx.graphics.getRawDeltaTime());
    }



    public void update(float delta) {


        // save old position
        float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
        boolean collisionX = false, collisionY = false;
        // move on x
        if ((Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.A))){
            System.out.println(getX());
            if(Gdx.input.isKeyPressed(Keys.D)){
                velocity.x = speed;
            } else{
                velocity.x = -speed;
            }
            translateX(velocity.x);
        }
        if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.S)){
            if(Gdx.input.isKeyPressed(Keys.W)){
                velocity.y = speed;
            }else{
                velocity.y = -speed;
            }
            translateY(velocity.y);
        }


/*
        if(velocity.x < 0) { // going left
            // top left
            collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");

            // middle left
            if(!collisionX)
                collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().containsKey("blocked");

            // bottom left
            if(!collisionX)
                collisionX = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
        } else if(velocity.x > 0) { // going right
            // top right
            collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");

            // middle right
            if(!collisionX)
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().containsKey("blocked");

            // bottom right
            if(!collisionX)
                collisionX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
        }

        // react to x collision
        if(collisionX) {
            setX(oldX);
            velocity.x = 0;
        }

        if(velocity.y < 0) { // going down
            // bottom left
            collisionY = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");

            // bottom middle
            if(!collisionY)
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");

            // bottom right
            if(!collisionY)
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");

        } else if(velocity.y > 0) { // going up
            // top left
            collisionY = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");

            // top middle
            if(!collisionY)
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");

            // top right
            if(!collisionY)
                collisionY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");
        }

        // react to y collision
        if(collisionY) {
            setY(oldY);
            velocity.y = 0;
        }

 */

    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }


    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Keys.A:
                velocity.x = -speed;
                break;
            case Keys.D:
                velocity.x = speed;
            case Keys.S:
                velocity.y = -speed;
                break;
            case Keys.W:
                velocity.y = speed;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode) {
            case Keys.A:
            case Keys.D:
                velocity.x = 0;
            case Keys.S:
            case Keys.W:
                velocity.y = 0;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}