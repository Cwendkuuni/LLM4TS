import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        try {
            new ConnectedComponents(2, 1); // Valid arguments
            new ConnectedComponents(0, 1); // Valid arguments

            new ConnectedComponents(-1, 1); // Invalid number of neighbors
            new ConnectedComponents(3, 1); // Invalid number of neighbors

            new ConnectedComponents(1, -1); // Invalid background color
            new ConnectedComponents(1, 2); // Invalid background color
        } catch (InvalidArgumentException e) {
            // Expected
        }
    }
}