import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);

        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object result = engine.createMockFor(annotation, field);

        assertNull(result);
    }
}