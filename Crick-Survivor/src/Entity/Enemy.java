package Entity;

import TileEntity.TileMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public abstract class Enemy extends GameObject{

    protected int health;
    protected int maxHealth;
    protected boolean dead;
    protected int damage;

    protected boolean flinching;
    protected long flinchTimer;
    private BufferedImage[] sprites;

    public Enemy(TileMap tm) {
        super(tm);
    }

    public void loadEnemySprite(String fileName, int frames, int delay) {
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(fileName)
            );

            sprites = new BufferedImage[frames];
            for (int i = 0; i < sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(
                        i * spriteWidth,
                        0,
                        spriteWidth,
                        spriteHeight
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(delay);

        isRight = true;
        isFacingRight = false;
    }

    public boolean isDead() {
        return dead;
    }

    public int getDamage() {
        return damage;
    }

    public void hit(int damage) {
        if (dead || flinching) {
            return;
        }
        health -= damage;
        if (health < 0) {
            health = 0;
        }
        if (health == 0) {
            dead = true;
        }
    }

    public void update() {
    }

}
