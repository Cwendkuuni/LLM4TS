package jigl.image.ops;

import static org.junit.Assert.*;

import jigl.image.types.*;
import jigl.image.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponents() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(1, 1, 1);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage outputImage = cc.apply(inputImage);
        assertEquals(3, outputImage.X());
        assertEquals(3, outputImage.Y());
        assertEquals(Integer.valueOf(1), outputImage.get(1, 1));
        assertEquals(Integer.valueOf(1), outputImage.get(1, 2));
        assertEquals(Integer.valueOf(1), outputImage.get(2, 1));
        assertEquals(Integer.valueOf(2), outputImage.get(2, 2));

        ConnectedComponents cc1 = new ConnectedComponents(1, 0);
        GrayImage outputImage1 = cc1.apply(inputImage);
        assertEquals(3, outputImage1.X());
        assertEquals(3, outputImage1.Y());
        assertEquals(Integer.valueOf(1), outputImage1.get(1, 1));
        assertEquals(Integer.valueOf(1), outputImage1.get(1, 2));
        assertEquals(Integer.valueOf(1), outputImage1.get(2, 1));
        assertEquals(Integer.valueOf(1), outputImage1.get(2, 2));
    }

    @Test
    public void testConnectedComponentsWithDifferentBackground() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(1, 1, 0);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 0);

        ConnectedComponents cc = new ConnectedComponents(0, 1);
        GrayImage outputImage = cc.apply(inputImage);
        assertEquals(3, outputImage.X());
        assertEquals(3, outputImage.Y());
        assertEquals(Integer.valueOf(1), outputImage.get(1, 1));
        assertEquals(Integer.valueOf(1), outputImage.get(1, 2));
        assertEquals(Integer.valueOf(2), outputImage.get(2, 1));
        assertEquals(Integer.valueOf(2), outputImage.get(2, 2));
    }

    @Test
    public void testConnectedComponentsWithFourNeighbors() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(1, 1, 1);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 1);
        inputImage.set(2, 3, 1);

        ConnectedComponents cc = new ConnectedComponents(4, 0);
        GrayImage outputImage = cc.apply(inputImage);
        assertEquals(3, outputImage.X());
        assertEquals(3, outputImage.Y());
        assertEquals(Integer.valueOf(1), outputImage.get(1, 1));
        assertEquals(Integer.valueOf(1), outputImage.get(1, 2));
        assertEquals(Integer.valueOf(1), outputImage.get(2, 1));
        assertEquals(Integer.valueOf(1), outputImage.get(2, 2));
        assertEquals(Integer.valueOf(1), outputImage.get(2, 3));
    }

    @Test
    public void testConnectedComponentsWithEightNeighbors() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(1, 1, 1);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 1, 1);
        inputImage.set(2, 2, 1);
        inputImage.set(2, 3, 1);
        inputImage.set(3, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(8, 0);
        GrayImage outputImage = cc.apply(inputImage);
        assertEquals(3, outputImage.X());
        assertEquals(3, outputImage.Y());
        assertEquals(Integer.valueOf(1), outputImage.get(1, 1));
        assertEquals(Integer.valueOf(1), outputImage.get(1, 2));
        assertEquals(Integer.valueOf(1), outputImage.get(2, 1));
        assertEquals(Integer.valueOf(1), outputImage.get(2, 2));
        assertEquals(Integer.valueOf(1), outputImage.get(2, 3));
        assertEquals(Integer.valueOf(1), outputImage.get(3, 2));
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArguments() {
        BinaryImage inputImage = new BinaryImage(3, 3);

        new ConnectedComponents(-1, 0).apply(inputImage);
        new ConnectedComponents(1, -1).apply(inputImage);
        new ConnectedComponents(2, 1).apply(inputImage);
        new ConnectedComponents(0, 2).apply(inputImage);
    }
}