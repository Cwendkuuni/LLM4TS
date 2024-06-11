package jigl.image.ops;

import static org.junit.Assert.*;
import jigl.image.types.*;
import org.junit.Test;
import jigl.image.*;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponents() {
        BinaryImage binaryImage = new BinaryImage(new int[][]{
                {0, 0, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 0, 1, 1},
                {0, 1, 0, 1, 1, 0},
                {0, 0, 0, 0, 0, 0}
        }, 3, 3);
        ConnectedComponents component = new ConnectedComponents(0, 0);
         GrayImage result = component.apply(binaryImage);
         int[] expected = {1, 1, 1, 2, 2, 3};
         for (int i = 0; i < result.X(); ++i) {
             for (int j = 0; j < result.Y(); ++j) {
                 assertEquals(Integer.valueOf(expected[i * result.X() + j]), result.get(i, j));
             }
         }
    }

    @Test
    public void testConnectedComponents_1Neighbor() {
        BinaryImage binaryImage = new BinaryImage(new int[][]{
                {0, 0, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 0, 1, 1},
                {0, 1, 0, 1, 1, 0},
                {0, 0, 0, 0, 0, 0},
                {1, 0, 0, 1, 1, 0},
                {1, 0, 0, 1, 1, 0},
                {1, 0, 0, 1, 1, 0},
                {0, 0, 0, 0, 0, 0}
        }, 3, 10);
         ConnectedComponents component = new ConnectedComponents(1, 0);
         GrayImage result = component.apply(binaryImage);
         int[] expected = {1, 1, 1, 2, 2, 3, 4, 4, 4, 5};
         for (int i = 0; i < result.X(); ++i) {
             for (int j = 0; j < result.Y(); ++j) {
                 assertEquals(Integer.valueOf(expected[i * result.X() + j]), result.get(i, j));
             }
         }
    }

    @Test
    public void testConnectedComponents_InvalidArgumentException() {
        BinaryImage binaryImage = new BinaryImage(new int[][]{
                {0, 1}
        }, 1, 1);

        try {
            new ConnectedComponents(2, 0).apply(binaryImage);
            fail("InvalidArgumentException expected");
        } catch (InvalidArgumentException e) {
            // expected
        }

        try {
            new ConnectedComponents(0, 2).apply(binaryImage);
            fail("InvalidArgumentException expected");
        } catch (InvalidArgumentException e) {
            // expected
        }
    }
}