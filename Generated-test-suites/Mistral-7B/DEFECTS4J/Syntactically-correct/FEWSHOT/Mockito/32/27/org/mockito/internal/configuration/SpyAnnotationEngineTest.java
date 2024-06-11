package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

public class SpyAnnotationEngineTest {

    private static final String FIELD_NAME = "testField";
    private static final String FIELD_VALUE = "testValue";
    private static final Class<Annotation> TEST_ANNOTATION = mock(Annotation.class);
    private static final Field TEST_FIELD = mock(Field.class);
    private static final Object TEST_INSTANCE = mock(Object.class);
    private static final Object TEST_TEST_CLASS = mock(Object.class);
    private SpyAnnotationEngine engine;

    @Before
    public void setup() {
        engine = new SpyAnnotationEngine();
        TEST_FIELD.setName(FIELD_NAME);
        TEST_FIELD.set(TEST_TEST_CLASS, TEST_INSTANCE);
    }

    @Test
    public void testCreateMockForReturnsNull() {
        assertNull(engine.createMockFor(TEST_ANNOTATION, TEST_FIELD));
    }

    @Test
    public void testProcessDoesNotSpyNullFields() {
        engine.process(Object.class, TEST_TEST_CLASS);
        assertNull(TEST_FIELD.get(TEST_TEST_CLASS));
    }

    @Test
    public void testProcessDoesNotSpyFieldsAlreadySpyed() {
        TEST_FIELD.set(TEST_TEST_CLASS, TEST_INSTANCE);
        engine.process(Object.class, TEST_TEST_CLASS);

        Object spiedInstance = Mockito.spy(TEST_INSTANCE);
        TEST_FIELD.set(TEST_TEST_CLASS, spiedInstance);

        engine.process(Object.class, TEST_TEST_CLASS);

        assertSame(spiedInstance, TEST_FIELD.get(TEST_TEST_CLASS));
        verifyNoMoreInteractions(TEST_INSTANCE);
    }

    @Test
    public void testProcessSpyUnknownField() {
        engine.process(Object.class, TEST_TEST_CLASS);

        Object spiedInstance = spy(TEST_INSTANCE);
        TEST_FIELD.set(TEST_TEST_CLASS, spiedInstance);

        engine.process(Object.class, TEST_TEST_CLASS);

        assertSame(spiedInstance, TEST_FIELD.get(TEST_TEST_CLASS));
        verifyNoMoreInteractions(TEST_INSTANCE);

        verifyZeroInteractions(spiedInstance);
    }

    @Test(expected = MockitoException.class)
    public void testProcessThrowsExceptionOnMissingInstance() {
        engine.process(Object.class, TEST_TEST_CLASS);

        TEST_FIELD.setAccessible(true);
        TEST_FIELD.set(TEST_TEST_CLASS, null);

        engine.process(Object.class, TEST_TEST_CLASS);
    }

    @Test
    public void testProcessDoesNotSpyFieldsAnnotatedWithOtherAnnotations() {
        @Mock Annotation otherAnnotation = mock(Annotation.class);
        TEST_FIELD.setAnnotation(otherAnnotation);

        engine.process(Object.class, TEST_TEST_CLASS);

        assertNull(TEST_FIELD.get(TEST_TEST_CLASS));
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testProcessThrowsExceptionOnMultipleSpyAnnotations() {
        @Spy @Captor @Mock Annotation spyAnnotation = mock(Annotation.class);
        TEST_FIELD.setAnnotation(spyAnnotation);
        TEST_FIELD.setAccessible(true);

        engine.process(Object.class, TEST_TEST_CLASS);

        engine.process(Object.class, TEST_TEST_CLASS);
    }

    @Test
    public void testProcessDoesNotSpyFieldsAccessibleOnlyByOwner() {
        TEST_FIELD.setAccessible(false);
        engine.process(Object.class, TEST_TEST_CLASS);

        assertNull(TEST_FIELD.get(TEST_TEST_CLASS));
    }

    @Test
    public void testProcessSpyInstanceWithAnswer() {
        Object spyInstance = mock(Object.class);
        Object answer = mock(Answer.class);
        TEST_FIELD.set(TEST_TEST_CLASS, spyInstance);

        engine.process(Object.class, TEST_TEST_CLASS);

        Mockito.verify(answer).answer(any(InvocationOnMock.class));
    }
}