package Entity;

import TileEntity.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends GameObject {

    private int health;
    private int maxHealth;
    private int fire;
    private int maxFire;
    private boolean dead;
    private boolean hasHammerAtack;

    private boolean firing;
    private int fireCost;
    private int fireBallDamage;
    private ArrayList<FireBall> fireBalls;

    // hammer atack
    private boolean hammerHit;
    private int hammerDamage;
    private int hammerRange;

    // fly
    private boolean fly;

    // sprite frames
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
        1, 6, 6, 1, 4, 7, 2
    };

    // sprite actions
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int FLY = 4;
    private static final int FIREBALL = 5;
    private static final int HAMMER_HIT = 6;

    public Player(TileMap tm) {
        super(tm);

        spriteWidth = 55;
        spriteHeight = 55;
        gameObjectWidth = 25;
        gameObjectHeight = 25;

        moveSpeed = 0.3;
        maxSpeed = 1.4;
        stopSpeed = 0.5;
        fallSpeed = 0.20;
        maxFallSpeed = 5.0;
        jumpStart = -5.8;
        stopJumpSpeed = 0.3;

        isFacingRight = false;

        health = maxHealth = 7;
        fire = maxFire = 3500;

        fireCost = 100;
        fireBallDamage = 5;
        fireBalls = new ArrayList<FireBall>();

        hammerDamage = 8;
        hammerRange = 40;

        // load sprites
        try {

            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Player/crick.png"
                    )
            );

            sprites = new ArrayList<BufferedImage[]>();
            for (int i = 0; i < 7; i++) {
                BufferedImage[] bi
                        = new BufferedImage[numFrames[i]];

                for (int j = 0; j < numFrames[i]; j++) {
                    if (i != HAMMER_HIT) {
                        bi[j] = spritesheet.getSubimage(j * spriteWidth,
                                i * spriteHeight,
                                spriteWidth,
                                spriteHeight
                        );
                    } else {
                        bi[j] = spritesheet.getSubimage(j * (spriteWidth * 3) / 2,
                                i * spriteHeight,
                                (spriteWidth * 3) / 2,
                                spriteHeight
                        );
                    }
                }

                sprites.add(bi);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(500);
    }

    public int getHealth() {
        return this.health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getFire() {
        return fire;
    }

    public int getMaxFire() {
        return maxFire;
    }

    public void setFiring() {
        firing = true;
    }

    public void setHammerHit() {
        hammerHit = true;
    }

    public void setFly(boolean b) {
        fly = b;
    }

    public void checkAttack(ArrayList<Enemy> enemies) {
        // enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            // hammer attack
            if (hammerHit) {
                if (isFacingRight) {
                    if (e.getX() > x
                            && e.getX() < x + hammerRange
                            && e.getY() > y - spriteHeight / 2
                            && e.getY() < y + spriteHeight / 2) {
                        e.hit(hammerDamage);
                    }
                } else {
                    if (e.getX() < x
                            && e.getX() > x - hammerRange
                            && e.getY() > y - spriteHeight / 2
                            && e.getY() < y + spriteHeight / 2) {
                        e.hit(hammerDamage);
                    }
                }
            }

            for (int j = 0; j < fireBalls.size(); j++) {
                if (fireBalls.get(j).hasCollide(e)) {
                    e.hit(fireBallDamage);
                    fireBalls.get(j).setHit();
                    break;
                }
            }

            if (hasCollide(e)) {
                hit(e.getDamage());
            }
        }
    }

    public void hit(int damage) {
        if (hasHammerAtack) {
            return;
        }
        health -= damage;
        if (health < 0) {
            health = 0;
        }
        if (health == 0) {
            dead = true;
        }
        hasHammerAtack = true;
    }

    private void getNextPosition() {
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
        } else {
            if (deltaX > 0) {
                deltaX -= stopSpeed;
                if (deltaX < 0) {
                    deltaX = 0;
                }
            } else if (deltaX < 0) {
                deltaX += stopSpeed;
                if (deltaX > 0) {
                    deltaX = 0;
                }
            }
        }

        if ((currentAction == HAMMER_HIT ||
                currentAction == FIREBALL)
                    && !(isJumping || isFalling)) {
            deltaX = 0;
        }

        if (isJumping && !isFalling) {
            deltaY = jumpStart;
            isFalling = true;
        }

        if (isFalling) {
            if (deltaY > 0 && fly) {
                deltaY += fallSpeed * 0.1;
            } else {
                deltaY += fallSpeed;
            }

            if (deltaY > 0) {
                isJumping = false;
            }
            if (deltaY < 0 && !isJumping) {
                deltaY += stopJumpSpeed;
            }

            if (deltaY > maxFallSpeed) {
                deltaY = maxFallSpeed;
            }
        }
    }

    public void update() {
        getNextPosition();
        checkTileMapCollision();
        setPosition(temporaryX, temporaryY);

        if (currentAction == HAMMER_HIT) {
            if (animation.hasPlayedOnce()) {
                hammerHit = false;
            }
        }
        if (currentAction == FIREBALL) {
            if (animation.hasPlayedOnce()) {
                firing = false;
            }
        }

        fire += 1;
        if (fire > maxFire) {
            fire = maxFire;
        }
        if (firing && currentAction != FIREBALL) {
            if (fire > fireCost) {
                fire -= fireCost;
                FireBall fb = new FireBall(tileMap, isFacingRight);
                fb.setPosition(x, y);
                fireBalls.add(fb);
            }
        }

        for (int i = 0; i < fireBalls.size(); i++) {
            fireBalls.get(i).update();
            if (fireBalls.get(i).shouldRemove()) {
                fireBalls.remove(i);
                i--;
            }
        }

        if (hasHammerAtack) {
            long elapsed
                    = System.nanoTime() / 1000000;
            if (elapsed > 1000) {
                hasHammerAtack = false;
            }
        }

        if (hammerHit) {
            if (currentAction != HAMMER_HIT) {
                currentAction = HAMMER_HIT;
                animation.setFrames(sprites.get(HAMMER_HIT));
                animation.setDelay(50);
                spriteWidth = 60;
            }
        } else if (firing) {
            if (currentAction != FIREBALL) {
                currentAction = FIREBALL;
                animation.setFrames(sprites.get(FIREBALL));
                animation.setDelay(100);
            }
        } else if (deltaY > 0) {
            if (fly) {
                if (currentAction != FLY) {
                    currentAction = FLY;
                    animation.setFrames(sprites.get(FLY));
                    animation.setDelay(100);
                }
            } else if (currentAction != FALLING) {
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
            }
        } else if (deltaY < 0) {
            if (currentAction != JUMPING) {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
            }
        } else if (isLeft || isRight) {
            if (currentAction != WALKING) {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
            }
        } else {
            if (currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
            }
        }

        animation.update();

        // direction
        if (currentAction != HAMMER_HIT && currentAction != FIREBALL) {
            if (isRight) {
                isFacingRight = false;
            }
            if (isLeft) {
                isFacingRight = true;
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        setMapPosition();

        for (int i = 0; i < fireBalls.size(); i++) {
            fireBalls.get(i).draw(g);
        }

        if (hasHammerAtack) {
            long elapsed
                    = System.nanoTime() / 1000000;
            if (elapsed / 100 % 2 == 0) {
                return;
            }
        }

        super.draw(g);
    }
}
