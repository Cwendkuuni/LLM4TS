package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        fieldToBeInjected = Mockito.mock(Field.class);
        fieldInstance = mock(Object.class);
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
    }

    @Test
    public void testFilterCandidate_whenFieldIsNotAnnotated_thenReturnOriginalInstance() {
        // given
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.isAnnotationPresent(MockitoAnnotations.Mock.class)).thenReturn(false);

        // when
        OngoingInjecter returnedObject = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // then
        verify(filter).doFilter(mocks, fieldToBeInjected, fieldInstance, null);
        assertSame(fieldInstance, returnedObject);
    }

    @Test
    public void testFilterCandidate_whenFieldIsAnnotatedWithMockitoMock_thenReturnMock() {
        // given
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.isAnnotationPresent(MockitoAnnotations.Mock.class)).thenReturn(true);

        String mock = mock(String.class);

        // when
        OngoingInjecter returnedObject = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // then
        verify(filter).doFilter(mocks, fieldToBeInjected, fieldInstance, mock);
        assertNotSame(fieldInstance, returnedObject);
        assertEquals(mock, returnedObject);
    }

    @Test
    public void testFilterCandidate_whenFieldIsAnnotatedWithSpy_thenReturnSpy() {
        // given
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.isAnnotationPresent(MockitoAnnotations.Spy.class)).thenReturn(true);

        String spy = spy(mock(String.class));

        // when
        OngoingInjecter returnedObject = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // then
        verify(filter).doFilter(mocks, fieldToBeInjected, fieldInstance, spy);
        assertNotSame(fieldInstance, returnedObject);
        assertSame(spy, returnedObject);
    }

    @Test
    public void testFilterCandidate_whenFieldIsAnnotatedWithMockAndSpy_thenReturnMock() {
        // given
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.isAnnotationPresent(MockitoAnnotations.Mock.class)).thenReturn(true);
        when(fieldToBeInjected.isAnnotationPresent(MockitoAnnotations.Spy.class)).thenReturn(true);

        String mock = mock(String.class);

        // when
        OngoingInjecter returnedObject = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // then
        verify(filter).doFilter(mocks, fieldToBeInjected, fieldInstance, mock);
        assertNotSame(fieldInstance, returnedObject);
        assertSame(mock, returnedObject);
    }

    @Test
    public void testFilterCandidate_whenFieldIsAnnotatedWithInjectMocks_thenReturnMock() {
        // given
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.isAnnotationPresent(MockitoAnnotations.InjectMocks.class)).thenReturn(true);

        String mock = mock(String.class);

        // when
        OngoingInjecter returnedObject = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // then
        verify(filter).doFilter(mocks, fieldToBeInjected, fieldInstance, mock);
        assertNotSame(fieldInstance, returnedObject);
        assertSame(mock, returnedObject);
    }

    @Test
    public void testDoFilter_whenFieldIsNotAnnotated_thenDoNothing() {
        // given
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.isAnnotationPresent(MockitoAnnotations.Mock.class)).thenReturn(false);

        // when
        filter.doFilter(mocks, fieldToBeInjected, fieldInstance, null);

        // then
        verifyNoMoreInteractions(filter);
    }

    @Test
    public void testDoFilter_whenFieldIsAnnotatedWithMockitoMock_thenCreateMock() {
        // given
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.isAnnotationPresent(MockitoAnnotations.Mock.class)).thenReturn(true);

        // when
        filter.doFilter(mocks, fieldToBeInjected, fieldInstance, null);

        // then
        verify(mocks).add(Mockito.isA(String.class));
    }

    @Test
    public void testDoFilter_whenFieldIsAnnotatedWithSpy_thenCreateSpy() {
        // given
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.isAnnotationPresent(MockitoAnnotations.Spy.class)).thenReturn(true);

        // when
        filter.doFilter(mocks, fieldToBeInjected, fieldInstance, null);

        // then
        verify(mocks).add(Mockito.isA(Spyanswers.withMock(String.class).getMock()));
    }

    @Test
    public void testDoFilter_whenFieldIsAnnotatedWithMockAndSpy_thenCreateMock() {
        // given
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.isAnnotationPresent(MockitoAnnotations.Mock.class)).thenReturn(true);
        when(fieldToBeInjected.isAnnotationPresent(MockitoAnnotations.Spy.class)).thenReturn(true);

        // when
        filter.doFilter(mocks, fieldToBeInjected, fieldInstance, null);

        // then
        verify(mocks).add(Mockito.isA(String.class));
    }

    @Test
    public void testDoFilter_whenFieldIsAnnotatedWithInjectMocks_thenCreateMocks() {
        // given
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.isAnnotationPresent(MockitoAnnotations.InjectMocks.class)).thenReturn(true);

        // when
        filter.doFilter(mocks, fieldToBeInjected, fieldInstance, null);

        // then
        verify(mocks).add(Mockito.isA(String.class));
    }

    private static class Spyanswers implements Answer<Object> {
        private final Class<?> type;

        public Spyanswers(Class<?> type) {
            this.type = type;
        }

        public Object answer(InvocationOnMock invocation) throws Throwable {
            Object spy = Mockito.spy(Mockito.mock(type));
            mocks.add(spy);
            return spy;
        }

        public static Object getMock() {
            return Mockito.mock(Object.class);
        }
    }
}