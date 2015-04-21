package nz.ac.aucklanduni.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class ImageSplitter {

    /**
     *  Splits image based on fixed tile width and tile height
     *
     * @param inputFilePath
     * @param outputPath
     * @param tileWidth
     * @param tileHeight
     * @throws java.io.IOException
     */
    public static void splitImageBySize(String inputFilePath, String outputPath, int tileWidth, int tileHeight) throws IOException {
        File file = new File(inputFilePath);
        FileInputStream fis = new FileInputStream(file);
        BufferedImage image = ImageIO.read(fis); //reading the image file

        int rows = (int)(Math.ceil((float)image.getHeight() / tileHeight));
        int cols = (int)(Math.ceil((float)image.getWidth() / tileWidth));
        int chunks = rows * cols;

        int count = 0;
        BufferedImage imgs[] = new BufferedImage[chunks]; //Image array to hold image chunks

        int width;
        int height;
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {

                //Initialize the image array with image chunks
                if(x < rows - 1 && y < cols - 1) {
                    width = tileWidth;
                    height = tileHeight;
                } else if (x < rows -1 && y >= cols -1) {
                    width = image.getWidth() - y * tileWidth;
                    height = tileHeight;
                } else if (x >= rows -1 && y < cols -1) {
                    width = tileWidth;
                    height = image.getHeight() - x * tileHeight;
                } else {
                    width = image.getWidth() - y * tileWidth;
                    height = image.getHeight() - x * tileHeight;
                }

                imgs[count] = new BufferedImage(width, height, image.getType());


                // draws the image chunk
                Graphics2D gr = imgs[count++].createGraphics();
                gr.drawImage(image, 0, 0, width, height, tileWidth * y, tileHeight * x, tileWidth * y + width, tileHeight * x + height, null);
                gr.dispose();
            }
        }

        //writing mini images into image files
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                ImageIO.write(imgs[x * cols + y], "png", new File(outputPath + "img_" + y + "_" + x + ".png"));
            }
        }
    }

    /**
     * Split image based on fixed chunk rows and columns
     *
     * @param filePath
     * @param columns
     * @param rows
     * @throws IOException
     */
    public static void splitImageByChunk(String filePath, int columns, int rows) throws IOException {
        // TODO
    }
}