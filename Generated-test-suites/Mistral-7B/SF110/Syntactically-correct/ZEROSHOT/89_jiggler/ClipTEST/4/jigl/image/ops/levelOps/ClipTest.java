package jigl.image.ops.levelOps;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import jigl.image.ops.SimpleOperator;
import jigl.image.ops.util.ImageOperations;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.types.ColorImage;
import jigl.image.types.RealColorImage;
import jigl.image.types.ROI;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

import org.junit.Test;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        GrayImage inputImage = ImageOperations.createRandomGrayImage(10, 10);
        Clip clip = new Clip(5, 10);
        GrayImage outputImage = new Clip().apply(inputImage);

        Clip clipInstance = new Clip(5, 10);
        GrayImage expectedOutputImage = ImageOperations.createNewGrayImage(inputImage.X(), inputImage.Y());
        for (int y = 0; y < inputImage.Y(); y++) {
            for (int x = 0; x < inputImage.X(); x++) {
                if (x < 5 || x > 5 || y < 5 || y > 5) {
                    expectedOutputImage.set(x, y, inputImage.get(x, y));
                } else {
                    expectedOutputImage.set(x, y, 5);
                }
            }
        }

        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData());
        assertFalse(outputImage.equals(inputImage));

        inputImage = ImageOperations.createRandomGrayImage(1, 1);
        clip = new Clip(5, 5);
        outputImage = clip.apply(inputImage);
        assertTrue(outputImage.equals(inputImage));

        inputImage = ImageOperations.createRandomGrayImage(1, 1);
        clip = new Clip(-5, 5);
        outputImage = clip.apply(inputImage);
        assertTrue(outputImage.equals(inputImage));
    }

    @Test
    public void testClipFloatFloat() {
        RealGrayImage inputImage = ImageOperations.createRandomRealGrayImage(10, 10);
        Clip clip = new Clip(5.5f, 10.5f);
        RealGrayImage outputImage = new Clip().apply(inputImage);

        Clip clipInstance = new Clip(5.5f, 10.5f);
        RealGrayImage expectedOutputImage = ImageOperations.createNewRealGrayImage(inputImage.X(), inputImage.Y());
        for (int y = 0; y < inputImage.Y(); y++) {
            for (int x = 0; x < inputImage.X(); x++) {
                if (x < 5 || x > 5 || y < 5 || y > 5) {
                    expectedOutputImage.set(x, y, inputImage.get(x, y));
                } else {
                    expectedOutputImage.set(x, y, 5.5f);
                }
            }
        }

        assertArrayEquals(expectedOutputImage.getData(), outputImage.getData(), 0.001f);
        assertFalse(outputImage.equals(inputImage));

        inputImage = ImageOperations.createRandomRealGrayImage(1, 1);
        clip = new Clip(5.5f, 5.5f);
        outputImage = clip.apply(inputImage);
        assertTrue(outputImage.equals(inputImage));

        inputImage = ImageOperations.createRandomRealGrayImage(1, 1);
        clip = new Clip(-5.5f, 5.5f);
        outputImage = clip.apply(inputImage);
        assertTrue(outputImage.equals(inputImage));
    }

    @Test
    public void testClipColorImage() {
        ColorImage inputImage = ImageOperations.createRandomColorImage(10, 10, 3);
        Clip clip = new Clip(new GrayImage(5, 5));
        ColorImage outputImage = new Clip().apply(inputImage);

        ColorImage expectedOutputImage = ImageOperations.createNewColorImage(inputImage.X(), inputImage.Y(), 3);
        for (int plane = 0; plane < 3; plane++) {
            for (int y = 0; y < inputImage.Y(); y++) {
                for (int x = 0; x < inputImage.X(); x++) {
                    if (x < 5 || x > 5 || y < 5 || y > 5) {
                        expectedOutputImage.setPlane(plane, x, y, inputImage.getPlane(plane, x, y));
                    } else {
                        expectedOutputImage.setPlane(plane, x, y, inputImage.getPlane(plane, x, y).getData()[0]);
                    }
                }
            }
        }

        for (int plane = 0; plane < 3; plane++) {
            assertArrayEquals(expectedOutputImage.getPlane(plane).getData(), outputImage.getPlane(plane).getData());
        }

        assertFalse(outputImage.equals(inputImage));

        inputImage = ImageOperations.createRandomColorImage(1, 1, 3);
        clip = new Clip(new GrayImage(5, 5));
        outputImage = clip.apply(inputImage);
        assertTrue(outputImage.equals(inputImage));

        inputImage = ImageOperations.createRandomColorImage(1, 1, 3);
        clip = new Clip(new GrayImage(5, 5).flip());
        outputImage = clip.apply(inputImage);
        assertTrue(outputImage.equals(inputImage));
    }

    @Test
    public void testClipRealColorImage() {
        RealColorImage inputImage = ImageOperations.createRandomRealColorImage(10, 10, 3);
        Clip clip = new Clip(new RealGrayImage(5.5f, 5.5f));
        RealColorImage outputImage = new Clip().apply(inputImage);

        RealColorImage expectedOutputImage = ImageOperations.createNewRealColorImage(inputImage.X(), inputImage.Y(), 3);
        for (int plane = 0; plane < 3; plane++) {
            for (int y = 0; y < inputImage.Y(); y++) {
                for (int x = 0; x < inputImage.X(); x++) {
                    if (x < 5 || x > 5 || y < 5 || y > 5) {
                        expectedOutputImage.setPlane(plane, x, y, inputImage.getPlane(plane, x, y).getData());
                    } else {
                        expectedOutputImage.setPlane(plane, x, y, inputImage.getPlane(plane, x, y).getData().clone());
                        float[] pixels = expectedOutputImage.getPlane(plane, x, y).getData().clone();
                        for (int i = 0; i < pixels.length; i++) {
                            pixels[i] = 5.5f;
                        }
                    }
                }
            }
        }

        for (int plane = 0; plane < 3; plane++) {
            assertArrayEquals(expectedOutputImage.getPlane(plane).getData(), outputImage.getPlane(plane).getData(), 0.001f);
        }

        assertFalse(outputImage.equals(inputImage));

        inputImage = ImageOperations.createRandomRealColorImage(1, 1, 3);
        clip = new Clip(new RealGrayImage(5.5f, 5.5f));
        outputImage = clip.apply(inputImage);
        assertTrue(outputImage.equals(inputImage));

        inputImage = ImageOperations.createRandomRealColorImage(1, 1, 3);
        clip = new Clip(new RealGrayImage(5.5f, 5.5f).flip());
        outputImage = clip.apply(inputImage);
        assertTrue(outputImage.equals(inputImage));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithNullInput() {
        Clip clip = new Clip(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithNegativeIntMin() {
        GrayImage inputImage = ImageOperations.createRandomGrayImage(10, 10);
        Clip clip = new Clip(-1);
        clip.apply(inputImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithNegativeIntMax() {
        GrayImage inputImage = ImageOperations.createRandomGrayImage(10, 10);
        Clip clip = new Clip(inputImage.X() + 1, -1);
        clip.apply(inputImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithNegativeFloatMin() {
        RealGrayImage inputImage = ImageOperations.createRandomRealGrayImage(10, 10);
        Clip clip = new Clip(-1.5f);
        clip.apply(inputImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithNegativeFloatMax() {
        RealGrayImage inputImage = ImageOperations.createRandomRealGrayImage(10, 10);
        Clip clip = new Clip(inputImage.X() + 1.5f, -1.5f);
        clip.apply(inputImage);
    }
}