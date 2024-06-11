package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.types.BinaryImage;

import org.junit.Before;
import org.junit.Test;

public class ConnectedComponentsTest {

    ConnectedComponents cc;
    BinaryImage binaryImage;

    @Before
    public void setup() {
        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        binaryImage = new BinaryImage(new int[][]{{0, 0, 0, 1, 1, 0, 0, 0},
                {0, 0, 1, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 0, 0, 0, 0},
                {1, 1, 0, 0, 0, 0, 1, 1},
                {1, 1, 0, 0, 0, 0, 1, 1},
                {0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 0, 0, 0},
                {0, 0, 0, 1, 1, 0, 0, 0}});
    }

    @Test
    public void testConnectedComponentsWithFourConnectedAndZeroBackground() throws InvalidArgumentException {
        GrayImage result = cc.apply(binaryImage);

        GrayImage expected = new GrayImage(new int[][]{{0, 0, 0, 2, 2, 0, 0, 0},
                {0, 0, 2, 2, 3, 3, 0, 0},
                {0, 2, 2, 3, 0, 0, 0, 0},
                {2, 2, 0, 0, 0, 0, 2, 2},
                {2, 2, 0, 0, 0, 0, 2, 2},
                {0, 2, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 2, 0, 0, 0},
                {0, 0, 0, 2, 2, 0, 0, 0}});

        for (int i = 0; i < result.X(); i++) {
            for (int j = 0; j < result.Y(); j++) {
                int resultValue = result.get(i, j).intValue();
                int expectedValue = expected.get(i, j).intValue();
                assertEquals(expectedValue, resultValue);
            }
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsWithInvalidNumberOfNeighbors() throws InvalidArgumentException {
        new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        new ConnectedComponents(-1, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsWithInvalidBackgroundColor() throws InvalidArgumentException {
        new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }
}