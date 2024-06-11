package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        GrayImage image = new ByteGrayImage(3, 3);
        image.setPixel(0, 0, (byte) 100);
        image.setPixel(0, 1, (byte) 50);
        image.setPixel(0, 2, (byte) 200);
        image.setPixel(1, 0, (byte) 150);
        image.setPixel(1, 1, (byte) 75);
        image.setPixel(1, 2, (byte) 255);
        image.setPixel(2, 0, (byte) 50);
        image.setPixel(2, 1, (byte) 100);
        image.setPixel(2, 2, (byte) 150);

        Clip clip = new Clip(50, 150);
        Image result = clip.apply(image);

        ByteGrayImage expected = new ByteGrayImage(3, 3);
        expected.setPixel(0, 0, (byte) 50);
        expected.setPixel(0, 1, (byte) 50);
        expected.setPixel(0, 2, (byte) 150);
        expected.setPixel(1, 0, (byte) 50);
        expected.setPixel(1, 1, (byte) 75);
        expected.setPixel(1, 2, (byte) 150);
        expected.setPixel(2, 0, (byte) 50);
        expected.setPixel(2, 1, (byte) 100);
        expected.setPixel(2, 2, (byte) 150);

        assertImageEquals(expected, result);
    }

    @Test
    public void testClipIntInt_zeroBounds() {
        GrayImage image = new ByteGrayImage(3, 3);
        image.setPixel(0, 0, (byte) 100);
        image.setPixel(0, 1, (byte) 50);
        image.setPixel(0, 2, (byte) 200);
        image.setPixel(1, 0, (byte) 150);
        image.setPixel(1, 1, (byte) 75);
        image.setPixel(1, 2, (byte) 255);
        image.setPixel(2, 0, (byte) 50);
        image.setPixel(2, 1, (byte) 100);
        image.setPixel(2, 2, (byte) 150);

        Clip clip = new Clip(0, 0);
        Image result = clip.apply(image);

        ByteGrayImage expected = new ByteGrayImage(3, 3);
        expected.setPixel(0, 0, (byte) 0);
        expected.setPixel(0, 1, (byte) 0);
        expected.setPixel(0, 2, (byte) 0);
        expected.setPixel(1, 0, (byte) 0);
        expected.setPixel(1, 1, (byte) 0);
        expected.setPixel(1, 2, (byte) 0);
        expected.setPixel(2, 0, (byte) 0);
        expected.setPixel(2, 1, (byte) 0);
        expected.setPixel(2, 2, (byte) 0);

        assertImageEquals(expected, result);
    }

    @Test
    public void testClipFloatFloat() {
        RealGrayImage image = new FloatGrayImage(3, 3);
        image.setPixel(0, 0, 100.f);
        image.setPixel(0, 1, 50.f);
        image.setPixel(0, 2, 200.f);
        image.setPixel(1, 0, 150.f);
        image.setPixel(1, 1, 75.f);
        image.setPixel(1, 2, 300.f);
        image.setPixel(2, 0, 50.f);
        image.setPixel(2, 1, 100.f);
        image.setPixel(2, 2, 150.f);

        Clip clip = new Clip(75.f, 125.f);
        Image result = clip.apply(image);

        RealGrayImage expected = new FloatGrayImage(3, 3);
        expected.setPixel(0, 0, 75.f);
        expected.setPixel(0, 1, 50.f);
        expected.setPixel(0, 2, 125.f);
        expected.setPixel(1, 0, 75.f);
        expected.setPixel(1, 1, 75.f);
        expected.setPixel(1, 2, 125.f);
        expected.setPixel(2, 0, 100.f);
        expected.setPixel(2, 1, 100.f);
        expected.setPixel(2, 2, 125.f);

        assertImageEquals(expected, result);
    }

    @Test
    public void testClipFloatFloat_zeroBounds() {
        RealGrayImage image = new FloatGrayImage(3, 3);
        image.setPixel(0, 0, 100.f);
        image.setPixel(0, 1, 50.f);
        image.setPixel(0, 2, 200.f);
        image.setPixel(1, 0, 150.f);
        image.setPixel(1, 1, 75.f);
        image.setPixel(1, 2, 300.f);
        image.setPixel(2, 0, 50.f);
        image.setPixel(2, 1, 100.f);
        image.setPixel(2, 2, 150.f);

        Clip clip = new Clip(0.f, 0.f);
        Image result = clip.apply(image);

        RealGrayImage expected = new FloatGrayImage(3, 3);
        expected.setPixel(0, 0, 0.f);
        expected.setPixel(0, 1, 0.f);
        expected.setPixel(0, 2, 0.f);
        expected.setPixel(1, 0, 0.f);
        expected.setPixel(1, 1, 0.f);
        expected.setPixel(1, 2, 0.f);
        expected.setPixel(2, 0, 0.f);
        expected.setPixel(2, 1, 0.f);
        expected.setPixel(2, 2, 0.f);

        assertImageEquals(expected, result);
    }

    @Test
    public void testClipColorIntInt() {
        ColorImage image = new RGBImage(3, 3, new byte[3 * 3], new int[3 * 3]);
        image.setPixel(0, 0, 100, 50, 0);
        image.setPixel(0, 1, 50, 75, 0);
        image.setPixel(0, 2, 200, 255, 0);
        image.setPixel(1, 0, 150, 75, 0);
        image.setPixel(1, 1, 75, 125, 0);
        image.setPixel(1, 2, 300, 255, 0);
        image.setPixel(2, 0, 50, 100, 0);
        image.setPixel(2, 1, 100, 150, 0);
        image.setPixel(2, 2, 150, 255, 0);

        Clip clip = new Clip(50, 150);
        Image result = clip.apply(image);

        RGBImage expected = new RGBImage(3, 3, new byte[3 * 3], new int[3 * 3]);
        expected.setPixel(0, 0, 50, 50, 0);
        expected.setPixel(0, 1, 50, 75, 0);
        expected.setPixel(0, 2, 150, 150, 0);
        expected.setPixel(1, 0, 50, 50, 0);
        expected.setPixel(1, 1, 75, 125, 0);
        expected.setPixel(1, 2, 150, 150, 0);
        expected.setPixel(2, 0, 100, 100, 0);
        expected.setPixel(2, 1, 100, 150, 0);
        expected.setPixel(2, 2, 150, 150, 0);

        assertImageEquals(expected, result);
    }

    @Test
    public void testClipColorFloatFloat() {
        ColorImage image = new RGBImage(3, 3, new byte[3 * 3], new int[3 * 3]);
        image.setPixel(0, 0, 100, 50, 0);
        image.setPixel(0, 1, 50, 75, 0);
        image.setPixel(0, 2, 200, 255, 0);
        image.setPixel(1, 0, 150, 75, 0);
        image.setPixel(1, 1, 75, 125, 0);
        image.setPixel(1, 2, 300, 255, 0);
        image.setPixel(2, 0, 50, 100, 0);
        image.setPixel(2, 1, 100, 150, 0);
        image.setPixel(2, 2, 150, 255, 0);

        Clip clip = new Clip(75.f, 125.f);
        Image result = clip.apply(image);

        RGBImage expected = new RGBImage(3, 3, new byte[3 * 3], new int[3 * 3]);
        expected.setPixel(0, 0, 75, 75, 0);
        expected.setPixel(0, 1, 50, 125, 0);
        expected.setPixel(0, 2, 125, 125, 0);
        expected.setPixel(1, 0, 75, 75, 0);
        expected.setPixel(1, 1, 75, 125, 0);
        expected.setPixel(1, 2, 125, 125, 0);
        expected.setPixel(2, 0, 100, 100, 0);
        expected.setPixel(2, 1, 100, 150, 0);
        expected.setPixel(2, 2, 125, 125, 0);

        assertImageEquals(expected, result);
    }
}