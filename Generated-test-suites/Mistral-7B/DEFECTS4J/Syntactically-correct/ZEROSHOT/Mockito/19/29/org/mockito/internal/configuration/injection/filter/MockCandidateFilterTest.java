package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setup() {
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        mocks = new ArrayList<>();
        fieldToBeInjected = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_whenFieldIsAnnotated_thenReturnFilteredMock() {
        Stubber stubber = Mockito.stub(Mockito.mock(Object.class).getClass());
        Object mock = stubber.createMock(Mockito.mock(Object.class).getClass());
        mocks.add(mock);

        // Given: Field is annotated
        Field fieldAnnotated = Mockito.spy(fieldToBeInjected);
        Mockito.doReturn(true).when(fieldAnnotated).isAnnotationPresent(MockitoUtil.class);

        // When: Filter Candidate
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then: Mock is returned
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNull(result);
        Mockito.verify(mocks).add(fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldIsNotAnnotated_thenReturnNull() {
        // Given: Field is not annotated
        Field fieldNotAnnotated = mock(Field.class);
        Mockito.doReturn(false).when(fieldNotAnnotated).isAnnotationPresent(MockitoUtil.class);

        // When: Filter Candidate
        OngoingInjecter result = filter.filterCandidate(mocks, fieldNotAnnotated, fieldInstance);

        // Then: Null is returned
        verify(filter).filterCandidate(mocks, fieldNotAnnotated, fieldInstance);
        assertNull(result);
        Mockito.verify(mocks, Mockito.never()).add(fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldIsAlreadyInjected_thenReturnNull() {
        Stubber stubber = Mockito.stub(Mockito.mock(Object.class).getClass());
        Object mock = stubber.createMock(Mockito.mock(Object.class).getClass());
        mocks.add(mock);
        Mockito.doReturn(mock).when(fieldInstance).getClass();
        Mockito.doReturn(mock).when(fieldInstance).get(fieldToBeInjected);

        // Given: Field is already injected
        Mockito.doReturn(true).when(mock).isMock();

        // When: Filter Candidate
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then: Null is returned
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNull(result);
        Mockito.verify(mocks, Mockito.never()).add(fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldIsAnnotatedAndInjected_thenReturnNull() {
        Stubber stubber = Mockito.stub(Mockito.mock(Object.class).getClass());
        Object mock = stubber.createMock(Mockito.mock(Object.class).getClass());
        mocks.add(mock);
        Mockito.doReturn(mock).when(fieldInstance).getClass();
        Mockito.doReturn(mock).when(fieldInstance).get(fieldToBeInjected);
        Mockito.doReturn(mock).when(filter).getFilteredMock(mock, fieldToBeInjected, fieldInstance);

        // Given: Field is annotated and injected
        Mockito.doReturn(true).when(fieldToBeInjected).isAnnotationPresent(MockitoUtil.class);

        // When: Filter Candidate
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then: Null is returned
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNull(result);
        Mockito.verify(mocks, Mockito.never()).add(fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldIsAnnotatedButNotInjected_thenReturnFilteredMock() {
        Stubber stubber = Mockito.stub(Mockito.mock(Object.class).getClass());
        Object mock = stubber.createMock(Mockito.mock(Object.class).getClass());
        mocks.add(mock);
        Mockito.doReturn(mock).when(fieldInstance).getClass();
        Mockito.doReturn(mock).when(filter).getFilteredMock(Mockito.any(), Mockito.any(), Mockito.any());

        // Given: Field is annotated but not injected
        Mockito.doReturn(true).when(fieldToBeInjected).isAnnotationPresent(MockitoUtil.class);
        Mockito.doReturn(null).when(mock).get(fieldToBeInjected);

        // When: Filter Candidate
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then: Filter mock is returned
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNull(result);
        Mockito.verify(mocks).add(Mockito.argThat(mock -> mock.getClass().getDeclaredField("filteredMock")
                .get(mock) == fieldInstance));
    }

    @Test
    public void testFilterCandidate_whenFieldIsNotAnnotatedButInjected_thenReturnInjectedMock() {
        Stubber stubber = Mockito.stub(Mockito.mock(Object.class).getClass());
        Object mock = stubber.createMock(Mockito.mock(Object.class).getClass());
        mocks.add(mock);

        // Given: Field is not annotated but injected
        Mockito.doReturn(null).when(fieldToBeInjected).isAnnotationPresent(MockitoUtil.class);
        Mockito.doReturn(mock).when(mock).get(fieldToBeInjected);

        // When: Filter Candidate
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then: Injected mock is returned
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNull(result);
        Mockito.verify(mocks, Mockito.never()).add(fieldInstance);
    }

}