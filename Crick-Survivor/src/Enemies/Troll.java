package Enemies;

import Entity.*;
import TileEntity.TileMap;
import java.awt.Graphics2D;

public class Troll extends Enemy {

    private final static String SPRITE_FILE_NAME = "/Sprites/Enemies/troll.png";

    public Troll(TileMap tm) {
        super(tm);

        moveSpeed = 0.6;
        maxSpeed = 0.6;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        spriteWidth = 55;
        spriteHeight = 35;
        gameObjectWidth = 20;
        gameObjectHeight = 20;

        health = maxHealth = 2;
        damage = 1;

        // load sprites
        this.loadEnemySprite(SPRITE_FILE_NAME, 12, 150);
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
        // when hits a wall, change direction
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
