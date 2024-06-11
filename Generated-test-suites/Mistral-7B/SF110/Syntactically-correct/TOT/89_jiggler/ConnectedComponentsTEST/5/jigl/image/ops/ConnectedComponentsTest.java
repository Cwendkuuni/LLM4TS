
package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.*;
import org.junit.Test;

public class ConnectedComponentsTest {
    @Test
    public void testConnectedComponentsConstructorValid() {
        ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 0);
        assertNotNull(cc);
    }

    @Test
    public void testConnectedComponentsEmptyImage() {
        BinaryImage inputImage = new BinaryImage(0, 0);
        ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 0);
        GrayImage outputImage = cc.apply(inputImage);
        assertEquals(0, outputImage.getWidth());
        assertEquals(0, outputImage.getHeight());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsInvalidArguments() {
        ConnectedComponents cc = new ConnectedComponents(2, 3);
    }

    @Test
    public void testConnectedComponentsSingleConnectedComponent() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(1, 1, 1);
        ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 0);
        GrayImage outputImage = cc.apply(inputImage);
        assertArrayEquals(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1}, outputImage.getData());
    }

    @Test
    public void testConnectedComponentsMultipleConnectedComponents() {
        BinaryImage inputImage = new BinaryImage(5, 5);
        inputImage.set(1, 1, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 2, 1);
        inputImage.set(3, 3, 1);
        inputImage.set(3, 4, 1);
        ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 0);
        GrayImage outputImage = cc.apply(inputImage);
        // Expected output depends on the actual expected connected components
        // Then you can use assertArrayEquals or other assertions to compare the results
    }

    @Test
    public void testConnectedComponentsEdgeCase() {
        BinaryImage inputImage = new BinaryImage(15, 15);
        // Fill the image with complex data, e.g. a cross or a star shape
        ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 0);
        GrayImage outputImage = cc.apply(inputImage);
        // Expected output depends on the actual expected connected components
        // Then you can use assertArrayEquals or other assertions to compare the results
    }

    @Test
    public void testConnectedComponentsConstructorEdgeCase() {
        ConnectedComponents cc = new ConnectedComponents(10, 255);
        assertEquals(10, cc.getNeighbors());
        assertEquals(255, cc.getBackground());
    }

    @Test
    public void testConnectedComponentsApplyTypicalUseCase() {
        BinaryImage inputImage = new BinaryImage(5, 5);
        inputImage.set(1, 1, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 2, 1);
        ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 0);
        GrayImage outputImage = cc.apply(inputImage);
        assertArrayEquals(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1}, outputImage.getData());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsApplyErrorCase() {
        BinaryImage inputImage = new BinaryImage(1, 1);
        inputImage.set(0, 0, 1);
        ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 0);
        try {
            cc.apply(inputImage);
            fail("Expected InvalidArgumentException to be thrown");
        } catch (InvalidArgumentException e) {
            // Expected, continue with the test
        }
    }
}
