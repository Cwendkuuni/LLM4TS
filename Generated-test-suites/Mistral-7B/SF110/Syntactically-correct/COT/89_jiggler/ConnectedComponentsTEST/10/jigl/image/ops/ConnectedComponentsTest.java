package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.types.IntegerImage;
import jigl.image.exceptions.InvalidArgumentException;

import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        try {
            new ConnectedComponents(2, 3);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected exception
        }

        try {
            new ConnectedComponents(-1, 3);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected exception
        }

        try {
            new ConnectedComponents(3, -1);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected exception
        }

        new ConnectedComponents(0, 0);
        new ConnectedComponents(1, 0);
        new ConnectedComponents(0, 1);
    }

    @Test
    public void testApply_backgroundIsSet() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 1);

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(image);

        assertEquals(result.get(0, 0), Integer.valueOf(1));
        assertEquals(result.get(0, 1), Integer.valueOf(1));
        assertEquals(result.get(1, 0), Integer.valueOf(1));
        assertEquals(result.get(1, 1), Integer.valueOf(1));
    }

    @Test
    public void testApply_backgroundIsZero() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 1);

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(image);

        assertEquals(result.get(0, 0), Integer.valueOf(0));
        assertEquals(result.get(0, 1), Integer.valueOf(1));
        assertEquals(result.get(1, 0), Integer.valueOf(1));
        assertEquals(result.get(1, 1), Integer.valueOf(1));
    }

    @Test
    public void testApply_singleComponent() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(1, 1, 1);

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(image);

        assertEquals(result.get(1, 1), Integer.valueOf(1));
        assertEquals(result.get(0, 1), Integer.valueOf(1));
        assertEquals(result.get(1, 0), Integer.valueOf(1));
        assertEquals(result.get(1, 2), Integer.valueOf(1));
        assertEquals(result.get(0, 0), Integer.valueOf(0));
        assertEquals(result.get(0, 2), Integer.valueOf(0));
        assertEquals(result.get(2, 0), Integer.valueOf(0));
        assertEquals(result.get(2, 2), Integer.valueOf(0));
    }

    @Test
    public void testApply_twoComponents() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(0, 2, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 0, 1);
        image.set(2, 1, 1);
        image.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(image);

        assertEquals(result.get(0, 0), Integer.valueOf(1));
        assertEquals(result.get(0, 1), Integer.valueOf(1));
        assertEquals(result.get(0, 2), Integer.valueOf(1));
        assertEquals(result.get(1, 0), Integer.valueOf(1));
        assertEquals(result.get(1, 1), Integer.valueOf(1));
        assertEquals(result.get(1, 2), Integer.valueOf(1));
        assertEquals(result.get(2, 0), Integer.valueOf(1));
        assertEquals(result.get(2, 1), Integer.valueOf(1));
        assertEquals(result.get(2, 2), Integer.valueOf(1));

        assertEquals(result.get(1, 1), Integer.valueOf(2));
    }

    @Test
    public void testApply_fourConnectedNeighbors() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 0, 1);
        image.set(2, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 1, 1);
        image.set(2, 1, 1);
        image.set(0, 2, 1);
        image.set(1, 2, 1);
        image.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(result.get(0, 0), Integer.valueOf(1));
        assertEquals(result.get(1, 0), Integer.valueOf(1));
        assertEquals(result.get(2, 0), Integer.valueOf(1));
        assertEquals(result.get(0, 1), Integer.valueOf(1));
        assertEquals(result.get(1, 1), Integer.valueOf(1));
        assertEquals(result.get(2, 1), Integer.valueOf(1));
        assertEquals(result.get(0, 2), Integer.valueOf(1));
        assertEquals(result.get(1, 2), Integer.valueOf(1));
        assertEquals(result.get(2, 2), Integer.valueOf(1));
    }

    @Test
    public void testApply_eightConnectedNeighbors() {
        BinaryImage image = new BinaryImage(4, 4);
        image.set(1, 1, 1);
        image.set(2, 1, 1);
        image.set(3, 1, 1);
        image.set(4, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 2, 1);
        image.set(3, 2, 1);
        image.set(4, 2, 1);
        image.set(1, 3, 1);
        image.set(2, 3, 1);
        image.set(3, 3, 1);
        image.set(4, 3, 1);
        image.set(1, 4, 1);
        image.set(2, 4, 1);
        image.set(3, 4, 1);
        image.set(4, 4, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(result.get(1, 1), Integer.valueOf(1));
        assertEquals(result.get(2, 1), Integer.valueOf(1));
        assertEquals(result.get(3, 1), Integer.valueOf(1));
        assertEquals(result.get(4, 1), Integer.valueOf(1));
        assertEquals(result.get(1, 2), Integer.valueOf(1));
        assertEquals(result.get(2, 2), Integer.valueOf(1));
        assertEquals(result.get(3, 2), Integer.valueOf(1));
        assertEquals(result.get(4, 2), Integer.valueOf(1));
        assertEquals(result.get(1, 3), Integer.valueOf(1));
        assertEquals(result.get(2, 3), Integer.valueOf(1));
        assertEquals(result.get(3, 3), Integer.valueOf(1));
        assertEquals(result.get(4, 3), Integer.valueOf(1));
        assertEquals(result.get(1, 4), Integer.valueOf(1));
        assertEquals(result.get(2, 4), Integer.valueOf(1));
        assertEquals(result.get(3, 4), Integer.valueOf(1));
        assertEquals(result.get(4, 4), Integer.valueOf(1));
    }

    @Test(expected = RuntimeException.class)
    public void testApply_illegalState() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(1, 0, 0);
        image.set(1, 1, 1);

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        cc.apply(image);

        // This should throw an exception
        cc.apply(image);
    }
}