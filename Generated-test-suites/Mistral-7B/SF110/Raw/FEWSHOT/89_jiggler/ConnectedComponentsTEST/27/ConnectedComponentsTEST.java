package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArgumentException() {
        new ConnectedComponents(-2, 1);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArgumentException2() {
        new ConnectedComponents(1, -1);
    }

    @Test
    public void testConnectedComponents() {
        int[][] input = new int[][]{{0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}};

        ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 0);
        GrayImage output = cc.apply(new BinaryImage(input));

        int[][] expected = new int[][]{{0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 4, 4, 4, 4, 4, 0, 0},
                {0, 0, 4, 4, 4, 4, 4, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}};

        assertArrayEquals(expected, output.getData());
    }

    @Test
    public void testConnectedComponentsEightConnected() {
        int[][] input = new int[][]{{0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}};

        ConnectedComponents cc = new ConnectedComponents(EIGHT_CONNECTED, 0);
        GrayImage output = cc.apply(new BinaryImage(input));

        int[][] expected = new int[][]{{0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 4, 4, 4, 4, 4, 0, 0},
                {0, 0, 4, 4, 4, 4, 4, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}};

        assertArrayEquals(expected, output.getData());
    }

    @Test
    public void testConnectedComponentsBackground() {
        int[][] input = new int[][]{{1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}};

        ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 1);
        GrayImage output = cc.apply(new BinaryImage(input));

        int[][] expected = new int[][]{{2, 2, 2, 2, 2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2, 2, 2},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {2, 2, 2, 2, 2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2, 2, 2},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}};

        assertArrayEquals(expected, output.getData());
    }

    @Test
    public void testConnectedComponentsBackgroundAndNeighbors() {
        int[][] input = new int[][]{{1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}};

        ConnectedComponents cc = new ConnectedComponents(EIGHT_CONNECTED, 1);
        GrayImage output = cc.apply(new BinaryImage(input));

        int[][] expected = new int[][]{{2, 2, 2, 2, 2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2, 2, 2},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {2, 2, 2, 2, 2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2, 2, 2},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}};

        assertArrayEquals(expected, output.getData());
    }

    @Test
    public void testConnectedComponentsNullInput() {
        ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 1);
        assertNull(cc.apply(null));
    }

    @Test
    public void testConnectedComponentsEmptyInput() {
        ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 1);
        GrayImage output = cc.apply(new BinaryImage(new int[0][0]));
        assertEquals(0, output.getX());
        assertEquals(0, output.getY());
    }

    @Test
    public void testConnectedComponentsOutputSize() {
        int[][] input = new int[][]{{0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}};

        ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 0);
        GrayImage output = cc.apply(new BinaryImage(input));

        assertEquals(input[0].length, output.getX());
        assertEquals(input.length, output.getY());
    }
}