package jigl.image.ops;

import static org.junit.Assert.*;
import jigl.image.types.*;
import jigl.image.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArgumentException() {
        new ConnectedComponents(-2, 1);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArgumentException2() {
        new ConnectedComponents(0, 2);
    }

    @Test
    public void testApplyEmptyBinaryImage() {
        BinaryImage binaryImage = new BinaryImage(new GrayImage(new int[0][0]));
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        GrayImage result = connectedComponents.apply(binaryImage);
        assertNotNull(result);
        assertEquals(0, result.X());
        assertEquals(0, result.Y());
    }

    @Test
    public void testApplyBinaryImageWithSingleComponent() {
        int[][] binaryImageData = new int[][]{
                {1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 1, 1, 1, 1}
        };
        BinaryImage binaryImage = new BinaryImage(new Size(5, 5), binaryImageData);
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        GrayImage result = connectedComponents.apply(binaryImage);

        assertEquals(5, result.X());
        assertEquals(5, result.Y());
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(1, 1));
        assertEquals(1, result.get(2, 2));
        assertEquals(1, result.get(3, 3));
        assertEquals(1, result.get(4, 4));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(1, 0));
        assertEquals(0, result.get(1, 4));
        assertEquals(0, result.get(4, 0));
        assertEquals(0, result.get(4, 1));
        assertEquals(0, result.get(4, 4));
    }

    @Test
    public void testApplyBinaryImageWithMultipleComponents() {
        int[][] binaryImageData = new int[][]{
                {1, 1, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 1, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0}
        };
        BinaryImage binaryImage = new BinaryImage(new Size(6, 5), binaryImageData);
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        GrayImage result = connectedComponents.apply(binaryImage);

        assertEquals(6, result.X());
        assertEquals(5, result.Y());
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(1, 1));
        assertEquals(1, result.get(2, 2));
        assertEquals(2, result.get(3, 3));
        assertEquals(1, result.get(4, 4));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(1, 0));
        assertEquals(0, result.get(1, 4));
        assertEquals(0, result.get(4, 0));
        assertEquals(0, result.get(4, 1));
        assertEquals(0, result.get(4, 4));
        assertEquals(2, result.get(5, 2));
        assertEquals(2, result.get(5, 3));
        assertEquals(0, result.get(5, 0));
        assertEquals(0, result.get(5, 1));
        assertEquals(0, result.get(5, 4));
    }

    @Test
    public void testApplyBinaryImageWithMultipleComponentsConnected() {
        int[][] binaryImageData = new int[][]{
                {1, 1, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 1, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 1, 1, 1, 1},
                {0, 0, 1, 1, 0},
                {0, 1, 1, 1, 0}
        };
        BinaryImage binaryImage = new BinaryImage(new Size(6, 5), binaryImageData);
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        GrayImage result = connectedComponents.apply(binaryImage);

        assertEquals(6, result.X());
        assertEquals(5, result.Y());
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(1, 1));
        assertEquals(1, result.get(2, 2));
        assertEquals(2, result.get(3, 3));
        assertEquals(1, result.get(4, 4));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(1, 0));
        assertEquals(0, result.get(1, 4));
        assertEquals(0, result.get(4, 0));
        assertEquals(0, result.get(4, 1));
        assertEquals(0, result.get(4, 4));
        assertEquals(3, result.get(5, 2));
        assertEquals(3, result.get(5, 3));
        assertEquals(0, result.get(5, 0));
        assertEquals(0, result.get(5, 1));
        assertEquals(0, result.get(5, 4));
    }

    @Test
    public void testApplyBinaryImageWithOneComponentConnectedMultipleWays() {
        int[][] binaryImageData = new int[][]{
                {1, 1, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 1, 1, 1, 1},
        };
        BinaryImage binaryImage = new BinaryImage(new Size(6, 5), binaryImageData);
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        GrayImage result = connectedComponents.apply(binaryImage);

        assertEquals(6, result.X());
        assertEquals(5, result.Y());
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(1, 1));
        assertEquals(1, result.get(2, 2));
        assertEquals(1, result.get(3, 3));
        assertEquals(1, result.get(4, 4));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(1, 0));
        assertEquals(0, result.get(1, 4));
        assertEquals(0, result.get(4, 0));
        assertEquals(0, result.get(4, 1));
        assertEquals(0, result.get(4, 4));
        assertEquals(1, result.get(5, 2));
        assertEquals(1, result.get(5, 3));
        assertEquals(0, result.get(5, 0));
        assertEquals(0, result.get(5, 1));
        assertEquals(0, result.get(5, 4));
    }

    @Test
    public void testApplyBinaryImageWithComponentsConnectedDifferentWays() {
        int[][] binaryImageData = new int[][]{
                {1, 1, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1},
                {1, 0, 1, 0, 1},
                {1, 1, 1, 1, 1},
                {0, 1, 1, 1, 0}
        };
        BinaryImage binaryImage = new BinaryImage(new Size(7, 5), binaryImageData);
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        GrayImage result = connectedComponents.apply(binaryImage);

        assertEquals(7, result.X());
        assertEquals(5, result.Y());
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(1, 1));
        assertEquals(1, result.get(2, 2));
        assertEquals(2, result.get(3, 3));
        assertEquals(1, result.get(4, 4));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(1, 0));
        assertEquals(0, result.get(1, 4));
        assertEquals(0, result.get(4, 0));
        assertEquals(0, result.get(4, 1));
        assertEquals(0, result.get(4, 4));
        assertEquals(3, result.get(5, 2));
        assertEquals(3, result.get(5, 3));
        assertEquals(0, result.get(5, 0));
        assertEquals(0, result.get(5, 1));
        assertEquals(0, result.get(5, 4));
        assertEquals(4, result.get(6, 2));
        assertEquals(4, result.get(6, 3));
        assertEquals(0, result.get(6, 0));
        assertEquals(0, result.get(6, 1));
        assertEquals(0, result.get(6, 4));
    }
}