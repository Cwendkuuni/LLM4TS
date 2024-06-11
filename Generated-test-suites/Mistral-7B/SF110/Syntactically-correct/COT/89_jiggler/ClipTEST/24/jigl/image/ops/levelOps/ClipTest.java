import junit.framework.TestCase;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.SimpleOperator;
import jigl.image.ops.GrayImage;
import jigl.image.ops.RealGrayImage;
import jigl.image.ops.ROI;
import jigl.image.Image;
import jigl.image.types.RGB;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

public class ClipTest extends TestCase {

    // Test constructor with integer values
    public void testClipConstructorInt() {
        Clip clip = new Clip(1, 5);
        assertNotNull(clip);
    }

    // Test constructor with float values
    public void testClipConstructorFloat() {
        Clip clip = new Clip(2.5f, 6.5f);
        assertNotNull(clip);
    }

    // Test apply method with GrayImage and integer ROI
    public void testApplyGrayImageIntROI() {
        GrayImage image = new GrayImage(2, 2);
        image.setPixel(0, 0, 127);
        image.setPixel(0, 1, 153);
        image.setPixel(1, 0, 112);
        image.setPixel(1, 1, 88);

        Clip clip = new Clip(0, 128);
        Image result = clip.apply(image);

        GrayImage expectedResult = new GrayImage(2, 2);
        expectedResult.setPixel(0, 0, (byte) 127);
        expectedResult.setPixel(0, 1, (byte) 128);
        expectedResult.setPixel(1, 0, (byte) 112);
        expectedResult.setPixel(1, 1, (byte) 128);

        assertEquals(expectedResult, result);
    }

    // Test apply method with GrayImage and float ROI
    public void testApplyGrayImageFloatROI() {
        GrayImage image = new GrayImage(2, 2);
        image.setPixel(0, 0, 127);
        image.setPixel(0, 1, 153);
        image.setPixel(1, 0, 112);
        image.setPixel(1, 1, 88);

        Clip clip = new Clip(0.5f, 1.5f);
        Image result = clip.apply(image);

        GrayImage expectedResult = new GrayImage(2, 2);
        expectedResult.setPixel(0, 0, (byte) 127);
        expectedResult.setPixel(0, 1, (byte) 127);
        expectedResult.setPixel(1, 0, (byte) 112);
        expectedResult.setPixel(1, 1, (byte) 112);

        assertEquals(expectedResult, result);
    }

    // Test apply method with RealGrayImage and integer ROI
    public void testApplyRealGrayImageIntROI() {
        RealGrayImage image = new RealGrayImage(2, 2);
        image.setPixel(0, 0, 0.127);
        image.setPixel(0, 1, 0.153);
        image.setPixel(1, 0, 0.112);
        image.setPixel(1, 1, 0.088);

        Clip clip = new Clip(0, 1.0);
        Image result = clip.apply(image);

        RealGrayImage expectedResult = new RealGrayImage(2, 2);
        expectedResult.setPixel(0, 0, 0.127);
        expectedResult.setPixel(0, 1, 0.1);
        expectedResult.setPixel(1, 0, 0.112);
        expectedResult.setPixel(1, 1, 0.1);

        assertEquals(expectedResult, result);
    }

    // Test apply method with RealGrayImage and float ROI
    public void testApplyRealGrayImageFloatROI() {
        RealGrayImage image = new RealGrayImage(2, 2);
        image.setPixel(0, 0, 0.127);
        image.setPixel(0, 1, 0.153);
        image.setPixel(1, 0, 0.112);
        image.setPixel(1, 1, 0.088);

        Clip clip = new Clip(0.5f, 0.5f);
        Image result = clip.apply(image);

        RealGrayImage expectedResult = new RealGrayImage(2, 2);
        expectedResult.setPixel(0, 0, 0.127);
        expectedResult.setPixel(0, 1, 0.127);
        expectedResult.setPixel(1, 0, 0.112);
        expectedResult.setPixel(1, 1, 0.112);

        assertEquals(expectedResult, result);
    }

    // Test apply method with ColorImage and integer ROI
    public void testApplyColorImageIntROI() {
        ColorImage image = new ColorImage(2, 2, new RGB(255, 255, 255), new RGB(0, 0, 0), 3);
        image.setPixel(0, 0, new RGB(127, 127, 127));
        image.setPixel(0, 1, new RGB(153, 153, 153));
        image.setPixel(1, 0, new RGB(112, 112, 112));
        image.setPixel(1, 1, new RGB(88, 88, 88));

        Clip clip = new Clip(0, 128);
        Image result = clip.apply(image);

        ColorImage expectedResult = new ColorImage(2, 2, new RGB(255, 255, 255), new RGB(0, 0, 0), 3);
        expectedResult.setPixel(0, 0, new RGB(127, 127, 127));
        expectedResult.setPixel(0, 1, new RGB(128, 128, 128));
        expectedResult.setPixel(1, 0, new RGB(112, 112, 112));
        expectedResult.setPixel(1, 1, new RGB(128, 128, 128));

        assertEquals(expectedResult, result);
    }

    // Test apply method with ColorImage and float ROI
    public void testApplyColorImageFloatROI() {
        ColorImage image = new ColorImage(2, 2, new RGB(255, 255, 255), new RGB(0, 0, 0), 3);
        image.setPixel(0, 0, new RGB(127, 127, 127));
        image.setPixel(0, 1, new RGB(153, 153, 153));
        image.setPixel(1, 0, new RGB(112, 112, 112));
        image.setPixel(1, 1, new RGB(88, 88, 88));

        Clip clip = new Clip(0.5f, 0.5f);
        Image result = clip.apply(image);

        ColorImage expectedResult = new ColorImage(2, 2, new RGB(255, 255, 255), new RGB(0, 0, 0), 3);
        expectedResult.setPixel(0, 0, new RGB(127, 127, 127));
        expectedResult.setPixel(0, 1, new RGB(127, 127, 127));
        expectedResult.setPixel(1, 0, new RGB(112, 112, 112));
        expectedResult.setPixel(1, 1, new RGB(112, 112, 112));

        assertEquals(expectedResult, result);
    }

    // Test main method with valid arguments
    public void testMainMethodValidArguments() {
        String inputFile = "testImage.bmp";
        float minValue = 1.0f;
        float maxValue = 2.0f;
        String outputFile = "outputImage.bmp";

        Clip.main(new String[]{"1.0", "2.0", inputFile, outputFile});
    }

    // Test main method with null image input
    public void testMainMethodNullImageInput() {
        try {
            String inputFile = "testImage.bmp";
            float minValue = 1.0f;
            float maxValue = 2.0f;
            String outputFile = "outputImage.bmp";
            Clip.main(new String[]{"1.0", "2.0", null, outputFile});
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test main method with invalid file extension
    public void testMainMethodInvalidFileExtension() {
        try {
            String inputFile = "testImage.txt";
            float minValue = 1.0f;
            float maxValue = 2.0f;
            String outputFile = "outputImage.bmp";
            Clip.main(new String[]{"1.0", "2.0", inputFile, outputFile});
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}