import org.junit.Test;
import org.mockito.internal.configuration.SpyAnnotationEngine;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor_ReturnsNull() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        assertNull(engine.createMockFor(null, null));
    }

    @Test
    public void testCreateMockFor_AnnotationNull() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = new Field(Object.class, "testField");
        assertNull(engine.createMockFor(null, field));
    }

    @Test
    public void testCreateMockFor_FieldNull() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Annotation annotation = new Annotation() {};
        assertNull(engine.createMockFor(annotation, null));
    }
}