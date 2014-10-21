package Enemies;

import Entity.*;
import TileEntity.TileMap;
import java.awt.Graphics2D;

public class Drill extends Enemy {

    private final static String SPRITE_FILE_NAME = "/Sprites/Enemies/pumpal.png";

    public Drill(TileMap tm) {
        super(tm);
        moveSpeed = 0.4;
        maxSpeed = 5.6;
        fallSpeed = 0.4;
        maxFallSpeed = 10.0;

        spriteWidth = 63;
        spriteHeight = 55;
        gameObjectWidth = 20;
        gameObjectHeight = 20;

        health = maxHealth = 3;
        damage = 2;
        this.loadEnemySprite(SPRITE_FILE_NAME, 10, 300);
    }

    public void getNextPosition() {
        if (isLeft) {
            deltaX -= moveSpeed;
            if (deltaX < -maxSpeed) {
                deltaX = -maxSpeed;
            }
        } else if (isRight) {
            deltaX += moveSpeed;
            if (deltaX > maxSpeed) {
                deltaX = maxSpeed;
            }
        }

        if (isFalling) {
            deltaY += fallSpeed;
        }
    }

    public void update() {
        getNextPosition();
        checkTileMapCollision();
        setPosition(temporaryX, temporaryY);

        if (isRight && deltaX == 0) {
            isRight = false;
            isLeft = true;
            isFacingRight = true;
        } else if (isLeft && deltaX == 0) {
            isRight = true;
            isLeft = false;
            isFacingRight = false;
        }

        animation.update();
    }

    public void draw(Graphics2D g) {
        setMapPosition();

        super.draw(g);
    }
}
