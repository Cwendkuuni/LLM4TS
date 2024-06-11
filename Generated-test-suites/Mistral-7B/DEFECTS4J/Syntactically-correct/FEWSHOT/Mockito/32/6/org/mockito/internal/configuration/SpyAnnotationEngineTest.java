package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockUtil;

public class SpyAnnotationEngineTest {

    @Mock
    private AnnotationEngine annotationEngine;

    @Mock
    private Reporter reporter;

    @Mock
    private Field testField;

    @Captor
    private ArgumentCaptor<Field> fieldCaptor;

    @Captor
    private ArgumentCaptor<Object> objectCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        assertNull(spyAnnotationEngine.createMockFor(null, null));
    }

    @Test
    public void testProcess_noSpyAnnotation_noInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);

        spyAnnotationEngine.process(context, testClass);

        verify(reporter).unsupportedCombinationOfAnnotations("SpyAnnotationEngine", "AnnotationEngine");
    }

    @Test
    public void testProcess_noSpyAnnotation_noInstancePresent() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);

        Field[] fields = new Field[1];
        fields[0] = field;

        when(context.getDeclaredFields()).thenReturn(fields);

        spyAnnotationEngine.process(context, testClass);

        verify(reporter, never()).unsupportedCombinationOfAnnotations(anyString(), anyString());
        verify(field, never()).set(testClass, any());
    }

    @Test
    public void testProcess_noSpyAnnotation_instancePresent() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Object instance = mock(Object.class);
        Field field = mock(Field.class);

        Field[] fields = new Field[1];
        fields[0] = field;

        when(context.getDeclaredFields()).thenReturn(fields);
        when(field.getName()).thenReturn("testField");
        when(field.isAnnotationPresent(Spy.class)).thenReturn(false);
        when(field.get(testClass)).thenReturn(instance);

        spyAnnotationEngine.process(context, testClass);

        verify(field, never()).set(testClass, any());
    }

    @Test
    public void testProcess_SpyAnnotationPresent_noInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);

        Field[] fields = new Field[1];
        fields[0] = field;

        when(context.getDeclaredFields()).thenReturn(fields);
        when(field.getName()).thenReturn("testField");
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);

        spyAnnotationEngine.process(context, testClass);

        verify(reporter).unsupportedCombinationOfAnnotations("SpyAnnotationEngine", "AnnotationEngine");
    }

    @Test
    public void testProcess_SpyAnnotationPresent_instanceNull() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);

        Field[] fields = new Field[1];
        fields[0] = field;

        when(context.getDeclaredFields()).thenReturn(fields);
        when(field.getName()).thenReturn("testField");
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);

        spyAnnotationEngine.process(context, testClass);

        verify(reporter).unsupportedCombinationOfAnnotations("SpyAnnotationEngine", "AnnotationEngine");
    }

    @Test
    public void testProcess_SpyAnnotationPresent_instanceNotAMock() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Object instance = mock(Object.class);
        Field field = mock(Field.class);

        Field[] fields = new Field[1];
        fields[0] = field;

        when(context.getDeclaredFields()).thenReturn(fields);
        when(field.getName()).thenReturn("testField");
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(testClass)).thenReturn(instance);

        when(new MockUtil().isMock(instance)).thenReturn(false);

        spyAnnotationEngine.process(context, testClass);

        Object spiedInstance = spy(instance);

        verify(field).set(testClass, spiedInstance);
    }

    @Test
    public void testProcess_SpyAnnotationPresent_instanceAlreadySpyed() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Object instance = mock(Object.class);
        Field field = mock(Field.class);

        Field[] fields = new Field[1];
        fields[0] = field;

        when(context.getDeclaredFields()).thenReturn(fields);
        when(field.getName()).thenReturn("testField");
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(testClass)).thenReturn(instance);
        when(new MockUtil().isMock(instance)).thenReturn(true);

        spyAnnotationEngine.process(context, testClass);

        verify(field, never()).set(testClass, any());
    }

    @Test
    public void testProcess_SpyAnnotationPresent_instanceAlreadySpyed_butReset() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Object instance = mock(Object.class);
        Field field = mock(Field.class);

        Field[] fields = new Field[1];
        fields[0] = field;

        when(context.getDeclaredFields()).thenReturn(fields);
        when(field.getName()).thenReturn("testField");
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(testClass)).thenReturn(instance);
        when(new MockUtil().isMock(instance)).thenReturn(true);

        Object spiedInstance = mock(Object.class);

        spyAnnotationEngine.process(context, testClass);

        verify(field).set(testClass, spiedInstance);

        spyAnnotationEngine.process(context, testClass);

        verify(instance, Mockito.times(1)).reset();
        verify(field, never()).set(testClass, any());
    }

    @Test
    public void testAssertNoAnnotations() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = mock(Field.class);

        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        verify(reporter, never()).unsupportedCombinationOfAnnotations(anyString(), anyString());
    }

    @Test
    public void testAssertNoAnnotations_fieldHasAnAnnotation() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = mock(Field.class);

        Field mockAnnotation = mock(Annotation.class);
        when(field.isAnnotationPresent(Mock.class)).thenReturn(true);
        when(field.isAnnotationPresent(mockAnnotation)).thenReturn(true);

        try {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
            fail();
        } catch (AssertionError e) {
            // expected
        }

        verify(reporter).unsupportedCombinationOfAnnotations("Spy", "Annotation");
    }
}