package jigl.image.ops;

import static org.junit.Assert.*;
import jigl.image.types.*;
import jigl.image.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponents() {
        BinaryImage input = new BinaryImage(5, 5);
        input.set(1, 1, 1);
        input.set(1, 2, 1);
        input.set(1, 3, 1);
        input.set(1, 4, 1);
        input.set(2, 1, 1);
        input.set(2, 2, 1);
        input.set(2, 3, 1);
        input.set(2, 4, 1);
        input.set(2, 5, 1);
        input.set(3, 1, 1);
        input.set(3, 2, 1);
        input.set(3, 3, 1);
        input.set(3, 4, 1);
        input.set(3, 5, 1);
        input.set(4, 1, 1);
        input.set(4, 2, 1);
        input.set(4, 3, 1);
        input.set(4, 4, 1);
        input.set(4, 5, 1);
        input.set(5, 1, 1);
        input.set(5, 2, 1);
        input.set(5, 3, 1);
        input.set(5, 4, 1);
        input.set(5, 5, 1);

        ConnectedComponents cc = new ConnectedComponents(0, 1);
        GrayImage output = cc.apply(input);

        assertEquals(13, output.X());
        assertEquals(13, output.Y());

        for (int i = 0; i < output.X(); i++) {
            for (int j = 0; j < output.Y(); j++) {
                assertEquals(1, output.get(i, j).intValue());
            }
        }
    }

    @Test
    public void testConnectedComponentsWithBackground() {
        BinaryImage input = new BinaryImage(5, 5);
        input.set(1, 1, 0);
        input.set(1, 2, 1);
        input.set(1, 3, 1);
        input.set(1, 4, 1);
        input.set(2, 1, 1);
        input.set(2, 2, 1);
        input.set(2, 3, 1);
        input.set(2, 4, 1);
        input.set(2, 5, 1);
        input.set(3, 1, 1);
        input.set(3, 2, 1);
        input.set(3, 3, 1);
        input.set(3, 4, 1);
        input.set(3, 5, 1);
        input.set(4, 1, 1);
        input.set(4, 2, 1);
        input.set(4, 3, 1);
        input.set(4, 4, 1);
        input.set(4, 5, 1);
        input.set(5, 1, 1);
        input.set(5, 2, 1);
        input.set(5, 3, 1);
        input.set(5, 4, 0);
        input.set(5, 5, 1);

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage output = cc.apply(input);

        assertEquals(13, output.X());
        assertEquals(13, output.Y());

        int count = 0;
        for (int i = 0; i < output.X(); i++) {
            for (int j = 0; j < output.Y(); j++) {
                if (output.get(i, j).intValue() == 0) {
                    count++;
                }
            }
        }
        assertEquals(1, count);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidConnectedComponentsParameters() {
        ConnectedComponents cc = new ConnectedComponents(2, 2);
    }
}