import org.junit.Test;
import jigl.image.ops.levelOps.Clip;
import jigl.image.*;
import static org.junit.Assert.*;

public class ClipTest {
    @Test
    public void testApplyGrayImage() {
        int min = 10;
        int max = 20;
        Clip clip = new Clip(min, max);
        GrayImage image = new GrayImage(5, 5);
        for (int i = 0; i < image.X(); i++) {
            for (int j = 0; j < image.Y(); j++) {
                image.set(i, j, (short) (i + j));
            }
        }
        GrayImage expected = new GrayImage(image.X(), image.Y());
        for (int i = 0; i < image.X(); i++) {
            for (int j = 0; j < image.Y(); j++) {
                short value = image.get(i, j);
                short result = clip.apply(image, new ROI(i, j, 1, 1)).get(0, 0).getValue().shortValue();
                assertEquals(Math.min(value, max), result);
                assertEquals(Math.max(value, min), result);
                image.set(i, j, result);
            }
        }
        assertEquals(image, clip.apply(image));
    }
}