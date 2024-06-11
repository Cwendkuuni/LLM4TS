package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import jigl.image.types.GrayImage;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponents() {
        BinaryImage binaryImage = new BinaryImage(new int[][]{
                {1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 0, 0, 1},
                {1, 1, 1, 1, 1}
        }, 5, 5);

        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        GrayImage result = connectedComponents.apply(binaryImage);

        int[] expected = new int[][]{
                {1, 1, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1},
                {1, 1, 1, 1, 1}
        };

        assertArrayEquals(expected, result.getPixels());
    }

    @Test
    public void testConnectedComponentsWithBackground() {
        BinaryImage binaryImage = new BinaryImage(new int[][]{
                {0, 0, 0, 1, 1},
                {0, 0, 1, 1, 1},
                {0, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1}
        }, 5, 5);

        ConnectedComponents connectedComponents = new ConnectedComponents(0, 1);
        GrayImage result = connectedComponents.apply(binaryImage);

        int[] expected = new int[][]{
                {0, 0, 0, 2, 3},
                {0, 1, 1, 2, 3},
                {0, 1, 1, 2, 3},
                {2, 2, 2, 2, 3},
                {2, 2, 2, 2, 3}
        };

        assertArrayEquals(expected, result.getPixels());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArguments() {
        ConnectedComponents connectedComponents = new ConnectedComponents(-1, 0);
    }

    @Test
    public void testConnectedComponentsWithNeighbors1() {
        BinaryImage binaryImage = new BinaryImage(new int[][]{
                {0, 1, 0, 1, 0},
                {1, 0, 1, 0, 1},
                {0, 1, 0, 1, 0},
                {1, 0, 1, 0, 1},
                {0, 1, 0, 1, 0}
        }, 5, 5);

        ConnectedComponents connectedComponents = new ConnectedComponents(1, 0);
        GrayImage result = connectedComponents.apply(binaryImage);

        int[] expected = new int[][]{
                {1, 1, 1, 1, 1},
                {1, 2, 2, 2, 1},
                {1, 2, 2, 2, 1},
                {1, 2, 2, 2, 1},
                {1, 1, 1, 1, 1}
        };

        assertArrayEquals(expected, result.getPixels());
    }

    @Test
    public void testConnectedComponentsWithNeighbors1AndBackground() {
        BinaryImage binaryImage = new BinaryImage(new int[][]{
                {0, 0, 0, 1, 1},
                {0, 0, 1, 1, 1},
                {0, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1}
        }, 5, 5);

        ConnectedComponents connectedComponents = new ConnectedComponents(1, 1);
        GrayImage result = connectedComponents.apply(binaryImage);

        int[] expected = new int[][]{
                {0, 0, 0, 3, 3},
                {0, 0, 3, 3, 3},
                {0, 3, 3, 3, 3},
                {3, 3, 3, 3, 3},
                {3, 3, 3, 3, 3}
        };

        assertArrayEquals(expected, result.getPixels());
    }
}