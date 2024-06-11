package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertSame;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;

    @Before
    public void setUp() {
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
    }

    @Test
    public void testFilterCandidate_ReturnsOngoingStubbing() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        // When
        OngoingStubbing<Object> result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertSame(filter, result.getMock());
    }
}