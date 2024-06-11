package jigl.image.ops.levelOps;

import jigl.image.*;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.*;
import jigl.image.io.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ClipTest {

    @Test
    public void testClipIntMinIntMax() {
        Clip clip = new Clip(5, 10);
        assertEquals(5, clip.int_min);
        assertEquals(10, clip.int_max);
    }

    @Test
    public void testClipFloatMinFloatMax() {
        Clip clip = new Clip(2.5f, 7.5f);
        assertEquals(2.5f, clip.float_min, 0.01f);
        assertEquals(7.5f, clip.float_max, 0.01f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipInvalidIntMin() {
        try {
            Clip clip = new Clip(Integer.MIN_VALUE, 10);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("An IllegalArgumentException should have been thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipInvalidIntMax() {
        try {
            Clip clip = new Clip(5, Integer.MAX_VALUE);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("An IllegalArgumentException should have been thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipInvalidFloatMin() {
        try {
            Clip clip = new Clip(-1.5f, 10.0f);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("An IllegalArgumentException should have been thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipInvalidFloatMax() {
        try {
            Clip clip = new Clip(5.0f, Float.NaN);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("An IllegalArgumentException should have been thrown");
    }

    @Test
    public void testClipGrayImage() {
        GrayImage inputImage = new GrayImage(3, 3);
        inputImage.set(0, 0, (byte) 150);
        inputImage.set(0, 1, (byte) 50);
        inputImage.set(1, 0, (byte) 200);
        inputImage.set(1, 1, (byte) 100);
        inputImage.set(2, 0, (byte) 175);
        inputImage.set(2, 1, (byte) 125);
        ROI roi = new ROI(1, 1, 1, 1);
        Clip clip = new Clip(125.0f, 175.0f);
        GrayImage outputImage = (GrayImage) clip.apply(inputImage);
        byte expectedOutputValue = 150;
        assertEquals("Output image value is not as expected", expectedOutputValue, outputImage.get(0, 0));
        assertEquals("Output image value is not as expected", expectedOutputValue, outputImage.get(0, 1));
        assertEquals("Output image value is not as expected", 175, outputImage.get(1, 0));
        assertEquals("Output image value is not as expected", 150, outputImage.get(1, 1));
    }

    @Test
    public void testClipRealGrayImage() {
        RealGrayImage inputImage = new RealGrayImage(3, 3);
        inputImage.set(0, 0, 150.0f);
        inputImage.set(0, 1, 50.0f);
        inputImage.set(1, 0, 200.0f);
        inputImage.set(1, 1, 100.0f);
        inputImage.set(2, 0, 175.0f);
        inputImage.set(2, 1, 125.0f);
        ROI roi = new ROI(1, 1, 1, 1);
        Clip clip = new Clip(125.0f, 175.0f);
        RealGrayImage outputImage = (RealGrayImage) clip.apply(inputImage);
        float expectedOutputValue = 150.0f;
        assertEquals("Output image value is not as expected", expectedOutputValue, outputImage.get(0, 0), 0.01f);
        assertEquals("Output image value is not as expected", expectedOutputValue, outputImage.get(0, 1), 0.01f);
        assertEquals("Output image value is not as expected", 175.0f, outputImage.get(1, 0), 0.01f);
        assertEquals("Output image value is not as expected", 150.0f, outputImage.get(1, 1), 0.01f);
    }
}