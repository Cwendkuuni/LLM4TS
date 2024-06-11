package jigl.image.ops;

import static org.junit.Assert.*;
import jigl.image.types.*;
import jigl.image.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponents() {
        BinaryImage binaryImage = new BinaryImage(new byte[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        });

        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        GrayImage result = connectedComponents.apply(binaryImage);

        int[] expected = new int[]{1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1};
        for (int i = 0; i < result.X() * result.Y(); ++i) {
            assertEquals(expected[i], result.get(i / result.X(), i % result.X()).intValue());
        }
    }

    @Test
    public void testConnectedComponentsWithBackground() {
        BinaryImage binaryImage = new BinaryImage(new byte[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        });

        ConnectedComponents connectedComponents = new ConnectedComponents(0, 1);
        GrayImage result = connectedComponents.apply(binaryImage);

        int[] expected = new int[]{2, 2, 2, 2, 2, 2};
        for (int i = 0; i < result.X() * result.Y(); ++i) {
            assertEquals(expected[i % result.X()], result.get(i / result.X(), i % result.X()).intValue());
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArguments() {
        ConnectedComponents connectedComponents = new ConnectedComponents(3, 2);
    }

    @Test(expected = InvalidImageException.class)
    public void testInvalidImage() {
        BinaryImage binaryImage = new BinaryImage(new byte[][]{});
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        connectedComponents.apply(binaryImage);
    }
}