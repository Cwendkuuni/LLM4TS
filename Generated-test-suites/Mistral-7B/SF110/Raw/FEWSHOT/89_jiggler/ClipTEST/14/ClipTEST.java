package jigl.image.ops.levelOps;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        GrayImage input = new ByteImage(new int[1][1], 1, 1);
        Clip clip = new Clip(0, 100);
        GrayImage output = clip.apply(input);
        byte[] outputArray = output.getData();
        byte[] expectedArray = new byte[output.X() * output.Y()];
        for (int i = 0; i < expectedArray.length; i++) {
            expectedArray[i] = (byte) Math.min(i, 100);
        }
        assertArrayEquals("Clip test with int min and int max failed.", expectedArray, outputArray);
    }

    @Test
    public void testClipIntIntWithMinGreaterThanMax() {
        GrayImage input = new ByteImage(new int[1][1], 1, 1);
        Clip clip = new Clip(100, 0);
        GrayImage output = clip.apply(input);
        assertNull("Clip test with int min greater than max did not return null.", output);
    }

    @Test
    public void testClipFloatFloat() {
        RealGrayImage input = new ByteImage(new float[1][1], 1, 1);
        Clip clip = new Clip(0.0f, 100.0f);
        RealGrayImage output = clip.apply(input);
        float[] outputArray = output.getData();
        float[] expectedArray = new float[output.X() * output.Y()];
        for (int i = 0; i < expectedArray.length; i++) {
            expectedArray[i] = Math.min(i / 255.0f, 100.0f);
        }
        assertArrayEquals("Clip test with float min and float max failed.", expectedArray, outputArray, 0.001f);
    }

    @Test
    public void testClipFloatFloatWithMinGreaterThanMax() {
        RealGrayImage input = new ByteImage(new float[1][1], 1, 1);
        Clip clip = new Clip(100.0f, 0.0f);
        RealGrayImage output = clip.apply(input);
        assertNull("Clip test with float min greater than max did not return null.", output);
    }

    @Test
    public void testClipApplyGrayImage() {
        GrayImage input = new ByteImage(new int[]{-1, 0, 1, 2, 3, 4, 5, 100}, 1, 8);
        Clip clip = new Clip(1, 5);
        GrayImage output = clip.apply(input);
        byte[] outputArray = output.getData();
        byte[] expectedArray = new byte[]{0, 0, 1, 2, 3, 4, 5, 1};
        assertArrayEquals("Clip test applying gray image failed.", expectedArray, outputArray);
    }

    @Test
    public void testClipApplyRealGrayImage() {
        RealGrayImage input = new ByteImage(new float[]{-1.5f, 0.0f, 1.5f, 2.0f, 3.5f, 4.5f, 5.5f, 100.0f}, 1, 8);
        Clip clip = new Clip(0.5f, 5.5f);
        RealGrayImage output = clip.apply(input);
        float[] outputArray = output.getData();
        float[] expectedArray = new float[]{0.5f, 0.0f, 1.5f, 2.0f, 3.5f, 4.5f, 5.5f, 1.0f};
        assertArrayEquals("Clip test applying real gray image failed.", expectedArray, outputArray, 0.001f);
    }

    @Test
    public void testClipApplyColorImage() {
        ColorImage input = new RGBImage(new byte[][]{
            {0, 0, 0},
            {255, 255, 255},
            {0, 0, 0},
            {128, 128, 128}
        }, 2, 2);
        Clip clip = new Clip(0.5f, 1.0f);
        ColorImage output = clip.apply(input);
        byte[][] outputPlanes = output.getPlanes();
        byte[][] expectedPlanes = new byte[][]{{(byte) 128, (byte) 128, (byte) 128},
                                              {(byte) 255, (byte) 255, (byte) 255},
                                              {(byte) 0, (byte) 0, (byte) 0},
                                              {(byte) 128, (byte) 128, (byte) 128}};
        for (int i = 0; i < 3; i++) {
            assertArrayEquals("Clip test applying color image failed for plane " + i,
                    expectedPlanes[i], outputPlanes[i]);
        }
    }

    @Test
    public void testClipApplyRealColorImage() {
        RealColorImage input = new RGBImage(new float[][]{
            {0.0f, 0.0f, 0.0f},
            {1.0f, 1.0f, 1.0f},
            {0.0f, 0.0f, 0.0f},
            {0.5f, 0.5f, 0.5f}
        }, 2, 2);
        Clip clip = new Clip(0.25f, 0.75f);
        RealColorImage output = clip.apply(input);
        float[][] outputPlanes = output.getPlanes();
        float[][] expectedPlanes = new float[][]{{0.25f, 0.25f, 0.25f},
                                                {1.0f, 1.0f, 1.0f},
                                                {0.0f, 0.0f, 0.0f},
                                                {0.5f, 0.5f, 0.5f}};
        for (int i = 0; i < 3; i++) {
            assertArrayEquals("Clip test applying real color image failed for plane " + i,
                    expectedPlanes[i], outputPlanes[i], 0.001f);
        }
    }
}