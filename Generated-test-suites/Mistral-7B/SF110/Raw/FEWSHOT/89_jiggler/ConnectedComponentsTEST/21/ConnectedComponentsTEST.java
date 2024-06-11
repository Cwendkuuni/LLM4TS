package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import jigl.image.types.GrayImage;
import jigl.image.types.IntegerImage;

public class ConnectedComponentsTest {

    private ConnectedComponents cc;
    private BinaryImage binaryImage;
    private GrayImage expectedResult;

    @Before
    public void setup() {
        byte[] imageData = {0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0};
        binaryImage = new IntegerImage(imageData, 12, 12);
        cc = new ConnectedComponents(0, 0);
        expectedResult = new GrayImage(12, 12);
    }

    @Test
    public void testConnectedComponentsWithFourConnectedNeighbors() {
        GrayImage result = cc.apply(binaryImage);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testConnectedComponentsWithEightConnectedNeighbors() {
        cc = new ConnectedComponents(1, 0);
        GrayImage result = cc.apply(binaryImage);
        assertEquals(expectedResult, result);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArgumentExceptionWhenWrongNumberOfNeighborsIsProvided() {
        cc = new ConnectedComponents(2, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArgumentExceptionWhenWrongBackgroundColorIsProvided() {
        cc = new ConnectedComponents(0, 3);
    }

    @Test
    public void testApplyMethodWithRandomInputImage() {
        byte[] randomImageData = new byte[(int) (Math.random() * 100) * (int) (Math.random() * 100)];
        IntegerImage randomBinaryImage = new IntegerImage(randomImageData, (int) (Math.random() * 100), (int) (Math.random() * 100));
        cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(randomBinaryImage);
        assertNotNull(result);
    }

    @Test
    public void testEquivalenceTableIsPopulatedCorrectlyWithFourConnectedNeighbors() {
        cc = new ConnectedComponents(0, 0);
        int[] equiv = new int[100000];
        for (int x = 0; x < expectedResult.X(); ++x) {
            for (int y = 0; y < expectedResult.Y(); ++y) {
                int curVal = expectedResult.get(x, y);
                for (int z = 0; z < 4; ++z) {
                    int neighbor = 0;
                    if (z == 0) {
                        neighbor = x - 1 >= 0 ? expectedResult.get(x - 1, y) : 0;
                    } else if (z == 1) {
                        neighbor = y - 1 >= 0 ? expectedResult.get(x, y - 1) : 0;
                    } else if (z == 2) {
                        neighbor = x + 1 < expectedResult.X() ? expectedResult.get(x + 1, y) : 0;
                    } else {
                        neighbor = y + 1 < expectedResult.Y() ? expectedResult.get(x, y + 1) : 0;
                    }

                    assertEquals(neighbor, equiv[curVal]);
                }
            }
        }
    }

    @Test
    public void testEquivalenceTableIsPopulatedCorrectlyWithEightConnectedNeighbors() {
        cc = new ConnectedComponents(1, 0);
        int[] equiv = new int[100000];
        for (int x = 0; x < expectedResult.X(); ++x) {
            for (int y = 0; y < expectedResult.Y(); ++y) {
                int curVal = expectedResult.get(x, y);
                for (int z = 0; z < 4; ++z) {
                    int neighbor = 0;
                    int dx = 0;
                    int dy = 0;
                    if (z == 0) {
                        dx = -1;
                        dy = 0;
                        neighbor = x - 1 >= 0 ? expectedResult.get(x - 1, y) : 0;
                    } else if (z == 1) {
                        dx = 0;
                        dy = -1;
                        neighbor = y - 1 >= 0 ? expectedResult.get(x, y - 1) : 0;
                    } else if (z == 2) {
                        dx = 1;
                        dy = 0;
                        neighbor = x + 1 < expectedResult.X() ? expectedResult.get(x + 1, y) : 0;
                    } else {
                        dx = 0;
                        dy = 1;
                        neighbor = y + 1 < expectedResult.Y() ? expectedResult.get(x, y + 1) : 0;
                    }

                    assertEquals(neighbor, equiv[curVal]);
                }
            }
        }
    }

    @Test
    public void testConnectedComponentsAlgorithmWithAllPossibleInputValues() {
        // Test with different input image sizes
        int[] sizes = new int[] {1, 1, 10, 100, 1000, 10000};
        byte[] randomImageData = new byte[100 * 100];
        for (int size : sizes) {
            IntegerImage randomBinaryImage = new IntegerImage(randomImageData, size, size);
            cc = new ConnectedComponents(0, 0);
            GrayImage result = cc.apply(randomBinaryImage);
            assertNotNull(result);
            assertTrue(result.X() == size && result.Y() == size);
        }

        // Test with different input image background values
        byte[] backgroundValues = new byte[] {1, 0, -1, 255, 256, 257};
        for (byte backgroundValue : backgroundValues) {
            IntegerImage randomBinaryImage = new IntegerImage(randomImageData, 10, 10);
            cc = new ConnectedComponents(0, backgroundValue);
            GrayImage result = cc.apply(randomBinaryImage);
            assertNotNull(result);
            assertEquals(backgroundValue, cc.background);
        }

        // Test with different number of neighbors
        int[] neighbors = new int[] {0, 1, 2, 3};
        for (int numNeighbors : neighbors) {
            cc = new ConnectedComponents(numNeighbors, 0);
            GrayImage result = cc.apply(binaryImage);
            assertNotNull(result);
            assertEquals(numNeighbors, cc.neighbors);
        }
    }
}