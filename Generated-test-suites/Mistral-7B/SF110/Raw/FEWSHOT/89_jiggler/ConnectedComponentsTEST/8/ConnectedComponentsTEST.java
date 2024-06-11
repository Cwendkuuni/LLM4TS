package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import jigl.image.types.*;
import jigl.image.ops.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsInvalidArg() {
        new ConnectedComponents(2, 1);
    }

    @Test
    public void testConnectedComponentsEmptyImage() {
        BinaryImage emptyImage = new BinaryImage(0, 0);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(emptyImage);
        assertTrue(result.X() == 0 && result.Y() == 0);
    }

    @Test
    public void testConnectedComponentsSinglePixel() {
        int[][] input = new int[][]{
                {1},
                {1}
        };
        BinaryImage image = new BinaryImage(input);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(image);
        GrayImage expected = new GrayImage(2, 2);
        expected.set(0, 0, Integer.valueOf(1));
        assertEquals(expected, result);
    }

    @Test
    public void testConnectedComponentsSingleComponent() {
        int[][] input = new int[][]{
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1}
        };
        BinaryImage image = new BinaryImage(input);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(image);
        GrayImage expected = new GrayImage(5, 5);
        int current = 1;
        for (int x = 0; x < 5; ++x) {
            for (int y = 0; y < 5; ++y) {
                expected.set(x, y, Integer.valueOf(current++));
            }
        }
        assertEquals(expected, result);
    }

    @Test
    public void testConnectedComponentsMultipleComponents() {
        int[][] input = new int[][]{
                {1, 1, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 1, 1, 1, 1}
        };
        BinaryImage image = new BinaryImage(input);
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage result = cc.apply(image);
        GrayImage expected = new GrayImage(5, 5);
        int current = 1;
        for (int x = 0; x < 5; ++x) {
            for (int y = 0; y < 5; ++y) {
                int expectedVal = 1;
                if (x == 1 && y == 1) {
                    expectedVal = 2;
                } else if (x == 1 && y == 3) {
                    expectedVal = 3;
                } else if (x == 3 && y == 1) {
                    expectedVal = 4;
                }
                expected.set(x, y, Integer.valueOf(expectedVal));
            }
        }
        assertEquals(expected, result);
    }

    @Test
    public void testConnectedComponentsImageWithBackground() {
        int[][] input = new int[][]{
                {0, 0, 1, 1, 1},
                {0, 0, 1, 0, 1},
                {0, 0, 1, 1, 1},
                {0, 0, 1, 0, 1},
                {0, 0, 1, 1, 1}
        };
        BinaryImage image = new BinaryImage(input);
        ConnectedComponents cc = new ConnectedComponents(0, 1);
        GrayImage result = cc.apply(image);
        GrayImage expected = new GrayImage(5, 5);
        int current = 1;
        for (int x = 0; x < 5; ++x) {
            for (int y = 0; y < 5; ++y) {
                if (x == 1 && y == 1) {
                    expected.set(x, y, Integer.valueOf(current++));
                } else {
                    expected.set(x, y, Integer.valueOf(0));
                }
            }
        }
        assertEquals(expected, result);
    }

    @Test
    public void testConnectedComponentsImageWithIsolatedPixels() {
        int[][] input = new int[][]{
                {1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1}
        };
        BinaryImage image = new BinaryImage(input);
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage result = cc.apply(image);
        GrayImage expected = new GrayImage(5, 5);
        int current = 1;
        for (int x = 0; x < 5; ++x) {
            for (int y = 0; y < 5; ++y) {
                if (x == 2 && y == 2) {
                    expected.set(x, y, Integer.valueOf(current++));
                } else {
                    expected.set(x, y, Integer.valueOf(0));
                }
            }
        }
        assertEquals(expected, result);
    }

    @Test
    public void testConnectedComponentsPerformance() {
        int width = 1000, height = 1000;
        int[][] input = new int[height][width];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                input[y][x] = (x % 2 == 0 && y % 2 == 0) ? 1 : 0;
            }
        }
        BinaryImage image = new BinaryImage(input);
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage result = cc.apply(image);
        long startTime = System.nanoTime();
        GrayImage expected = new GrayImage(width, height);
        int current = 1;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (x % 2 == 0 && y % 2 == 0) {
                    expected.set(x, y, Integer.valueOf(current++));
                } else {
                    expected.set(x, y, Integer.valueOf(0));
                }
            }
        }
        assertEquals(expected, result);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println("Performed in " + duration + " nanoseconds.");
    }
}