package jigl.image.ops.test;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        try {
            new ConnectedComponents(2, 0);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        try {
            new ConnectedComponents(0, 2);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        new ConnectedComponents(0, 1);
        new ConnectedComponents(1, 0);
        new ConnectedComponents(1, 1);
    }

    @Test
    public void testApply_background_is_0() {
        BinaryImage input = new BinaryImage(3, 3);
        input.set(1, 1, 1); // Set pixel to 1 (foreground)
        InputOutputOperator test = new ConnectedComponents(1, 0);
        GrayImage output = test.apply(input);
        assertEquals(Integer.valueOf(1), output.get(1, 1));
    }

    @Test
    public void testApply_background_is_not_0() {
        BinaryImage input = new BinaryImage(3, 3);
        input.set(1, 1, 1); // Set pixel to 1 (foreground)
        input.set(0, 0, 0); // Set background to 0
        InputOutputOperator test = new ConnectedComponents(1, 0);
        GrayImage output = test.apply(input);
        assertEquals(Integer.valueOf(0), output.get(0, 0));
    }

    @Test
    public void testApply_all_pixels_are_background() {
        BinaryImage input = new BinaryImage(3, 3);
        InputOutputOperator test = new ConnectedComponents(1, 0);
        GrayImage output = test.apply(input);
        for (int x = 0; x < output.X(); ++x) {
            for (int y = 0; y < output.Y(); ++y) {
                assertEquals(Integer.valueOf(0), output.get(x, y));
            }
        }
    }

    @Test
    public void testApply_edge_cases_neighbors() {
        BinaryImage input = new BinaryImage(3, 3);
        InputOutputOperator test = new ConnectedComponents(2, 1);
        GrayImage output = test.apply(input);

        // Test for the case where all neighbors are background
        input.set(1, 1, 1);
        input.set(0, 0, 0);
        input.set(2, 0, 0);
        input.set(0, 1, 0);
        input.set(1, 2, 0);
        input.set(2, 1, 0);
        input.set(2, 2, 0);
        output = test.apply(input);
        for (int x = 0; x < output.X(); ++x) {
            for (int y = 0; y < output.Y(); ++y) {
                assertEquals(Integer.valueOf(1), output.get(1, 1));
                assertEquals(Integer.valueOf(0), output.get(x, y));
            }
        }

        // Test for the case where all neighbors are the same non-background value
        input.set(1, 1, 1);
        input.set(0, 0, 2);
        input.set(2, 0, 2);
        input.set(0, 1, 2);
        input.set(1, 2, 2);
        input.set(2, 1, 2);
        input.set(2, 2, 2);
        output = test.apply(input);
        for (int x = 0; x < output.X(); ++x) {
            for (int y = 0; y < output.Y(); ++y) {
                assertEquals(Integer.valueOf(2), output.get(1, 1));
                assertEquals(Integer.valueOf(2), output.get(x, y));
            }
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testApply_edge_cases_index_out_of_bounds() {
        BinaryImage input = new BinaryImage(3, 3);
        InputOutputOperator test = new ConnectedComponents(1, 1);
        test.apply(input); // No exception thrown yet

        input.set(1, 1, 1);
        input.set(0, 0, 0);
        input.set(2, 0, 0);
        input.set(0, 1, 0);
        input.set(1, 2, 0);
        input.set(2, 1, 0);
        input.set(2, 2, 0);

        int x = 10; // Test index out of bounds
        int y = 10;
        test.apply(input).get(x, y); // This will throw IndexOutOfBoundsException
    }
}