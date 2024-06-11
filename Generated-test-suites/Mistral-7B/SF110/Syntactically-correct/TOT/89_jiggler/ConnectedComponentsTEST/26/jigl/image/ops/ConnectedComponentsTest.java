import org.junit.Assert.*;
import jigl.image.ops.ConnectedComponents;
import jigl.image.types.*;
import jigl.image.*;
import jigl.image.exceptions.*;

public class ConnectedComponentsTest {

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArguments() {
        try {
            new ConnectedComponents(-1, 1);
            fail("Expected InvalidArgumentException to be thrown");
        } catch (InvalidArgumentException e) {
            // Expected exception, nothing to do here
        }

        try {
            new ConnectedComponents(2, 1);
            fail("Expected InvalidArgumentException to be thrown");
        } catch (InvalidArgumentException e) {
            // Expected exception, nothing to do here
        }

        try {
            new ConnectedComponents(1, 2);
            fail("Expected InvalidArgumentException to be thrown");
        } catch (InvalidArgumentException e) {
            // Expected exception, nothing to do here
        }

        try {
            new ConnectedComponents(1, 2.5);
            fail("Expected InvalidArgumentException to be thrown");
        } catch (InvalidArgumentException e) {
            // Expected exception, nothing to do here
        }
    }

    @Test
    public void testValidArguments() {
        ConnectedComponents cc = new ConnectedComponents(0, 1);
        ConnectedComponents cc1 = new ConnectedComponents(1, 1);
        assertNotNull(cc);
        assertNotNull(cc1);
    }

    @Test
    public void testConnectedComponents() {
        BinaryImage binaryImage = new BinaryImage(5, 5);
        binaryImage.set(1, 1, 1);
        binaryImage.set(2, 1, 1);
        binaryImage.set(3, 1, 1);
        binaryImage.set(4, 1, 1);
        binaryImage.set(1, 2, 1);
        binaryImage.set(2, 2, 1);
        binaryImage.set(3, 2, 1);
        binaryImage.set(4, 2, 1);
        binaryImage.set(1, 3, 1);
        binaryImage.set(2, 3, 1);
        binaryImage.set(3, 3, 1);
        binaryImage.set(4, 3, 1);
        binaryImage.set(1, 4, 1);
        binaryImage.set(2, 4, 1);
        binaryImage.set(3, 4, 1);
        binaryImage.set(4, 4, 1);
        binaryImage.set(3, 3, 0);

        ConnectedComponents connectedComponents = new ConnectedComponents(FOUR_CONNECTED, 0);
        GrayImage result = connectedComponents.apply(binaryImage);

        // Checking the result for the given input
        for (int i = 0; i < result.X(); i++) {
            for (int j = 0; j < result.Y(); j++) {
                int expected = (i / 5 + j / 5) * 5 + (i % 5 + j % 5) % 5 + 1;
                assertEquals(Integer.valueOf(expected), result.get(i, j));
            }
        }
    }

    @Test
    public void testConnectedComponentsWithEightNeighbors() {
        BinaryImage binaryImage = new BinaryImage(5, 5);
        binaryImage.set(1, 1, 1);
        binaryImage.set(2, 1, 1);
        binaryImage.set(3, 1, 1);
        binaryImage.set(4, 1, 1);
        binaryImage.set(1, 2, 1);
        binaryImage.set(2, 2, 1);
        binaryImage.set(3, 2, 1);
        binaryImage.set(4, 2, 1);
        binaryImage.set(1, 3, 1);
        binaryImage.set(2, 3, 1);
        binaryImage.set(3, 3, 1);
        binaryImage.set(4, 3, 1);
        binaryImage.set(1, 4, 1);
        binaryImage.set(2, 4, 1);
        binaryImage.set(3, 4, 1);
        binaryImage.set(4, 4, 1);
        binaryImage.set(3, 3, 1);
        binaryImage.set(3, 2, 1);
        binaryImage.set(3, 4, 1);
        binaryImage.set(2, 3, 1);
        binaryImage.set(2, 4, 1);
        binaryImage.set(4, 3, 1);
        binaryImage.set(4, 2, 1);

        ConnectedComponents connectedComponents = new ConnectedComponents(EIGHT_CONNECTED, 0);
        GrayImage result = connectedComponents.apply(binaryImage);

        // Checking the result for the given input
        for (int i = 0; i < result.X(); i++) {
            for (int j = 0; j < result.Y(); j++) {
                int expected = (i / 5 + j / 5) * 5 * 5 + (i % 5 + j % 5) * 5 + (i % 5 + j % 5 + 1) % 5 + 1;
                assertEquals(Integer.valueOf(expected), result.get(i, j));
            }
        }
    }

    @Test
    public void testConnectedComponentsWithInvalidBackgroundColor() {
        BinaryImage binaryImage = new BinaryImage(5, 5);
        binaryImage.set(1, 1, 1);
        binaryImage.set(2, 1, 1);
        binaryImage.set(3, 1, 1);
        binaryImage.set(4, 1, 1);
        binaryImage.set(1, 2, 1);
        binaryImage.set(2, 2, 1);
        binaryImage.set(3, 2, 1);
        binaryImage.set(4, 2, 1);
        binaryImage.set(1, 3, 1);
        binaryImage.set(2, 3, 1);
        binaryImage.set(3, 3, 1);
        binaryImage.set(4, 3, 1);
        binaryImage.set(1, 4, 1);
        binaryImage.set(2, 4, 1);
        binaryImage.set(3, 4, 1);
        binaryImage.set(4, 4, 1);
        binaryImage.set(3, 3, 0);

        try {
            new ConnectedComponents(FOUR_CONNECTED, 2);
            fail("Expected InvalidArgumentException to be thrown");
        } catch (InvalidArgumentException e) {
            // Expected exception, nothing to do here
        }
    }
}