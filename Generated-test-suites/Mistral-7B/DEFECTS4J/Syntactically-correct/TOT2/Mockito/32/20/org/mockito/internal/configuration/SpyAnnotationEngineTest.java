
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        Object result = new SpyAnnotationEngine().createMockFor(annotation, field);
        assertThat(result).isNull();
    }

    @Test
    public void testCreateMockFor_AnnotationNull() {
        Field field = mock(Field.class);
        Object result = new SpyAnnotationEngine().createMockFor(null, field);
        assertThat(result).isNull();
    }

    @Test
    public void testCreateMockFor_FieldNull() {
        Annotation annotation = mock(Annotation.class);
        Object result = new SpyAnnotationEngine().createMockFor(annotation, null);
        assertThat(result).isNull();
    }

    @Test
    public void testProcess() throws IllegalAccessException {
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        SpyAnnotationEngine engine = new SpyAnnotationEngine();

        Field field = mock(Field.class);
        Object instance = mock(Object.class);
        field.set(testClass, instance);

        engine.process(context, testClass);

        verify(testClass).set(field, engine.spy(instance));
    }

    @Test
    public void testProcess_InstanceAlreadySpyed() throws IllegalAccessException {
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        SpyAnnotationEngine engine = new SpyAnnotationEngine();

        Field field = mock(Field.class);
        Object instance = mock(Object.class);
        field.set(testClass, instance);
        field.set(testClass, Mockito.spy(instance));

        engine.process(context, testClass);

        verify(testClass, Mockito.never()).set(field, Mockito.spy(instance));
    }

    @Test
    public void testProcess_InstanceAlreadyMock() throws IllegalAccessException {
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Object instance = mock(Object.class);
        Mockito.doAnswer(i -> instance).when(testClass).getField(Mockito.anyString());
        SpyAnnotationEngine engine = new SpyAnnotationEngine();

        engine.process(context, testClass);

        verify(testClass).set(Mockito.anyField(), Mockito.isA(SpyAnnotationEngine.class));
    }

    @Test
    public void testProcess_InstanceNull() throws IllegalAccessException {
        Class context = mock(Class.class);
        Object testClass = null;
        SpyAnnotationEngine engine = new SpyAnnotationEngine();

        try {
            engine.process(context, testClass);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // expected
        }

        verify(testClass, Mockito.never()).set(Mockito.anyField(), Mockito.any());
    }

    @Test
    public void testAssertNoAnnotations_NoAnnotationsPresent() {
        Class annotation = mock(Class.class);
        Field field = mock(Field.class);

        new SpyAnnotationEngine().assertNoAnnotations(annotation, field);

        verifyNoMoreInteractions(field);
    }

    @Test
    public void testAssertNoAnnotations_AnnotationNull() {
        Field field = mock(Field.class);
        Class annotation = null;

        try {
            new SpyAnnotationEngine().assertNoAnnotations(annotation, field);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // expected
        }

        verify(field, Mockito.never()).getClass();
    }

    @Test
    public void testAssertNoAnnotations_FieldNull() {
        Class annotation = mock(Class.class);
        Field field = null;

        try {
            new SpyAnnotationEngine().assertNoAnnotations(annotation, field);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // expected
        }

        verify(annotation, Mockito.never()).getSimpleName();
    }

    @Test
    public void testAssertNoAnnotations_AnnotationPresent() {
        Class annotation = mock(Class.class);
        Field field = mock(Field.class);
        field.getClass().getDeclaredFields()[0].setAccessible(true);
        field.getClass().getDeclaredFields()[0].set(field, mock(Annotation.class));

        try {
            new SpyAnnotationEngine().assertNoAnnotations(annotation, field);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // expected
        }

        verify(annotation, Mockito.atLeastOnce()).getSimpleName();
    }

    @Test
    public void testAssertNoAnnotations_AnnotationAndUndesiredAnnotationsPresent() {
        Class annotation = mock(Class.class);
        Class undesiredAnnotation = mock(Class.class);
        Field field = mock(Field.class);
        field.getClass().getDeclaredFields()[0].setAccessible(true);
        field.getClass().getDeclaredFields()[0].set(field, mock(Annotation.class));
        field.getClass().getDeclaredFields()[0].setAccessible(true);
        field.getClass().getDeclaredFields()[0].set(field, mock(undesiredAnnotation));

        try {
            new SpyAnnotationEngine().assertNoAnnotations(annotation, field, undesiredAnnotation);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // expected
        }

        verify(annotation, Mockito.atLeastOnce()).getSimpleName();
    }

    @Test
    public void testAssertNoAnnotations_AnnotationIsInterfaceAndFieldIsInstanceOfItsImplementation() {
        Class annotation = mock(Class.class);
        Class interfaceAnnotation = mock(Class.class);
        Class implementation = mock(Class.class);
        Class fieldType = mock(Class.class);
        Field field = mock(Field.class);
        field.setAccessible(true);
        Object instance = mock(implementation);

        doReturn(implementation).when(field).getType();
        doReturn(annotation).when(field).getAnnotation(annotation.getName());
        doReturn(instance).when(field).get(Mockito.anyObject());

        try {
            new SpyAnnotationEngine().assertNoAnnotations(annotation, field);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // expected
        }

        verify(annotation, Mockito.atLeastOnce()).getSimpleName();
    }
}