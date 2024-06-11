package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.exceptions.base.MockitoException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

    @Rule
    public MockitoRule mockitoRule = new MockitoRule();

    private SpyAnnotationEngine annotationEngine;
    @Mock
    private Object testClass;

    @Before
    public void setUp() {
        annotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor() {
        assertNull(annotationEngine.createMockFor(null, null));
    }

    @Test(expected = MockitoException.class)
    public void testProcessNullContext() {
        annotationEngine.process(null, testClass);
    }

    @Test
    public void testProcessNoSpyAnnotation() {
        Field field = mock(Field.class);
        SpyAnnotationEngine annotationEngine = spy(new SpyAnnotationEngine());
        annotationEngine.process(TestClass.class, testClass);
        annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        verify(annotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
    }

    @Test
    public void testProcessSpyAnnotationPresent() {
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        SpyAnnotationEngine annotationEngine = spy(new SpyAnnotationEngine());

        annotationEngine.process(TestClass.class, testClass);

        Object instance = mock(Object.class);
        when(field.get(testClass)).thenReturn(instance);
        annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        verify(annotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        verify(annotationEngine, times(2)).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        verify(field).setAccessible(true);
        verify(annotationEngine).setAccessible(field, testClass);
        verify(field).setAccessible(true);

        Object spiedInstance = annotationEngine.process(TestClass.class, testClass);
        verify(field).set(testClass, spiedInstance);
        verify(annotationEngine).set(testClass, spiedInstance);
        assertEquals(instance, spiedInstance);

        verify(annotationEngine).reset(spiedInstance);
        verify(spiedInstance).getClass();

        verifyNoMoreInteractions(field, annotationEngine);
    }

    @Test(expected = MockitoException.class)
    public void testProcessFieldNullInstance() {
        SpyAnnotationEngine annotationEngine = spy(new SpyAnnotationEngine());
        Field field = mock(Field.class);
        when(field.get(testClass)).thenReturn(null);

        annotationEngine.process(TestClass.class, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessDuplicateSpyAnnotation() {
        SpyAnnotationEngine annotationEngine = spy(new SpyAnnotationEngine());
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);

        annotationEngine.process(TestClass.class, testClass);
        annotationEngine.process(TestClass.class, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessFieldWithUndesiredAnnotation() {
        SpyAnnotationEngine annotationEngine = spy(new SpyAnnotationEngine());
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAnnotationPresent(Mock.class)).thenReturn(true);

        annotationEngine.process(TestClass.class, testClass);
    }

    @Test
    public void testProcessFieldWithCaptorAnnotation() {
        SpyAnnotationEngine annotationEngine = spy(new SpyAnnotationEngine());
        Field field = mock(Field.class);
        ArgumentCaptor<Field> fieldCaptor = ArgumentCaptor.forClass(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAnnotationPresent(Captor.class)).thenReturn(true);

        annotationEngine.process(TestClass.class, testClass);
        verify(annotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        verify(annotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        verify(annotationEngine, times(3)).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        verify(annotationEngine).assertNoAnnotations(Spy.class, fieldCaptor.capture(), Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        verify(annotationEngine).assertNoAnnotations(Spy.class, fieldCaptor.getValue(), Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
    }

}

class TestClass {
}