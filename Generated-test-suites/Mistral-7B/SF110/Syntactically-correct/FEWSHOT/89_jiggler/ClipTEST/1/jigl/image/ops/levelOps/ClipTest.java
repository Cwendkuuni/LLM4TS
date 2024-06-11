package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipIntIntWithGrayImage() {
        GrayImage inputImage = new ByteGrayImage(2, 2, new byte[][]{{(byte) 50, (byte) 75}, {(byte) 100, (byte) 125}});
        Clip clip = new Clip(0, 100);
        GrayImage expectedOutputImage = new ByteGrayImage(2, 2, new byte[][]{{(byte) 50, (byte) 75}, {(byte) 100, (byte) 100}});
        GrayImage outputImage = (GrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());

        clip = new Clip(101, 200);
        expectedOutputImage = new ByteGrayImage(2, 2, new byte[][]{{(byte) 75, (byte) 75}, {(byte) 125, (byte) 125}});
        outputImage = (GrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());

        clip = new Clip(-1, 50);
        expectedOutputImage = new ByteGrayImage(2, 2, new byte[][]{{(byte) 50, (byte) 50}, {(byte) 50, (byte) 50}});
        outputImage = (GrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());

        clip = new Clip(0, -1);
        expectedOutputImage = new ByteGrayImage(2, 2, new byte[][]{{(byte) 50, (byte) 50}, {(byte) 50, (byte) 50}});
        outputImage = (GrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());
    }

    @Test
    public void testClipFloatFloatWithGrayImage() {
        GrayImage inputImage = new ByteGrayImage(2, 2, new byte[][]{{(byte) 50, (byte) 75}, {(byte) 100, (byte) 125}});
        Clip clip = new Clip(1.0f, 100.0f);
        GrayImage expectedOutputImage = new ByteGrayImage(2, 2, new byte[][]{{(byte) 50, (byte) 75}, {(byte) 100, (byte) 100}});
        GrayImage outputImage = (GrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());

        clip = new Clip(101.0f, 200.0f);
        expectedOutputImage = new ByteGrayImage(2, 2, new byte[][]{{(byte) 75, (byte) 75}, {(byte) 125, (byte) 125}});
        outputImage = (GrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());

        clip = new Clip(-1.0f, 50.0f);
        expectedOutputImage = new ByteGrayImage(2, 2, new byte[][]{{(byte) 50, (byte) 50}, {(byte) 50, (byte) 50}});
        outputImage = (GrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());

        clip = new Clip(0.0f, -1.0f);
        expectedOutputImage = new ByteGrayImage(2, 2, new byte[][]{{(byte) 50, (byte) 50}, {(byte) 50, (byte) 50}});
        outputImage = (GrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());
    }

    @Test
    public void testClipIntIntWithRealGrayImage() {
        RealGrayImage inputImage = new FloatGrayImage(2, 2, new float[][]{{1.5f, 2.75f}, {3.0f, 4.25f}});
        Clip clip = new Clip(0, 100);
        RealGrayImage expectedOutputImage = new FloatGrayImage(2, 2, new float[][]{{1.5f, 2.75f}, {3.0f, 100.0f}});
        RealGrayImage outputImage = (RealGrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);

        clip = new Clip(101, 200);
        expectedOutputImage = new FloatGrayImage(2, 2, new float[][]{{2.75f, 2.75f}, {4.25f, 4.25f}});
        outputImage = (RealGrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);

        clip = new Clip(-1, 50);
        expectedOutputImage = new FloatGrayImage(2, 2, new float[][]{{1.5f, 1.5f}, {1.5f, 1.5f}});
        outputImage = (RealGrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);

        clip = new Clip(0, -1);
        expectedOutputImage = new FloatGrayImage(2, 2, new float[][]{{1.5f, 1.5f}, {1.5f, 1.5f}});
        outputImage = (RealGrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);
    }

    @Test
    public void testClipFloatFloatWithRealGrayImage() {
        RealGrayImage inputImage = new FloatGrayImage(2, 2, new float[][]{{1.5f, 2.75f}, {3.0f, 4.25f}});
        Clip clip = new Clip(1.0f, 100.0f);
        RealGrayImage expectedOutputImage = new FloatGrayImage(2, 2, new float[][]{{1.5f, 2.75f}, {3.0f, 100.0f}});
        RealGrayImage outputImage = (RealGrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);

        clip = new Clip(101.0f, 200.0f);
        expectedOutputImage = new FloatGrayImage(2, 2, new float[][]{{2.75f, 2.75f}, {4.25f, 4.25f}});
        outputImage = (RealGrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);

        clip = new Clip(-1.0f, 50.0f);
        expectedOutputImage = new FloatGrayImage(2, 2, new float[][]{{1.5f, 1.5f}, {1.5f, 1.5f}});
        outputImage = (RealGrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);

        clip = new Clip(0.0f, -1.0f);
        expectedOutputImage = new FloatGrayImage(2, 2, new float[][]{{1.5f, 1.5f}, {1.5f, 1.5f}});
        outputImage = (RealGrayImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);
    }

    @Test
    public void testClipIntIntWithColorImage() {
        ByteRGBImage inputImage = new ByteRGBImage(2, 2, new byte[][]{{(byte) 50, (byte) 75, (byte) 100},
                {(byte) 150, (byte) 200, (byte) 255}, {(byte) 200, (byte) 230, (byte) 255}, {(byte) 255, (byte) 255, (byte) 255}});
        Clip clip = new Clip(0, 100);
        ByteRGBImage expectedOutputImage = new ByteRGBImage(2, 2, new byte[][]{{(byte) 50, (byte) 75, (byte) 100},
                {(byte) 150, (byte) 200, (byte) 100}, {(byte) 200, (byte) 230, (byte) 100}, {(byte) 255, (byte) 255, (byte) 100}});
        ByteRGBImage outputImage = (ByteRGBImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());

        clip = new Clip(101, 200);
        expectedOutputImage = new ByteRGBImage(2, 2, new byte[][]{{(byte) 150, (byte) 200, (byte) 255},
                {(byte) 200, (byte) 230, (byte) 255}, {(byte) 255, (byte) 255, (byte) 255}, {(byte) 255, (byte) 255, (byte) 255}});
        outputImage = (ByteRGBImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());

        clip = new Clip(-1, 50);
        expectedOutputImage = new ByteRGBImage(2, 2, new byte[][]{{(byte) 50, (byte) 50, (byte) 50},
                {(byte) 50, (byte) 50, (byte) 50}, {(byte) 50, (byte) 50, (byte) 50}, {(byte) 50, (byte) 50, (byte) 50}});
        outputImage = (ByteRGBImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());

        clip = new Clip(0, -1);
        expectedOutputImage = new ByteRGBImage(2, 2, new byte[][]{{(byte) 50, (byte) 50, (byte) 50},
                {(byte) 50, (byte) 50, (byte) 50}, {(byte) 50, (byte) 50, (byte) 50}, {(byte) 50, (byte) 50, (byte) 50}});
        outputImage = (ByteRGBImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());
    }

    @Test
    public void testClipFloatFloatWithColorImage() {
        FloatRGBImage inputImage = new FloatRGBImage(2, 2, new float[][]{{1.5f, 2.75f, 3.0f},
                {2.0f, 3.25f, 4.0f}, {3.5f, 4.5f, 5.0f}, {6.0f, 7.0f, 8.0f}});
        Clip clip = new Clip(1.0f, 100.0f);
        FloatRGBImage expectedOutputImage = new FloatRGBImage(2, 2, new float[][]{{1.5f, 2.75f, 3.0f},
                {2.0f, 3.25f, 100.0f}, {3.5f, 4.5f, 100.0f}, {6.0f, 7.0f, 100.0f}});
        FloatRGBImage outputImage = (FloatRGBImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);

        clip = new Clip(101.0f, 200.0f);
        expectedOutputImage = new FloatRGBImage(2, 2, new float[][]{{2.0f, 3.25f, 4.0f},
                {3.5f, 4.5f, 4.5f}, {6.0f, 7.0f, 7.0f}, {6.0f, 7.0f, 7.0f}});
        outputImage = (FloatRGBImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);

        clip = new Clip(-1.0f, 50.0f);
        expectedOutputImage = new FloatRGBImage(2, 2, new float[][]{{1.5f, 1.5f, 1.5f},
                {1.5f, 1.5f, 1.5f}, {1.5f, 1.5f, 1.5f}, {1.5f, 1.5f, 1.5f}});
        outputImage = (FloatRGBImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);

        clip = new Clip(0.0f, -1.0f);
        expectedOutputImage = new FloatRGBImage(2, 2, new float[][]{{1.5f, 1.5f, 1.5f},
                {1.5f, 1.5f, 1.5f}, {1.5f, 1.5f, 1.5f}, {1.5f, 1.5f, 1.5f}});
        outputImage = (FloatRGBImage) clip.apply(inputImage);
        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);
    }
}