
package jigl.image.ops;
import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.exceptions.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorValidArguments() {
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        assertNotNull(cc);
    }

    @Test
    public void testConstructorInvalidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 0);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected exception, no action required
        }
    }

    @Test
    public void testApplyValidBinaryImage() {
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

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage outputImage = cc.apply(inputImage);

        // Test if all pixels have been labeled correctly
        for (int x = 0; x < inputImage.getWidth(); ++x) {
            for (int y = 0; y < inputImage.getHeight(); ++y) {
                assertEquals(Integer.valueOf(x + y + 1), outputImage.get(x, y));
            }
        }
    }

    @Test
    public void testApplyEmptyBinaryImage() {
        BinaryImage inputImage = new BinaryImage(0, 0);
        ConnectedComponents cc = new ConnectedComponents(0, 0);

        try {
            GrayImage outputImage = cc.apply(inputImage);
            fail("Expected InvalidImageSizeException");
        } catch (InvalidImageSizeException e) {
            // Expected exception, no action required
        }
    }

    @Test
    public void testApplyBackgroundPixel() {
        BinaryImage inputImage = new BinaryImage(1, 1);
        inputImage.set(0, 0, 0);

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage outputImage = cc.apply(inputImage);

        assertEquals(Integer.valueOf(0), outputImage.get(0, 0));
    }

    @Test
    public void testApplySinglePixel() {
        BinaryImage inputImage = new BinaryImage(1, 1);
        inputImage.set(0, 0, 1);

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage outputImage = cc.apply(inputImage);

        assertEquals(Integer.valueOf(1), outputImage.get(0, 0));
    }
}
