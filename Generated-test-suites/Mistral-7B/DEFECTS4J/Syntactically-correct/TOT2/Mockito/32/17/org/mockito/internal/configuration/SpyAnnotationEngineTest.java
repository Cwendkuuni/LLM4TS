import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoJUnitRunner.*;
import java.lang.reflect.*;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

    private static final String FIELD_NAME = "testField";

    @Mock
    private Object testClass;

    @Mock
    private Field testField;

    @Mock
    private Object fieldInstance;

    @Captor
    private ArgumentCaptor<Field> fieldCaptor;

    @Test
    public void testCreateMockForReturnsNull() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<Annotation> annotationClass = mock(Annotation.class);
        Field field = mock(Field.class);

        Object result = engine.createMockFor(annotationClass, field);
        assertNull(result);
        
        verifyNoMoreInteractions(annotationClass, field);
    }

    @Test
    public void testProcessSpyField() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();

        doAnswer(doesNothing()).when(testField).setAccessible(true);
        when(testClass.getClass()).thenReturn(SpyAnnotationEngine.class);
        when(testField.getName()).thenReturn(FIELD_NAME);
        when(testField.getType()).thenReturn(Integer.class);
        when(testField.get(testClass)).thenReturn(fieldInstance);

        engine.process(testClass.getClass(), testClass);

        Object spiedFieldInstance = get(testClass, FIELD_NAME);
        verify(testField).set(testClass, eq(spiedFieldInstance));
        verify(spiedFieldInstance).getClass().equals(fieldInstance.getClass());
        verifyNoMoreInteractions(testField, fieldInstance);
        assertFalse(MockUtil.isMock(spiedFieldInstance));
    }
}