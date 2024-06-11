package org.mockito.internal.configuration.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine annotationEngine;
    private Object testClass;

    @Before
    public void setUp() {
        annotationEngine = new SpyAnnotationEngine();
        testClass = new Object() {};
    }

    @Test
    public void testCreateMockForNullAnnotation() {
        Field field = null;
        assertNull(annotationEngine.createMockFor(null, field));
    }

    @Test
    public void testCreateMockForNonSpyAnnotation() {
        Field field = createTestFieldWithMockAnnotation();
        assertNull(annotationEngine.createMockFor(Mockito.class, field));
    }

    @Test
    public void testCreateMockForSpyAnnotationWithNullField() {
        assertThrows(MockitoException.class, () -> annotationEngine.process(SpyAnnotationEngine.class, null));
    }

    @Test
    public void testCreateMockForSpyAnnotationWithEmptyClass() {
        assertThrows(MockitoException.class, () -> annotationEngine.process(SpyAnnotationEngine.class, testClass));
    }

    @Test
    public void testCreateMockForSpyAnnotationWithInstanceAlreadySpyed() {
        Object instance = mock(String.class);
        setTestClassFieldValue(instance);
        assertNotNull(getTestClassFieldValue(instance));
        assertNotNull(MockUtil.isMock(getTestClassFieldValue(instance)));

        annotationEngine.process(SpyAnnotationEngine.class, testClass);

        // The field value should be reset and set back to the spied instance
        assertNotNull(getTestClassFieldValue(instance));
        assertNotNull(MockUtil.isMock(getTestClassFieldValue(instance)));
    }

    @Test
    public void testCreateMockForSpyAnnotationWithNullInstance() {
        Field field = createTestFieldWithSpyAnnotation();
        try {
            annotationEngine.process(SpyAnnotationEngine.class, testClass);
            throw new AssertionError("MockitoException should have been thrown");
        } catch (MockitoException e) {
            // Expected
        } finally {
            assertFalse(field.isAccessible());
        }
    }

    @Test
    public void testCreateMockForSpyAnnotationWithExistingMock() {
        Object instance = mock(String.class);
        setTestClassFieldValue(instance);
        assertNotNull(getTestClassFieldValue(instance));
        assertNotNull(MockUtil.isMock(getTestClassFieldValue(instance)));

        annotationEngine.process(SpyAnnotationEngine.class, testClass);

        // The field value should be reset and set back to the spied instance
        assertNotNull(getTestClassFieldValue(instance));
        assertNotNull(MockUtil.isMock(getTestClassFieldValue(instance)));

        // Verify that the mock instance was not modified
        assertSame(instance, MockUtil.getMock(getTestClassFieldValue(instance)));
    }

    @Test
    public void testCreateMockForSpyAnnotationWithNewInstance() {
        Object instance = new Object();
        setTestClassFieldValue(instance);
        assertNull(getTestClassFieldValue(instance));

        annotationEngine.process(SpyAnnotationEngine.class, testClass);

        // The field value should be set to a spied instance
        Field field = createTestFieldWithSpyAnnotation();
        assertNotNull(getTestClassFieldValue(instance));
        assertNotNull(MockUtil.isMock(getTestClassFieldValue(instance)));
        assertFalse(field.isAccessible());
    }

    @Test
    public void testCreateMockForSpyAnnotationWithAccessibleField() {
        Object instance = new Object();
        setTestClassFieldValue(instance);
        assertNull(getTestClassFieldValue(instance));

        // Make the field accessible
        Field field = createTestFieldWithSpyAnnotation();
        field.setAccessible(true);

        // Set the field value to the new instance
        field.set(testClass, instance);

        annotationEngine.process(SpyAnnotationEngine.class, testClass);

        // The field value should be set to a spied instance
        assertNotNull(getTestClassFieldValue(instance));
        assertNotNull(MockUtil.isMock(getTestClassFieldValue(instance)));
        assertFalse(field.isAccessible());
    }

    @Test
    public void testCreateMockForSpyAnnotationWithFieldAlreadyAccessible() {
        Object instance = new Object();
        setTestClassFieldValue(instance);
        assertNull(getTestClassFieldValue(instance));

        // Make the field accessible
        Field field = createTestFieldWithSpyAnnotation();
        field.setAccessible(true);

        // Set the field value to the new instance
        field.set(testClass, instance);

        annotationEngine.process(SpyAnnotationEngine.class, testClass);

        // The field value should be set to a spied instance
        assertNotNull(getTestClassFieldValue(instance));
        assertNotNull(MockUtil.isMock(getTestClassFieldValue(instance)));
        assertTrue(field.isAccessible());
    }

    @Test
    public void testCreateMockForSpyAnnotationWithDuplicateAnnotations() {
        Object instance = new Object();
        setTestClassFieldValue(instance);
        assertNull(getTestClassFieldValue(instance));

        // Create a field with both Mock and Spy annotations
        Field field = createTestFieldWithBothMockAndSpyAnnotations();

        // Set the field value to the new instance
        field.set(testClass, instance);

        try {
            annotationEngine.process(SpyAnnotationEngine.class, testClass);
            throw new AssertionError("MockitoException should have been thrown");
        } catch (MockitoException e) {
            // Expected
        } finally {
            assertFalse(field.isAccessible());
        }
    }

    @Test
    public void testCreateMockForSpyAnnotationWithMissingInstance() {
        Field field = createTestFieldWithSpyAnnotation();

        try {
            annotationEngine.process(SpyAnnotationEngine.class, testClass);
            throw new AssertionError("MockitoException should have been thrown");
        } catch (MockitoException e) {
            String expectedErrorMessage = "Cannot create a @Spy for 'fieldName' field because the *instance* is missing";
            assertTrue(e.getMessage().startsWith(expectedErrorMessage));
        } finally {
            assertFalse(field.isAccessible());
        }
    }

    private Field createTestFieldWithMockAnnotation() {
        return createTestField("@Mock");
    }

    private Field createTestFieldWithSpyAnnotation() {
        return createTestField("@Spy");
    }

    private Field createTestFieldWithBothMockAndSpyAnnotations() {
        Field field = createTestField("@Mock @Spy");
        return field;
    }

    private Field createTestField(String annotation) {
        String fieldName = "testField";
        Field field = null;
        try {
            field = testClass.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.annotations().stream()
                    .filter(a -> a.annotationType().isAnnotationPresent(Annotation.class))
                    .map(a -> a.annotationType())
                    .forEach(annotationType -> {
                        if (annotationType.getSimpleName().equals(annotation)) {
                            return; // Stop searching when the expected annotation is found
                        }
                    });
            if (field.isAnnotationPresent(Annotation.class) && field.isAnnotationPresent(annotation.getClass())) {
                return field;
            }
            field = new Field("testField", Object.class, testClass.getClass());
            field.setAccessible(true);
            field.setAnnotation(new Annotation() {});
            field.setAnnotation(Annotation.class.cast(annotation.getClass().getAnnotation(Annotation.class)));
        } catch (NoSuchFieldException e) {
            try {
                testClass.getClass().getDeclaredConstructor().newInstance();
                field = new Field(fieldName, Object.class, testClass.getClass());
                field.setAccessible(true);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return field;
    }

    private void setTestClassFieldValue(Object instance) {
        Field field = createTestFieldWithSpyAnnotation();
        field.setAccessible(true);
        if (field.get(testClass) == null) {
            field.set(testClass, instance);
        }
    }

    private Object getTestClassFieldValue(Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Field field;
}