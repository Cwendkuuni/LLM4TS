package jigl.image.ops;

import static org.junit.Assert.*;
import jigl.image.types.*;
import jigl.image.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponents() {
        BinaryImage binaryImage = new GrayImage(5, 5);
        binaryImage.set(1, 1, 1);
        binaryImage.set(1, 2, 1);
        binaryImage.set(1, 3, 1);
        binaryImage.set(1, 4, 1);
        binaryImage.set(2, 1, 1);
        binaryImage.set(2, 3, 1);
        binaryImage.set(2, 4, 1);
        binaryImage.set(3, 1, 1);
        binaryImage.set(3, 2, 1);
        binaryImage.set(3, 3, 1);
        binaryImage.set(3, 4, 1);
        binaryImage.set(4, 1, 1);
        binaryImage.set(4, 2, 1);
        binaryImage.set(4, 3, 1);
        binaryImage.set(4, 4, 1);
        binaryImage.set(5, 1, 1);
        binaryImage.set(5, 2, 1);
        binaryImage.set(5, 3, 1);
        binaryImage.set(5, 4, 1);
        binaryImage.set(5, 5, 1);

        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        GrayImage result = connectedComponents.apply(binaryImage);

        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                int expected = 1;
                int actual = ((Integer) result.get(x, y)).intValue();
                assertEquals("Expected: " + expected + ", but got: " + actual, expected, actual);
            }
        }

        ConnectedComponents connectedComponents1 = new ConnectedComponents(1, 0);
        GrayImage result1 = connectedComponents1.apply(binaryImage);

        int componentCount = 0;
        for (int x = 0; x < result1.X(); ++x) {
            for (int y = 0; y < result1.Y(); ++y) {
                int actual = ((Integer) result1.get(x, y)).intValue();
                if (actual != 0) {
                    componentCount++;
                }
            }
        }
        assertEquals("Component count should be 10", 10, componentCount);

        ConnectedComponents connectedComponents2 = new ConnectedComponents(1, 1);
        GrayImage result2 = connectedComponents2.apply(binaryImage);

        componentCount = 0;
        for (int x = 0; x < result2.X(); ++x) {
            for (int y = 0; y < result2.Y(); ++y) {
                int actual = ((Integer) result2.get(x, y)).intValue();
                if (actual != 0) {
                    componentCount++;
                }
            }
        }
        assertEquals("Component count should be 9", 9, componentCount);
    }
}