package jigl.image.ops.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.InvalidArgumentException;
import jigl.image.types.GrayImage;
import jigl.image.types.BinaryImage;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructorInvalidArguments() {
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(2, 2));
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(0, 2));
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(0, 1));
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(-1, 2));
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(2, -1));
    }

    @Test
    public void testConnectedComponentsBasicFunctionality() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(0, 0, 1);
        inputImage.set(1, 0, 1);
        inputImage.set(1, 1, 1);
        inputImage.set(2, 0, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 0);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);

        GrayImage outputImage = cc.apply(inputImage);

        GrayImage expectedOutputImage = new GrayImage(3, 3);
        expectedOutputImage.set(0, 0, 1);
        expectedOutputImage.set(1, 0, 1);
        expectedOutputImage.set(1, 1, 2);
        expectedOutputImage.set(2, 0, 1);
        expectedOutputImage.set(2, 1, 2);
        expectedOutputImage.set(2, 2, 0);

        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                assertEquals(expectedOutputImage.get(x, y), outputImage.get(x, y));
            }
        }
    }

    @Test
    public void testConnectedComponentsWithBackground() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(0, 0, 0);
        inputImage.set(1, 0, 1);
        inputImage.set(1, 1, 1);
        inputImage.set(2, 0, 0);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 0);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);

        GrayImage outputImage = cc.apply(inputImage);

        GrayImage expectedOutputImage = new GrayImage(3, 3);
        expectedOutputImage.set(0, 0, 0);
        expectedOutputImage.set(1, 0, 1);
        expectedOutputImage.set(1, 1, 2);
        expectedOutputImage.set(2, 0, 0);
        expectedOutputImage.set(2, 1, 2);
        expectedOutputImage.set(2, 2, 0);

        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                assertEquals(expectedOutputImage.get(x, y), outputImage.get(x, y));
            }
        }
    }

    @Test
    public void testConnectedComponentsEightNeighbors() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(0, 0, 1);
        inputImage.set(1, 0, 1);
        inputImage.set(1, 1, 1);
        inputImage.set(2, 0, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);

        GrayImage outputImage = cc.apply(inputImage);

        GrayImage expectedOutputImage = new GrayImage(3, 3);
        expectedOutputImage.set(0, 0, 1);
        expectedOutputImage.set(1, 0, 1);
        expectedOutputImage.set(1, 1, 2);
        expectedOutputImage.set(2, 0, 1);
        expectedOutputImage.set(2, 1, 2);
        expectedOutputImage.set(2, 2, 2);

        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                assertEquals(expectedOutputImage.get(x, y), outputImage.get(x, y));
            }
        }
    }

    @Test
    public void testConnectedComponentsEdgeCases() {
        BinaryImage inputImageAllZero = new BinaryImage(1, 1);
        inputImageAllZero.set(0, 0, 0);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage outputImageAllZero = cc.apply(inputImageAllZero);
        assertEquals(Integer.valueOf(0), outputImageAllZero.get(0, 0));

        BinaryImage inputImageAllOne = new BinaryImage(1, 1);
        inputImageAllOne.set(0, 0, 1);

        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage outputImageAllOne = cc.apply(inputImageAllOne);
        assertEquals(Integer.valueOf(1), outputImageAllOne.get(0, 0));

        BinaryImage inputImageOnlyBackground = new BinaryImage(1, 1);
        inputImageOnlyBackground.set(0, 0, 0);

        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage outputImageOnlyBackground = cc.apply(inputImageOnlyBackground);
        assertEquals(Integer.valueOf(0), outputImageOnlyBackground.get(0, 0));

        BinaryImage inputImageEmpty = new BinaryImage(0, 0);

        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage outputImageEmpty = cc.apply(inputImageEmpty);
        // Empty image doesn't have any pixels, so we can't validate it
    }
}