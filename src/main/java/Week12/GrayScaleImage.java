package Week12;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;

public class GrayScaleImage extends RecursiveAction {

    private final int row;
    private final BufferedImage image;

    public GrayScaleImage(int row, BufferedImage image) {
        this.row = row;
        this.image = image;
    }

    @Override
    protected void compute() {
        for (int col = 0; col < image.getWidth(); col++) {
            int rgb = image.getRGB(col, row);
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;
            int gray = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
            int grayRGB = (0xFF << 24) | (gray << 16) | (gray << 8) | gray;
            image.setRGB(col, row, grayRGB);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        String imagePath = "src/main/java/Week12/image.jpg";

        BufferedImage img = ImageIO.read(new File(imagePath));
        ForkJoinPool pool = new ForkJoinPool();

        for (int row = 0; row < img.getHeight(); row++) {
            pool.execute(new GrayScaleImage(row, img));
        }
        pool.shutdown();
        while (!pool.isTerminated()) {
            Thread.sleep(5);
        }

        File output = new File("grayscale_output.png");
        ImageIO.write(img, "png", output);
        System.out.println("Grayscale image written to grayscale_output.png");

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(output);
        }
    }
}
