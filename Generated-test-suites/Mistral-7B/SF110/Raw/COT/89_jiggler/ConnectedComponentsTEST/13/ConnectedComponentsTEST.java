import org.junit.Test;
import org.junit.Assert.*;
import jigl.image.*;
import jigl.image.ops.ConnectedComponents;
import static jigl.image.types.GrayImage.*;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructorWithValidArguments() {
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        Assert.assertEquals(0, cc.neighbors);
        Assert.assertEquals(0, cc.background);
    }

    @Test
    public void testConnectedComponentsConstructorWithInvalidArguments() {
        try {
            new ConnectedComponents(2, 3);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // expected
        }
    }

    @Test
    public void testApplyWithAllPixelsBackground() {
        BinaryImage input = new BinaryImage(5, 5);
        for (int x = 0; x < input.X(); ++x) {
            for (int y = 0; y < input.Y(); ++y) {
                input.set(x, y, 0);
            }
        }
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage output = cc.apply(input);
        Assert.assertEquals(1, output.elements().length);
        Assert.assertEquals(0, output.get(0, 0).intValue());
    }

    @Test
    public void testApplyWithSinglePixelNotBackground() {
        BinaryImage input = new BinaryImage(5, 5);
        input.set(2, 2, 1);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage output = cc.apply(input);
        Assert.assertEquals(2, output.elements().length);
        Assert.assertEquals(1, output.get(0, 0).intValue());
        Assert.assertEquals(1, output.get(2, 2).intValue());
    }

    @Test
    public void testApplyWithMultipleConnectedComponents() {
        BinaryImage input = new BinaryImage(5, 5);
        input.set(0, 0, 1);
        input.set(1, 1, 1);
        input.set(2, 1, 1);
        input.set(1, 2, 1);
        input.set(2, 2, 1);
        input.set(3, 2, 1);
        input.set(2, 3, 1);
        input.set(2, 4, 1);
        input.set(3, 4, 1);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage output = cc.apply(input);
        Assert.assertEquals(3, output.elements().length);
        Assert.assertEquals(1, output.get(0, 0).intValue());
        Assert.assertEquals(1, output.get(2, 2).intValue());
        Assert.assertEquals(1, output.get(2, 4).intValue());
    }

    @Test
    public void testApplyWithMultipleConnectedComponentsAndBackground() {
        BinaryImage input = new BinaryImage(5, 5);
        input.set(0, 0, 1);
        input.set(1, 1, 1);
        input.set(2, 1, 1);
        input.set(1, 2, 1);
        input.set(2, 2, 1);
        input.set(3, 2, 1);
        input.set(2, 3, 1);
        input.set(2, 4, 1);
        input.set(3, 4, 1);
        input.set(4, 4, 0);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage output = cc.apply(input);
        Assert.assertEquals(4, output.elements().length);
        Assert.assertEquals(1, output.get(0, 0).intValue());
        Assert.assertEquals(1, output.get(2, 2).intValue());
        Assert.assertEquals(1, output.get(2, 4).intValue());
        Assert.assertEquals(0, output.get(4, 4).intValue());
    }

    @Test
    public void testApplyWithEdgeCases() {
        // Test empty image
        BinaryImage emptyInput = new BinaryImage(0, 0);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        try {
            GrayImage output = cc.apply(emptyInput);
            fail("Expected InvalidImageSizeException");
        } catch (InvalidImageSizeException e) {
            // expected
        }

        // Test image with only background pixels
        BinaryImage backgroundInput = new BinaryImage(5, 5);
        for (int x = 0; x < backgroundInput.X(); ++x) {
            for (int y = 0; y < backgroundInput.Y(); ++y) {
                backgroundInput.set(x, y, 0);
            }
        }
        GrayImage backgroundOutput = new ConnectedComponents(0, 0).apply(backgroundInput);
        Assert.assertEquals(1, backgroundOutput.elements().length);
        Assert.assertEquals(0, backgroundOutput.get(0, 0).intValue());

        // Test image with all pixels as valid component labels
        BinaryImage labeledInput = new BinaryImage(5, 5);
        for (int x = 0; x < labeledInput.X(); ++x) {
            for (int y = 0; y < labeledInput.Y(); ++y) {
                labeledInput.set(x, y, x * 10 + y);
            }
        }
        GrayImage labeledOutput = new ConnectedComponents(0, 1).apply(labeledInput);
        Assert.assertEquals(25, labeledOutput.elements().length);
        for (int x = 0; x < labeledOutput.X(); ++x) {
            for (int y = 0; y < labeledOutput.Y(); ++y) {
                int expected = x * 10 + y;
                Assert.assertEquals(expected, labeledOutput.get(x, y).intValue());
            }
        }

        // Test image with all pixels as invalid component labels
        BinaryImage invalidInput = new BinaryImage(5, 5);
        for (int x = 0; x < invalidInput.X(); ++x) {
            for (int y = 0; y < invalidInput.Y(); ++y) {
                invalidInput.set(x, y, Integer.MAX_VALUE);
            }
        }
        try {
            new ConnectedComponents(0, 0).apply(invalidInput);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // expected
        }
    }
}