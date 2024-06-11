
package jigl.image.ops;

import jigl.image.types.*;
import jigl.image.*;
import jigl.image.exceptions.*;
import org.junit.Assert;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertFalse(cc.hasException());
        ConnectedComponents ccInvalidArguments = new ConnectedComponents(2, 2);
        assertTrue(ccInvalidArguments.hasException());
    }

    @Test
    public void testConnectedComponentsApplyFourConnected() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(1, 1, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 2, 1);
        inputImage.set(1, 3, 1);
        inputImage.set(2, 3, 1);
        inputImage.set(3, 1, 1);
        inputImage.set(3, 2, 1);
        inputImage.set(3, 3, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage expectedOutput = new GrayImage(3, 3);
        expectedOutput.set(1, 1, 1);
        expectedOutput.set(1, 2, 1);
        expectedOutput.set(1, 3, 1);
        expectedOutput.set(2, 1, 1);
        expectedOutput.set(2, 2, 2);
        expectedOutput.set(2, 3, 2);
        expectedOutput.set(3, 1, 3);
        expectedOutput.set(3, 2, 3);
        expectedOutput.set(3, 3, 3);

        GrayImage output = cc.apply(inputImage);
        Assert.assertArrayEquals(expectedOutput.getData(), output.getData());
    }

    @Test
    public void testConnectedComponentsApplyEightConnected() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(1, 1, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 2, 1);
        inputImage.set(1, 3, 1);
        inputImage.set(2, 3, 1);
        inputImage.set(3, 1, 1);
        inputImage.set(3, 2, 1);
        inputImage.set(3, 3, 1);

        inputImage.set(0, 0, 1);
        inputImage.set(0, 1, 1);
        inputImage.set(0, 2, 1);
        inputImage.set(0, 3, 1);
        inputImage.set(1, 0, 1);
        inputImage.set(1, 3, 1);
        inputImage.set(2, 0, 1);
        inputImage.set(2, 3, 1);
        inputImage.set(3, 0, 1);
        inputImage.set(3, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage expectedOutput = new GrayImage(3, 3);
        expectedOutput.set(0, 0, 1);
        expectedOutput.set(0, 1, 1);
        expectedOutput.set(0, 2, 1);
        expectedOutput.set(0, 3, 1);
        expectedOutput.set(1, 0, 1);
        expectedOutput.set(1, 1, 2);
        expectedOutput.set(1, 2, 2);
        expectedOutput.set(1, 3, 2);
        expectedOutput.set(2, 0, 2);
        expectedOutput.set(2, 1, 2);
        expectedOutput.set(2, 2, 3);
        expectedOutput.set(2, 3, 3);
        expectedOutput.set(3, 0, 3);
        expectedOutput.set(3, 1, 3);
        expectedOutput.set(3, 2, 3);
        expectedOutput.set(3, 3, 3);

        GrayImage output = cc.apply(inputImage);
        Assert.assertArrayEquals(expectedOutput.getData(), output.getData());
    }

    @Test
    public void testConnectedComponentsApplyEmptyImage() {
        BinaryImage inputImage = new BinaryImage(0, 0);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage output = cc.apply(inputImage);
        assertTrue(output.isEmpty());
    }

    @Test
    public void testConnectedComponentsConstructorInvalidArguments() {
        ConnectedComponents cc = new ConnectedComponents(2, 2);
        assertTrue(cc.hasException());
    }

}
