
package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.exceptions.*;

import org.junit.Test;

public class ConnectedComponentsTest {

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArguments() {
        // ... Expert 1's test code ...
    }

    @Test
    public void testConnectedComponents() {
        // ... Expert 2's test code ...
    }

    @Test
    public void testConnectedComponentsEdgeCasesAndErrors() {
        // ... Expert 3's test code ...
    }

    @Test
    public void testConnectedComponentsWithBackground() {
        BinaryImage binaryImage = new BinaryImage(3, 3);
        binaryImage.set(1, 1, 1);
        binaryImage.set(1, 2, 0);
        binaryImage.set(2, 1, 1);
        binaryImage.set(2, 2, 1);
        binaryImage.set(2, 3, 1);
        binaryImage.set(3, 1, 1);
        binaryImage.set(3, 2, 0);
        binaryImage.set(3, 3, 1);

        ConnectedComponents connectedComponents = new ConnectedComponents(FOUR_CONNECTED, 1);
        GrayImage result = connectedComponents.apply(binaryImage);

        assertEquals(7, result.X());
        assertEquals(7, result.Y());
        assertArrayEquals(new Integer[]{1, 1, 1, 1, 1, 1, 1}, result.getData());
    }
}
