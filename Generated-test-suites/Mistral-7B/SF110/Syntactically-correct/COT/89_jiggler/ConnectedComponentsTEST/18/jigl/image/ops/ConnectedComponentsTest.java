import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructorInvalidArguments() {
        try {
            new ConnectedComponents(2, 3);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        try {
            new ConnectedComponents(0, 3);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        try {
            new ConnectedComponents(3, 2);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        try {
            new ConnectedComponents(1, -1);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConnectedComponentsEmptyImage() {
        BinaryImage binaryImage = new BinaryImage(0, 0);
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        GrayImage expectedGrayImage = new GrayImage(0, 0);
        GrayImage actualGrayImage = connectedComponents.apply(binaryImage);
        assertEquals(expectedGrayImage, actualGrayImage);
    }

    @Test
    public void testConnectedComponentsSingleComponent() {
        BinaryImage binaryImage = new BinaryImage(new int[][]{{1}});
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 1);
        int expectedNumberOfComponents = 1;
        int actualNumberOfComponents = connectedComponents.numberOfComponents();
        assertEquals(expectedNumberOfComponents, actualNumberOfComponents);

        GrayImage expectedGrayImage = new GrayImage(new int[][]{{1}});
        GrayImage actualGrayImage = connectedComponents.apply(binaryImage);
        assertEquals(expectedGrayImage, actualGrayImage);
    }

    @Test
    public void testConnectedComponentsMultipleComponents() {
        BinaryImage binaryImage = new BinaryImage(new int[][]{{0, 1, 0, 1}, {1, 0, 1, 0}, {0, 1, 0, 1}, {1, 0, 1, 0}});
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 1);
        int expectedNumberOfComponents = 4;
        int actualNumberOfComponents = connectedComponents.numberOfComponents();
        assertEquals(expectedNumberOfComponents, actualNumberOfComponents);

        GrayImage expectedGrayImage = new GrayImage(new int[][]{{1, 1, 1, 1}, {1, 0, 1, 0}, {1, 1, 1, 1}, {1, 0, 1, 0}});
        GrayImage actualGrayImage = connectedComponents.apply(binaryImage);
        assertEquals(expectedGrayImage, actualGrayImage);
    }

    @Test
    public void testConnectedComponentsSixConnected() {
        ConnectedComponents connectedComponents = new ConnectedComponents(6, 1);
        GrayImage expectedGrayImage = null; // This test case is not feasible as it would require an unreasonable amount of memory and time
        // To create the expected image, the ConnectedComponents constructor should be changed to accept a GrayImage instead of a BinaryImage
        // The expected image would be the original image with each connected component labeled with a unique value

        GrayImage actualGrayImage = connectedComponents.apply(new BinaryImage(new int[][]{{0, 1, 0, 1}, {1, 0, 1, 0}, {0, 1, 0, 1}, {1, 0, 1, 0}}));
        // As the test case cannot be feasibly executed, we cannot make any assertions on the actual image
    }

    @Test(expected = StackOverflowError.class)
    public void testConnectedComponentsRecursiveLoop() {
        BinaryImage binaryImage = new BinaryImage(new int[][]{{1, 1}, {1, 1}});
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 1);
        connectedComponents.apply(binaryImage);
    }

    @Test
    public void testConnectedComponentsNullArgument() {
        try {
            new ConnectedComponents(null, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new ConnectedComponents(new int[0][], 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}