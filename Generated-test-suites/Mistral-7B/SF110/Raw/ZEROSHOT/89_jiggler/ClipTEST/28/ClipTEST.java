package jigl.image.ops.levelOps;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import jigl.image.ops.SimpleOperator;
import jigl.image.ops.levelOps.Clip;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.types.ColorImage;
import jigl.image.types.RealColorImage;
import jigl.image.ops.ROI;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

import org.junit.Test;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        int min = 5;
        int max = 10;
        Clip clip = new Clip(min, max);
        GrayImage input = new GrayImage(10, 10, 1);
        for (int i = 0; i < input.numPixels(); i++) {
            input.set(i, (byte) (i % 20 + 1));
        }

        GrayImage output = clip.apply(input);
        for (int y = 0; y < output.Y(); y++) {
            for (int x = 0; x < output.X(); x++) {
                int expected = Math.min(Math.max(input.get(x, y), min), max);
                int actual = output.get(x, y);
                assertTrue("At (" + x + ", " + y + "), expected value is " + expected + " but actual value is " + actual, actual == expected);
            }
        }
    }

    @Test
    public void testClipIntIntWithZeroMinMax() {
        int min = 0;
        int max = 0;
        Clip clip = new Clip(min, max);
        GrayImage input = new GrayImage(10, 10, 1);

        clip.apply(input); // Set min and max based on input image

        GrayImage output = clip.apply(input);
        for (int y = 0; y < output.Y(); y++) {
            for (int x = 0; x < output.X(); x++) {
                int expected = Math.min(Math.max(input.get(x, y), min), max);
                int actual = output.get(x, y);
                assertTrue("At (" + x + ", " + y + "), expected value is " + expected + " but actual value is " + actual, actual == expected);
            }
        }
    }

    @Test
    public void testClipFloatFloat() {
        float min = 5.5f;
        float max = 10.5f;
        Clip clip = new Clip(min, max);
        RealGrayImage input = new RealGrayImage(10, 10, 1);
        for (int i = 0; i < input.numPixels(); i++) {
            input.set(i, (float) (i % 20 + 1.1f));
        }

        RealGrayImage output = clip.apply(input);
        for (int y = 0; y < output.Y(); y++) {
            for (int x = 0; x < output.X(); x++) {
                float expected = Math.min(Math.max(input.get(x, y), min), max);
                float actual = output.get(x, y);
                assertTrue("At (" + x + ", " + y + "), expected value is " + expected + " but actual value is " + actual, Float.compare(expected, actual) == 0);
            }
        }
    }

    @Test
    public void testClipFloatFloatWithZeroMinMax() {
        float min = 0.0f;
        float max = 0.0f;
        Clip clip = new Clip(min, max);
        RealGrayImage input = new RealGrayImage(10, 10, 1);

        clip.apply(input); // Set min and max based on input image

        RealGrayImage output = clip.apply(input);
        for (int y = 0; y < output.Y(); y++) {
            for (int x = 0; x < output.X(); x++) {
                float expected = Math.min(Math.max(input.get(x, y), min), max);
                float actual = output.get(x, y);
                assertTrue("At (" + x + ", " + y + "), expected value is " + expected + " but actual value is " + actual, Float.compare(expected, actual) == 0);
            }
        }
    }

    @Test
    public void testClipColorImage() {
        int min = 5;
        int max = 10;
        Clip clip = new Clip(min, max);
        ColorImage input = new ColorImage(10, 10, 3);
        for (int y = 0; y < input.height(); y++) {
            for (int x = 0; x < input.width(); x++) {
                for (int c = 0; c < 3; c++) {
                    input.set(x, y, (byte) (c == 0 ? (x % 20 + 1) : (x % 20 + 10)));
                }
            }
        }

        ColorImage output = clip.apply(input);
        for (int y = 0; y < output.height(); y++) {
            for (int x = 0; x < output.width(); x++) {
                for (int c = 0; c < 3; c++) {
                    int channelMin = min;
                    int channelMax = max;
                    int channel = c * 3;
                    for (int i = 0; i < input.numPixels(); i++) {
                        int value = input.get(x, y, channel);
                        if (value < channelMin) {
                            channelMin = value;
                        }
                        if (value > channelMax) {
                            channelMax = value;
                        }
                    }
                    int expected = Math.min(Math.max(input.get(x, y, channel), channelMin), channelMax);
                    int actual = output.get(x, y, channel);
                    assertTrue("At (" + x + ", " + y + ") for channel " + c + ", expected value is " + expected + " but actual value is " + actual, actual == expected);
                }
            }
        }
    }

    @Test
    public void testClipRealColorImage() {
        float min = 5.5f;
        float max = 10.5f;
        Clip clip = new Clip(min, max);
        RealColorImage input = new RealColorImage(10, 10, 3);
        for (int y = 0; y < input.height(); y++) {
            for (int x = 0; x < input.width(); x++) {
                for (int c = 0; c < 3; c++) {
                    input.set(x, y, (float) (c == 0 ? (x % 20 + 1.1f) : (x % 20 + 11.1f)));
                }
            }
        }

        RealColorImage output = clip.apply(input);
        for (int y = 0; y < output.height(); y++) {
            for (int x = 0; x < output.width(); x++) {
                for (int c = 0; c < 3; c++) {
                    float channelMin = min;
                    float channelMax = max;
                    int channel = c * 3;
                    for (int i = 0; i < input.numPixels(); i++) {
                        float value = input.get(x, y, channel);
                        if (value < channelMin) {
                            channelMin = value;
                        }
                        if (value > channelMax) {
                            channelMax = value;
                        }
                    }
                    float expected = Math.min(Math.max(input.get(x, y, channel), channelMin), channelMax);
                    float actual = output.get(x, y, channel);
                    assertTrue("At (" + x + ", " + y + ") for channel " + c + ", expected value is " + expected + " but actual value is " + actual, Float.compare(expected, actual) == 0);
                }
            }
        }
    }

    @Test
    public void testClipWithMainMethod() throws Exception {
        String inputFile = "test_input.bmp";
        String outputFile = "test_output.bmp";
        float min = 5.5f;
        float max = 10.5f;

        // Create input image and save it to a file
        Image image = new GrayImage(10, 10, 1);
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, (byte) (x % 20 + 1));
            }
        }
        ImageOutputStream outputStream = new ImageOutputStream(new FileOutputStream(inputFile));
        outputStream.write(image);
        outputStream.close();

        // Run Clip.main method with input file, output file, min, and max values
        String[] argv = {"5.5", "10.5", inputFile, outputFile};
        Clip.main(argv);

        // Read output image and compare it with expected image
        ImageInputStream inputStream = new ImageInputStream(new FileInputStream(outputFile));
        GrayImage expectedOutput = input.clone();
        GrayImage actualOutput = (GrayImage) inputStream.read();
        inputStream.close();

        assertArrayEquals("Images do not match pixel by pixel", expectedOutput.getData(), actualOutput.getData());
    }
}