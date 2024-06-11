package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

import org.junit.Before;
import org.junit.Test;

public class ConnectedComponentsTest {

    private ConnectedComponents cc;
    private BinaryImage binaryImage;

    @Before
    public void setUp() {
        this.cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        this.binaryImage = new BinaryImage(3, 3);
        this.binaryImage.set(1, 1, 1);
        this.binaryImage.set(1, 2, 1);
        this.binaryImage.set(2, 1, 1);
        this.binaryImage.set(2, 2, 1);
        this.binaryImage.set(2, 3, 1);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArguments() {
        new ConnectedComponents(2, 0);
    }

    @Test
    public void testConnectedComponents() {
        GrayImage result = this.cc.apply(this.binaryImage);
        assertEquals(result.X(), 3);
        assertEquals(result.Y(), 3);
        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                int expected = 1;
                if ((x == 0 && y == 0) || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)) {
                    expected = 0;
                }
                assertEquals(result.get(x, y), Integer.valueOf(expected));
            }
        }
    }

    @Test
    public void testConnectedComponentsEightConnected() {
        this.cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = this.cc.apply(this.binaryImage);
        assertEquals(result.X(), 3);
        assertEquals(result.Y(), 3);
        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                int expected = 1;
                if ((x == 0 && y == 0) || (x == 1 && y == 1) || (x == 1 && y == 2) || (x == 2 && y == 1)
                        || (x == 2 && y == 2) || (x == 2 && y == 3) || (x == 1 && y == 3) || (x == 0 && y == 3)) {
                    expected = 0;
                }
                assertEquals(result.get(x, y), Integer.valueOf(expected));
            }
        }
    }

    @Test
    public void testConnectedComponentsBackground() {
        this.cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
        GrayImage result = this.cc.apply(this.binaryImage);
        assertEquals(result.X(), 3);
        assertEquals(result.Y(), 3);
        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                if (x == 0 && y == 0) {
                    assertEquals(result.get(x, y), Integer.valueOf(1));
                } else {
                    assertEquals(result.get(x, y), Integer.valueOf(0));
                }
            }
        }
    }

    @Test
    public void testConnectedComponentsEmptyImage() {
        BinaryImage emptyImage = new BinaryImage(0, 0);
        this.cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = this.cc.apply(emptyImage);
        assertEquals(result.X(), 0);
        assertEquals(result.Y(), 0);
    }

    @Test
    public void testConnectedComponentsInvalidImage() {
        BinaryImage invalidImage = new BinaryImage(-1, 3);
        this.cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        try {
            this.cc.apply(invalidImage);
            fail("Expected InvalidImageException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid image dimensions: -1, 3", e.getMessage());
        }
    }
}