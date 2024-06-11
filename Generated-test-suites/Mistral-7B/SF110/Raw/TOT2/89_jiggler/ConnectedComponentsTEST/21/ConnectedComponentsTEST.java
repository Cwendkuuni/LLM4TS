import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.ConnectedComponents;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        // Test valid arguments
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        assertNotNull(cc);

        ConnectedComponents cc1 = new ConnectedComponents(1, 1);
        assertNotNull(cc1);

        // Test invalid arguments
        try {
            ConnectedComponents cc2 = new ConnectedComponents(2, 1);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException iae) {
        }

        try {
            ConnectedComponents cc3 = new ConnectedComponents(-1, 1);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException iae) {
        }

        try {
            ConnectedComponents cc4 = new ConnectedComponents(1, 2);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException iae) {
        }

        try {
            ConnectedComponents cc5 = new ConnectedComponents(1, -1);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException iae) {
        }
    }
}