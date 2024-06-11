package org.mockito.internal.configuration;

import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

    @Mock
    private Object testClass;

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        assertNull(engine.createMockFor(null, null));
    }

    @Test
    public void testProcess() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(Object.class, testClass);
    }

    @Test
    public void testProcess_EmptyClass() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(Object[].class, testClass);
    }

    @Test
    public void testProcess_NullTestClass() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(Object.class, null);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_SpyAnnotatedFieldAlreadySpyed() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = Mockito.fieldTests(Object.class, "testField");
        field.setAccessible(true);
        Object instance = Mockito.mock(Object.class);
        field.set(testClass, instance);
        // Spy the field again
        engine.process(Object.class, testClass);
    }

    @Test
    public void testProcess_UnexpectedAnnotationPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = Mockito.fieldTests(Object.class, "testField");
        field.setAccessible(true);
        field.set(testClass, new Object());
        MockitoConfigurationAnnotations.addAnnotation(field, MockitoAnnotations.Mock.class);
        try {
            engine.process(Object.class, testClass);
        } catch (MockitoException e) {
            // Assert that the exception message matches the expected error message
            assertTrue(e.getMessage().contains("unsupportedCombinationOfAnnotations"));
        }
    }

    @Test(expected = IllegalAccessException.class)
    public void testProcess_AccessibilityException() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = Mockito.fieldTests(Object.class, "testField");
        try {
            engine.process(Object.class, testClass);
            field.setAccessible(false);
            engine.process(Object.class, testClass);
        } catch (IllegalAccessException e) {
            // Expected exception
        }
    }
}