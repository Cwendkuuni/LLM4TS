package jigl.image.ops;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jigl.image.ops.ConnectedComponents;
import jigl.image.types.*;
import jigl.image.*;
import jigl.image.exceptions.*;

public class ConnectedComponentsTest {
    ConnectedComponents cc;

    @Before
    public void setup() {
        try {
            cc = new ConnectedComponents(0, 0);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testConnectedComponentsConstructor() {
        Assert.assertEquals(0, cc.getNeighbors());
        Assert.assertEquals(0, cc.getBackground());
    }

    // Test for constructor with invalid arguments
    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsConstructorInvalidArguments() {
        try {
            cc = new ConnectedComponents(2, 10);
            Assert.fail();
        } catch (InvalidArgumentException e) {
            // Expected exception
        }
    }

    // Test for constructor with invalid background color
    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsConstructorInvalidBackgroundColor() {
        try {
            cc = new ConnectedComponents(0, 2);
            Assert.fail();
        } catch (InvalidArgumentException e) {
            // Expected exception
        }
    }

    // Test for apply method with empty image
    @Test
    public void testConnectedComponentsApplyEmptyImage() {
        BinaryImage emptyImage = new BinaryImage(0, 0);
        try {
            GrayImage result = cc.apply(emptyImage);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Test for apply method with image dimensions less than 0
    @Test(expected = IllegalArgumentException.class)
    public void testConnectedComponentsApplyNegativeImageDimensions() {
        BinaryImage negativeImage = new BinaryImage(-1, -1);
        try {
            GrayImage result = cc.apply(negativeImage);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Test for apply method with all pixels having the same value
    @Test
    public void testConnectedComponentsApplyAllPixelsSameValue() {
        BinaryImage sameValueImage = new BinaryImage(3, 3);
        sameValueImage.setAll(1);
        GrayImage result = cc.apply(sameValueImage);
        // Test the result here
    }

    // Test for apply method with ArrayIndexOutOfBoundsException
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testConnectedComponentsApplyArrayIndexOutOfBoundsException() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 1);
        image.set(2, 2, 1);

        try {
            GrayImage result = cc.apply(image);
            // Test the result here
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    // Test for apply method with typical use case goes here
}