package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.BinaryImage;

import org.junit.Before;
import org.junit.Test;

public class ConnectedComponentsTest {

    private ConnectedComponents cc;
    private GrayImage result;
    private BinaryImage input;

    @Before
    public void setUp() {
        this.cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        this.input = new BinaryImage(5, 5);
        this.input.set(0, 0, 1);
        this.input.set(1, 0, 1);
        this.input.set(2, 0, 1);
        this.input.set(3, 0, 1);
        this.input.set(4, 0, 1);
        this.input.set(0, 1, 1);
        this.input.set(1, 1, 1);
        this.input.set(2, 1, 1);
        this.input.set(3, 1, 0);
        this.input.set(4, 1, 1);
        this.input.set(0, 2, 1);
        this.input.set(1, 2, 1);
        this.input.set(2, 2, 1);
        this.input.set(3, 2, 1);
        this.input.set(4, 2, 1);
        this.input.set(0, 3, 1);
        this.input.set(1, 3, 1);
        this.input.set(2, 3, 1);
        this.input.set(3, 3, 1);
        this.input.set(4, 3, 1);
        this.input.set(0, 4, 1);
        this.input.set(1, 4, 1);
        this.input.set(2, 4, 1);
        this.input.set(3, 4, 0);
        this.input.set(4, 4, 1);
        this.input.set(1, 2, 0);
        this.result = new GrayImage(5, 5);
    }

    @Test
    public void testConnectedComponents() throws InvalidArgumentException {
        this.cc.apply(this.input, this.result);

        // Test that all pixels with background value are set to 0
        for (int x = 0; x < this.result.X(); ++x) {
            for (int y = 0; y < this.result.Y(); ++y) {
                if (this.input.get(x, y) == 0) {
                    assertEquals("Pixel with background value not set to 0", 0, this.result.get(x, y).intValue());
                }
            }
        }

        // Test that all connected components are labeled correctly
        assertEquals("Connected component labels not equal to expected", 7, this.result.X() * this.result.Y());
        assertEquals("Connected component label for pixel (0, 0)", 1, this.result.get(0, 0).intValue());
        assertEquals("Connected component label for pixel (1, 0)", 1, this.result.get(1, 0).intValue());
        assertEquals("Connected component label for pixel (2, 0)", 1, this.result.get(2, 0).intValue());
        assertEquals("Connected component label for pixel (3, 0)", 2, this.result.get(3, 0).intValue());
        assertEquals("Connected component label for pixel (4, 0)", 1, this.result.get(4, 0).intValue());
        assertEquals("Connected component label for pixel (0, 1)", 1, this.result.get(0, 1).intValue());
        assertEquals("Connected component label for pixel (1, 1)", 1, this.result.get(1, 1).intValue());
        assertEquals("Connected component label for pixel (2, 1)", 1, this.result.get(2, 1).intValue());
        assertEquals("Connected component label for pixel (3, 1)", 3, this.result.get(3, 1).intValue());
        assertEquals("Connected component label for pixel (4, 1)", 1, this.result.get(4, 1).intValue());
        assertEquals("Connected component label for pixel (0, 2)", 1, this.result.get(0, 2).intValue());
        assertEquals("Connected component label for pixel (1, 2)", 1, this.result.get(1, 2).intValue());
        assertEquals("Connected component label for pixel (2, 2)", 1, this.result.get(2, 2).intValue());
        assertEquals("Connected component label for pixel (3, 2)", 1, this.result.get(3, 2).intValue());
        assertEquals("Connected component label for pixel (4, 2)", 1, this.result.get(4, 2).intValue());
        assertEquals("Connected component label for pixel (0, 3)", 1, this.result.get(0, 3).intValue());
        assertEquals("Connected component label for pixel (1, 3)", 1, this.result.get(1, 3).intValue());
        assertEquals("Connected component label for pixel (2, 3)", 1, this.result.get(2, 3).intValue());
        assertEquals("Connected component label for pixel (3, 3)", 4, this.result.get(3, 3).intValue());
        assertEquals("Connected component label for pixel (4, 3)", 1, this.result.get(4, 3).intValue());
        assertEquals("Connected component label for pixel (0, 4)", 1, this.result.get(0, 4).intValue());
        assertEquals("Connected component label for pixel (1, 4)", 1, this.result.get(1, 4).intValue());
        assertEquals("Connected component label for pixel (2, 4)", 1, this.result.get(2, 4).intValue());
        assertEquals("Connected component label for pixel (3, 4)", 5, this.result.get(3, 4).intValue());
        assertEquals("Connected component label for pixel (4, 4)", 1, this.result.get(4, 4).intValue());

        // Test that pixels with the same label are connected
        int label1 = this.result.get(0, 0).intValue();
        int x1 = 0, y1 = 0;
        int label2 = this.result.get(1, 1).intValue();
        int x2 = 1, y2 = 1;
        int label3 = this.result.get(3, 1).intValue();
        int x3 = 3, y3 = 1;
        assertTrue("Pixels with labels " + label1 + " and " + label2 + " not connected", this.areConnected(x1, y1, x2, y2));
        assertTrue("Pixels with labels " + label1 + " and " + label3 + " not connected", this.areConnected(x1, y1, x3, y3));
        assertTrue("Pixels with labels " + label2 + " and " + label3 + " not connected", this.areConnected(x2, y2, x3, y3));

        // Test that pixels with different labels are not connected
        int label4 = this.result.get(0, 3).intValue();
        int label5 = this.result.get(3, 4).intValue();
        assertFalse("Pixels with labels " + label4 + " and " + label5 + " connected", this.areConnected(x1, y1, x3, y3));

        // Test that all pixels with label 1 are connected to a pixel with label 3
        int label6 = this.result.get(3, 2).intValue();
        int x6 = 3, y6 = 2;
        for (int x = 0; x < this.result.X(); ++x) {
            for (int y = 0; y < this.result.Y(); ++y) {
                if (this.result.get(x, y).intValue() == 1) {
                    assertTrue("Pixel with label 1 not connected to pixel with label 3", this.areConnected(x, y, x6, y6));
                }
            }
        }

        // Test that all pixels with label 3 are connected to a pixel with label 1
        for (int x = 0; x < this.result.X(); ++x) {
            for (int y = 0; y < this.result.Y(); ++y) {
                if (this.result.get(x, y).intValue() == 3) {
                    assertTrue("Pixel with label 3 not connected to pixel with label 1", this.areConnected(x, y, x1, y1));
                }
            }
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArgumentException() throws InvalidArgumentException {
        new ConnectedComponents(-1, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArgumentException2() throws InvalidArgumentException {
        new ConnectedComponents(0, 2);
    }

    private boolean areConnected(int x1, int y1, int x2, int y2) {
        return this.cc.apply(new BinaryImage(this.result.X(), this.result.Y()), this.result).get(x2, y2).intValue()
                == this.result.get(x1, y1).intValue();
    }
}