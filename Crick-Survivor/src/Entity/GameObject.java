package Entity;

import Interfaces.Collidable;
import Interfaces.Drawable;
import TileEntity.TileMap;
import TileEntity.Tile;
import java.awt.Rectangle;

public abstract class GameObject implements Drawable, Collidable {

    protected double x;
    protected double y;
    protected double deltaX;
    protected double deltaY;

    protected TileMap tileMap;
    protected int tileSize;
    protected double xMap;
    protected double yMap;

    protected int spriteWidth;
    protected int spriteHeight;

    protected int gameObjectWidth;
    protected int gameObjectHeight;

    protected int currentRow;
    protected int currentCol;
    protected double xDestination;
    protected double yDestination;
    protected double temporaryX;
    protected double temporaryY;
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;

    protected Animation animation;
    protected int currentAction;
    protected int previousAction;
    protected boolean isFacingRight;

    protected boolean isLeft;
    protected boolean isRight;
    protected boolean isUp;
    protected boolean isDown;
    protected boolean isJumping;
    protected boolean isFalling;

    protected double moveSpeed;
    protected double maxSpeed;
    protected double stopSpeed;
    protected double fallSpeed;
    protected double maxFallSpeed;
    protected double jumpStart;
    protected double stopJumpSpeed;

    public GameObject(TileMap tiles) {
        tileMap = tiles;
        tileSize = tiles.getTileSize();
    }

    public boolean hasCollide(GameObject other) {
        return this.getBounds()
                .intersects(other.getBounds());
    }

    public Rectangle getBounds() {
        return new Rectangle(
                (int) x - gameObjectWidth,
                (int) y - gameObjectHeight,
                gameObjectWidth,
                gameObjectHeight
        );
    }

    public void calculateCorners(double x, double y) {
        int leftTile = (int) (x - gameObjectWidth / 2) / tileSize;
        int rightTile = (int) (x + gameObjectWidth / 2 - 1) / tileSize;
        int topTile = (int) (y - gameObjectHeight / 2) / tileSize;
        int bottomTile = (int) (y + gameObjectHeight / 2 - 1) / tileSize;

        int topLeftTile = tileMap.getTileType(topTile, leftTile);
        int topRightTile = tileMap.getTileType(topTile, rightTile);
        int bottomLeftTile = tileMap.getTileType(bottomTile, leftTile);
        int bottomRightTile = tileMap.getTileType(bottomTile, rightTile);

        topLeft = topLeftTile == Tile.BLOCKED;
        topRight = topRightTile == Tile.BLOCKED;
        bottomLeft = bottomLeftTile == Tile.BLOCKED;
        bottomRight = bottomRightTile == Tile.BLOCKED;

    }

    public void checkTileMapCollision() {
        currentCol = (int) x / tileSize;
        currentRow = (int) y / tileSize;

        xDestination = x + deltaX;
        yDestination = y + deltaY;

        temporaryX = x;
        temporaryY = y;

        calculateCorners(x, yDestination);

        if (deltaY < 0) {
            if (topLeft || topRight) {
                deltaY = 0;
                temporaryY = currentRow * tileSize + gameObjectHeight / 2;
            } else {
                temporaryY += deltaY;
            }
        }
        if (deltaY > 0) {
            if (bottomLeft || bottomRight) {
                deltaY = 0;
                isFalling = false;
                temporaryY = (currentRow + 1) * tileSize - gameObjectHeight / 2;
            } else {
                temporaryY += deltaY;
            }
        }

        calculateCorners(xDestination, y);

        if (deltaX < 0) {
            if (topLeft || bottomLeft) {
                deltaX = 0;
                temporaryX = currentCol * tileSize + gameObjectWidth / 2;
            } else {
                temporaryX += deltaX;
            }
        }
        if (deltaX > 0) {
            if (topRight || bottomRight) {
                deltaX = 0;
                temporaryX = (currentCol + 1) * tileSize - gameObjectWidth / 2;
            } else {
                temporaryX += deltaX;
            }
        }

        if (!isFalling) {
            calculateCorners(x, yDestination + 1);
            if (!bottomLeft && !bottomRight) {
                isFalling = true;
            }
        }
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public int getObjectWidth() {
        return gameObjectWidth;
    }

    public int getObjectHeight() {
        return gameObjectHeight;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setVector(double dx, double dy) {
        this.deltaX = dx;
        this.deltaY = dy;
    }

    public void setMapPosition() {
        xMap = tileMap.getX();
        yMap = tileMap.getY();
    }

    public void setLeft(boolean isLeft) {
        this.isLeft = isLeft;
    }

    public void setRight(boolean isRight) {
        this.isRight = isRight;
    }

    public void setUp(boolean isUp) {
        this.isUp = isUp;
    }

    public void setDown(boolean isDown) {
        this.isDown = isDown;
    }

    public void setJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public void draw(java.awt.Graphics2D g) {
        if (isFacingRight) {
            g.drawImage(animation.getImage(),
                    (int) (x + xMap - spriteWidth / 2),
                    (int) (y + yMap - spriteHeight / 2),
                    null
            );
        } else {
            g.drawImage(animation.getImage(),
                    (int) (x + xMap - spriteWidth / 2 + spriteWidth),
                    (int) (y + yMap - spriteHeight / 2),
                    -spriteWidth,
                    spriteHeight,
                    null
            );
        }
    }
}
