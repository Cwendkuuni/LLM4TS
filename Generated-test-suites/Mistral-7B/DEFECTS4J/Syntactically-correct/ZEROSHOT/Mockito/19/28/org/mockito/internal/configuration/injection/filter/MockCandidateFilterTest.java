package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;

    @Rule
    public Answer<Object> answer = new Answer<Object>() {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            return null;
        }
    };

    @Before
    public void setUp() {
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
    }

    @Test
    public void testFilterCandidateWithNullMocks() {
        Collection<Object> mocks = null;
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidateWithEmptyMocks() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
        verify(mock).answerOnCall(answer);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter result1 = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result1);
        verify(mock1).answerOnCall(answer);
        verify(mock2).answerOnCall(answer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullField() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field nullField = null;
        Object fieldInstance = mock(Object.class);
        filter.filterCandidate(mocks, nullField, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = Mockito.mock(Field.class);
        Object nullFieldInstance = null;
        filter.filterCandidate(mocks, field, nullFieldInstance);
    }

}