package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        try {
            new ConnectedComponents(2, 1);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        try {
            new ConnectedComponents(0, 2);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        new ConnectedComponents(1, 0);
        new ConnectedComponents(1, 1);
    }

    @Test
    public void testApply_EmptyImage() {
        BinaryImage binaryImage = new BinaryImage(0, 0);
        ConnectedComponents connectedComponents = new ConnectedComponents(1, 0);
        GrayImage expectedGrayImage = new GrayImage(0, 0);
        GrayImage actualGrayImage = connectedComponents.apply(binaryImage);

        assertEquals(expectedGrayImage, actualGrayImage);
    }

    @Test
    public void testApply_SinglePixel() {
        BinaryImage binaryImage = new BinaryImage(1, 1);
        binaryImage.set(0, 0, 1);
        ConnectedComponents connectedComponents = new ConnectedComponents(1, 0);
        GrayImage expectedGrayImage = new GrayImage(1, 1);
        expectedGrayImage.set(0, 0, 1);
        GrayImage actualGrayImage = connectedComponents.apply(binaryImage);

        assertEquals(expectedGrayImage, actualGrayImage);
    }

    @Test
    public void testApply_MultipleConnectedComponents() {
        int[][] input = {
            { 0, 0, 1, 1, 0, 0, 1, 1, 0, 0 },
            { 0, 0, 1, 1, 0, 0, 1, 1, 0, 0 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            { 0, 0, 1, 1, 0, 0, 1, 1, 0, 0 },
            { 0, 0, 1, 1, 0, 0, 1, 1, 0, 0 }
        };

        BinaryImage binaryImage = new BinaryImage(5, 5);
        for (int y = 0; y < binaryImage.Y(); ++y) {
            for (int x = 0; x < binaryImage.X(); ++x) {
                binaryImage.set(x, y, input[y][x]);
            }
        }

        ConnectedComponents connectedComponents = new ConnectedComponents(1, 0);
        GrayImage expectedGrayImage = new GrayImage(5, 5);
        int label = 1;
        for (int y = 0; y < binaryImage.Y(); ++y) {
            for (int x = 0; x < binaryImage.X(); ++x) {
                int inputValue = binaryImage.get(x, y);
                if (inputValue != 0) {
                    expectedGrayImage.set(x, y, Integer.valueOf(label++));
                }
            }
        }

        GrayImage actualGrayImage = connectedComponents.apply(binaryImage);

        assertEquals(expectedGrayImage, actualGrayImage);
    }

    @Test(expected = Exception.class)
    public void testApply_InvalidImage() {
        BinaryImage binaryImage = new BinaryImage(-1, 1);
        ConnectedComponents connectedComponents = new ConnectedComponents(1, 0);
        connectedComponents.apply(binaryImage);
    }
}