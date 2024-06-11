import org.junit.Assert;
import org.junit.Test;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.SimpleOperator;
import jigl.image.ops.GrayImage;
import jigl.image.ops.RealGrayImage;
import jigl.image.ops.ROI;

public class ClipTest {

    @Test
    public void testClipConstructorIntInt() {
        Clip clip = new Clip(1, 10);
        Assert.assertNotNull(clip);
    }

    @Test
    public void testClipConstructorFloatFloat() {
        Clip clip = new Clip(1.5f, 5.5f);
        Assert.assertNotNull(clip);
    }

    @Test
    public void testApplyGrayImageIntMinIntMax() {
        GrayImage image = new GrayImage(new int[3][3]);
        image.set(0, 0, 5);
        image.set(0, 1, 10);
        image.set(0, 2, 15);
        image.set(1, 0, 3);
        image.set(1, 1, 8);
        image.set(1, 2, 12);
        image.set(2, 0, 2);
        image.set(2, 1, 7);
        image.set(2, 2, 11);

        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        Clip clip = new Clip(1, 5);
        GrayImage result = clip.apply(image, roi);

        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.get(0, 0));
        Assert.assertEquals(5, result.get(0, 1));
        Assert.assertEquals(5, result.get(0, 2));
        Assert.assertEquals(5, result.get(1, 0));
        Assert.assertEquals(5, result.get(1, 1));
        Assert.assertEquals(5, result.get(1, 2));
        Assert.assertEquals(5, result.get(2, 0));
        Assert.assertEquals(5, result.get(2, 1));
        Assert.assertEquals(5, result.get(2, 2));
    }

    @Test
    public void testApplyGrayImageIntMinMaxNegative() {
        GrayImage image = new GrayImage(new int[3][3]);
        image.set(0, 0, 5);
        image.set(0, 1, 10);
        image.set(0, 2, 15);
        image.set(1, 0, 3);
        image.set(1, 1, 8);
        image.set(1, 2, 12);
        image.set(2, 0, 2);
        image.set(2, 1, 7);
        image.set(2, 2, 11);

        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        Clip clip = new Clip(-1, 5);
        GrayImage result = clip.apply(image, roi);

        Assert.assertNull(result);
    }

    @Test
    public void testApplyGrayImageIntMinMaxGreaterThanImageSize() {
        GrayImage image = new GrayImage(new int[3][3]);
        image.set(0, 0, 5);
        image.set(0, 1, 10);
        image.set(0, 2, 15);
        image.set(1, 0, 3);
        image.set(1, 1, 8);
        image.set(1, 2, 12);
        image.set(2, 0, 2);
        image.set(2, 1, 7);
        image.set(2, 2, 11);

        ROI roi = new ROI(image.X(), image.Y(), image.X() - 1, image.Y() - 1);

        Clip clip = new Clip(1, 5);
        GrayImage result = clip.apply(image, roi);

        Assert.assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyGrayImageIntMinIntMaxZero() {
        GrayImage image = new GrayImage(new int[3][3]);

        Clip clip = new Clip(0, 0);
        clip.apply(image, new ROI(0, 0, image.X() - 1, image.Y() - 1));
    }

    @Test
    public void testApplyRealGrayImageFloatMinFloatMax() {
        RealGrayImage image = new RealGrayImage(new float[3][3]);
        image.set(0, 0, 5.0f);
        image.set(0, 1, 10.0f);
        image.set(0, 2, 15.0f);
        image.set(1, 0, 3.0f);
        image.set(1, 1, 8.0f);
        image.set(1, 2, 12.0f);
        image.set(2, 0, 2.0f);
        image.set(2, 1, 7.0f);
        image.set(2, 2, 11.0f);

        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        Clip clip = new Clip(1.5f, 5.5f);
        RealGrayImage result = clip.apply(image, roi);

        Assert.assertNotNull(result);
        Assert.assertEquals(1.5f, result.get(0, 0), 0.01f);
        Assert.assertEquals(1.5f, result.get(0, 1), 0.01f);
        Assert.assertEquals(1.5f, result.get(0, 2), 0.01f);
        Assert.assertEquals(1.5f, result.get(1, 0), 0.01f);
        Assert.assertEquals(1.5f, result.get(1, 1), 0.01f);
        Assert.assertEquals(1.5f, result.get(1, 2), 0.01f);
        Assert.assertEquals(1.5f, result.get(2, 0), 0.01f);
        Assert.assertEquals(1.5f, result.get(2, 1), 0.01f);
        Assert.assertEquals(1.5f, result.get(2, 2), 0.01f);
    }

    @Test
    public void testApplyRealGrayImageFloatMinMaxNegative() {
        RealGrayImage image = new RealGrayImage(new float[3][3]);
        image.set(0, 0, 5.0f);
        image.set(0, 1, 10.0f);
        image.set(0, 2, 15.0f);
        image.set(1, 0, 3.0f);
        image.set(1, 1, 8.0f);
        image.set(1, 2, 12.0f);
        image.set(2, 0, 2.0f);
        image.set(2, 1, 7.0f);
        image.set(2, 2, 11.0f);

        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        Clip clip = new Clip(-1.5f, 5.5f);
        RealGrayImage result = clip.apply(image, roi);

        Assert.assertNull(result);
    }

    @Test
    public void testApplyRealGrayImageFloatMinMaxGreaterThanImageSize() {
        RealGrayImage image = new RealGrayImage(new float[3][3]);
        image.set(0, 0, 5.0f);
        image.set(0, 1, 10.0f);
        image.set(0, 2, 15.0f);
        image.set(1, 0, 3.0f);
        image.set(1, 1, 8.0f);
        image.set(1, 2, 12.0f);
        image.set(2, 0, 2.0f);
        image.set(2, 1, 7.0f);
        image.set(2, 2, 11.0f);

        ROI roi = new ROI(image.X() * 2, image.Y() * 2, image.X() - 1, image.Y() - 1);

        Clip clip = new Clip(1.5f, 5.5f);
        RealGrayImage result = clip.apply(image, roi);

        Assert.assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyRealGrayImageFloatMinFloatMaxZero() {
        RealGrayImage image = new RealGrayImage(new float[3][3]);

        Clip clip = new Clip(0.0f, 0.0f);
        clip.apply(image, new ROI(0, 0, image.X() - 1, image.Y() - 1));
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(new int[3][3][3], new int[][]{}, new int[][]{});
        image.setPlane(0, new GrayImage(new int[3][3]));
        image.setPlane(1, new GrayImage(new int[3][3]));
        image.setPlane(2, new GrayImage(new int[3][3]));

        GrayImage expectedGrayImage = new GrayImage(new int[3][3]);
        expectedGrayImage.set(0, 0, 5);
        expectedGrayImage.set(0, 1, 10);
        expectedGrayImage.set(0, 2, 15);
        expectedGrayImage.set(1, 0, 3);
        expectedGrayImage.set(1, 1, 8);
        expectedGrayImage.set(1, 2, 12);
        expectedGrayImage.set(2, 0, 2);
        expectedGrayImage.set(2, 1, 7);
        expectedGrayImage.set(2, 2, 11);

        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        Clip clip = new Clip(1, 5);
        GrayImage resultGrayImage = (GrayImage) clip.apply(image, roi);

        Assert.assertNotNull(resultGrayImage);
        for (int i = 0; i < resultGrayImage.X(); i++) {
            for (int j = 0; j < resultGrayImage.Y(); j++) {
                Assert.assertEquals(expectedGrayImage.get(i, j), resultGrayImage.get(i, j));
            }
        }
    }

    @Test
    public void testMain() {
        String inputFile = "test.jpg";
        float minValue = 1.5f;
        float maxValue = 5.5f;

        try {
            Clip clip = new Clip(minValue, maxValue);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}