package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import jigl.image.types.*;
import jigl.image.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsWithFourConnectedNeighbors() {
        final BinaryImage binaryImage = new BinaryImage(new int[][]{
                {1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 1, 1, 1},
                {1, 1, 1, 1, 1}
        });

        final ConnectedComponents componentFinder = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        final GrayImage outputImage = componentFinder.apply(binaryImage);

        final int[] expectedOutput = new int[]{
                1, 1, 1, 1, 1,
                1, 0, 1, 0, 1,
                1, 0, 1, 1, 1,
                1, 0, 1, 1, 1,
                1, 1, 1, 1, 1
        };

        assertArrayEquals(expectedOutput, outputImage.getData());
    }

    @Test
    public void testConnectedComponentsWithEightConnectedNeighbors() {
        final BinaryImage binaryImage = new BinaryImage(new int[][]{
                {1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 1, 1, 1},
                {1, 1, 1, 1, 1}
        });

        final ConnectedComponents componentFinder = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        final GrayImage outputImage = componentFinder.apply(binaryImage);

        final int[] expectedOutput = new int[]{
                1, 1, 1, 1, 1,
                1, 0, 1, 0, 1,
                1, 0, 1, 1, 1,
                1, 0, 1, 1, 1,
                1, 1, 1, 1, 1
        };

        assertArrayEquals(expectedOutput, outputImage.getData());
    }

    @Test
    public void testInvalidArgumentException() {
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(-1, 0));
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(0, -1));
    }

    @Test
    public void testConnectedComponentsWithNegativeBackground() {
        final BinaryImage binaryImage = new BinaryImage(new int[][]{
                {1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 1, 1, 1},
                {1, 1, 1, 1, 1}
        });

        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, -1));
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, -1));
    }

    @Test
    public void testConnectedComponentsWithZeroBackground() {
        final BinaryImage binaryImage = new BinaryImage(new int[][]{
                {1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 1, 1, 1},
                {1, 1, 1, 1, 1}
        });

        final ConnectedComponents componentFinder = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        final GrayImage outputImage = componentFinder.apply(binaryImage);

        assertNotNull(outputImage);
    }

    @Test
    public void testConnectedComponentsWithNullInput() {
        assertNull(new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0).apply(null));
    }

    @Test
    public void testConnectedComponentsWithEmptyImage() {
        final BinaryImage binaryImage = new BinaryImage(new int[0][0]);

        final ConnectedComponents componentFinder = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        final GrayImage outputImage = componentFinder.apply(binaryImage);

        assertNotNull(outputImage);
        assertEquals(0, outputImage.X());
        assertEquals(0, outputImage.Y());
    }
}