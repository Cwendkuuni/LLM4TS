import junit.framework.TestCase;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.GrayImage;
import jigl.image.ops.RealGrayImage;
import jigl.image.ops.ROI;

public class ClipTest {

    @org.junit.Test
    public void testConstructorWithIntMinAndIntMax() {
        Clip clip = new Clip(5, 10);
        assertNotNull(clip);
    }

    @org.junit.Test
    public void testConstructorWithFloatMinAndFloatMax() {
        Clip clip = new Clip(2.5f, 7.5f);
        assertNotNull(clip);
    }

    @org.junit.Test
    public void testApplyWithGrayImageAndIntMinAndIntMax() {
        GrayImage inputImage = new GrayImage(new int[3][3], 3, 3);
        inputImage.set(0, 0, 10);
        inputImage.set(0, 1, 15);
        inputImage.set(0, 2, 7);
        inputImage.set(1, 0, 12);
        inputImage.set(1, 1, 17);
        inputImage.set(1, 2, 4);
        inputImage.set(2, 0, 9);
        inputImage.set(2, 1, 11);
        inputImage.set(2, 2, 6);

        Clip clip = new Clip(5, 10);
        GrayImage outputImage = clip.apply(inputImage);

        // Checking the output image values against expected values.
        for (int y = 0; y < outputImage.Y(); ++y) {
            for (int x = 0; x < outputImage.X(); ++x) {
                int expectedValue = Math.max(Math.min(inputImage.get(x, y), 10), 5);
                int actualValue = outputImage.get(x, y).intValue();
                assertEquals(expectedValue, actualValue);
            }
        }
    }

    @org.junit.Test
    public void testApplyWithRealGrayImageAndFloatMinAndFloatMax() {
        RealGrayImage inputImage = new RealGrayImage(new float[3][3], 3, 3);
        inputImage.set(0, 0, 10.5f);
        inputImage.set(0, 1, 15.8f);
        inputImage.set(0, 2, 7.2f);
        inputImage.set(1, 0, 12.3f);
        inputImage.set(1, 1, 17.6f);
        inputImage.set(1, 2, 4.1f);
        inputImage.set(2, 0, 9.0f);
        inputImage.set(2, 1, 11.0f);
        inputImage.set(2, 2, 6.3f);

        Clip clip = new Clip(5.5f, 10.5f);
        RealGrayImage outputImage = clip.apply(inputImage);

        // Checking the output image values against expected values.
        for (int y = 0; y < outputImage.Y(); ++y) {
            for (int x = 0; x < outputImage.X(); ++x) {
                float expectedValue = Math.max(Math.min(inputImage.get(x, y), 10.5f), 5.5f);
                float actualValue = outputImage.get(x, y).floatValue();
                assertEquals(expectedValue, actualValue, 0.001f);
            }
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithNullImage() {
        Clip clip = new Clip(5, 10);
        clip.apply(null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithNullROI() {
        GrayImage inputImage = new GrayImage(new int[3][3], 3, 3);
        Clip clip = new Clip(5, 10);
        clip.apply(inputImage, null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithNegativeWidthOrHeights() {
        GrayImage inputImage = new GrayImage(new int[3][3], 3, 3);
        ROI roi = new ROI(-1, 0, inputImage.X(), inputImage.Y());
        Clip clip = new Clip(5, 10);
        clip.apply(inputImage, roi);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithROIOutsideImageBounds() {
        GrayImage inputImage = new GrayImage(new int[3][3], 3, 3);
        ROI roi = new ROI(inputImage.X(), 0, inputImage.X(), inputImage.Y());
        Clip clip = new Clip(5, 10);
        clip.apply(inputImage, roi);
    }
}