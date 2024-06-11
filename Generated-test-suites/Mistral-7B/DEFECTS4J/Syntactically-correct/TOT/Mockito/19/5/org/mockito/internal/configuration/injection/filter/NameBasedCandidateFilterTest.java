package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.naming.Name;
import org.mockito.invocation.MethodIntercepted;
import org.mockito.stub.Answer;
import org.mockito.stub.Stub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
        filter = new NameBasedCandidateFilter(new DummyMockCandidateFilter());
    }

    @Test
    public void testFilterCandidate_whenSingleMockWithMatchingName_thenReturnThatMock() {
        Object mock = mock(Object.class, new Name("Mock1"));
        Object fieldInstance = new Object();
        Field field = new Field("field1", String.class, null);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mock).intercept(new MethodIntercepted(mockUtil.getMockName(mock), new Object[]{}, new Object[0], new Object[0], new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return fieldInstance;
            }
        }));

        verify(mock).answer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return result;
            }
        });

        verify(mock).lastCallOnMockWasAnswer();
        verify(mock).lastStubbingWasAnswer();

        assertEquals(mock, result);
    }

    @Test
    public void testFilterCandidate_whenMultipleMocksWithMatchingNames_thenReturnTheMockWithTheHighestPriority() {
        Object mock1 = mock(Object.class, new Name("Mock1_HighPriority"));
        Object mock2 = mock(Object.class, new Name("Mock1_LowPriority"));
        Object fieldInstance = new Object();
        Field field = new Field("field1", String.class, null);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mock1).intercept(new MethodIntercepted(mockUtil.getMockName(mock1), new Object[]{}, new Object[0], new Object[0], new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return new Name("Mock1_HighPriority").toInternalValue();
            }
        }));

        verify(mock1).answer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return fieldInstance;
            }
        });

        verify(mock1).lastCallOnMockWasAnswer();
        verify(mock1).lastStubbingWasAnswer();

        assertEquals(mock1, result);
    }

    @Test
    public void testFilterCandidate_whenNoMocksWithMatchingNames_thenReturnNull() {
        Object fieldInstance = new Object();
        Field field = new Field("field1", String.class, null);

        List<Object> mocks = new ArrayList<>();

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_whenMockWithConflictingFieldName_thenReturnTheMockWithTheMatchingFieldName() {
        Object mock = mock(Object.class, new Name("Mock1"));
        Object conflictingMock = mock(Object.class, new Name("Mock2_Field1"));
        Object fieldInstance = new Object();
        Field conflictingField = new Field("field1", String.class, null);
        Field targetField = new Field("field2", String.class, null);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        mocks.add(conflictingMock);

        Object result = filter.filterCandidate(mocks, targetField, fieldInstance);

        verify(mock).intercept(new MethodIntercepted(mockUtil.getMockName(mock), new Object[]{}, new Object[0], new Object[0], new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return fieldInstance;
            }
        }));

        verify(mock).answer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return result;
            }
        });

        verify(mock).lastCallOnMockWasAnswer();
        verify(mock).lastStubbingWasAnswer();

        assertEquals(mock, result);
    }

    @Test
    public void testFilterCandidate_whenMultipleMocksWithConflictingFieldNames_thenReturnTheMockWithTheFieldNameMatchingTheProvidedFieldName() {
        Object mock1 = mock(Object.class, new Name("Mock1_Field1"));
        Object mock2 = mock(Object.class, new Name("Mock2_Field2"));
        Object fieldInstance = new Object();
        Field targetField = new Field("field1", String.class, null);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Object result = filter.filterCandidate(mocks, targetField, fieldInstance);

        verify(mock1).intercept(new MethodIntercepted(mockUtil.getMockName(mock1), new Object[]{}, new Object[0], new Object[0], new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return new Name("Mock1_Field1").toInternalValue();
            }
        }));

        verify(mock1).answer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return fieldInstance;
            }
        });

        verify(mock1).lastCallOnMockWasAnswer();
        verify(mock1).lastStubbingWasAnswer();

        assertEquals(mock1, result);
    }
}

class DummyMockCandidateFilter implements MockCandidateFilter {
    @Override
    public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
        throw new UnsupportedOperationException();
    }
}