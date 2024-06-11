package jigl.image.ops.levelOps;

import org.junit.Test;
import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;
import static org.junit.Assert.*;

public class ClipTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidMinValue() {
        new Clip(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidMaxValue() {
        new Clip(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidMinAndMaxValues() {
        new Clip(-1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullInput() {
        new Clip(null, null);
    }

    @Test
    public void testConstructor() {
        // Test int constructor with valid values
        Clip clip = new Clip(5, 10);
        assertEquals(5, clip.int_min);
        assertEquals(10, clip.int_max);

        // Test int constructor with min equal to max
        clip = new Clip(5, 5);
        assertEquals(5, clip.int_min);
        assertEquals(5, clip.int_max);

        // Test float constructor with valid values
        clip = new Clip(2.5f, 7.5f);
        assertEquals((int)2.5f, clip.float_min);
        assertEquals((int)7.5f, clip.float_max);

        // Test float constructor with min equal to max
        clip = new Clip(2.5f, 2.5f);
        assertEquals((int)2.5f, clip.float_min);
        assertEquals((int)2.5f, clip.float_max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyWithNullImage() throws Exception {
        Clip clip = new Clip(0.5f, 1.5f);
        ROI roi = new ROI(0, 0, 10, 10);
        clip.apply(null, roi);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyWithNullROI() throws Exception {
        Clip clip = new Clip(0.5f, 1.5f);
        GrayImage image = new GrayImage(10, 10);
        clip.apply(image, null);
    }

    @Test
    public void testApplyWithEmptyROI() throws Exception {
        Clip clip = new Clip(0.5f, 1.5f);
        GrayImage inputImage = new GrayImage(1, 1);
        ROI roi = new ROI(1, 1, 0, 0);
        GrayImage outputImage = (GrayImage) clip.apply(inputImage, roi);
        assertTrue(outputImage.X() == 1 && outputImage.Y() == 1);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageOutputStream os = new ImageOutputStream(outputStream);
        os.write(outputImage);
        byte[] expectedOutput = {0, 0, 0, 255};
        byte[] actualOutput = ((ByteArrayInputStream) new ByteArrayInputStream(outputStream.toByteArray())).readAllBytes();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testApplyWithValidInput() throws Exception {
        Clip clip = new Clip(0.5f, 1.5f);
        GrayImage inputImage = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, inputImage.X() - 1, inputImage.Y() - 1);
        GrayImage expectedOutput = new GrayImage(inputImage.X(), inputImage.Y());
        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float inputValue = (float) Math.random();
                float expectedValue = Math.max(Math.min(inputValue, 1.5f), 0.5f);
                inputImage.set(x, y, (short) (inputValue * 255));
                expectedOutput.set(x, y, (short) (expectedValue * 255));
            }
        }
        GrayImage outputImage = (GrayImage) clip.apply(inputImage, roi);
        assertEquals(expectedOutput, outputImage);
    }

    @Test
    public void testApplyWithROIOutsideOfInputImage() throws Exception {
        Clip clip = new Clip(0.5f, 1.5f);
        GrayImage inputImage = new GrayImage(10, 10);
        ROI roi = new ROI(11, 11, inputImage.X() - 1, inputImage.Y() - 1);
        GrayImage outputImage = (GrayImage) clip.apply(inputImage, roi);
        assertTrue(outputImage.X() == 0 && outputImage.Y() == 0 && outputImage.width() == 0 && outputImage.height() == 0);
    }
}