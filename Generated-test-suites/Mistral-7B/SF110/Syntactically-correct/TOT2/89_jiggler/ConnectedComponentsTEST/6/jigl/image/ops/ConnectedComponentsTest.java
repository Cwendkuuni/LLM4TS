import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() {
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        assertNotNull(cc);
    }
}