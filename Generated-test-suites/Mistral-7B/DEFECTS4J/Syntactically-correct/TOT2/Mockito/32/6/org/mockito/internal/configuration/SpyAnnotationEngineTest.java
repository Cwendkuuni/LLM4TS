import org.junit.Test;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        Object result = annotationEngine.createMockFor(annotation, field);
        assertNull(result);

        verify(annotationEngine, times(1)).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
    }
}