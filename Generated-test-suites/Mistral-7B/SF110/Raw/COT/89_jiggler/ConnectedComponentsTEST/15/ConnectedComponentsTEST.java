package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;

import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        try {
            new ConnectedComponents(2, 1);
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

        new ConnectedComponents(1, 0);
        new ConnectedComponents(1, 1);
    }

    @Test
    public void testApply_allPixelsAreBackground() {
        BinaryImage input = new BinaryImage(3, 3);
        for (int x = 0; x < input.X(); ++x) {
            for (int y = 0; y < input.Y(); ++y) {
                input.set(x, y, (byte) 0);
            }
        }

        ConnectedComponents component = new ConnectedComponents(1, 0);
        GrayImage output = component.apply(input);

        for (int x = 0; x < output.X(); ++x) {
            for (int y = 0; y < output.Y(); ++y) {
                assertEquals(Integer.valueOf(0), output.get(x, y));
            }
        }
    }

    @Test
    public void testApply_singleIsland() {
        BinaryImage input = new BinaryImage(3, 3);
        input.set(1, 1, (byte) 1);
        input.set(2, 1, (byte) 1);
        input.set(1, 2, (byte) 1);
        input.set(2, 2, (byte) 1);

        ConnectedComponents component = new ConnectedComponents(1, 0);
        GrayImage output = component.apply(input);

        for (int x = 0; x < output.X(); ++x) {
            for (int y = 0; y < output.Y(); ++y) {
                if (x == 1 && y == 1) {
                    assertEquals(Integer.valueOf(2), output.get(x, y));
                } else {
                    assertEquals(Integer.valueOf(0), output.get(x, y));
                }
            }
        }
    }

    @Test
    public void testApply_multipleIslands() {
        BinaryImage input = new BinaryImage(4, 4);
        input.set(1, 1, (byte) 1);
        input.set(2, 1, (byte) 1);
        input.set(2, 2, (byte) 1);
        input.set(3, 2, (byte) 1);
        input.set(1, 3, (byte) 1);
        input.set(2, 3, (byte) 1);
        input.set(3, 3, (byte) 1);

        ConnectedComponents component = new ConnectedComponents(1, 0);
        GrayImage output = component.apply(input);

        for (int x = 0; x < output.X(); ++x) {
            for (int y = 0; y < output.Y(); ++y) {
                if (x == 1 && y == 1) {
                    assertEquals(Integer.valueOf(3), output.get(x, y));
                } else if (x == 1 && y == 2) {
                    assertEquals(Integer.valueOf(3), output.get(x, y));
                } else if (x == 2 && y == 1) {
                    assertEquals(Integer.valueOf(3), output.get(x, y));
                } else if (x == 2 && y == 2) {
                    assertEquals(Integer.valueOf(3), output.get(x, y));
                } else {
                    assertEquals(Integer.valueOf(0), output.get(x, y));
                }
            }
        }
    }

    @Test
    public void testApply_edgeCases_backgroundPixelIsInsideTheComponent() {
        BinaryImage input = new BinaryImage(3, 3);
        input.set(1, 1, (byte) 1);
        input.set(2, 1, (byte) 1);
        input.set(1, 2, (byte) 1);
        input.set(2, 2, (byte) 1);
        input.set(0, 2, (byte) 1);

        ConnectedComponents component = new ConnectedComponents(1, 0);
        try {
            component.apply(input);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testApply_edgeCases_backgroundPixelIsOutsideTheImage() {
        BinaryImage input = new BinaryImage(3, 3);
        input.set(1, 1, (byte) 1);
        input.set(2, 1, (byte) 1);
        input.set(1, 2, (byte) 1);
        input.set(2, 2, (byte) 1);

        ConnectedComponents component = new ConnectedComponents(1, 0);
        try {
            component.apply(new BinaryImage(2, 2));
            fail("Expected ImageSizeException");
        } catch (ImageSizeException e) {
            // Expected
        }
    }

}