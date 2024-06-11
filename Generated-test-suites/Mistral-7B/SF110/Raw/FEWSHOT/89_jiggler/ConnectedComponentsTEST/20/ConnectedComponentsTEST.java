package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import jigl.image.types.GrayImage;
import jigl.image.ImageFactory;
import jigl.image.exceptions.InvalidArgumentException;

import org.junit.Test;

public class ConnectedComponentsTest {

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsWithInvalidArguments() {
        new ConnectedComponents(-1, 1);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsWithInvalidBackground() {
        new ConnectedComponents(0, -1);
    }

    @Test
    public void testConnectedComponentsWithFourConnected() {
        final int width = 5;
        final int height = 5;
        final byte[] inputData = new byte[] { 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 };
        final BinaryImage inputImage = new GrayImage(width, height, inputData);
        final ConnectedComponents connectedComponents = new ConnectedComponents(0, 1);
        final GrayImage outputImage = connectedComponents.apply(inputImage);
        final byte[] expectedOutputData = new byte[] { 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 };
        final byte[] actualOutputData = new byte[outputImage.size()];
        for (int i = 0; i < outputImage.size(); ++i) {
            actualOutputData[i] = ((Number) outputImage.get(i)).byteValue();
        }
        assertArrayEquals(expectedOutputData, actualOutputData);
    }

    @Test
    public void testConnectedComponentsWithEightConnected() {
        final int width = 5;
        final int height = 5;
        final byte[] inputData = new byte[] { 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 };
        final BinaryImage inputImage = new GrayImage(width, height, inputData);
        final ConnectedComponents connectedComponents = new ConnectedComponents(1, 1);
        final GrayImage outputImage = connectedComponents.apply(inputImage);
        final byte[] expectedOutputData = new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
        final byte[] actualOutputData = new byte[outputImage.size()];
        for (int i = 0; i < outputImage.size(); ++i) {
            actualOutputData[i] = ((Number) outputImage.get(i)).byteValue();
        }
        assertArrayEquals(expectedOutputData, actualOutputData);
    }

    @Test
    public void testConnectedComponentsWithRandomInput() {
        final int width = 25;
        final int height = 25;
        final byte[] inputData = ImageFactory.randomBinaryImageData(width, height);
        final BinaryImage inputImage = new GrayImage(width, height, inputData);
        final ConnectedComponents connectedComponents = new ConnectedComponents(0, 1);
        final GrayImage outputImage = connectedComponents.apply(inputImage);
        final int numPixels = width * height;
        int currentComponent = 1;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (inputImage.get(x, y) != 0) {
                    assertTrue(outputImage.get(x, y) instanceof Integer);
                    assertEquals(currentComponent, ((Number) outputImage.get(x, y)).intValue());
                    ++currentComponent;
                }
            }
        }
        assertTrue(currentComponent <= numPixels);
    }

    @Test
    public void testConnectedComponentsWithInvalidBackgroundValue() {
        final int width = 5;
        final int height = 5;
        final byte[] inputData = new byte[] { 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 };
        final BinaryImage inputImage = new GrayImage(width, height, inputData);
        try {
            new ConnectedComponents(0, 2);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConnectedComponentsWithInvalidNumberOfNeighbors() {
        final int width = 5;
        final int height = 5;
        final byte[] inputData = new byte[] { 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0 };
        final BinaryImage inputImage = new GrayImage(width, height, inputData);
        try {
            new ConnectedComponents(2, 1);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConnectedComponentsWithNullInputImage() {
        try {
            new ConnectedComponents(0, 1).apply(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}