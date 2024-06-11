import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        mockCandidateFilter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
    }

    @Test
    public void testFilterCandidate_nullCollection() {
        Collection<Object> mocks = null;
        Field fieldToBeInjected = new Field("testField", Object.class);
        Object fieldInstance = mock(Object.class);

        assertSame("Expected null when filtering with null collection", null, mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_emptyCollection() {
        Collection<Object> mocks = Collections.emptyList();
        Field fieldToBeInjected = new Field("testField", Object.class);
        Object fieldInstance = mock(Object.class);

        assertSame("Expected null when filtering with empty collection", null, mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_filterCandidateAddedToMock() throws IllegalAccessException {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field fieldToBeInjected = new Field("testField", Object.class);
        Object fieldInstance = mock(Object.class);

        InvocationOnMock invocation = Mockito.mockStatic(Mockito.class).oMock();
        invocation.when(Mockito.class).spy(Mockito.any(Object.class));
        Answer<Object> answer = Mockito.answer(fieldInstance);
        invocation.when(Mockito.class).spyAnswer(Mockito.eq(fieldToBeInjected), answer);

        Object result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Mockito.verify(mock).injectField(fieldToBeInjected, fieldInstance);
        assertSame("Expected filtered mock to be returned", mock, result);
    }

    @Test
    public void testFilterCandidate_filterCandidateNotAddedToMock() throws IllegalAccessException {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        Object mockFiltered = mock(Object.class);
        mocks.add(mock);
        Field fieldToBeInjected = new Field("testField", Object.class);
        Object fieldInstance = mock(Object.class);

        InvocationOnMock invocation = Mockito.mockStatic(Mockito.class).oMock();
        invocation.when(Mockito.class).spy(Mockito.any(Object.class));
        invocation.when(Mockito.class).spyAnswer(Mockito.eq(fieldToBeInjected), Mockito.doReturn(mockFiltered).answeredWith(fieldInstance));

        Object result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Mockito.verify(mock, Mockito.never()).injectField(fieldToBeInjected, fieldInstance);
        assertSame("Expected filtered mock to be returned", mockFiltered, result);
    }

}