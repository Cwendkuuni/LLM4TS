package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.StubbingOperations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private Reporter reporter;
    private BeanPropertySetter propertySetter;
    private FieldSetter fieldSetter;
    private FinalMockCandidateFilter filter;

    @Before
    public void setup() {
        reporter = mock(Reporter.class);
        propertySetter = mock(BeanPropertySetter.class);
        fieldSetter = mock(FieldSetter.class);
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        // Given
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));

        // When
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Object mock = mocks.get(0);
        verify(propertySetter).set(fieldInstance, field, mock);
        verifyNoInteractions(fieldSetter);
        verify(reporter).cannotInjectDependency(field, mock, isNull());
        verify(reporter).cannotInjectDependency(field, mock, eq(new RuntimeException("Test Exception")));
        verify(reporter, never()).cannotInjectDependency(field, mock, any(RuntimeException.class));

        Answer<Object> answer = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return actualInjecter.thenInject();
            }
        };
        Object result = when(mock.getClass().getDeclaredField("result")).thenReturn(answer).getMock();

        Object expectedResult = mock;

        // Then
        assertEquals(expectedResult, result);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateWithOneMockAndException() {
        // Given
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));

        // When
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Object mock = mocks.get(0);
        throw new RuntimeException("Test Exception");
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        // Given
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        // When
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verifyNoInteractions(propertySetter);
        verifyNoInteractions(fieldSetter);
        verifyNoInteractions(reporter);

        Answer<Object> answer = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        Object result = when(actualInjecter.thenInject()).thenReturn(answer).getMock();

        // Then
        assertNull(result);
    }
}