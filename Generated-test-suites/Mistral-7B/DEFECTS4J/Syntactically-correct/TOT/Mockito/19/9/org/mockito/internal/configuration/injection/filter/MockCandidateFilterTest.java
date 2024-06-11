package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private List<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setup() {
        mocks = new ArrayList<>();
        fieldToBeInjected = Mockito.class.getDeclaredField("mockMaker");
        fieldInstance = Mockito.mock(Object.class);
        filter = Mockito.spy(new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter() {
                    @Override
                    public Object getMockInstance() {
                        return null;
                    }
                };
            }
        });
    }

    @Test
    public void testFilterCandidate_WithEmptyMocksCollection_ReturnsNull() {
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) {
                return null;
            }
        });
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_WithNonMockInstance_ReturnsNull() {
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) {
                return null;
            }
        });
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_WithMockInstance_ReturnsOngoingInjecter() {
        OngoingInjecter expectedInjecter = Mockito.mock(OngoingInjecter.class);
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(expectedInjecter);
        assertEquals(expectedInjecter, filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_WithNullFieldInstance_ReturnsNull() {
        when(filter.filterCandidate(mocks, fieldToBeInjected, null)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) {
                return null;
            }
        });
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, null));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_WithNullMocksCollection_ThrowsNullPointerException() {
        when(filter.filterCandidate(null, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) {
                throw new NullPointerException("mocks should not be null");
            }
        });
        try {
            filter.filterCandidate(null, fieldToBeInjected, fieldInstance);
        } catch (NullPointerException e) {
            //expected exception
        }
    }

    @Test
    public void testFilterCandidate_WithLargeMocksCollection_ReturnsOngoingInjecter() {
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(new OngoingInjecter() {
            @Override
            public Object getMockInstance() {
                return new Object();
            }
        });
        List<Object> largeMocksCollection = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            largeMocksCollection.add(new Object());
        }
        assertEquals(new OngoingInjecter() {
            @Override
            public Object getMockInstance() {
                return new Object();
            }
        }, filter.filterCandidate(largeMocksCollection, fieldToBeInjected, fieldInstance));
    }
}