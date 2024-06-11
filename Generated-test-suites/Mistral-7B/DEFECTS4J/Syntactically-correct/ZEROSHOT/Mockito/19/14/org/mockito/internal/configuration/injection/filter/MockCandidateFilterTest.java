package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field fieldToBeInjected;

    @Before
    public void setup() {
        filter = Mockito.mock(MockCandidateFilter.class);
        mocks = new ArrayList<>();
        fieldInstance = mock(Object.class);
        fieldToBeInjected = Mockito.spy(Mockito.field(Mockito.class, "someField"));
    }

    @Test
    public void testFilterCandidate_mocksAreFilteredOut() {
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                Object target = invocation.getTarget();
                assertMockInCollection(target, mocks);
                return Mockito.mock(OngoingInjecter.class);
            }
        });

        Mockito.doReturn(fieldInstance).when(fieldToBeInjected).get(fieldInstance);

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_nonMocksAreReturned() {
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(Mockito.mock(OngoingInjecter.class));

        Mockito.doReturn(fieldInstance).when(fieldToBeInjected).get(fieldInstance);

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNotNull(result);
    }

    private void assertMockInCollection(Object mock, Collection<Object> mocks) {
        for (Object mockInMocks : mocks) {
            if (mockInMocks.equals(mock)) {
                return;
            }
        }
        throw new AssertionError("Expected mock to be in the mocks collection: " + mock);
    }

}