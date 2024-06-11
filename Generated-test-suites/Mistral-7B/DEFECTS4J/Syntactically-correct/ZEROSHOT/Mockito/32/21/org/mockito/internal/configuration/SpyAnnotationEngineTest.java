package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito. ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

    @SuppressWarnings("unchecked")
    private SpyAnnotationEngine annotationEngine;

    @Mock
    private Object testClass;

    @Before
    public void setUp() {
        annotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor() {
        // The createMockFor method is not implemented and returns null
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        assertNull(annotationEngine.createMockFor(annotation, field));
    }

    @Test
    public void testProcess_FieldWithoutSpyAnnotation_NoActionTaken() {
        // Given
        Class<?> context = getClass();
        Field field1 = getField("nonSpyField1", int.class, testClass);

        // When
        annotationEngine.process(context, testClass);

        // Then
        // No changes should be made to the field
        assertFalse(field1.isAnnotationPresent(Spy.class));
    }

    @Test
    public void testProcess_FieldWithSpyAnnotation_SpyInstance() {
        // Given
        Class<?> context = getClass();
        Object instance = mock(Object.class);
        Field field1 = getField("spyField", String.class, testClass);
        field1.set(testClass, instance);
        field1.setAccessible(true);

        // When
        annotationEngine.process(context, testClass);

        // Then
        // The instance should be spied
        verify(instance).getClass();
        assertTrue(field1.isAnnotationPresent(Spy.class));
        assertTrue(field1.isAccessible());
        assertThat(field1.get(testClass)).isInstanceOf(spy(instance));
    }

    @Test(expected = MockitoException.class)
    public void testProcess_FieldWithSpyAnnotation_InstanceIsMissing() {
        // Given
        Class<?> context = getClass();
        Field field1 = getField("spyFieldMissingInstance", String.class, testClass);

        // When
        annotationEngine.process(context, testClass);

        // Then
        // An exception should be thrown
        verify(field1).setAccessible(true);
        String expectedMessage = "Cannot create a @Spy for 'spyFieldMissingInstance' field because the instance is missing\n" +
                "The instance must be created before initMocks();\n" +
                "Example of correct usage of @Spy:\n" +
                "   @Spy List mock = new LinkedList<>();\n" +
                "   //also, don't forget about MockitoAnnotations.initMocks();\n";
        throw new AssertionError("Unexpected exception: " + annotationEngine.getClass().getSimpleName() + ".process() did not throw MockitoException with expected message:\n" + expectedMessage);
    }

    @Test
    public void testProcess_FieldWithSpyAnnotation_InstanceIsAlreadyAMock() {
        // Given
        Class<?> context = getClass();
        Object instance = mock(Object.class);
        Field field1 = getField("spyFieldAlreadyAMock", String.class, testClass);
        field1.set(testClass, instance);
        field1.setAccessible(true);

        // When
        annotationEngine.process(context, testClass);

        // Then
        // No changes should be made to the field
        assertTrue(field1.isAnnotationPresent(Spy.class));
        assertTrue(field1.isAccessible());
        assertEquals(instance, field1.get(testClass));
    }

    @Test
    public void testProcess_FieldWithSpyAnnotation_FieldAccessible() {
        // Given
        Class<?> context = getClass();
        Field field1 = getField("spyFieldAccessible", String.class, testClass);

        // When
        annotationEngine.process(context, testClass);

        // Then
        // The field access should be granted
        assertTrue(field1.isAnnotationPresent(Spy.class));
        assertTrue(field1.isAccessible());
    }

    @Test
    public void testAssertNoAnnotations_AnnotationPresent_UnexpectedAnnotation() {
        // Given
        Field field1 = getField("unexpectedAnnotationField", String.class, testClass);

        // When
        annotationEngine.assertNoAnnotations(Spy.class, field1, Mockito.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        // Then
        // An exception should be thrown
        String expectedMessage = "Unexpected annotation @" + Mockito.class.getSimpleName() + " found on field 'unexpectedAnnotationField'";
        throw new AssertionError("Unexpected exception: " + annotationEngine.getClass().getSimpleName() + ".assertNoAnnotations() did not throw expected exception with message:\n" + expectedMessage);
    }

    @Test
    public void testAssertNoAnnotations_NoAnnotationPresent_NoActionTaken() {
        // Given
        Field field1 = getField("noAnnotationField", String.class, testClass);

        // When
        annotationEngine.assertNoAnnotations(Spy.class, field1, Mockito.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        // Then
        // No changes should be made
        assertTrue(field1.isAnnotationPresent(Spy.class));
        assertFalse(field1.isAnnotationPresent(Mockito.class));
        assertFalse(field1.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class));
        assertFalse(field1.isAnnotationPresent(Captor.class));
    }

    @Test
    public void testAssertNoAnnotations_AnnotationNotPresent_NoActionTaken() {
        // Given
        Field field1 = getField("noAnnotationField2", String.class, testClass);

        // When
        annotationEngine.assertNoAnnotations(Spy.class, field1, Mockito.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        // Then
        // No changes should be made
        assertFalse(field1.isAnnotationPresent(Spy.class));
        assertFalse(field1.isAnnotationPresent(Mockito.class));
        assertFalse(field1.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class));
        assertFalse(field1.isAnnotationPresent(Captor.class));
    }

    private Field getField(String name, Class type, Object target) {
        try {
            Field field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Error getting field " + name + " of class " + target.getClass(), e);
        }
    }
}