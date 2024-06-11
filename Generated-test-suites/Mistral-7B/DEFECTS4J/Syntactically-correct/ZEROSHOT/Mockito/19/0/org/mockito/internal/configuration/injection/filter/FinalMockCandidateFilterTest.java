package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injectingannotatedfields.FakeAnnotationProcessor;
import org.mockito.internal.configuration.injectingannotatedfields.model.AnnotatedClassWithField;
import org.mockito.internal.configuration.injectingannotatedfields.model.AnnotatedClassWithProperty;
import org.mockito.internal.reflection.Whitebox;
import org.mockito.mock.MockComponent;
import org.mockito.plugins.MockitoCore;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Reporter reporter;

    @Rule
    public FakeAnnotationProcessor annotationProcessor = new FakeAnnotationProcessor();

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        reporter = mock(Reporter.class);
    }

    @Test
    public void testFilterCandidateSingleMock() {
        MockComponent mockedComponent = mock(MockComponent.class);
        AnnotatedClassWithProperty annotatedClassWithProperty = new AnnotatedClassWithProperty("testPropertyName");
        Field field = Whitebox.getInternalState(annotatedClassWithProperty, "field");

        List<Object> mocks = new ArrayList<>();
        mocks.add(mockedComponent);

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, annotatedClassWithProperty);

        verify(mockedComponent).dependsOn(annotatedClassWithProperty);
        verify(mockedComponent).addAnnotatedField(annotatedClassWithProperty);
        verifyNoInteractions(reporter);

        Object injectedObject = actualInjecter.thenInject();
        verify(mockedComponent).addMock(annotatedClassWithProperty);
        verify(annotatedClassWithProperty).setMock(injectedObject);
        verify(annotatedClassWithProperty).setAnnotatedComponent(mockedComponent);

        assertEquals(mockedComponent, injectedObject);
    }

    @Test
    public void testFilterCandidateMultipleMocks() {
        MockComponent mockedComponent1 = mock(MockComponent.class);
        MockComponent mockedComponent2 = mock(MockComponent.class);
        AnnotatedClassWithField annotatedClassWithField = new AnnotatedClassWithField("testFieldName");
        Field field = Whitebox.getInternalState(annotatedClassWithField, "field");

        List<Object> mocks = new ArrayList<>();
        mocks.add(mockedComponent1);
        mocks.add(mockedComponent2);

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, annotatedClassWithField);

        verify(mockedComponent1).dependsOn(annotatedClassWithField);
        verify(mockedComponent1).addAnnotatedField(annotatedClassWithField);
        verifyNoInteractions(mockedComponent2, reporter);

        Object injectedObject = actualInjecter.thenInject();
        verify(mockedComponent1).addMock(annotatedClassWithField);
        verify(annotatedClassWithField).setMock(injectedObject);
        verify(annotatedClassWithField).setAnnotatedComponent(mockedComponent1);

        assertEquals(mockedComponent1, injectedObject);

        // Verify no interaction with the second mock
        verifyNoInteractions(mockedComponent2);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateException() {
        MockComponent mockedComponent = mock(MockComponent.class);
        AnnotatedClassWithProperty annotatedClassWithProperty = new AnnotatedClassWithProperty("testPropertyName");
        Field field = Whitebox.getInternalState(annotatedClassWithProperty, "field");

        List<Object> mocks = new ArrayList<>();
        mocks.add(mockedComponent);

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, spy(annotatedClassWithProperty));

        try {
            actualInjecter.thenInject();
        } catch (RuntimeException e) {
            // Expected exception
            throw e;
        }

        verify(reporter).cannotInjectDependency(field, mockedComponent, e);
        verify(mockedComponent).dependsOn(annotatedClassWithProperty);
        verify(mockedComponent).addAnnotatedField(annotatedClassWithProperty);
    }

    @Test
    public void testFilterCandidateNoMockFound() {
        AnnotatedClassWithProperty annotatedClassWithProperty = new AnnotatedClassWithProperty("testPropertyName");
        Field field = Whitebox.getInternalState(annotatedClassWithProperty, "field");

        List<Object> mocks = Collections.emptyList();

        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, annotatedClassWithProperty);

        verifyNoInteractions(reporter);
        verifyNoInteractions(annotatedClassWithProperty);

        assertEquals(new OngoingInjecter() {
            public Object thenInject() {
                return null;
            }
        }, actualInjecter);
    }
}