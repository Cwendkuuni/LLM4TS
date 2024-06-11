import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import org.junit.Assert;
import org.junit.Test;

public class ConnectedComponentsTest {
    @Test
    public void testConnectedComponentsConstructors() {
        try {
            new ConnectedComponents(2, 0);
            Assert.fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        try {
            new ConnectedComponents(0, 2);
            Assert.fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        new ConnectedComponents(0, 0);
    }
}