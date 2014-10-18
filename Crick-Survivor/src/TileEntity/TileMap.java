package TileEntity;

import Interfaces.Drawable;
import java.awt.*;
import java.awt.image.*;

import java.io.*;
import javax.imageio.ImageIO;

import Main.GamePanel;

public class TileMap implements Drawable {

    // position
    private double x;
    private double y;

    // bounds
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;

    // map
    private int[][] map;
    private int tileSize;
    private int mapRows;
    private int mapCols;
    private int mapWidth;
    private int mapHeight;
    private static final int TILE_SET_ROWS = 2;
//TODO : create the .gif file with the tiles.
    //The first row(row=0) will be the NORMAL tiles, that means that the player 
    //can move through it. The second row(row=1) will be the BLOCKED tiles, 
    //so the player can intersects with it.

    private BufferedImage tileset;
    private int TilesCount;
    private Tile[][] tiles;

    private int numberOfRowsToDraw;
    private int numberOfColsToDraw;
    private int rowOffset;
    private int colOffset;

    public TileMap(int tileSize) {
        this.tileSize = tileSize;
        numberOfRowsToDraw = GamePanel.HEIGHT / tileSize + 3;
        numberOfColsToDraw = GamePanel.WIDTH / tileSize + 3;

    }

    public void loadTiles(String s) {
        try {
            tileset = ImageIO.read(getClass()
                    .getResourceAsStream(s)
            );
            //All tiles in the .gif file
            TilesCount = tileset.getWidth() / tileSize;
            tiles = new Tile[TILE_SET_ROWS][TilesCount];

            BufferedImage subimage;
            for (int col = 0; col < TilesCount; col++) {
                subimage = tileset.getSubimage(
                        col * tileSize,
                        0,
                        tileSize,
                        tileSize
                );

                tiles[0][col] = new Tile(subimage, Tile.NORMAL);
                subimage = tileset.getSubimage(
                        col * tileSize,
                        tileSize,
                        tileSize,
                        tileSize
                );
                tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String s) {
        try {
            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(in));

            mapCols = Integer.parseInt(bufferedReader.readLine());
            mapRows = Integer.parseInt(bufferedReader.readLine());
            map = new int[mapRows][mapCols];
            mapWidth = mapCols * tileSize;
            mapHeight = mapRows * tileSize;

            xmin = GamePanel.WIDTH - mapWidth;
            xmax = 0;
            ymin = GamePanel.HEIGHT - mapHeight;
            ymax = 0;

            String splitDelimiter = "\\s+";
            for (int row = 0; row < mapRows; row++) {
                String line = bufferedReader.readLine();
                String[] tokens = line.split(splitDelimiter);

                for (int col = 0; col < mapCols; col++) {
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTileSize() {
        return tileSize;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getTileType(int row, int col) {
        int mapValue = map[row][col];
        int rowOfTile = getRowOfTile(mapValue);
        int colOfTile = getColOfTile(mapValue);

        return tiles[rowOfTile][colOfTile].getType();
    }

    private int getColOfTile(int mapValue) {
        int colOfTile = mapValue % TilesCount;
        return colOfTile;
    }

    private int getRowOfTile(int mapValue) {
        int rowOfTile = mapValue / TilesCount;
        return rowOfTile;
    }

    public void setPosition(double x, double y) {
        this.x += (x - this.x);
        this.y += (y - this.y);

        fixBounds();

        colOffset = (int) -this.x / tileSize;
        rowOffset = (int) -this.y / tileSize;
    }

    private void fixBounds() {
        if (x < xmin) {
            x = xmin;
        }
        if (y < ymin) {
            y = ymin;
        }
        if (x > xmax) {
            x = xmax;
        }
        if (y > ymax) {
            y = ymax;
        }
    }
    
    public void draw(Graphics2D g) {
        for (int row = rowOffset;
                row < rowOffset + numberOfRowsToDraw;
                row++) {

            if (row >= mapRows) {
                break;
            }

            for (int col = colOffset;
                    col < colOffset + numberOfColsToDraw;
                    col++) {

                if (col >= mapCols) {
                    break;
                }

                if (map[row][col] == 0) {
                    continue;
                }

                int mapValue = map[row][col];
                int rowOfTile = getRowOfTile(mapValue);
                int colOfTile = getColOfTile(mapValue);

                g.drawImage(
                        tiles[rowOfTile][colOfTile].getImage(),
                        (int) x + col * tileSize,
                        (int) y + row * tileSize,
                        null
                );
            }
        }
    }
}
