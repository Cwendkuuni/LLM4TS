package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.BinaryImage;

import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsInstance() {
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 1);
            assertFalse(cc instanceof ExceptionInInitializerError);
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not be thrown when creating ConnectedComponents instance with valid arguments");
        }

        try {
            ConnectedComponents cc = new ConnectedComponents(2, 1);
            fail("InvalidArgumentException should be thrown when creating ConnectedComponents instance with invalid neighbors argument");
        } catch (InvalidArgumentException e) {
            // expected
        }

        try {
            ConnectedComponents cc = new ConnectedComponents(-1, 1);
            fail("InvalidArgumentException should be thrown when creating ConnectedComponents instance with invalid neighbors argument");
        } catch (InvalidArgumentException e) {
            // expected
        }

        try {
            ConnectedComponents cc = new ConnectedComponents(1, 2);
            fail("InvalidArgumentException should be thrown when creating ConnectedComponents instance with invalid background argument");
        } catch (InvalidArgumentException e) {
            // expected
        }

        try {
            ConnectedComponents cc = new ConnectedComponents(1, -1);
            fail("InvalidArgumentException should be thrown when creating ConnectedComponents instance with invalid background argument");
        } catch (InvalidArgumentException e) {
            // expected
        }
    }

    @Test
    public void testApply() {
        BinaryImage binaryImage = new BinaryImage(new byte[][] { { 0, 1, 1, 1, 1, 1, 1, 0 },
                { 1, 1, 0, 1, 1, 1, 1, 1 },
                { 1, 0, 1, 1, 1, 0, 1, 1 },
                { 1, 1, 1, 1, 0, 1, 1, 1 },
                { 1, 1, 1, 0, 1, 1, 1, 1 },
                { 1, 1, 0, 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 1, 0 },
                { 0, 1, 1, 1, 1, 1, 1, 0 } }, 4, 4);

        ConnectedComponents cc = new ConnectedComponents(0, 1);
        GrayImage expectedGrayImage = new GrayImage(new int[] { 0, 1, 1, 1, 1, 1, 1, 0 }, 8);
        GrayImage resultGrayImage = cc.apply(binaryImage);

        assertEquals(expectedGrayImage.getX(), resultGrayImage.getX());
        assertEquals(expectedGrayImage.getY(), resultGrayImage.getY());

        for (int i = 0; i < resultGrayImage.size(); ++i) {
            int expected = expectedGrayImage.get(i);
            int result = resultGrayImage.get(i);
            assertEquals(expected, result);
        }

        ConnectedComponents ccEight = new ConnectedComponents(1, 1);
        GrayImage expectedGrayImageEight = new GrayImage(new int[] { 0, 1, 2, 2, 2, 2, 2, 0 }, 8);
        GrayImage resultGrayImageEight = ccEight.apply(binaryImage);

        assertEquals(expectedGrayImageEight.getX(), resultGrayImageEight.getX());
        assertEquals(expectedGrayImageEight.getY(), resultGrayImageEight.getY());

        for (int i = 0; i < resultGrayImageEight.size(); ++i) {
            int expected = expectedGrayImageEight.get(i);
            int result = resultGrayImageEight.get(i);
            assertEquals(expected, result);
        }
    }

}