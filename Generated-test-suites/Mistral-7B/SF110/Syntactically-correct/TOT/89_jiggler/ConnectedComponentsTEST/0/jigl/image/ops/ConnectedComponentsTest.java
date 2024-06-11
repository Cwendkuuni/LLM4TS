
import static org.junit.jupiter.api.AssertAsserts.assertEquals;
import static org.junit.jupiter.api.AssertAsserts.assertArrayEquals;
import static org.junit.jupiter.api.AssertAsserts.fail;

import jigl.image.BinaryImage;
import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.GrayImage;
import org.junit.jupiter.api.*;

public class ConnectedComponentsTest {
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidArguments() {
        try {
            new ConnectedComponents(-1, 1);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        try {
            new ConnectedComponents(2, 1);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        try {
            new ConnectedComponents(1, 2);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testApplyBackgroundZero() {
        BinaryImage input = new BinaryImage(3, 3);
        input.set(1, 1, 1);

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage expectedOutput = new GrayImage(3, 3);
        expectedOutput.set(1, 1, 1);

        GrayImage output = cc.apply(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testApplyMultipleConnectedComponents() {
        BinaryImage input = new BinaryImage(5, 5);
        input.set(1, 1, 1);
        input.set(2, 1, 1);
        input.set(3, 1, 1);
        input.set(3, 2, 1);
        input.set(3, 3, 1);
        input.set(3, 4, 1);
        input.set(4, 2, 1);
        input.set(4, 3, 1);

        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage expectedOutput = new GrayImage(5, 5);
        expectedOutput.set(1, 1, 2);
        expectedOutput.set(2, 1, 2);
        expectedOutput.set(3, 1, 2);
        expectedOutput.set(3, 2, 3);
        expectedOutput.set(3, 3, 3);
        expectedOutput.set(3, 4, 3);
        expectedOutput.set(4, 2, 4);
        expectedOutput.set(4, 3, 4);

        GrayImage output = cc.apply(input);

        assertArrayEquals(expectedOutput.getData(), output.getData());
    }
}
