import static org.junit.Assert.*;
import jigl.image.*;
import jigl.image.ops.*;
import jigl.image.types.*;
import org.junit.Test;

public class ClipTest {

    @Test
    public void testClipWithIntMinAndIntMax() {
        // Test code goes here (same as Expert 1)
    }

    @Test
    public void testClipWithFloatMinAndFloatMax() {
        // Test code goes here (same as Expert 2)
    }

    @Test
    public void testApplyGrayImageWithIntMinAndIntMax() {
        GrayImage image = ImageFactory.createGrayImage(2, 2);
        image.set(0, 0, 15);
        image.set(0, 1, 7);
        image.set(1, 0, 12);
        image.set(1, 1, 11);
        Clip clip = new Clip(5, 10);
        GrayImage expected = ImageFactory.createGrayImage(2, 2);
        expected.set(0, 0, 10);
        expected.set(0, 1, 7);
        expected.set(1, 0, 5);
        expected.set(1, 1, 5);
        assertEquals(expected, clip.apply(image));
    }

    @Test
    public void testApplyGrayImageWithMinGreaterThanMax() {
        GrayImage image = ImageFactory.createGrayImage(2, 2);
        image.set(0, 0, 15);
        image.set(0, 1, 7);
        image.set(1, 0, 12);
        image.set(1, 1, 11);
        Clip clip = new Clip(10, 5);
        // Expected result should be the original image
        GrayImage expected = image;
        assertEquals(expected, clip.apply(image));
    }

    @Test
    public void testApplyRealGrayImageWithFloatMinAndFloatMax() {
        RealGrayImage image = ImageFactory.createRealGrayImage(2, 2);
        image.set(0, 0, 15.5f);
        image.set(0, 1, 7.2f);
        image.set(1, 0, 12.1f);
        image.set(1, 1, 11.3f);
        Clip clip = new Clip(5.5f, 10.5f);
        RealGrayImage expected = ImageFactory.createRealGrayImage(2, 2);
        expected.set(0, 0, 10.5f);
        expected.set(0, 1, 7.2f);
        expected.set(1, 0, 5.5f);
        expected.set(1, 1, 5.5f);
        assertEquals(expected, clip.apply(image));
    }

    @Test
    public void testApplyRealGrayImageWithMinGreaterThanMax() {
        RealGrayImage image = ImageFactory.createRealGrayImage(2, 2);
        image.set(0, 0, 15.5f);
        image.set(0, 1, 7.2f);
        image.set(1, 0, 12.1f);
        image.set(1, 1, 11.3f);
        Clip clip = new Clip(10.5f, 5.5f);
        // Expected result should be the original image
        RealGrayImage expected = image;
        assertEquals(expected, clip.apply(image));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testApplyNullImage() {
        Clip clip = new Clip(5, 10);
        GrayImage image = null;
        clip.apply(image);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testApplyNullROI() {
        GrayImage image = ImageFactory.createGrayImage(2, 2);
        Clip clip = new Clip(5, 10);
        ROI roi = null;
        clip.apply(image, roi);
    }
}