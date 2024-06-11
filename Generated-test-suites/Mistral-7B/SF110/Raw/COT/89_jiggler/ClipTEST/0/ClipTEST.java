import junit.framework.TestCase;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.GrayImage;
import jigl.image.ops.ROI;
import jigl.image.types.FloatType;
import jigl.image.types.IntegerType;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

public class ClipTest {

    @org.junit.Test
    public void testClipIntConstructor() {
        Clip clip = new Clip(5, 10);
        assertNotNull(clip);
    }

    @org.junit.Test
    public void testClipIntMethods() {
        Clip clip = new Clip(5, 10);
        GrayImage image = new GrayImage(new IntegerType(5, 5), new ROI(0, 0, 5, 5));

        clip.apply(image);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                int value = image.get(x, y).intValue();
                assertTrue(value >= 5 && value <= 10);
            }
        }
    }

    @org.junit.Test
    public void testClipIntMinMaxEqualZero() {
        Clip clip = new Clip(0, 0);
        GrayImage image = new GrayImage(new IntegerType(5, 5), new ROI(0, 0, 5, 5));

        clip.apply(image);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                int value = image.get(x, y).intValue();
                assertEquals(0, value);
            }
        }
    }

    @org.junit.Test
    public void testClipFloatConstructor() {
        Clip clip = new Clip(2.5f, 7.5f);
        assertNotNull(clip);
    }

    @org.junit.Test
    public void testClipFloatMethods() {
        Clip clip = new Clip(2.5f, 7.5f);
        RealGrayImage image = new RealGrayImage(new FloatType(5, 5), new ROI(0, 0, 5, 5));

        clip.apply(image);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                float value = image.get(x, y).floatValue();
                assertTrue(value >= 2.5f && value <= 7.5f);
            }
        }
    }

    @org.junit.Test
    public void testClipMinMaxZero() {
        Clip clip = new Clip(0.0f, 0.0f);
        RealGrayImage image = new RealGrayImage(new FloatType(5, 5), new ROI(0, 0, 5, 5));

        clip.apply(image);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                float value = image.get(x, y).floatValue();
                assertEquals(0.0f, value, 0.001f);
            }
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testClipNullImage() {
        Clip clip = new Clip(2.5f, 7.5f);
        GrayImage image = null;

        clip.apply(image);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testClipNullROI() {
        Clip clip = new Clip(2.5f, 7.5f);
        ROI roi = null;

        clip.apply(new GrayImage(new IntegerType(5, 5), new ROI(0, 0, 5, 5)), roi);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testClipInvalidROI() {
        Clip clip = new Clip(2.5f, 7.5f);
        ROI roi = new ROI(-1, 0, 5, 5);

        clip.apply(new GrayImage(new IntegerType(5, 5), new ROI(0, 0, 5, 5)), roi);
    }

    @org.junit.Test
    public void testMain() throws Exception {
        String inputFile = "testImage.bmp";
        String outputFile = "outputImage.bmp";

        Image image = new GrayImage(new IntegerType(5, 5), new ROI(0, 0, 5, 5));
        Image image2 = null;
        ImageInputStream is = new ImageInputStream(inputFile);
        ImageOutputStream os = new ImageOutputStream(outputFile);

        Clip clip = new Clip(2.5f, 7.5f);
        image2 = clip.apply(image);

        is.read();
        os.write(image2);
        is.close();
        os.close();
    }
}