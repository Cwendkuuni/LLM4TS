package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private Object fieldInstance;

    @Before
    public void setUp() {
        mockCandidateFilter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_whenMocksCollectionIsEmpty_thenReturnNull() {
        Collection<Object> mocks = new ArrayList<>();

        assertNull(mockCandidateFilter.filterCandidate(mocks, getFieldToBeInjected("field1", Object.class), fieldInstance));
    }

    @Test
    public void testFilterCandidate_whenMocksCollectionIsNotEmpty_thenReturnFilteredMock() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return mock(OngoingInjecter.class);
            }
        }).when(mockCandidateFilter).filterCandidate(anyCollection(), getFieldToBeInjected("field1", Object.class), fieldInstance);

        OngoingInjecter filteredMock = mockCandidateFilter.filterCandidate(mocks, getFieldToBeInjected("field1", Object.class), fieldInstance);

        assertSame(mock1, filteredMock);
    }

    @Test
    public void testFilterCandidate_whenFieldToBeInjectedIsNotAccessible_thenThrowIllegalAccessException() {
        Collection<Object> mocks = new ArrayList<>();

        try {
            mockCandidateFilter.filterCandidate(mocks, getFieldToBeInjected("field1", String.class), fieldInstance);
            fail("IllegalAccessException expected");
        } catch (IllegalAccessException e) {
            //expected
        }
    }

    private Field getFieldToBeInjected(String fieldName, Class type) {
        try {
            return fieldInstance.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field " + fieldName + " not found", e);
        }
    }

}