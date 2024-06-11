package jigl.image.ops;

import static org.junit.Assert.*;

import jigl.image.types.*;
import jigl.image.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponents() {
        BinaryImage binaryImage = new BinaryImage(new int[][]{
                {1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 1},
                {1, 0, 1, 0, 0, 1},
                {1, 0, 1, 0, 1, 1},
                {1, 1, 1, 1, 1, 1}
        });

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(binaryImage);

        int[] expected = {1, 2, 2, 2, 2, 1};
        for (int i = 0; i < result.X() * result.Y(); ++i) {
            assertEquals(expected[i / result.X()], result.get(i / result.X(), i % result.X()).intValue());
        }
    }

    @Test
    public void testConnectedComponentsWithBackground() {
        BinaryImage binaryImage = new BinaryImage(new int[][]{
                {0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 0},
                {0, 1, 1, 1, 1, 0},
                {0, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0}
        });

        ConnectedComponents cc = new ConnectedComponents(0, 1);
        GrayImage result = cc.apply(binaryImage);

        int[] expected = {2, 2, 2, 2, 2, 2};
        for (int i = 0; i < result.X() * result.Y(); ++i) {
            assertEquals(expected[i / result.X()], result.get(i / result.X(), i % result.X()).intValue());
        }
    }

    @Test
    public void testConnectedComponentsWithNeighbors1() {
        BinaryImage binaryImage = new BinaryImage(new int[][]{
                {1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 1},
                {1, 0, 1, 1, 1, 1},
                {1, 0, 1, 0, 0, 1},
                {1, 0, 1, 0, 1, 1},
                {1, 1, 1, 1, 1, 1}
        });

        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage result = cc.apply(binaryImage);

        int[] expected = {1, 1, 1, 1, 1, 1};
        for (int i = 0; i < result.X() * result.Y(); ++i) {
            assertEquals(expected[i], result.get(i / result.X(), i % result.X()).intValue());
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArguments() {
        ConnectedComponents cc = new ConnectedComponents(2, 2);
    }

    @Test
    public void testApplyWithEmptyImage() {
        BinaryImage binaryImage = new BinaryImage(new int[0][0]);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(binaryImage);

        assertEquals(0, result.X());
        assertEquals(0, result.Y());
    }

    @Test
    public void testApplyWithNullImage() {
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        assertNull(cc.apply(null));
    }
}