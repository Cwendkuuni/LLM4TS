
package jigl.image.ops.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import jigl.image.ops.*;
import jigl.image.types.*;
import jigl.image.exceptions.*;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class ConnectedComponentsTest {

    @Parameterized.Parameters(name = "{index}: testApply_{0}_{1}")
    public static Object[] data() {
        return new Object[][]{
                {new int[][]{
                        {1, 1, 1, 1, 1},
                        {1, 0, 1, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 0, 1, 0, 1},
                        {1, 1, 1, 1, 1}
                }, 0, "4Neighbors"},
                {new int[][]{
                        {1, 1, 1, 1, 1},
                        {1, 1, 1, 1, 1},
                        {1, 0, 1, 0, 1},
                        {1, 0, 0, 0, 1},
                        {1, 1, 1, 1, 1}
                }, 1, "8Neighbors"},
                {new int[][]{
                        {}
                }, null, "EmptyImage"}
        };
    }

    private final int[][] _input;
    private final Object _background;
    private final String _neighbors;

    public ConnectedComponentsTest(int[][] input, Object background, String neighbors) {
        _input = input;
        _background = background;
        _neighbors = neighbors;
    }

    @Test
    public void testApply() {
        BinaryImage binaryImage = new BinaryImage(_input);
        ConnectedComponents connectedComponents = new ConnectedComponents(_neighbors.equals("8Neighbors") ? 1 : 0, _background);
        GrayImage result = connectedComponents.apply(binaryImage);
        // Test the result of the apply() method

        int expectedSizeX = binaryImage.X();
        int expectedSizeY = binaryImage.Y();
        assertEquals(expectedSizeX, result.X());
        assertEquals(expectedSizeY, result.Y());

        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                int expectedValue = (_input[x][y] + 1) / 2;
                assertEquals(expectedValue, result.get(x, y).intValue());
            }
        }

        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApply_EmptyImage() {
        BinaryImage binaryImage = new BinaryImage(new int[][]{});
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        // Test an error scenario with an empty image
    }

    @Test
    public void testConnectedComponentsConstruction_ValidArguments() {
        ConnectedComponents connectedComponents = new ConnectedComponents(0, 0);
        // Test the constructor with valid arguments
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsConstruction_InvalidArguments() {
        ConnectedComponents connectedComponents = new ConnectedComponents(2, 2);
        // Test the constructor with invalid arguments
    }
}