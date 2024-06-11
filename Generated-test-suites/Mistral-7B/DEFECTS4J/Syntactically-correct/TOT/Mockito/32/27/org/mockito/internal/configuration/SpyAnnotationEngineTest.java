import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Field;
import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine annotationEngine;
    private Object testClass;

    @Before
    public void setup() {
        annotationEngine = new SpyAnnotationEngine();
        testClass = new Object() {};
        MockitoAnnotations.initMocks(testClass);
    }

    @Test
    public void testCreateMockForSpyAnnotationWithMissingInstance() {
        Class<SpyAnnotationEngine> context = SpyAnnotationEngine.class;
        Field field = null;

        try {
            Field declaredField = context.getDeclaredField("annotationEngine");
            field = declaredField;
        } catch (NoSuchFieldException e) {
            // This exception is expected, as the field is not present in the test class
        }

        assertNotNull(field);
        annotationEngine.process(context, testClass);

        try {
            annotationEngine.createMockFor(new Spy(), field);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // Expected exception
        }
    }

    @Test
    public void testSpyAnnotationOnNullInstance() {
        @Spy
        Field field = null;

        try {
            annotationEngine.process(Object.class, testClass);
            annotationEngine.createMockFor(new Spy(), field);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // Expected exception
        }
    }

    @Test
    public void testSpyAnnotationAlreadyMocked() {
        @Spy
        LinkedList<String> list = new LinkedList<>();
        annotationEngine.process(Object.class, testClass);
        when(list.add("test")).thenReturn(true);

        try {
            annotationEngine.createMockFor(new Spy(), getField(testClass, "list"));
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // Expected exception
        }

        verify(list).add("test");
    }

    @Test
    public void testSpyAnnotationWithMultipleUndesiredAnnotations() {
        @Spy
        @Mock
        Field field = mock(Field.class);

        try {
            annotationEngine.process(Object.class, testClass);
            annotationEngine.createMockFor(new Spy(), field);
            fail("Expected Reporter.unsupportedCombinationOfAnnotations exception");
        } catch (MockitoException e) {
            // Expected exception
        }

        ArgumentCaptor<String> messageCaptor = argumentCaptor(String.class);
        verify(Reporter.class).unsupportedCombinationOfAnnotations(eq("Spy"), eq(Spy.class.getName()), messageCaptor.capture());
        assertNotNull(messageCaptor.getValue());
    }

    private Field getField(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field not found: " + fieldName, e);
        }
    }

    @Test(expected = MockitoException.class)
    public void testProcessThrowsIllegalAccessException() {
        Field field = new Field() {
            private boolean accessible = false;

            @Override
            public Object get(Object obj) throws IllegalAccessException {
                if (!accessible) {
                    throw new IllegalAccessException("Access denied");
                }
                return null;
            }

            @Override
            public void setAccessible(boolean b) {
                accessible = b;
            }
        };

        annotationEngine.process(Object.class, testClass);
        annotationEngine.createMockFor(new Spy(), field);
    }
}