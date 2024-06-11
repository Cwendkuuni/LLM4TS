import org.junit.Test;
import static org.junit.Assert.assertNull;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        AnnotationEngine engine = new SpyAnnotationEngine();
        assertNull(engine.createMockFor(null, null));
    }
}