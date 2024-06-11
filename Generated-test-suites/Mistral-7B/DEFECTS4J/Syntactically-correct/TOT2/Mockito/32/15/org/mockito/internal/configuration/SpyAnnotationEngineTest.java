import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SpyAnnotationEngineTest {

    @Mock
    Annotation annotation;

    @Mock
    Field field;

    @Test
    public void testCreateMockForNullAnnotation() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        assertNull(engine.createMockFor(null, field));
    }

    @Test
    public void testCreateMockForNullField() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        assertNull(engine.createMockFor(annotation, null));
    }

    @Test
    public void testCreateMockForAnnotationAndField() {
        initMocks(this);
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        when(field.getAnnotation(Spy.class)).thenReturn(annotation);
        Object mock = engine.createMockFor(annotation, field);
        verify(field).set(any(), eq(mock));
    }
}