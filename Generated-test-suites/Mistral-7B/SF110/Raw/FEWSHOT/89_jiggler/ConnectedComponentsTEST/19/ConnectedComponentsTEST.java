package jigl.image.ops;

import static org.junit.Assert.*;

import jigl.image.types.*;
import org.junit.Test;

import static jigl.image.ops.ConnectedComponents.FOUR_CONNECTED;
import static jigl.image.ops.ConnectedComponents.EIGHT_CONNECTED;

public class ConnectedComponentsTest {
    @Test(expected = InvalidArgumentException.class)
    public void testInvalidNumberOfNeighborsAndBackgroundColor() {
        new ConnectedComponents(-1, 1);
    }

    @Test
    public void testConnectedComponentsWithFourConnected() {
        int[][] input = {{0, 0, 1, 1, 0, 0},
                         {0, 0, 1, 1, 0, 0},
                         {1, 1, 1, 1, 1, 1},
                         {0, 0, 1, 1, 0, 0},
                         {0, 0, 1, 1, 0, 0}};
        BinaryImage binaryImage = new BinaryImage(input);
        ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 1);
        GrayImage result = cc.apply(binaryImage);

        int[][] expected = {{0, 0, 1, 2, 0, 0},
                            {0, 0, 1, 2, 0, 0},
                            {1, 1, 1, 3, 1, 1},
                            {0, 0, 1, 3, 0, 0},
                            {0, 0, 1, 3, 0, 0}};
        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                assertEquals(expected[x][y], result.get(x, y));
            }
        }
    }

    @Test
    public void testConnectedComponentsWithEightConnected() {
        int[][] input = {{0, 0, 1, 1, 0, 0},
                         {0, 0, 1, 1, 0, 0},
                         {1, 1, 1, 1, 1, 1},
                         {0, 0, 1, 1, 0, 0},
                         {0, 0, 1, 1, 0, 0}};
        BinaryImage binaryImage = new BinaryImage(input);
        ConnectedComponents cc = new ConnectedComponents(EIGHT_CONNECTED, 1);
        GrayImage result = cc.apply(binaryImage);

        int[][] expected = {{0, 0, 1, 1, 0, 0},
                            {0, 0, 1, 1, 0, 0},
                            {1, 1, 1, 2, 1, 1},
                            {0, 0, 1, 2, 0, 0},
                            {0, 0, 1, 2, 0, 0}};
        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                assertEquals(expected[x][y], result.get(x, y));
            }
        }
    }
}