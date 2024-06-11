package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_whenFilterIsNotSet_thenReturnOriginalInstance() {
        // Given
        Collection<Object> mocks = Collections.singletonList(fieldInstance);
        Field fieldToBeInjected = Mockito.spy(Field.class);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        verify(filter).filterCandidate(any(Collection.class), any(Field.class), eq(fieldInstance));
        assertSame(fieldInstance, result);
    }

    @Test
    public void testFilterCandidate_whenFilterIsSet_thenReturnFilteredInstance() {
        // Given
        Collection<Object> mocks = Collections.singletonList(fieldInstance);
        Field fieldToBeInjected = Mockito.spy(Field.class);
        Object filterInstance = mock(Object.class);

        when(filter.filterCandidate(any(Collection.class), any(Field.class), any(Object.class)))
                .thenAnswer(new Answer<OngoingInjecter>() {
                    @Override
                    public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                        return filterInstance;
                    }
                });

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        verify(filter).filterCandidate(any(Collection.class), any(Field.class), eq(fieldInstance));
        assertSame(filterInstance, result);
    }
}