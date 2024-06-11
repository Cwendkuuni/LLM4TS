package jigl.image.ops;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import jigl.image.types.GrayImage;
import jigl.image.types.Pixel;
import jigl.image.exceptions.InvalidArgumentException;

@RunWith(Parameterized.class)
public class ConnectedComponentsTest {

    private static final int width = 5, height = 5;
    private static final byte background = 0;
    private static final byte[][] input = {
            { 1, 1, 1, 1, 1 },
            { 1, 0, 0, 0, 1 },
            { 1, 0, 1, 1, 1 },
            { 1, 0, 1, 0, 1 },
            { 1, 1, 1, 1, 1 }
    };
    private static final byte[] expectedOutputFOUR_CONNECTED = {
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 2, 2, 1,
            1, 1, 2, 3, 1,
            1, 1, 1, 1, 1
    };
    private static final byte[] expectedOutputEIGHT_CONNECTED = {
            1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
            1, 1, 2, 2, 3,
            1, 1, 2, 3, 4,
            1, 1, 1, 1, 1
    };
    private static final GrayImage inputImage;
    private static final GrayImage expectedOutput;
    private int neighbors;

    public ConnectedComponentsTest(int neighbors) {
        this.neighbors = neighbors;
        inputImage = new GrayImage(width, height);
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                inputImage.set(x, y, new Pixel(input[x][y]));
            }
        }
        try {
            ConnectedComponents cc = new ConnectedComponents(neighbors, background);
            expectedOutput = cc.apply(inputImage);
        } catch (InvalidArgumentException e) {
            throw new RuntimeException("InvalidArgumentException should not be thrown", e);
        }
    }

    @Parameterized.Parameters(name = "{0} connected components")
    public static Object[] parameters() {
        return new Object[][] { {0}, {1} };
    }

    @Test
    public void testConnectedComponents() {
        if (neighbors == 0) {
            byte[] output = expectedOutputFOUR_CONNECTED;
            byte[] test = new byte[output.length];
            for (int i = 0; i < output.length; ++i) {
                test[i] = output[i];
            }
            assertArrayEquals(test, expectedOutput.getData());
        } else {
            byte[] output = expectedOutputEIGHT_CONNECTed;
            byte[] test = new byte[output.length];
            for (int i = 0; i < output.length; ++i) {
                test[i] = output[i];
            }
            assertArrayEquals(test, expectedOutput.getData());
        }
    }

    @Test
    public void testApplyWithBackground() {
        byte[] output = new byte[width * height];
        Arrays.fill(output, background);
        GrayImage backgroundImage = new GrayImage(width, height);
        backgroundImage.setData(output);
        ConnectedComponents cc = new ConnectedComponents(neighbors, background);
        GrayImage result = cc.apply(backgroundImage);
        for (int x = 0; x < result.X(); ++x) {
            for (int y = 0; y < result.Y(); ++y) {
                assertEquals(background, result.get(x, y));
            }
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArguments() {
        try {
            new ConnectedComponents(-1, background);
        } catch (InvalidArgumentException e) {
            return;
        }
        throw new AssertionError("InvalidArgumentException not thrown");
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidBackground() {
        try {
            new ConnectedComponents(neighbors, 2);
        } catch (InvalidArgumentException e) {
            return;
        }
        throw new AssertionError("InvalidArgumentException not thrown");
    }
}