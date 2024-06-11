package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;

    @Before
    public void setup() {
        filter = new MockitoMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_NullCollection() {
        testFilterCandidate(null, null, null);
    }

    @Test
    public void testFilterCandidate_NullFieldToBeInjected() {
        testFilterCandidate(Collections.emptyList(), null, null);
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        testFilterCandidate(Collections.emptyList(), new Field(), null);
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        testFilterCandidateWithMocks(Collections.emptyList(), new Field(), new Object());
    }

    @Test
    public void testFilterCandidate_DisregardedMock() {
        testFilterCandidateWithMocks(List.of(mock(DisregardedMock.class), mock(MockedMock.class)), new Field(), new Object());
    }

    @Test
    public void testFilterCandidate_MockedMock() {
        testFilterCandidateWithMocks(List.of(mock(MockedMock.class), mock(MockedMock.class)), new Field(), new Object());
    }

    @Test
    public void testFilterCandidate_MockedSpy() {
        testFilterCandidateWithMocks(List.of(spy(MockedSpy.class), mock(MockedMock.class)), new Field(), new Object());
    }

    @Test
    public void testFilterCandidate_FilteredMock() {
        testFilterCandidateWithMocks(List.of(mock(FilteredMock.class), mock(MockedMock.class)), new Field(), new Object());
    }

    private void testFilterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    private void testFilterCandidateWithMocks(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
        when(fieldToBeInjected.getType()).thenReturn(MockedMock.class);
        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        for (Object mock : mocks) {
            if (result != mock) {
                assertNull(mock instanceof FilteredMock);
                verify(mock).getMockingContext();
            }
        }
    }

    private static class DisregardedMock {

        public void setMockingContext(Object context) throws IllegalAccessException, InvocationTargetException, NoSuchFieldException {
            throw new UnsupportedOperationException();
        }

    }

    private static class MockedMock {

        public void setMockingContext(Object context) throws IllegalAccessException, InvocationTargetException, NoSuchFieldException {
            // do nothing
        }

    }

    private static class MockedSpy {

        public void setMockingContext(Object context) throws IllegalAccessException, InvocationTargetException, NoSuchFieldException {
            doNothing().when(this).getMockingContext();
        }

    }

    private static class FilteredMock extends MockedMock {

        @Override
        public void setMockingContext(Object context) throws IllegalAccessException, InvocationTargetException, NoSuchFieldException {
            super.setMockingContext(context);
            // additional filtering logic here
        }

    }

    private static Answer<?> doNothing() {
        return invocation -> {
            throw new UndeclaredThrowableException(new RuntimeException());
        };
    }

}