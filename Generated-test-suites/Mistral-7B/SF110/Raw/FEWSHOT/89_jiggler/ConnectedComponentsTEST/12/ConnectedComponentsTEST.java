package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        assertNotNull(cc);

        ConnectedComponents cc1 = new ConnectedComponents(1, 0);
        assertNotNull(cc1);

        ConnectedComponents cc2 = new ConnectedComponents(0, 1);
        assertNotNull(cc2);

        ConnectedComponents cc3 = new ConnectedComponents(1, 1);
        assertNotNull(cc3);

        ConnectedComponents cc4 = new ConnectedComponents(2, 1);
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(2, 1));

        ConnectedComponents cc5 = new ConnectedComponents(1, 2);
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(1, 2));
    }

    @Test
    public void testConnectedComponentsApply() throws InvalidArgumentException {
        BinaryImage binaryImage = new BinaryImage(new byte[][]{{1, 1, 1, 1, 1}, {1, 1, 0, 1, 1}, {1, 0, 1, 0, 1},
                {1, 1, 0, 1, 1}, {1, 1, 1, 1, 1}});
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(binaryImage);

        int[] expectedResult = new int[][]{
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1}
        };

        assertArrayEquals(expectedResult, result.getData());

        int[] expectedLabels = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        for (int i = 0; i < result.X() * result.Y(); ++i) {
            assertTrue(expectedLabels[i] == result.get(i) || expectedLabels[expectedLabels[i]] == result.get(i));
        }
    }

    @Test
    public void testConnectedComponentsApplyWithFourNeighbors() throws InvalidArgumentException {
        BinaryImage binaryImage = new BinaryImage(new byte[][]{{1, 1, 1, 1, 1}, {1, 1, 0, 1, 1}, {1, 0, 1, 0, 1},
                {1, 1, 0, 1, 1}, {1, 1, 1, 1, 1}});
        ConnectedComponents cc = new ConnectedComponents(4, 0);
        GrayImage result = cc.apply(binaryImage);

        int[] expectedResult = new int[][]{
                {1, 1, 1, 1, 1},
                {1, 2, 1, 2, 1},
                {1, 2, 1, 2, 1},
                {1, 2, 1, 2, 1},
                {1, 1, 1, 1, 1}
        };

        assertArrayEquals(expectedResult, result.getData());

        int[] expectedLabels = new int[]{1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        for (int i = 0; i < result.X() * result.Y(); ++i) {
            assertTrue(expectedLabels[i] == result.get(i) || expectedLabels[expectedLabels[i]] == result.get(i));
        }
    }

    @Test
    public void testConnectedComponentsApplyWithEightNeighbors() throws InvalidArgumentException {
        BinaryImage binaryImage = new BinaryImage(new byte[][]{{1, 1, 1, 1, 1}, {1, 1, 0, 1, 1}, {1, 0, 1, 0, 1},
                {1, 1, 0, 1, 1}, {1, 1, 1, 1, 1}});
        ConnectedComponents cc = new ConnectedComponents(8, 0);
        GrayImage result = cc.apply(binaryImage);

        int[] expectedResult = new int[][]{
                {1, 1, 1, 1, 1},
                {1, 2, 1, 2, 1},
                {1, 2, 1, 2, 1},
                {1, 2, 1, 2, 1},
                {1, 1, 1, 1, 1}
        };

        assertArrayEquals(expectedResult, result.getData());

        int[] expectedLabels = new int[]{1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        for (int i = 0; i < result.X() * result.Y(); ++i) {
            assertTrue(expectedLabels[i] == result.get(i) || expectedLabels[expectedLabels[i]] == result.get(i));
        }
    }

    @Test
    public void testConnectedComponentsApplyWithFourNeighborsAndBackground() throws InvalidArgumentException {
        BinaryImage binaryImage = new BinaryImage(new byte[][]{{1, 1, 1, 1, 1}, {1, 1, 0, 1, 1}, {1, 0, 1, 0, 1},
                {1, 1, 0, 1, 1}, {1, 1, 1, 1, 1}});
        ConnectedComponents cc = new ConnectedComponents(4, 1);
        GrayImage result = cc.apply(binaryImage);

        int[] expectedResult = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0},
                {0, 1, 1, 1, 0},
                {0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0}
        };

        assertArrayEquals(expectedResult, result.getData());

        int[] expectedLabels = new int[]{0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        for (int i = 0; i < result.X() * result.Y(); ++i) {
            assertTrue(expectedLabels[i] == result.get(i) || expectedLabels[expectedLabels[i]] == result.get(i));
        }
    }

    @Test
    public void testConnectedComponentsApplyWithEightNeighborsAndBackground() throws InvalidArgumentException {
        BinaryImage binaryImage = new BinaryImage(new byte[][]{{1, 1, 1, 1, 1}, {1, 1, 0, 1, 1}, {1, 0, 1, 0, 1},
                {1, 1, 0, 1, 1}, {1, 1, 1, 1, 1}});
        ConnectedComponents cc = new ConnectedComponents(8, 1);
        GrayImage result = cc.apply(binaryImage);

        int[] expectedResult = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0},
                {0, 1, 1, 1, 0},
                {0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0}
        };

        assertArrayEquals(expectedResult, result.getData());

        int[] expectedLabels = new int[]{0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        for (int i = 0; i < result.X() * result.Y(); ++i) {
            assertTrue(expectedLabels[i] == result.get(i) || expectedLabels[expectedLabels[i]] == result.get(i));
        }
    }
}