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
            new ConnectedComponents(2, 2);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        new ConnectedComponents(0, 1);
        new ConnectedComponents(1, 0);
        new ConnectedComponents(1, 1);
    }

    @Test
    public void testApply_EmptyImage() {
        BinaryImage emptyImage = new BinaryImage(0, 0);
        ConnectedComponents cc = new ConnectedComponents(1, 1);
        GrayImage result = cc.apply(emptyImage);
        assertTrue(result.equals(new GrayImage(0, 0)));
    }

    @Test
    public void testApply_SinglePixel() {
        BinaryImage singlePixelImage = new BinaryImage(1, 1);
        singlePixelImage.set(0, 0, 1);
        ConnectedComponents cc = new ConnectedComponents(1, 1);
        GrayImage result = cc.apply(singlePixelImage);
        assertEquals(Integer.valueOf(1), result.get(0, 0));
    }

    @Test
    public void testApply_ConnectedComponents() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(0, 0, 1);
        inputImage.set(1, 1, 1);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 0, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(1, 1);
        GrayImage result = cc.apply(inputImage);

        assertEquals(Integer.valueOf(1), result.get(0, 0));
        assertEquals(Integer.valueOf(1), result.get(1, 1));
        assertEquals(Integer.valueOf(1), result.get(1, 2));
        assertEquals(Integer.valueOf(1), result.get(2, 0));
        assertEquals(Integer.valueOf(1), result.get(2, 1));
        assertEquals(Integer.valueOf(1), result.get(2, 2));
    }

    @Test
    public void testApply_DisconnectedPixels() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(0, 0, 1);
        inputImage.set(1, 1, 1);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 0, 1);
        inputImage.set(2, 1, 0);
        inputImage.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(1, 1);
        GrayImage result = cc.apply(inputImage);

        assertEquals(Integer.valueOf(1), result.get(0, 0));
        assertEquals(Integer.valueOf(0), result.get(1, 1));
        assertEquals(Integer.valueOf(1), result.get(1, 2));
        assertEquals(Integer.valueOf(1), result.get(2, 0));
        assertEquals(Integer.valueOf(0), result.get(2, 1));
        assertEquals(Integer.valueOf(1), result.get(2, 2));
    }

    @Test
    public void testApply_ImageBorders() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(0, 0, 1);
        inputImage.set(1, 0, 1);
        inputImage.set(2, 0, 1);
        inputImage.set(0, 1, 1);
        inputImage.set(1, 1, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(0, 2, 1);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(1, 1);
        GrayImage result = cc.apply(inputImage);

        assertEquals(Integer.valueOf(1), result.get(0, 0));
        assertEquals(Integer.valueOf(1), result.get(1, 0));
        assertEquals(Integer.valueOf(1), result.get(2, 0));
        assertEquals(Integer.valueOf(1), result.get(0, 1));
        assertEquals(Integer.valueOf(1), result.get(1, 1));
        assertEquals(Integer.valueOf(1), result.get(2, 1));
        assertEquals(Integer.valueOf(1), result.get(0, 2));
        assertEquals(Integer.valueOf(1), result.get(1, 2));
        assertEquals(Integer.valueOf(1), result.get(2, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApply_InvalidNumberOfNeighbors() {
        BinaryImage inputImage = new BinaryImage(1, 1);
        inputImage.set(0, 0, 1);
        ConnectedComponents cc = new ConnectedComponents(2, 1);
        cc.apply(inputImage);
        fail("Expected InvalidArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApply_InvalidBackgroundColor() {
        BinaryImage inputImage = new BinaryImage(1, 1);
        inputImage.set(0, 0, 1);
        ConnectedComponents cc = new ConnectedComponents(1, 2);
        cc.apply(inputImage);
        fail("Expected InvalidArgumentException");
    }
}