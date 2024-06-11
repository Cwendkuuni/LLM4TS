import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.GrayImage;
import jigl.image.types.ROI;

public class ClipTest {

    @Test
    public void testClipGrayImage() {
        // Setup
        GrayImage image = new GrayImage(new int[5][5]);
        int[][] pixelValues = {
                {10, 20, 30, 40, 50},
                {60, 70, 80, 90, 100},
                {110, 120, 130, 140, 150},
                {160, 170, 180, 190, 200},
                {210, 220, 230, 240, 250}
        };
        image.setPixels(pixelValues);

        Clip clip = new Clip(50.0f, 150.0f);
        ROI roi = new ROI(1, 1, 3, 3);

        // Exercise
        GrayImage expected = new GrayImage(new int[3][3]);
        int[][] expectedPixelValues = {
                {50, 50, 50},
                {50, 80, 50},
                {50, 110, 50}
        };
        expected.setPixels(expectedPixelValues);

        GrayImage result = (GrayImage) clip.apply(image, roi);

        // Verify
        assertArrayEquals(expectedPixelValues, result.getPixels());
    }
}