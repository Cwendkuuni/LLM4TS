import junit.framework.TestCase;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.GrayImage;
import jigl.image.ops.ROI;
import jigl.image.types.Image;

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
    public void testApplyWithGrayImageAndROI_intMinAndIntMax() {
        int min = 5;
        int max = 10;
        Clip clip = new Clip(min, max);
        GrayImage inputImage = new GrayImage(3, 3);
        inputImage.set(0, 0, 10);
        inputImage.set(0, 1, 15);
        inputImage.set(0, 2, 7);
        inputImage.set(1, 0, 3);
        inputImage.set(1, 1, 12);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 0, 8);
        inputImage.set(2, 1, 11);
        inputImage.set(2, 2, 5);
        ROI roi = new ROI(0, 0, 2, 2);
        Image expectedOutputImage = new GrayImage(2, 2);
        expectedOutputImage.set(0, 0, 5);
        expectedOutputImage.set(0, 1, 5);
        expectedOutputImage.set(1, 0, 5);
        expectedOutputImage.set(1, 1, 5);
        Image outputImage = clip.apply(inputImage, roi);
        assertNotNull(outputImage);
        for (int y = 0; y < outputImage.Y(); ++y) {
            for (int x = 0; x < outputImage.X(); ++x) {
                int value = outputImage.get(x, y).intValue();
                int expectedValue = expectedOutputImage.get(x, y).intValue();
                assertEquals(expectedValue, value);
            }
        }
    }

    @org.junit.Test
    public void testApplyWithGrayImageAndROI_floatMinAndFloatMax() {
        float min = 2.5f;
        float max = 7.5f;
        Clip clip = new Clip(min, max);
        GrayImage inputImage = new GrayImage(3, 3);
        inputImage.set(0, 0, 10.5f);
        inputImage.set(0, 1, 15.8f);
        inputImage.set(0, 2, 7.2f);
        inputImage.set(1, 0, 3.1f);
        inputImage.set(1, 1, 12.3f);
        inputImage.set(1, 2, 1.8f);
        inputImage.set(2, 0, 8.6f);
        inputImage.set(2, 1, 11.1f);
        inputImage.set(2, 2, 5.5f);
        ROI roi = new ROI(0, 0, 2, 2);
        Image expectedOutputImage = new GrayImage(2, 2);
        expectedOutputImage.set(0, 0, 7.5f);
        expectedOutputImage.set(0, 1, 7.5f);
        expectedOutputImage.set(1, 0, 7.5f);
        expectedOutputImage.set(1, 1, 7.5f);
        Image outputImage = clip.apply(inputImage, roi);
        assertNotNull(outputImage);
        for (int y = 0; y < outputImage.Y(); ++y) {
            for (int x = 0; x < outputImage.X(); ++x) {
                float value = outputImage.get(x, y).floatValue();
                float expectedValue = expectedOutputImage.get(x, y).floatValue();
                assertEquals(expectedValue, value, 0.001f);
            }
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithNullImage() {
        Clip clip = new Clip(2.5f, 7.5f);
        Image inputImage = null;
        ROI roi = new ROI(0, 0, 2, 2);
        clip.apply(inputImage, roi);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithNullROI() {
        Clip clip = new Clip(2.5f, 7.5f);
        GrayImage inputImage = new GrayImage(3, 3);
        ROI roi = null;
        clip.apply(inputImage, roi);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testApplyWithInvalidROI() {
        Clip clip = new Clip(2.5f, 7.5f);
        GrayImage inputImage = new GrayImage(3, 3);
        ROI roi = new ROI(-1, 0, 2, 2);
        clip.apply(inputImage, roi);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithNegativeIntMin() {
        Clip clip = new Clip(-5, 10);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithNegativeIntMax() {
        Clip clip = new Clip(5, -10);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithNegativeFloatMin() {
        Clip clip = new Clip(-2.5f, 7.5f);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyWithNegativeFloatMax() {
        Clip clip = new Clip(2.5f, -7.5f);
    }

    @org.junit.Test
    public void testMainWithValidArguments() {
        String inputFile = "input.jpg";
        float min = 2.5f;
        float max = 7.5f;
        String outputFile = "output.jpg";
        Clip.main(new String[]{".", inputFile, Float.toString(min), Float.toString(max), outputFile});
    }

    @org.junit.Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testMainWithInvalidNumberOfArguments() {
        String inputFile = "input.jpg";
        float min = 2.5f;
        String outputFile = "output.jpg";
        Clip.main(new String[]{".", inputFile, Float.toString(min)});
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testMainWithInvalidInputFile() {
        String inputFile = "nonExistentFile.jpg";
        float min = 2.5f;
        float max = 7.5f;
        String outputFile = "output.jpg";
        Clip.main(new String[]{".", inputFile, Float.toString(min), Float.toString(max), outputFile});
    }

    @org.junit.Test(expected = IOException.class)
    public void testMainWithIOException() {
        String inputFile = "input.jpg";
        float min = 2.5f;
        float max = 7.5f;
        String outputFile = "output.jpg";
        java.io.File outputFileObject = new java.io.File(outputFile);
        java.io.PrintWriter writer = new java.io.PrintWriter(outputFileObject);
        writer.write("An error occurred.");
        writer.close();
        Clip.main(new String[]{".", inputFile, Float.toString(min), Float.toString(max), outputFile});
    }
}