
import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.BinaryImage;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructorValidArguments() {
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        assertEquals(1, cc.neighbors);
        assertEquals(0, cc.background);
    }

    @Test
    public void testConnectedComponentsEmptyImage() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage expectedOutput = new GrayImage(3, 3);
        expectedOutput.setAll(Integer.valueOf(0));
        GrayImage actualOutput = cc.apply(inputImage);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsInvalidNumberOfNeighbors() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 0);
        } catch (InvalidArgumentException e) {
            // expected
        }
    }

    @Test
    public void testConnectedComponentsDisconnectedComponents() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(1, 1, 1);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 2, 1);
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage expectedOutput = new GrayImage(3, 3);
        expectedOutput.set(1, 1, Integer.valueOf(1));
        expectedOutput.set(1, 2, Integer.valueOf(1));
        expectedOutput.set(2, 2, Integer.valueOf(1));
        expectedOutput.set(1, 1, Integer.valueOf(1));
        expectedOutput.set(1, 2, Integer.valueOf(1));
        expectedOutput.set(2, 2, Integer.valueOf(1));
        GrayImage actualOutput = cc.apply(inputImage);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsInvalidBackgroundColor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(1, 2);
        } catch (InvalidArgumentException e) {
            // expected
        }
    }

    @Test
    public void testConnectedComponentsValidImage() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(1, 1, 1);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 1);
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage expectedOutput = new GrayImage(3, 3);
        expectedOutput.set(1, 1, Integer.valueOf(1));
        expectedOutput.set(1, 2, Integer.valueOf(1));
        expectedOutput.set(2, 1, Integer.valueOf(1));
        expectedOutput.set(2, 2, Integer.valueOf(1));
        GrayImage actualOutput = cc.apply(inputImage);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testConnectedComponentsOnlyBackground() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.setAll(0);
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage expectedOutput = new GrayImage(3, 3);
        expectedOutput.setAll(Integer.valueOf(0));
        GrayImage actualOutput = cc.apply(inputImage);
        assertEquals(expectedOutput, actualOutput);
    }
}