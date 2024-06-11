package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 1);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // expected
        }

        try {
            ConnectedComponents cc = new ConnectedComponents(0, 2);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // expected
        }

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        assertNotNull(cc);

        ConnectedComponents cc1 = new ConnectedComponents(1, 1);
        assertNotNull(cc1);
    }

    @Test
    public void testApply_emptyImage() {
        BinaryImage input = new BinaryImage(0, 0);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage output = cc.apply(input);
        assertTrue(output.X() == 0 && output.Y() == 0);
    }

    @Test
    public void testApply_singleWhitePixel() {
        BinaryImage input = new BinaryImage(new int[][] { { 1 } });
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage output = cc.apply(input);
        assertEquals(Integer.valueOf(1), output.get(0, 0));
    }

    @Test
    public void testApply_singleBlackPixel() {
        BinaryImage input = new BinaryImage(new int[][] { { 0 } });
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage output = cc.apply(input);
        assertEquals(Integer.valueOf(0), output.get(0, 0));
    }

    @Test
    public void testApply_someWhitePixels() {
        int width = 3;
        int height = 3;
        BinaryImage input = new BinaryImage(new int[][] {
                { 1, 1, 1 },
                { 1, 0, 1 },
                { 1, 1, 1 }
        });
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage output = cc.apply(input);
        assertEquals(Integer.valueOf(1), output.get(0, 0));
        assertEquals(Integer.valueOf(1), output.get(1, 0));
        assertEquals(Integer.valueOf(1), output.get(0, 1));
        assertEquals(Integer.valueOf(1), output.get(1, 1));
        assertEquals(Integer.valueOf(1), output.get(0, 2));
        assertEquals(Integer.valueOf(1), output.get(1, 2));
        assertEquals(Integer.valueOf(1), output.get(2, 0));
        assertEquals(Integer.valueOf(1), output.get(2, 1));
        assertEquals(Integer.valueOf(1), output.get(2, 2));
    }

    @Test
    public void testApply_someBlackPixels() {
        int width = 3;
        int height = 3;
        BinaryImage input = new BinaryImage(new int[][] {
                { 0, 0, 0 },
                { 0, 1, 0 },
                { 0, 0, 0 }
        });
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage output = cc.apply(input);
        assertEquals(Integer.valueOf(0), output.get(0, 0));
        assertEquals(Integer.valueOf(0), output.get(1, 0));
        assertEquals(Integer.valueOf(0), output.get(0, 1));
        assertEquals(Integer.valueOf(0), output.get(1, 1));
        assertEquals(Integer.valueOf(0), output.get(0, 2));
        assertEquals(Integer.valueOf(0), output.get(1, 2));
        assertEquals(Integer.valueOf(0), output.get(2, 0));
        assertEquals(Integer.valueOf(0), output.get(2, 1));
        assertEquals(Integer.valueOf(0), output.get(2, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApply_invalidNeighbors() {
        BinaryImage input = new BinaryImage(new int[][] {
                { 0, 0, 0 },
                { 0, 1, 0 },
                { 0, 0, 0 }
        });
        ConnectedComponents cc = new ConnectedComponents(2, 0);
        cc.apply(input);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApply_invalidBackground() {
        BinaryImage input = new BinaryImage(new int[][] {
                { 0, 0, 0 },
                { 0, 1, 0 },
                { 0, 0, 0 }
        });
        ConnectedComponents cc = new ConnectedComponents(0, 2);
        cc.apply(input);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testApply_edgeCases() {
        BinaryImage input = new BinaryImage(new int[][] {
                { 1, 1, 1 },
                { 1, 0, 1 },
                { 1, 0, 1 }
        });
        ConnectedComponents cc = new ConnectedComponents(0, 0);

        // Check the components are properly connected
        for (int x = 0; x < input.X(); ++x) {
            for (int y = 0; y < input.Y(); ++y) {
                if (input.get(x, y) != 0 && input.get(x, y) != -1) {
                    int currentPixel = input.get(x, y);
                    int neighborPixel = 0;
                    if (x > 0) neighborPixel = input.get(x - 1, y);
                    if (y > 0) neighborPixel = Math.max(neighborPixel, input.get(x, y - 1));
                    if (x < input.X() - 1) neighborPixel = Math.max(neighborPixel, input.get(x + 1, y));
                    if (y < input.Y() - 1) neighborPixel = Math.max(neighborPixel, input.get(x, y + 1));
                    assertEquals(neighborPixel, currentPixel);
                }
            }
        }
    }
}