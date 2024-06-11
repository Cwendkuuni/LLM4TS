package jigl.image.ops.levelOps.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.*;
import jigl.image.types.*;
import jigl.image.io.*;

import org.junit.Test;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        int min = 5;
        int max = 10;

        Clip clip = new Clip(min, max);
        GrayImage image = new GrayImage(5, 5, new IntType());
        image.set(0, 0, 255);
        image.set(0, 1, 128);
        image.set(0, 2, 200);
        image.set(0, 3, 150);
        image.set(0, 4, 180);

        image = clip.apply(image);

        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                int expectedValue = Math.min(Math.max(image.get(x, y).intValue(), min), max);
                int actualValue = image.get(x, y).intValue();
                assertEquals(expectedValue, actualValue);
            }
        }
    }

    @Test
    public void testClipIntIntApplyToRealGrayImage() {
        int min = 5;
        int max = 10;

        Clip clip = new Clip(min, max);
        RealGrayImage image = new RealGrayImage(5, 5);
        image.set(0, 0, 2.5f);
        image.set(0, 1, 1.2f);
        image.set(0, 2, 3.5f);
        image.set(0, 3, 0.5f);
        image.set(0, 4, 2.0f);

        image = clip.apply(image);

        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                float expectedValue = Math.min(Math.max(image.get(x, y).floatValue(), min), max);
                float actualValue = image.get(x, y).floatValue();
                assertEquals(expectedValue, actualValue, 0.001f);
            }
        }
    }

    @Test
    public void testClipFloatFloat() {
        float min = 5.5f;
        float max = 10.5f;

        Clip clip = new Clip(min, max);
        GrayImage image = new GrayImage(5, 5, new IntType());
        image.set(0, 0, 255);
        image.set(0, 1, 128);
        image.set(0, 2, 200);
        image.set(0, 3, 150);
        image.set(0, 4, 180);

        image = clip.apply(image);

        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                float expectedValue = Math.min(Math.max(image.get(x, y).intValue() / 255.0f, min), max);
                float actualValue = image.get(x, y).intValue() / 255.0f;
                assertEquals(expectedValue, actualValue, 0.001f);
            }
        }
    }

    @Test
    public void testClipFloatFloatApplyToRealGrayImage() {
        float min = 5.5f;
        float max = 10.5f;

        Clip clip = new Clip(min, max);
        RealGrayImage image = new RealGrayImage(5, 5);
        image.set(0, 0, 2.5f);
        image.set(0, 1, 1.2f);
        image.set(0, 2, 3.5f);
        image.set(0, 3, 0.5f);
        image.set(0, 4, 2.0f);

        image = clip.apply(image);

        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                float expectedValue = Math.min(Math.max(image.get(x, y).floatValue(), min), max);
                float actualValue = image.get(x, y).floatValue();
                assertEquals(expectedValue, actualValue, 0.001f);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithoutMinValue() {
        new Clip(0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithoutMaxValue() {
        new Clip(10.5f, 0.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithoutMinOrMaxValue() {
        new Clip(0.0f, 0.0f);
    }

    @Test
    public void testClipMain() {
        String inputFile = "input.bmp";
        String outputFile = "output.bmp";

        float min = 5.5f;
        float max = 10.5f;

        Clip clip = new Clip(min, max);

        Image image = new ImageInputStream(inputFile).read();

        Image image2 = clip.apply(image);

        new ImageOutputStream(outputFile).write(image2);

        // This test assumes that the image processing library is able to read the input file,
        // apply the clip operation, and write the output file correctly.
        Image readImage = new ImageInputStream(outputFile).read();

        assertEquals(image2.width(), readImage.width());
        assertEquals(image2.height(), readImage.height());

        for (int y = 0; y < image2.Y(); y++) {
            for (int x = 0; x < image2.X(); x++) {
                float expectedValue = Math.min(Math.max(image.get(x, y).intValue() / 255.0f, min), max);
                float actualValue = image2.get(x, y).intValue() / 255.0f;
                assertEquals(expectedValue, actualValue, 0.001f);
            }
        }
    }
}