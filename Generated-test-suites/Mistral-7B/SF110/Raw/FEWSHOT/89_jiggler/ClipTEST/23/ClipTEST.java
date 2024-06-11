package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        GrayImage image = new ByteGrayImage(3, 3, new byte[]{(byte)10, (byte)15, (byte)12, (byte)5, (byte)20, (byte)8, (byte)18, (byte)13, (byte)16});
        ROI roi = new ROI(1, 1, 2, 2);

        Clip clip = new Clip(5, 15);
        Image result = clip.apply(image, roi);

        byte[] expected = {(byte)5, (byte)5, (byte)12, (byte)5, (byte)13, (byte)5, (byte)13, (byte)13, (byte)15};
        assertArrayEquals(expected, result.pixels());
    }

    @Test
    public void testClipIntIntApplyGrayImage() {
        GrayImage image = new ByteGrayImage(3, 3, new byte[]{(byte)10, (byte)15, (byte)12, (byte)5, (byte)20, (byte)8, (byte)18, (byte)13, (byte)16});

        Clip clip = new Clip(5, 15);
        Image result = clip.apply(image);

        byte[] expected = {(byte)5, (byte)5, (byte)12, (byte)5, (byte)13, (byte)5, (byte)13, (byte)13, (byte)15};
        assertArrayEquals(expected, result.pixels());
    }

    @Test
    public void testClipIntIntApplyRealGrayImage() {
        RealGrayImage image = new FloatGrayImage(3, 3, new float[]{1.1f, 1.5f, 1.2f, 0.5f, 2.0f, 0.8f, 1.8f, 1.3f, 1.6f});

        Clip clip = new Clip(5.0f, 15.0f);
        Image result = clip.apply(image);

        float[] expected = {5.0f, 5.0f, 12.0f, 5.0f, 13.0f, 5.0f, 13.0f, 13.0f, 15.0f};
        assertArrayEquals(expected, result.pixels(), 0.01f);
    }

    @Test
    public void testClipFloatFloat() {
        GrayImage image = new ByteGrayImage(3, 3, new byte[]{(byte)10, (byte)15, (byte)12, (byte)5, (byte)20, (byte)8, (byte)18, (byte)13, (byte)16});
        ROI roi = new ROI(1, 1, 2, 2);

        Clip clip = new Clip(5.5f, 15.5f);
        Image result = clip.apply(image, roi);

        byte[] expected = {(byte)5, (byte)5, (byte)12, (byte)5, (byte)13, (byte)5, (byte)13, (byte)13, (byte)15};
        assertArrayEquals(expected, result.pixels());
    }

    @Test
    public void testClipFloatFloatApplyGrayImage() {
        GrayImage image = new ByteGrayImage(3, 3, new byte[]{(byte)10, (byte)15, (byte)12, (byte)5, (byte)20, (byte)8, (byte)18, (byte)13, (byte)16});

        Clip clip = new Clip(5.5f, 15.5f);
        Image result = clip.apply(image);

        byte[] expected = {(byte)5, (byte)5, (byte)12, (byte)5, (byte)13, (byte)5, (byte)13, (byte)13, (byte)15};
        assertArrayEquals(expected, result.pixels());
    }

    @Test
    public void testClipFloatFloatApplyRealGrayImage() {
        RealGrayImage image = new FloatGrayImage(3, 3, new float[]{1.1f, 1.5f, 1.2f, 0.5f, 2.0f, 0.8f, 1.8f, 1.3f, 1.6f});

        Clip clip = new Clip(5.5f, 15.5f);
        Image result = clip.apply(image);

        float[] expected = {5.5f, 5.5f, 12.0f, 5.5f, 13.0f, 5.5f, 13.0f, 13.0f, 15.5f};
        assertArrayEquals(expected, result.pixels(), 0.01f);
    }

    @Test
    public void testClipIntIntWithNullImage() {
        Clip clip = new Clip(5, 15);
        assertNull(clip.apply(null));
    }

    @Test
    public void testClipIntIntWithNullROI() {
        GrayImage image = new ByteGrayImage(3, 3, new byte[]{(byte)10, (byte)15, (byte)12, (byte)5, (byte)20, (byte)8, (byte)18, (byte)13, (byte)16});

        Clip clip = new Clip(5, 15);
        assertNull(clip.apply(image, null));
    }

    @Test
    public void testClipFloatFloatWithNullImage() {
        Clip clip = new Clip(5.5f, 15.5f);
        assertNull(clip.apply(null));
    }

    @Test
    public void testClipFloatFloatWithNullROI() {
        GrayImage image = new ByteGrayImage(3, 3, new byte[]{(byte)10, (byte)15, (byte)12, (byte)5, (byte)20, (byte)8, (byte)18, (byte)13, (byte)16});

        Clip clip = new Clip(5.5f, 15.5f);
        assertNull(clip.apply(image, null));
    }
}