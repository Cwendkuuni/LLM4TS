import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jUpasTestRunner;
import org.mockito.internal.configuration.SpyAnnotationEngine;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

    @Mock
    private Object testClass;

    @Captor
    private ArgumentCaptor<Object> instanceCaptor;

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);

        try {
            annotationEngine.createMockFor(null, field);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            annotationEngine.createMockFor(annotation, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        assertNull(annotationEngine.createMockFor(annotation, field));
    }

    @Test
    public void testProcess() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        // test null context
        try {
            annotationEngine.process(null, testClass);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        // test null testClass
        try {
            annotationEngine.process(SpyAnnotationEngineTest.class, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        // test no fields annotated with @Spy
        when(testClass.getDeclaredFields()).thenReturn(new Field[0]);
        annotationEngine.process(SpyAnnotationEngineTest.class, testClass);

        // test field is not annotated with @Spy
        Field field = Mockito.spy(new Field("testField") {
            @SuppressWarnings("unused")
            public Annotation getAnnotation(Class<? extends Annotation> annotationType) {
                return null;
            }
        });
        annotationEngine.process(SpyAnnotationEngineTest.class, testClass);

        // test illegal access exception
        Field illegallyAccessibleField = Mockito.spy(new Field("illegallyAccessibleField") {
            public boolean isAccessible() {
                return false;
            }
        });
        try {
            annotationEngine.process(SpyAnnotationEngineTest.class, testClass);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // expected
        }

        // test null field value
        Field nullFieldValue = Mockito.spy(new Field("nullFieldValue") {
            public Object get(Object obj) {
                return null;
            }
        });
        try {
            annotationEngine.process(SpyAnnotationEngineTest.class, testClass);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        // test field value is MockitoException
        Field mockitoExceptionFieldValue = Mockito.spy(new Field("mockitoExceptionFieldValue") {
            public Object get(Object obj) {
                throw new MockitoException("Test exception");
            }
        });
        try {
            annotationEngine.process(SpyAnnotationEngineTest.class, testClass);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // expected
        }

        // test successful processing
        Field field = Mockito.spy(new Field("testField") {
            @Spy
            public Object instance = new Object();
        });

        when(testClass.getClass()).thenReturn(SpyAnnotationEngineTest.class);
        when(testClass.getDeclaredFields()).thenReturn(new Field[]{field});

        annotationEngine.process(SpyAnnotationEngineTest.class, testClass);

        verify(annotationEngine, times(1)).process(SpyAnnotationEngineTest.class, testClass);
        verify(testClass, times(1)).getClass();
        verify(testClass, times(1)).getDeclaredFields();
        verify(field, times(1)).isAnnotationPresent(Spy.class);
        verify(field, times(1)).setAccessible(true);
        verify(field, times(1)).get(testClass);
        verify(annotationEngine, times(1)).createMockFor(eq(Spy.class), eq(field));
        verify(field, times(1)).set(testClass, instanceCaptor.capture());
        Object spiedInstance = instanceCaptor.getValue();

        Mockito.verifyNoMoreInteractions(testClass, field);

        assertNotNull(spiedInstance);
    }

    @Test
    public void testAssertNoAnnotations() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        // test no annotations present
        Field field = Mockito.spy(new Field("noAnnotationsField") {
            @SuppressWarnings("unused")
            public Annotation getAnnotation(Class<? extends Annotation> annotationType) {
                return null;
            }
        });
        annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        // test annotation present
        Field annotatedField = Mockito.spy(new Field("annotatedField") {
            @Spy
            public Annotation getAnnotation(Class<? extends Annotation> annotationType) {
                return Mockito.mock(annotationType);
            }
        });
        try {
            annotationEngine.assertNoAnnotations(Spy.class, annotatedField, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}