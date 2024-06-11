package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        try {
            new ConnectedComponents(2, 0);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // expected
        }

        try {
            new ConnectedComponents(0, 2);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // expected
        }

        new ConnectedComponents(0, 1);
        new ConnectedComponents(1, 0);
        new ConnectedComponents(1, 1);
    }

    @Test
    public void testApply_emptyImage() {
        BinaryImage image = new BinaryImage(0, 0);
        ConnectedComponents cc = new ConnectedComponents(1, 1);
        GrayImage result = cc.apply(image);
        assertEquals(0, result.X());
        assertEquals(0, result.Y());
    }

    @Test
    public void testApply_singlePixel() {
        Integer[][] input = { { 1 } };
        BinaryImage image = new BinaryImage(input);
        ConnectedComponents cc = new ConnectedComponents(1, 1);
        GrayImage result = cc.apply(image);
        GrayImage expected = new GrayImage(image.X(), image.Y());
        expected.set(0, 0, Integer.valueOf(1));
        assertEquals(expected, result);
    }

    @Test
    public void testApply_consecutivePixels() {
        Integer[][] input = { { 1, 1, 1, 1 }, { 1, 1, 1, 1 } };
        BinaryImage image = new BinaryImage(input);
        ConnectedComponents cc = new ConnectedComponents(1, 1);
        GrayImage result = cc.apply(image);
        GrayImage expected = new GrayImage(image.X(), image.Y());
        expected.set(0, 0, Integer.valueOf(1));
        expected.set(1, 0, Integer.valueOf(1));
        expected.set(0, 1, Integer.valueOf(1));
        expected.set(1, 1, Integer.valueOf(1));
        assertEquals(expected, result);
    }

    @Test
    public void testApply_disconnectedPixels() {
        Integer[][] input = { { 1, 0, 1 }, { 0, 1, 0 } };
        BinaryImage image = new BinaryImage(input);
        ConnectedComponents cc = new ConnectedComponents(1, 1);
        GrayImage result = cc.apply(image);
        GrayImage expected = new GrayImage(image.X(), image.Y());
        expected.set(0, 0, Integer.valueOf(1));
        expected.set(1, 0, Integer.valueOf(2));
        expected.set(0, 1, Integer.valueOf(3));
        expected.set(1, 1, Integer.valueOf(3));
        assertEquals(expected, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testApply_negativeCoordinates() {
        Integer[][] input = { { 1, 1 }, { 1, 1 } };
        BinaryImage image = new BinaryImage(input);
        ConnectedComponents cc = new ConnectedComponents(1, 1);
        GrayImage result = cc.apply(image);
        int x = -1;
        int y = -1;
        result.get(x, y);
    }

    @Test(expected = NullPointerException.class)
    public void testApply_nullImage() {
        ConnectedComponents cc = new ConnectedComponents(1, 1);
        GrayImage result = cc.apply(null);
    }
}