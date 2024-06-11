package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;

import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine annotationEngine;
    private Class testClass;

    @Before
    public void setUp() {
        annotationEngine = new SpyAnnotationEngine();
        testClass = MockitoTest.class;
        initMocks(testClass);
    }

    @Test
    public void testCreateMockFor() {
        assertNull(annotationEngine.createMockFor(Spy.class, null));
    }

    @Test
    public void testProcessNullContext() {
        assertNull(annotationEngine.process(null, testClass));
    }

    @Test
    public void testProcessEmptyClass() {
        assertNull(annotationEngine.process(testClass.getSuperclass(), testClass));
    }

    @Test
    public void testProcessWithNoSpyAnnotation() {
        Field field = null;
        try {
            field = testClass.getDeclaredField("nonSpyField");
        } catch (NoSuchFieldException e) {
            // expected
        }

        assertNull(annotationEngine.process(testClass, testClass));
        assertFalse(field.isAccessible());
    }

    @Test
    public void testProcessWithAccessibleField() {
        Field field = null;
        try {
            field = testClass.getDeclaredField("spyField");
        } catch (NoSuchFieldException e) {
            // expected
        }

        assertTrue(field.isAccessible());
        assertFalse(new MockUtil().isMock(field.get(testClass)));

        annotationEngine.process(testClass, testClass);
        assertFalse(field.isAccessible());
        assertNotNull(Mockito.mockingDetails(field.get(testClass)));
    }

    @Test
    public void testProcessWithAccessibleNullField() {
        Field field = null;
        try {
            field = testClass.getDeclaredField("nullField");
        } catch (NoSuchFieldException e) {
            // expected
        }

        field.setAccessible(true);
        assertThrows(NullPointerException.class, () -> annotationEngine.process(testClass, testClass));
        assertFalse(field.isAccessible());
    }

    @Test
    public void testProcessWithAccessibleMockField() {
        Field field = null;
        try {
            field = testClass.getDeclaredField("mockField");
        } catch (NoSuchFieldException e) {
            // expected
        }

        assertTrue(field.isAccessible());
        assertNotNull(Mockito.mockingDetails(field.get(testClass)));

        annotationEngine.process(testClass, testClass);
        assertEquals(Mockito.reset(field.get(testClass)), Mockito.mockingDetails(field.get(testClass)).getMockingDetail());
    }

    @Test
    public void testProcessWithAccessibleSpyField() {
        Field field = null;
        try {
            field = testClass.getDeclaredField("spyField");
        } catch (NoSuchFieldException e) {
            // expected
        }

        assertTrue(field.isAccessible());
        assertNull(Mockito.mockingDetails(field.get(testClass)));

        annotationEngine.process(testClass, testClass);
        assertNotNull(Mockito.mockingDetails(field.get(testClass)));
    }

    @Test
    public void testProcessWithAccessibleSpyFieldAlreadySpyed() {
        Field field = null;
        try {
            field = testClass.getDeclaredField("spyField");
        } catch (NoSuchFieldException e) {
            // expected
        }

        assertTrue(field.isAccessible());
        assertNotNull(Mockito.mockingDetails(field.get(testClass)));

        field.set(testClass, mock(String.class));
        annotationEngine.process(testClass, testClass);
        assertEquals(Mockito.reset(Mockito.mock(String.class)), Mockito.mockingDetails(field.get(testClass)).getMockingDetail());
    }

    @Test
    public void testProcessWithAccessibleSpyFieldAndMockAnnotation() {
        Field field = null;
        try {
            field = testClass.getDeclaredField("spyAndMockField");
        } catch (NoSuchFieldException e) {
            // expected
        }
        assertFalse(field.isAccessible());

        annotationEngine.process(testClass, testClass);
        assertTrue(field.isAccessible());
        assertNotNull(Mockito.mockingDetails(field.get(testClass)));
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithAccessibleSpyFieldAndTwoSpyAnnotation() {
        Field field = null;
        try {
            field = testClass.getDeclaredField("twoSpyField");
        } catch (NoSuchFieldException e) {
            // expected
        }
        assertFalse(field.isAccessible());

        annotationEngine.process(testClass, testClass);
        assertTrue(field.isAccessible());
        new Reporter().unsupportedCombinationOfAnnotations("Spy", "Spy");
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithAccessibleSpyFieldAndMockAnnotationOnField() {
        Field field = null;
        try {
            field = testClass.getDeclaredField("spyAndMockField");
        } catch (NoSuchFieldException e) {
            // expected
        }
        assertFalse(field.isAccessible());

        annotationEngine.process(testClass, testClass);
        assertTrue(field.isAccessible());
        Mockito.verify(field, Mockito.times(1)).isAnnotationPresent(MockitoMockAnnotation.class);
        new Reporter().unsupportedCombinationOfAnnotations("Spy", "Mock");
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithAccessibleSpyFieldAndCaptorAnnotation() {
        Field field = null;
        try {
            field = testClass.getDeclaredField("spyAndCaptorField");
        } catch (NoSuchFieldException e) {
            // expected
        }
        assertFalse(field.isAccessible());

        annotationEngine.process(testClass, testClass);
        assertTrue(field.isAccessible());
        new Reporter().unsupportedCombinationOfAnnotations("Spy", "Captor");
    }
}