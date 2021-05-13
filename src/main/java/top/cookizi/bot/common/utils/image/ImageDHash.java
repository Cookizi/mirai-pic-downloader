package top.cookizi.bot.common.utils.image;


import java.awt.image.BufferedImage;

/**
 * @author ：zwy
 * @date ：Created in 2019/8/26 19:11
 */
public class ImageDHash {

    private static final int WIDTH = 9;
    private static final int HEIGHT = 8;


    public static String getDHash(BufferedImage bufferedImage) {
        BufferedImage image = ImageUtil.resize(bufferedImage, WIDTH, HEIGHT);
        int[][] grayScalePixel = ImageUtil.garyScale(image);
        int[][] hashArray = new int[WIDTH - 1][HEIGHT];
        for (int i = 1; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (grayScalePixel[i][j] >= grayScalePixel[i - 1][j]) {
                    hashArray[i - 1][j] = 1;
                } else {
                    hashArray[i - 1][j] = 0;
                }
            }
        }
        StringBuilder resultBuilder = new StringBuilder();
        StringBuilder tempBuilder = new StringBuilder();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tempBuilder.append(hashArray[i][j]);
            }
            String temp = Integer.toHexString(Integer.parseInt(tempBuilder.toString(), 2));
            if (temp.length() < 2) {
                temp = 0 + temp;
            }
            resultBuilder.append(temp);
            tempBuilder = new StringBuilder();
        }
        return resultBuilder.toString();
    }
}