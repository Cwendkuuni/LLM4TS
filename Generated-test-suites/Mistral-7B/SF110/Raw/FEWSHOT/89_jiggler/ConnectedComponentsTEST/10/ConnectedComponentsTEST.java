package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import jigl.image.types.*;
import jigl.image.*;
import jigl.image.exceptions.*;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponents_0Connected() throws InvalidArgumentException {
        BinaryImage binaryImage = new BinaryImage(new byte[][]{{1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 0, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}});
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(binaryImage);

        int[] expected = new int[5];
        expected[0] = 1;
        expected[1] = 1;
        expected[2] = 1;
        expected[3] = 1;
        expected[4] = 1;

        assertArrayEquals(expected, result.getPixels());
    }

    @Test
    public void testConnectedComponents_1Connected() throws InvalidArgumentException {
        BinaryImage binaryImage = new BinaryImage(new byte[][]{{1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 0, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}});
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage result = cc.apply(binaryImage);

        int[] expected = new int[5];
        expected[0] = 1;
        expected[1] = 1;
        expected[2] = 2;
        expected[3] = 2;
        expected[4] = 2;

        assertArrayEquals(expected, result.getPixels());
    }

    @Test
    public void testConnectedComponents_InvalidArguments() {
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(2, 0));
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(0, -1));
    }

    @Test
    public void testConnectedComponents_BackgroundPixels() throws InvalidArgumentException {
        BinaryImage binaryImage = new BinaryImage(new byte[][]{{0, 0, 0, 0, 0}, {0, 1, 1, 1, 0}, {0, 1, 0, 1, 0}, {0, 1, 1, 1, 0}, {0, 0, 0, 0, 0}});
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(binaryImage);

        assertTrue(result.getPixels()[0] == 0);
        assertTrue(result.getPixels()[1] == 1);
        assertTrue(result.getPixels()[2] == 1);
        assertTrue(result.getPixels()[3] == 1);
        assertTrue(result.getPixels()[4] == 0);
    }

    @Test
    public void testConnectedComponents_SameComponent() throws InvalidArgumentException {
        BinaryImage binaryImage = new BinaryImage(new byte[][]{{1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}});
        ConnectedComponents cc = new ConnectedComponents(1, 1);
        GrayImage result = cc.apply(binaryImage);

        int[] expected = new int[25];
        for (int i = 0; i < expected.length; ++i) {
            expected[i] = 1;
        }

        assertArrayEquals(expected, result.getPixels());
    }

    @Test
    public void testConnectedComponents_MixedComponents() throws InvalidArgumentException {
        BinaryImage binaryImage = new BinaryImage(new byte[][]{{1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 0, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}});
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage result = cc.apply(binaryImage);

        int[] expected = {1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        assertArrayEquals(expected, result.getPixels());
    }

    @Test
    public void testConnectedComponents_IdenticalConnectedComponents() throws InvalidArgumentException {
        BinaryImage binaryImage = new BinaryImage(new byte[][]{{1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}});
        ConnectedComponents cc1 = new ConnectedComponents(1, 0);
        GrayImage result1 = cc1.apply(binaryImage);

        int[] expected1 = {1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        assertArrayEquals(expected1, result1.getPixels());

        ConnectedComponents cc2 = new ConnectedComponents(1, 0);
        GrayImage result2 = cc2.apply(binaryImage);

        assertArrayEquals(expected1, result2.getPixels());
    }
}