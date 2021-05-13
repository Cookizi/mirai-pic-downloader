package top.cookizi.bot.common.utils.image;


import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

/*
 * pHash-like image hash.
 * Author: Elliot Shepherd (elliot@jarofworms.com
 * Based On: http://www.hackerfactor.com/blog/index.php?/archives/432-Looks-Like-It.html
 */
public class ImagePHash {

    private final static int SIZE = 32;
    private final static int SMALLER_SIZE = 8;
    private final static double[] C = initCoefficients();

    private static double[] initCoefficients() {
        double[] get = new double[SIZE];
        for (int i = 1; i < SIZE; i++) {
            get[i] = 1;
        }
        get[0] = 1 / Math.sqrt(2.0);
        return get;
    }


    public static int distance(String s1, String s2) {
        int counter = 0;
        for (int k = 0; k < s1.length(); k++) {
            if (s1.charAt(k) != s2.charAt(k)) {
                counter++;
            }
        }
        return counter;
    }

    public static String getHash(String fileURI) {
        URL url = null;
        try {
            url = new URL(fileURI);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        BufferedImage bufferedImage = null;
        try {
            if (url != null) {
                bufferedImage = ImageIO.read(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getHash(bufferedImage);
    }

    public static String getHash(BufferedImage img) {
        img = ImageUtil.resize(img, SIZE, SIZE);
        grayScale(img);

        double[][] values = new double[SIZE][SIZE];

        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                values[x][y] = getBlue(img, x, y);
            }
        }
        double[][] dctValues = applyDCT(values);
        double total = 0;

        for (int x = 0; x < SMALLER_SIZE; x++) {
            for (int y = 0; y < SMALLER_SIZE; y++) {
                total += dctValues[x][y];
            }
        }
        total -= dctValues[0][0];

        double avg = total / (double) ((SMALLER_SIZE * SMALLER_SIZE) - 1);
        StringBuilder hash = new StringBuilder();
        for (int x = 0; x < SMALLER_SIZE; x++) {
            for (int y = 0; y < SMALLER_SIZE; y++) {
                if (x != 0 && y != 0) {
                    hash.append(dctValues[x][y] > avg ? "1" : "0");
                }
            }
        }
        return hash.toString();
    }

    // Returns a 'binary string' (like. 001010111011100010) which is easy to do
    // a hamming distance on.
    public static String getHash(InputStream is) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getHash(bufferedImage);
    }

    private static ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

    private static void grayScale(BufferedImage img) {
        colorConvert.filter(img, img);
    }

    private static int getBlue(BufferedImage img, int x, int y) {
        return (img.getRGB(x, y)) & 0xff;
    }

    // DCT function stolen from
    // http://stackoverflow.com/questions/4240490/problems-with-dct-and-idct-algorithm-in-java
    private static double[][] applyDCT(double[][] f) {
        int N = SIZE;

        double[][] F = new double[N][N];
        for (int u = 0; u < N; u++) {
            for (int v = 0; v < N; v++) {
                double sum = 0.0;
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        sum += Math.cos(((2 * i + 1) / (2.0 * N)) * u * Math.PI)
                                * Math.cos(((2 * j + 1) / (2.0 * N)) * v * Math.PI) * (f[i][j]);
                    }
                }
                sum *= ((C[u] * C[v]) / 4.0);
                F[u][v] = sum;
            }
        }
        return F;
    }

    /**
     * @param srcUrl
     * @param canUrl
     * @return 值越小相识度越高，10之内可以简单判断这两张图片内容一致
     * @throws Exception
     * @throws
     */
    public static int distance(URL srcUrl, URL canUrl) {
        String imgStr = null;
        String canStr = null;
        try {
            imgStr = getHash(srcUrl.openStream());
            canStr = getHash(canUrl.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return distance(Objects.requireNonNull(imgStr), canStr);
    }

    /**
     * @param srcFile
     * @param canFile
     * @return 值越小相识度越高，10之内可以简单判断这两张图片内容一致
     */
    public static int distance(File srcFile, File canFile) {
        String imageSrcFile = null;
        String imageCanFile = null;
        try {
            imageSrcFile = getHash(new FileInputStream(srcFile));
            imageCanFile = getHash(new FileInputStream(canFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return distance(Objects.requireNonNull(imageSrcFile), imageCanFile);
    }

}