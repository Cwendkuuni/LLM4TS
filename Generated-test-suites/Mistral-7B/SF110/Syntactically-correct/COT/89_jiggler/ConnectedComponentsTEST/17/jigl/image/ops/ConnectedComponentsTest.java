package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.types.IntegerImage;
import jigl.image.exceptions.InvalidArgumentException;

import org.junit.Test;

public class ConnectedComponentsTest {

    // Test constructor with valid arguments
    @Test
    public void testConnectedComponentsValidConstructor() {
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        assertNotNull(cc);
    }

    // Test constructor with invalid arguments
    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsInvalidConstructor() {
        new ConnectedComponents(2, 3);
    }

    // Test apply method with valid image and valid neighbors and background
    @Test
    public void testApplyValidImageValidBackgroundValidNeighbors() throws Exception {
        BinaryImage inputImage = new BinaryImage(5, 5);
        inputImage.set(1, 1, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 1);
        inputImage.set(3, 2, 1);
        inputImage.set(4, 2, 1);
        inputImage.set(1, 3, 1);
        inputImage.set(2, 3, 1);
        inputImage.set(3, 3, 1);
        inputImage.set(4, 3, 1);
        inputImage.set(1, 4, 1);
        inputImage.set(2, 4, 1);
        inputImage.set(3, 4, 1);
        inputImage.set(4, 4, 1);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage expectedOutput = new GrayImage(5, 5);
        expectedOutput.set(1, 1, 1);
        expectedOutput.set(2, 1, 1);
        expectedOutput.set(2, 2, 1);
        expectedOutput.set(3, 2, 1);
        expectedOutput.set(4, 2, 1);
        expectedOutput.set(1, 3, 2);
        expectedOutput.set(2, 3, 2);
        expectedOutput.set(3, 3, 2);
        expectedOutput.set(4, 3, 2);
        expectedOutput.set(1, 4, 3);
        expectedOutput.set(2, 4, 3);
        expectedOutput.set(3, 4, 3);
        expectedOutput.set(4, 4, 3);
        IntegerImage output = cc.apply(inputImage);
        assertEquals(expectedOutput, output);
    }

    // Test apply method with empty image and valid neighbors and background
    @Test
    public void testApplyEmptyImageValidBackgroundValidNeighbors() throws Exception {
        BinaryImage inputImage = new BinaryImage(0, 0);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage expectedOutput = new GrayImage(0, 0);
        IntegerImage output = cc.apply(inputImage);
        assertEquals(expectedOutput, output);
    }

    // Test apply method with image having only background and valid neighbors and background
    @Test
    public void testApplyBackgroundImageValidBackgroundValidNeighbors() throws Exception {
        BinaryImage inputImage = new BinaryImage(5, 5);
        for (int x = 0; x < inputImage.X(); ++x) {
            for (int y = 0; y < inputImage.Y(); ++y) {
                inputImage.set(x, y, 0);
            }
        }
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage expectedOutput = new GrayImage(5, 5);
        expectedOutput.set(0, 0, 0);
        IntegerImage output = cc.apply(inputImage);
        assertEquals(expectedOutput, output);
    }

    // Test apply method with image having connected components and valid neighbors and background
    @Test
    public void testApplyConnectedComponentsValidBackgroundValidNeighbors() throws Exception {
        BinaryImage inputImage = new BinaryImage(5, 5);
        inputImage.set(1, 1, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 1);
        inputImage.set(3, 2, 1);
        inputImage.set(4, 2, 1);
        inputImage.set(1, 3, 1);
        inputImage.set(2, 3, 1);
        inputImage.set(3, 3, 1);
        inputImage.set(4, 3, 1);
        inputImage.set(1, 4, 1);
        inputImage.set(2, 4, 1);
        inputImage.set(3, 4, 1);
        inputImage.set(4, 4, 1);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage expectedOutput = new GrayImage(5, 5);
        expectedOutput.set(1, 1, 1);
        expectedOutput.set(2, 1, 1);
        expectedOutput.set(2, 2, 1);
        expectedOutput.set(3, 2, 1);
        expectedOutput.set(4, 2, 1);
        expectedOutput.set(1, 3, 2);
        expectedOutput.set(2, 3, 2);
        expectedOutput.set(3, 3, 2);
        expectedOutput.set(4, 3, 2);
        expectedOutput.set(1, 4, 3);
        expectedOutput.set(2, 4, 3);
        expectedOutput.set(3, 4, 3);
        expectedOutput.set(4, 4, 3);
        IntegerImage output = cc.apply(inputImage);
        assertEquals(expectedOutput, output);
    }

    // Test apply method with image having connected components and invalid neighbors
    @Test(expected = InvalidArgumentException.class)
    public void testApplyConnectedComponentsInvalidNeighbors() throws Exception {
        BinaryImage inputImage = new BinaryImage(5, 5);
        inputImage.set(1, 1, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 1);
        inputImage.set(3, 2, 1);
        inputImage.set(4, 2, 1);
        inputImage.set(1, 3, 1);
        inputImage.set(2, 3, 1);
        inputImage.set(3, 3, 1);
        inputImage.set(4, 3, 1);
        inputImage.set(1, 4, 1);
        inputImage.set(2, 4, 1);
        inputImage.set(3, 4, 1);
        inputImage.set(4, 4, 1);
        ConnectedComponents cc = new ConnectedComponents(2, 0); // invalid neighbors
        cc.apply(inputImage);
    }

