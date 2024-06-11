import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

    @Mock
    private Object testClass;

    @Test
    public void testCreateMockFor() {
        AnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        Object mock = annotationEngine.createMockFor(annotation, field);

        // Check if the returned object is a mock
        assertThat(mock).isInstanceOf(org.mockito.Mockito.mock(Object.class));
    }

    @Test
    public void testProcess_NoSpyAnnotation_FieldIsNotAccessible() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class<?> context = Mockito.mock(Class.class);
        Field field = Mockito.mock(Field.class);

        // Set the field to be not accessible
        doReturn(false).when(field).isAccessible();

        annotationEngine.process(context, testClass);

        // Check if the field was not accessed
        verify(field, never()).setAccessible(true);
    }

    @Test
    public void testProcess_SpyAnnotationPresent_FieldIsAccessible() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class<?> context = Mockito.mock(Class.class);
        Object instance = Mockito.mock(Object.class);
        Field field = Mockito.mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("testField");
        when(field.get(testClass)).thenReturn(instance);
        doReturn(true).when(field).isAccessible();

        annotationEngine.process(context, testClass);

        verify(field).setAccessible(true);
        verify(field).set(testClass, Mockito.spy(instance));
    }

    @Test(expected = MockitoException.class)
    public void testProcess_SpyAnnotationPresent_InstanceIsNull() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class<?> context = Mockito.mock(Class.class);
        Field field = Mockito.mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("testField");
        when(field.get(testClass)).thenReturn(null);
        doReturn(true).when(field).isAccessible();

        annotationEngine.process(context, testClass);
    }
}