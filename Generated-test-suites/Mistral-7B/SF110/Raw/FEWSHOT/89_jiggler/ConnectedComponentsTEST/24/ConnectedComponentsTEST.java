package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import jigl.image.types.GrayImage;
import jigl.image.types.IntegerGrayImage;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        assertNotNull(cc);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsConstructorInvalidArguments() {
        new ConnectedComponents(2, 1);
    }

    @Test
    public void testApplyMethodForSimpleCase() {
        BinaryImage binaryImage = new BinaryImage(1, 1, new int[][]{{1}});
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        IntegerGrayImage output = connectedComponents.apply(binaryImage);
        int[] expectedOutput = {1};
        assertArrayEquals(expectedOutput, output.getPixels());
    }

    @Test
    public void testApplyMethodForComplexCase() {
        BinaryImage binaryImage = new BinaryImage(3, 3, new int[][]{{1, 1, 1}, {1, 2, 1}, {1, 1, 1}});
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        IntegerGrayImage output = connectedComponents.apply(binaryImage);
        int[] expectedOutput = {1, 2, 3};
        assertArrayEquals(expectedOutput, output.getPixels());
    }

    @Test
    public void testApplyMethodForComplexCaseWithFourConnected() {
        BinaryImage binaryImage = new BinaryImage(3, 3, new int[][]{{1, 1, 1}, {1, 2, 1}, {1, 1, 1}});
        ConnectedComponents connectedComponents = new ConnectedComponents(4, 0);
        IntegerGrayImage output = connectedComponents.apply(binaryImage);
        int[] expectedOutput = {1, 2, 3};
        assertArrayEquals(expectedOutput, output.getPixels());
    }

    @Test
    public void testApplyMethodForComplexCaseWithEightConnected() {
        BinaryImage binaryImage = new BinaryImage(3, 3, new int[][]{{1, 1, 1}, {1, 2, 1}, {1, 1, 1}});
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 8);
        IntegerGrayImage output = connectedComponents.apply(binaryImage);
        int[] expectedOutput = {1, 2};
        assertArrayEquals(expectedOutput, output.getPixels());
    }

    @Test
    public void testApplyMethodForComplexCaseWithBackground() {
        BinaryImage binaryImage = new BinaryImage(3, 3, new int[][]{{1, 1, 1}, {2, 2, 0}, {1, 1, 1}});
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        IntegerGrayImage output = connectedComponents.apply(binaryImage);
        int[] expectedOutput = {1, 2, 3};
        assertArrayEquals(expectedOutput, output.getPixels());
    }
}