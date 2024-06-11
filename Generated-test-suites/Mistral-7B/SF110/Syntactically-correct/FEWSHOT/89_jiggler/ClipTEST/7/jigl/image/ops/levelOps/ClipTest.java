package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;
import org.junit.Test;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        GrayImage image = new ByteGrayImage(3, 3, new byte[]{(byte) 1, (byte) 2, (byte) 3,
                (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9});
        ROI roi = new ROI(1, 1, 2, 2);
        Clip clip = new Clip(5, 7);
        Image result = clip.apply(image, roi);

        byte[] expected = {(byte) 5, (byte) 5, (byte) 5, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 9};
        assertArrayEquals(expected, result.getPixels());
    }

    @Test
    public void testClipIntInt_ApplyGrayImage() {
        GrayImage image = new ByteGrayImage(3, 3, new byte[]{(byte) 1, (byte) 2, (byte) 3,
                (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9});
        Clip clip = new Clip(5, 7);
        Image result = clip.apply(image);

        byte[] expected = {(byte) 5, (byte) 5, (byte) 5, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 9};
        assertArrayEquals(expected, result.getPixels());
    }

    @Test
    public void testClipIntInt_ApplyRealGrayImage() {
        RealGrayImage image = new FloatGrayImage(3, 3, new float[]{1.f, 2.f, 3.f,
                4.f, 5.f, 6.f, 7.f, 8.f, 9.f});
        Clip clip = new Clip(5.5f, 7.5f);
        Image result = clip.apply(image);

        float[] expected = {5.5f, 5.5f, 5.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 9.0f};
        assertArrayEquals(expected, result.getPixels(), 0.01f);
    }

    @Test
    public void testClipIntInt_ApplyColorImage() {
        ColorImage image = new ByteColorImage(3, 3, new byte[]{
                0, 0, 0, 0, 1, 1, 1, 1, 1,
                0, 0, 0, 1, 1, 1, 1, 1, 1,
                0, 0, 1, 1, 1, 1, 1, 1, 1},
                new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1},
                new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1});
        Clip clip = new Clip(5, 7);
        Image result = clip.apply(image);

        byte[][] expectedPlanes = new byte[][]{
                {(byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 7, (byte) 7, (byte) 7},
                {(byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 7, (byte) 7, (byte) 7},
                {(byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 7, (byte) 7, (byte) 7}
        };
        assertArrayEquals(expectedPlanes, result.getPlanes());
    }

    @Test
    public void testClipFloatFloat() {
        RealGrayImage image = new FloatGrayImage(3, 3, new float[]{1.f, 2.f, 3.f,
                4.f, 5.f, 6.f, 7.f, 8.f, 9.f});
        ROI roi = new ROI(1, 1, 2, 2);
        Clip clip = new Clip(5.5f, 7.5f);
        Image result = clip.apply(image, roi);

        float[] expected = {5.5f, 5.5f, 5.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 9.0f};
        assertArrayEquals(expected, result.getPixels(), 0.01f);
    }

    @Test
    public void testClipFloatFloat_ApplyGrayImage() {
        GrayImage image = new ByteGrayImage(3, 3, new byte[]{(byte) 1, (byte) 2, (byte) 3,
                (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9});
        Clip clip = new Clip(5.5f, 7.5f);
        Image result = clip.apply(image);

        byte[] expected = {(byte) 5, (byte) 5, (byte) 5, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 9};
        assertArrayEquals(expected, result.getPixels());
    }

    @Test
    public void testClipFloatFloat_ApplyRealGrayImage() {
        RealGrayImage image = new FloatGrayImage(3, 3, new float[]{1.f, 2.f, 3.f,
                4.f, 5.f, 6.f, 7.f, 8.f, 9.f});
        Clip clip = new Clip(5.5f, 7.5f);
        Image result = clip.apply(image);

        float[] expected = {5.5f, 5.5f, 5.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 9.0f};
        assertArrayEquals(expected, result.getPixels(), 0.01f);
    }

    @Test
    public void testClipFloatFloat_ApplyColorImage() {
        ColorImage image = new ByteColorImage(3, 3, new byte[]{
                0, 0, 0, 0, 1, 1, 1, 1, 1,
                0, 0, 0, 1, 1, 1, 1, 1, 1,
                0, 0, 1, 1, 1, 1, 1, 1, 1},
                new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1},
                new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1});
        Clip clip = new Clip(5.5f, 7.5f);
        Image result = clip.apply(image);

        byte[][] expectedPlanes = new byte[][]{
                {(byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 7, (byte) 7, (byte) 7},
                {(byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 7, (byte) 7, (byte) 7},
                {(byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 5, (byte) 7, (byte) 7, (byte) 7}
        };
        assertArrayEquals(expectedPlanes, result.getPlanes());
    }

    @Test
    public void testApplyWithInvalidArgs() {
        try {
            new Clip("invalid", 7);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new Clip(5, "invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testApplyWithNullImage() {
        Clip clip = new Clip(5, 7);
        assertNull(clip.apply(null));
    }

    @Test
    public void testApplyWithNullROI() {
        GrayImage image = new ByteGrayImage(3, 3, new byte[]{(byte) 1, (byte) 2, (byte) 3,
                (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9});
        Clip clip = new Clip(5, 7);
        assertNull(clip.apply(image, null));
    }
}