    // Test apply method with image having connected components and invalid background
    @Test(expected = InvalidArgumentException.class)
    public void testApplyConnectedComponentsInvalidBackground() throws Exception {
        BinaryImage inputImage = new BinaryImage(5, 5);
        inputImage.set(1, 1, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 1);
        inputImage.set(3, 2, 1);
        inputImage.set(4, 2, 1);
        inputImage.set(1, 3, 1);
        inputImage.set(2, 3, 1);
        inputImage.set(3, 3, 1);
        inputImage.set(4, 3, 1);
        inputImage.set(1, 4, 1);
        inputImage.set(2, 4, 1);
        inputImage.set(3, 4, 1);
        inputImage.set(4, 4, 1);
        ConnectedComponents cc = new ConnectedComponents(0, 2); // invalid background
        cc.apply(inputImage);
    }

    // Test apply method with image having connected components and four connected neighbors
    @Test
    public void testApplyConnectedComponentsFourConnected() throws Exception {
        BinaryImage inputImage = new BinaryImage(5, 5);
        inputImage.set(1, 1, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 1);
        inputImage.set(3, 2, 1);
        inputImage.set(4, 2, 1);
        inputImage.set(1, 3, 1);
        inputImage.set(2, 3, 1);
        inputImage.set(3, 3, 1);
        inputImage.set(4, 3, 1);
        inputImage.set(1, 4, 1);
        inputImage.set(2, 4, 1);
        inputImage.set(3, 4, 1);
        inputImage.set(4, 4, 1);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage expectedOutput = new GrayImage(5, 5);
        expectedOutput.set(1, 1, 1);
        expectedOutput.set(2, 1, 1);
        expectedOutput.set(2, 2, 1);
        expectedOutput.set(3, 2, 1);
        expectedOutput.set(4, 2, 1);
        expectedOutput.set(1, 3, 2);
        expectedOutput.set(2, 3, 2);
        expectedOutput.set(3, 3, 2);
        expectedOutput.set(4, 3, 2);
        expectedOutput.set(1, 4, 3);
        expectedOutput.set(2, 4, 3);
        expectedOutput.set(3, 4, 3);
        expectedOutput.set(4, 4, 3);
        IntegerImage output = cc.apply(inputImage);
        assertEquals(expectedOutput, output);
    }

    // Test apply method with image having connected components and eight connected neighbors
    @Test
    public void testApplyConnectedComponentsEightConnected() throws Exception {
        BinaryImage inputImage = new BinaryImage(5, 5);
        inputImage.set(1, 1, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 1);
        inputImage.set(3, 2, 1);
        inputImage.set(4, 2, 1);
        inputImage.set(1, 3, 1);
        inputImage.set(2, 3, 1);
        inputImage.set(3, 3, 1);
        inputImage.set(4, 3, 1);
        inputImage.set(1, 4, 1);
        inputImage.set(2, 4, 1);
        inputImage.set(3, 4, 1);
        inputImage.set(4, 4, 1);
        inputImage.set(5, 4, 1);
        inputImage.set(6, 4, 1);
        inputImage.set(5, 3, 1);
        inputImage.set(6, 3, 1);
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage expectedOutput = new GrayImage(5, 5);
        expectedOutput.set(1, 1, 1);
        expectedOutput.set(2, 1, 1);
        expectedOutput.set(2, 2, 1);
        expectedOutput.set(3, 2, 1);
        expectedOutput.set(4, 2, 1);
        expectedOutput.set(1, 3, 2);
        expectedOutput.set(2, 3, 2);
        expectedOutput.set(3, 3, 2);
        expectedOutput.set(4, 3, 2);
        expectedOutput.set(1, 4, 3);
        expectedOutput.set(2, 4, 3);
        expectedOutput.set(3, 4, 3);
        expectedOutput.set(4, 4, 3);
        expectedOutput.set(5, 4, 4);
        expectedOutput.set(6, 4, 4);
        expectedOutput.set(5, 3, 4);
        expectedOutput.set(6, 3, 4);
        IntegerImage output = cc.apply(inputImage);
        assertEquals(expectedOutput, output);
    }

    // Test apply method with image having connected components and various edge cases
    @Test
    public void testApplyConnectedComponentsEdgeCases() throws Exception {
        BinaryImage inputImage = new BinaryImage(5, 5);
        
        // Edge case 1: All pixels are background
        for (int x = 0; x < inputImage.X(); ++x) {
            for (int y = 0; y < inputImage.Y(); ++y) {
                inputImage.set(x, y, 0);
            }
        }
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage expectedOutput1 = new GrayImage(5, 5);
        expectedOutput1.set(0, 0, 0);
        IntegerImage output1 = cc.apply(inputImage);
        assertEquals(expectedOutput1, output1);

        // Edge case 2: All pixels are connected component
        for (int x = 0; x < inputImage.X(); ++x) {
            for (int y = 0; y < inputImage.Y(); ++y) {
                inputImage.set(x, y, 1);
            }
        }
        ConnectedComponents cc2 = new ConnectedComponents(0, 0);
        IntegerImage output2 = cc2.apply(inputImage);
        GrayImage expectedOutput2 = new GrayImage(5, 5);
        for (int x = 0; x < inputImage.X(); ++x) {
            for (int y = 0; y < inputImage.Y(); ++y) {
                expectedOutput2.set(x, y, 1);
            }
        }
        assertEquals(expectedOutput2, output2);

        // Edge case 3: Connected components at edges
        inputImage.set(0, 0, 1);
        inputImage.set(0, 1, 1);
        inputImage.set(1, 0, 1);
        inputImage.set(1, 1, 1);
        ConnectedComponents cc3 = new ConnectedComponents(0, 0);
        GrayImage expectedOutput3 = new GrayImage(5, 5);
        expectedOutput3.set(0, 0, 1);
        expectedOutput3.set(0, 1, 2);
        expectedOutput3.set(1, 0, 1);
        expectedOutput3.set(1, 1, 2);
        IntegerImage output3 = cc3.apply(inputImage);
        assertEquals(expectedOutput3, output3);
    }
}