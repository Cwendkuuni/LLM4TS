package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter filter;
    private Object mock1;
    private Object mock2;
    private Object mock3;

    @Before
    public void setUp() {
        filter = new TypeBasedCandidateFilter(null);
        mock1 = Mockito.mock(String.class);
        mock2 = Mockito.mock(Number.class);
        mock3 = Mockito.mock(Object.class);
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsAssignableFromMockType_ThenMockIsReturned() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock1, result.getMock());
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsNotAssignableFromMockType_ThenNextFilterIsCalled() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = new Field("testField", Number.class, null, null);
        Object fieldInstance = new Object();

        Stubber stubber = Mockito.mock(Stubber.class);
        MockCandidateFilter next = new DummyMockCandidateFilter(stubber);
        filter = new TypeBasedCandidateFilter(next);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Mockito.verify(next).filterCandidate(eq(new ArrayList<>()), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsAssignableFromMockTypeAndNextFilterReturnsNull_ThenNullIsReturned() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = new Object();

        MockCandidateFilter next = new NullMockCandidateFilter();
        filter = new TypeBasedCandidateFilter(next);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(null, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_WhenNullIsPassedAsNextFilter_ThenIllegalArgumentExceptionIsThrown() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = new Object();

        filter = new TypeBasedCandidateFilter(null);

        // When & Then
        filter.filterCandidate(mocks, field, fieldInstance);
    }
}

class DummyMockCandidateFilter implements MockCandidateFilter {
    private Stubber stubber;

    public DummyMockCandidateFilter(Stubber stubber) {
        this.stubber = stubber;
    }

    public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
        return new OngoingInjecter(mocks.get(0), new InvocationOnMock() {});
    }
}

class NullMockCandidateFilter implements MockCandidateFilter {
    public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
        return null;
    }
}