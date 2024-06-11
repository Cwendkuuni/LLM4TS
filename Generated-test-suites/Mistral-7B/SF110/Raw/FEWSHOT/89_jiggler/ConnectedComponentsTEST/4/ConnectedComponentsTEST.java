package jigl.image.ops;

import static org.junit.Assert.*;
import jigl.image.types.*;
import jigl.image.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponents() {
        BinaryImage binaryImage = new BinaryImage(5, 5);
        binaryImage.set(1, 1, 1);
        binaryImage.set(1, 2, 1);
        binaryImage.set(1, 3, 1);
        binaryImage.set(1, 4, 1);
        binaryImage.set(2, 2, 1);
        binaryImage.set(3, 2, 1);
        binaryImage.set(3, 3, 1);
        binaryImage.set(4, 3, 1);
        binaryImage.set(4, 4, 1);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(binaryImage);
        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                int expected = 1;
                int current = result.get(x, y).intValue();
                if (x == 1 && y == 1) {
                    expected = 1;
                } else if (x == 1 && y == 2) {
                    expected = 1;
                } else if (x == 1 && y == 3) {
                    expected = 1;
                } else if (x == 1 && y == 4) {
                    expected = 1;
                } else if (x == 2 && y == 2) {
                    expected = 1;
                } else if (x == 3 && y == 2) {
                    expected = 1;
                } else if (x == 3 && y == 3) {
                    expected = 1;
                } else if (x == 4 && y == 3) {
                    expected = 1;
                } else if (x == 4 && y == 4) {
                    expected = 1;
                }
                assertEquals(expected, current);
            }
        }
    }

    @Test
    public void testConnectedComponentsTwoComponents() {
        BinaryImage binaryImage = new BinaryImage(5, 5);
        binaryImage.set(1, 1, 1);
        binaryImage.set(2, 2, 1);
        binaryImage.set(3, 2, 1);
        binaryImage.set(4, 2, 1);
        binaryImage.set(1, 3, 1);
        binaryImage.set(2, 3, 1);
        binaryImage.set(3, 3, 1);
        binaryImage.set(4, 3, 1);
        binaryImage.set(1, 4, 1);
        binaryImage.set(2, 4, 1);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(binaryImage);
        int component1Size = 0;
        int component2Size = 0;
        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                int current = result.get(x, y).intValue();
                if (current == 1) {
                    ++component1Size;
                } else if (current != 0) {
                    ++component2Size;
                }
            }
        }
        assertEquals(2, result.X() * result.Y());
        assertEquals(5, component1Size);
        assertEquals(5, component2Size);
    }

    @Test
    public void testConnectedComponentsOneComponent() {
        BinaryImage binaryImage = new BinaryImage(5, 5);
        binaryImage.set(1, 1, 1);
        binaryImage.set(2, 1, 1);
        binaryImage.set(3, 1, 1);
        binaryImage.set(4, 1, 1);
        binaryImage.set(5, 1, 1);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(binaryImage);
        assertEquals(1, result.X() * result.Y());
        int componentSize = 0;
        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                int current = result.get(x, y).intValue();
                if (current != 0) {
                    ++componentSize;
                }
            }
        }
        assertEquals(5 * 5, componentSize);
    }

    @Test
    public void testConnectedComponentsThreeComponents() {
        BinaryImage binaryImage = new BinaryImage(5, 5);
        binaryImage.set(1, 1, 1);
        binaryImage.set(1, 2, 1);
        binaryImage.set(1, 3, 1);
        binaryImage.set(1, 4, 1);
        binaryImage.set(2, 2, 1);
        binaryImage.set(3, 2, 1);
        binaryImage.set(3, 3, 1);
        binaryImage.set(4, 3, 1);
        binaryImage.set(4, 4, 1);
        binaryImage.set(2, 3, 1);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(binaryImage);
        int component1Size = 0;
        int component2Size = 0;
        int component3Size = 0;
        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                int current = result.get(x, y).intValue();
                if (current == 1) {
                    ++component1Size;
                } else if (current == 2) {
                    ++component2Size;
                } else if (current != 0) {
                    ++component3Size;
                }
            }
        }
        assertEquals(3, result.X() * result.Y());
        assertEquals(4, component1Size);
        assertEquals(4, component2Size);
        assertEquals(4, component3Size);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsInvalidArguments() {
        BinaryImage binaryImage = new BinaryImage(5, 5);
        ConnectedComponents cc = new ConnectedComponents(1, 2);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsInvalidArguments2() {
        BinaryImage binaryImage = new BinaryImage(5, 5);
        ConnectedComponents cc = new ConnectedComponents(0, 2);
    }
}