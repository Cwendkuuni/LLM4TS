package jigl.image.ops.levelOps;

import static org.junit.Assert.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;
import org.junit.Test;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        GrayImage image = new ByteGrayImage(new uint8[]{}, 1, 1);
        Clip clip = new Clip(5, 10);
        Image result = clip.apply(image);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                int value = image.get(x, y).intValue();
                assertEquals(Math.max(value, 5), result.get(x, y).intValue());
                assertEquals(Math.min(value, 10), result.get(x, y).intValue());
            }
        }
    }

    @Test
    public void testClipIntIntWithZeroLimits() {
        GrayImage image = new ByteGrayImage(new uint8[]{}, 1, 1);
        Clip clip = new Clip(0, 0);
        Image result = clip.apply(image);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                int value = image.get(x, y).intValue();
                assertEquals(0, result.get(x, y).intValue());
            }
        }
    }

    @Test
    public void testClipFloatFloat() {
        RealGrayImage image = new FloatGrayImage(new float[]{}, 1, 1);
        Clip clip = new Clip(2.5f, 7.5f);
        Image result = clip.apply(image);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                float value = image.get(x, y);
                assertEquals(Math.max(value, 2.5f), result.get(x, y), 0.01f);
                assertEquals(Math.min(value, 7.5f), result.get(x, y), 0.01f);
            }
        }
    }

    @Test
    public void testClipFloatFloatWithZeroLimits() {
        RealGrayImage image = new FloatGrayImage(new float[]{}, 1, 1);
        Clip clip = new Clip(0.0f, 0.0f);
        Image result = clip.apply(image);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                float value = image.get(x, y);
                assertEquals(0.0f, result.get(x, y), 0.01f);
            }
        }
    }

    @Test
    public void testClipColorImageIntInt() {
        ColorImage image = new RGBImage(new byte[][]{
                new byte[]{(byte) 0, (byte) 255, (byte) 0},
                new byte[]{(byte) 255, (byte) 0, (byte) 255},
                new byte[]{(byte) 0, (byte) 255, (byte) 255}}, 3, 2);

        Clip clip = new Clip(50, 150);
        Image result = clip.apply(image);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                for (int plane = 0; plane < image.numPlanes(); ++plane) {
                    int value = image.get(x, y, plane).intValue();
                    assertEquals(Math.max(value, 50), result.get(x, y, plane).intValue());
                    assertEquals(Math.min(value, 150), result.get(x, y, plane).intValue());
                }
            }
        }
    }

    @Test
    public void testClipRealColorImageFloatFloat() {
        RealColorImage image = new FloatRGBImage(new float[][]{
                new float[]{1.0f, 2.5f, 3.0f},
                new float[]{2.5f, 1.0f, 4.5f},
                new float[]{3.0f, 4.5f, 1.0f}}, 3, 2);

        Clip clip = new Clip(2.0f, 5.5f);
        Image result = clip.apply(image);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                for (int plane = 0; plane < image.numPlanes(); ++plane) {
                    float value = image.get(x, y, plane);
                    assertEquals(Math.max(value, 2.0f), result.get(x, y, plane), 0.01f);
                    assertEquals(Math.min(value, 5.5f), result.get(x, y, plane), 0.01f);
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithNullImage() {
        Clip clip = new Clip(0, 0);
        clip.apply(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithNullROI() {
        GrayImage image = new ByteGrayImage(new uint8[]{}, 1, 1);
        Clip clip = new Clip(0, 0);
        clip.apply(image, null);
    }
}