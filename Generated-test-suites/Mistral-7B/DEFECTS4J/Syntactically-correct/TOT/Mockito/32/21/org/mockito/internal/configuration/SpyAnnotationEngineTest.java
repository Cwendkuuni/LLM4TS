import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.MockitoJUnit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private static final String FIELD_NAME = "testField";
    private SpyAnnotationEngine annotationEngine;
    private Object testClass;

    @Before
    public void setUp() throws Exception {
        annotationEngine = new SpyAnnotationEngine();
        testClass = createTestClass();
        annotationEngine.process(SpyAnnotationEngine.class, testClass);
    }

    @Test
    public void testCreateMockFor_AnnotationPresent_FieldNull_ThrowsException() {
        // Given
        Field field = null;
        Annotation annotation = createAnnotation(Spy.class);

        // When & Then
        try {
            annotationEngine.createMockFor(annotation, field);
            fail("Expected MockitoException to be thrown.");
        } catch (MockitoException e) {
            // Expected
            assertNull(field);
            assertNull(annotation);
            assertEquals("Cannot create a @Spy for '" + FIELD_NAME + "' field because the *instance* is missing", e.getMessage());
        }
    }

    @Test
    public void testCreateMockFor_AnnotationPresent_InstanceNotNull_SpyExistingInstance() {
        // Given
        Object instance = new Object();
        Field field = createField(FIELD_NAME, instance);

        // When
        annotationEngine.process(SpyAnnotationEngine.class, testClass);

        // Then
        assertNotNull(field.get(testClass));
        verify(field, times(1)).set(testClass, argThat(isA(Mockito.class)));
        verify(field, times(1)).setAccessible(true);
    }

    @Test
    public void testCreateMockFor_AnnotationPresent_InstanceNotNull_SpyNewInstance() {
        // Given
        Object instance = new Object();
        Field field = createField(FIELD_NAME, instance);

        // When
        annotationEngine.process(SpyAnnotationEngine.class, testClass);

        // Then
        Object spiedInstance = Mockito.spy(instance);
        verify(field, times(1)).set(testClass, spiedInstance);
        verify(field, times(1)).setAccessible(true);

        // Verify behavior
        assertNotNull(spiedInstance);
        verify(spiedInstance).toString();
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockFor_AnnotationPresent_FieldAccessFailed() {
        // Given
        Field field = createField(FIELD_NAME);

        // When
        annotationEngine.process(SpyAnnotationEngine.class, testClass);

        // Then
        assertThrows(MockitoException.class, () -> field.get(testClass));
    }

    @Test
    public void testProcess_AnnotationPresent_FieldAccessible_InstanceNull() {
        // Given
        Field field = createField(FIELD_NAME);

        // When
        annotationEngine.process(SpyAnnotationEngine.class, testClass);

        // Then
        assertNull(field.get(testClass));
    }

    @Test
    public void testProcess_AnnotationPresent_FieldAccessible_InstanceNotNull_NoPreviousMock() {
        // Given
        Object instance = new Object();
        Field field = createField(FIELD_NAME, instance);

        // When
        annotationEngine.process(SpyAnnotationEngine.class, testClass);

        // Then
        Object spiedInstance = Mockito.spy(instance);
        assertSame(spiedInstance, field.get(testClass));
    }

    @Test(expected = MockitoException.class)
    public void testProcess_AnnotationPresent_FieldAccessible_InstanceNotNull_PreviousMock() {
        // Given
        Object instance = new Object();
        Object existingMock = mock(Object.class);
        Field field = createField(FIELD_NAME, instance);
        field.set(testClass, existingMock);

        // When
        annotationEngine.process(SpyAnnotationEngine.class, testClass);

        // Then
        verify(field, times(1)).set(testClass, argThat(isA(Mockito.class)));
        verify(field, times(1)).setAccessible(true);

        // Expected
        throw new MockitoException("instance has been spied earlier");
    }

    @Test
    public void testProcess_AnnotationNotPresent() {
        // Given
        Field field = createField("nonSpyField");

        // When
        annotationEngine.process(SpyAnnotationEngine.class, testClass);

        // Then
        assertNull(field.get(testClass));
    }

    @Test(expected = IllegalAccessException.class)
    public void testProcess_AnnotationPresent_FieldInaccessible() {
        // Given
        Field field = createField(FIELD_NAME);
        field.setAccessible(false);

        // When
        annotationEngine.process(SpyAnnotationEngine.class, testClass);

        // Then
        assertThrows(IllegalAccessException.class, () -> field.get(testClass));
    }

    @Test
    public void testAssertNoAnnotations_AnnotationPresent_UndesiredAnnotationNotPresent() {
        // Given
        Field field = createField(FIELD_NAME);

        // When
        annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, Captor.class);

        // Then
        // No exception is thrown
    }

    @Test(expected = org.mockito.internal.exceptions.MockitoBaseException.class)
    public void testAssertNoAnnotations_AnnotationPresent_UndesiredAnnotationPresent() {
        // Given
        Field field = createField(FIELD_NAME);
        field.setAnnotation(new MockAnnotation());

        // When
        annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, Captor.class);

        // Then
        // Expected exception
    }

    private Object createTestClass() {
        return new Object() {};
    }

    private Field createField(String name) {
        try {
            Field field = testClass.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field not found: " + name, e);
        }
    }

    private Field createField(String name, Object instance) {
        try {
            Field field = testClass.getClass().getDeclaredField(name);
            field.set(testClass, instance);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field not found: " + name, e);
        }
    }

    private Annotation createAnnotation(Class<? extends Annotation> annotationType) {
        try {
            return testClass.getClass().getDeclaredField(annotationType.getSimpleName()).getAnnotation(annotationType);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Annotation not found", e);
        }
    }
}