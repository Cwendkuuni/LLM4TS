
package jigl.image.ops;
import org.junit.Assert;
import org.junit.Test;
import jigl.image.*;
import jigl.image.ops.*;

public class ConnectedComponentsTest {

    @Test
    public void testInvalidArgumentException() {
        // Given
        int numberNeighbors = 2;
        int backgroundColor = 3;

        // When & Then
        try {
            new ConnectedComponents(numberNeighbors, backgroundColor);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        // Given
        numberNeighbors = 3;
        backgroundColor = 0;

        // When & Then
        try {
            new ConnectedComponents(numberNeighbors, backgroundColor);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        // Given
        numberNeighbors = 1;
        backgroundColor = 1;

        // When
        ConnectedComponents cc = new ConnectedComponents(numberNeighbors, backgroundColor);

        // Then
        assertEquals(numberNeighbors, cc.neighbors);
        assertEquals(backgroundColor, cc.background);
    }

    @Test
    public void testConnectedComponents() {
        // Given
        BinaryImage inputImage = new BinaryImage(5, 5);
        inputImage.set(1, 1, 1);
        inputImage.set(1, 2, 1);
        inputImage.set(1, 3, 1);
        inputImage.set(1, 4, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 0);
        inputImage.set(2, 3, 1);
        inputImage.set(2, 4, 1);
        inputImage.set(3, 1, 0);
        inputImage.set(3, 2, 1);
        inputImage.set(3, 3, 1);
        inputImage.set(3, 4, 1);
        inputImage.set(4, 1, 0);
        inputImage.set(4, 2, 0);
        inputImage.set(4, 3, 1);
        inputImage.set(4, 4, 1);

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage expectedOutput = new GrayImage(5, 5);
        expectedOutput.set(1, 1, 1);
        expectedOutput.set(1, 2, 1);
        expectedOutput.set(1, 3, 1);
        expectedOutput.set(1, 4, 1);
        expectedOutput.set(2, 1, 1);
        expectedOutput.set(2, 2, 0);
        expectedOutput.set(2, 3, 1);
        expectedOutput.set(2, 4, 1);
        expectedOutput.set(3, 1, 1);
        expectedOutput.set(3, 2, 1);
        expectedOutput.set(3, 3, 1);
        expectedOutput.set(3, 4, 1);
        expectedOutput.set(4, 1, 1);
        expectedOutput.set(4, 2, 1);
        expectedOutput.set(4, 3, 1);
        expectedOutput.set(4, 4, 1);

        // When
        GrayImage output = cc.apply(inputImage);

        // Then
        assertArrayEquals(expectedOutput.getData(), output.getData());
    }

    @Test
    public void testEdgeCases() {
        // Given
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(2, 2, 1);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage expectedOutput = new GrayImage(3, 3);
        expectedOutput.set(2, 2, 1);
        expectedOutput.set(0, 0, 1);
        expectedOutput.set(0, 1, 1);
        expectedOutput.set(0, 2, 1);
        expectedOutput.set(1, 0, 1);
        expectedOutput.set(1, 1, 1);
        expectedOutput.set(1, 2, 1);
        expectedOutput.set(2, 0, 1);
        expectedOutput.set(2, 1, 1);
        expectedOutput.set(2, 2, 1);
        expectedOutput.set(2, 3, 1);
        expectedOutput.set(3, 0, 1);
        expectedOutput.set(3, 1, 1);
        expectedOutput.set(3, 2, 1);
        expectedOutput.set(3, 3, 1);

        // When
        GrayImage output = cc.apply(inputImage);

        // Then
        assertArrayEquals(expectedOutput.getData(), output.getData());

        // Given
        inputImage = new BinaryImage(1, 1);
        inputImage.set(0, 0, 1);
        expectedOutput = new GrayImage(1, 1);
        expectedOutput.set(0, 0, 1);

        // When
        cc = new ConnectedComponents(0, 1);
        output = cc.apply(inputImage);

        // Then
        assertArrayEquals(expectedOutput.getData(), output.getData());
    }

    @Test
    public void testAllBlackImage() {
        // Given
        BinaryImage inputImage = new BinaryImage(3, 3);
        for (int i = 0; i < inputImage.getSize(); i++) {
            inputImage.set(i, 0);
        }

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage expectedOutput = new GrayImage(3, 3);
        expectedOutput.set(0, 0, 0);
        for (int i = 1; i < expectedOutput.getSize(); i++) {
            expectedOutput.set(i, 0);
        }

        // When
        GrayImage output = cc.apply(inputImage);

        // Then
        assertArrayEquals(expectedOutput.getData(), output.getData());
    }

    @Test
    public void testAllWhiteImage() {
        // Given
        BinaryImage inputImage = new BinaryImage(3, 3);
        for (int i = 0; i < inputImage.getSize(); i++) {
            inputImage.set(i, 1);
        }

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage expectedOutput = new GrayImage(3, 3);
        for (int i = 0; i < expectedOutput.getSize(); i++) {
            expectedOutput.set(i, 1);
        }

        // When
        GrayImage output = cc.apply(inputImage);

        // Then
        assertArrayEquals(expectedOutput.getData(), output.getData());
    }
}