package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.BinaryImage;

import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, -1);
            fail("Expected InvalidArgumentException when neighbors is not 0 or 1");
        } catch (InvalidArgumentException e) {
            //expected
        }

        try {
            ConnectedComponents cc = new ConnectedComponents(-1, 2);
            fail("Expected InvalidArgumentException when background is not 0 or 1");
        } catch (InvalidArgumentException e) {
            //expected
        }

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        assertNotNull(cc);

        ConnectedComponents cc1 = new ConnectedComponents(1, 1);
        assertNotNull(cc1);
    }

    @Test
    public void testConnectedComponentsApplyWithBinaryImage() {
        BinaryImage binaryImage = new BinaryImage(3, 3, new byte[][] { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } });
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(binaryImage);
        int[] expectedValues = new int[] { 1, 1, 1, 1, 1, 1, 1 };
        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                assertEquals(Integer.valueOf(expectedValues[x * result.X() + y]), result.get(x, y));
            }
        }

        binaryImage = new BinaryImage(3, 3, new byte[][] { { 1, 1, 1, 1, 1 }, { 1, 0, 1, 1, 1 }, { 1, 1, 1, 1, 1 } });
        result = cc.apply(binaryImage);
        expectedValues = new int[] { 1, 1, 1, 2, 1, 1, 1 };
        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                assertEquals(Integer.valueOf(expectedValues[x * result.X() + y]), result.get(x, y));
            }
        }
    }

    @Test
    public void testConnectedComponentsApplyWithBinaryImageDifferentNeighborMode() {
        BinaryImage binaryImage = new BinaryImage(3, 3, new byte[][] { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } });
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage result = cc.apply(binaryImage);
        int[] expectedValues = new int[] { 1, 1, 1, 1, 1, 1, 1 };
        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                assertEquals(Integer.valueOf(expectedValues[x * result.X() + y]), result.get(x, y));
            }
        }

        binaryImage = new BinaryImage(3, 3, new byte[][] { { 1, 1, 1, 1, 1 }, { 1, 0, 1, 1, 1 }, { 1, 1, 1, 1, 1 } });
        result = cc.apply(binaryImage);
        expectedValues = new int[] { 1, 1, 1, 2, 1, 1, 1 };
        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                assertEquals(Integer.valueOf(expectedValues[x * result.X() + y]), result.get(x, y));
            }
        }
    }
}