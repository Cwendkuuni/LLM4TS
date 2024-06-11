package jigl.image.ops;

import static org.junit.Assert.*;
import jigl.image.types.*;
import jigl.image.*;

public class ConnectedComponentsTest {
    @Test
    public void testConnectedComponents() {
        BinaryImage binaryImage = new BinaryImage(3, 3, new byte[][]{{1, 1, 1}, {1, 0, 1}, {1, 1, 1}});
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        GrayImage result = connectedComponents.apply(binaryImage);
        assertEquals(3, result.X());
        assertEquals(3, result.Y());
        assertEquals(Integer.valueOf(1), result.get(0, 0));
        assertEquals(Integer.valueOf(1), result.get(1, 1));
        assertEquals(Integer.valueOf(2), result.get(2, 0));
        assertEquals(Integer.valueOf(2), result.get(2, 1));
        assertEquals(Integer.valueOf(2), result.get(2, 2));

        BinaryImage binaryImageEightConnected = new BinaryImage(3, 3, new byte[][]{{1, 1, 1}, {1, 0, 1}, {1, 1, 1}});
        ConnectedComponents connectedComponentsEightConnected = new ConnectedComponents(1, 0);
        GrayImage resultEightConnected = connectedComponentsEightConnected.apply(binaryImageEightConnected);
        assertEquals(3, resultEightConnected.X());
        assertEquals(3, resultEightConnected.Y());
        assertEquals(Integer.valueOf(1), resultEightConnected.get(0, 0));
        assertEquals(Integer.valueOf(1), resultEightConnected.get(1, 1));
        assertEquals(Integer.valueOf(2), resultEightConnected.get(2, 0));
        assertEquals(Integer.valueOf(2), resultEightConnected.get(1, 0));
        assertEquals(Integer.valueOf(2), resultEightConnected.get(1, 2));
        assertEquals(Integer.valueOf(2), resultEightConnected.get(2, 1));
        assertEquals(Integer.valueOf(2), resultEightConnected.get(2, 2));
    }

    @Test
    public void testInvalidArguments() {
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(2, 0));
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(0, 2));
    }

    @Test
    public void testApplyNullImage() {
        assertThrows(NullPointerException.class, () -> new ConnectedComponents(0, 0).apply(null));
    }

    @Test
    public void testApplyEmptyImage() {
        BinaryImage emptyImage = new BinaryImage(0, 0, new byte[0][0]);
        GrayImage result = new ConnectedComponents(0, 0).apply(emptyImage);
        assertTrue(result.isEmpty());
    }
}