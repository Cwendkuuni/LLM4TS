
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_SameNameDifferentTypes() {
        String mockName1 = "mock1";
        String mockName2 = "mock1";
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(String.class));

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mock(MockCandidateFilter.class));

        doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                Object mock = invocation.getMock();
                if (mockName1.equals(mockUtil.getMockName(mock).toString())) {
                    return mock;
                }
                return null;
            }
        }).when(mockUtil).filterCandidate(mocks, field, fieldInstance);

        Object expectedResult1 = mocks.get(0);
        Object result1 = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mockUtil).filterCandidate(mocks, field, fieldInstance);
        verify(mocks.get(0)).equals(expectedResult1);
        verify(mocks.get(1)).never().equals(result1);

        doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                Object mock = invocation.getMock();
                if (mockName2.equals(mockUtil.getMockName(mock).toString())) {
                    return mock;
                }
                return null;
            }
        }).when(mockUtil).filterCandidate(mocks, field, fieldInstance);

        Object expectedResult2 = mocks.get(1);
        Object result2 = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mockUtil).filterCandidate(mocks, field, fieldInstance);
        verify(mocks.get(0)).never().equals(result2);
        verify(mocks.get(1)).equals(expectedResult2);
    }

    @Test
    public void testFilterCandidate_SameNameDifferentClasses() {
        String mockName1 = "mock1";
        String mockName2 = "mock1";
        Field field = mock(Field.class);
        Object fieldInstance1 = mock(Object.class);
        Object fieldInstance2 = mock(String.class);
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(String.class));

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mock(MockCandidateFilter.class));

        doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                Object mock = invocation.getMock();
                if (mockName1.equals(mockUtil.getMockName(mock).toString())) {
                    return mock;
                }
                return null;
            }
        }).when(mockUtil).filterCandidate(mocks, field, fieldInstance1);

        Object expectedResult1 = mocks.get(0);
        Object result1 = filter.filterCandidate(mocks, field, fieldInstance1);

        verify(mockUtil).filterCandidate(mocks, field, fieldInstance1);
        verify(mocks.get(0)).equals(expectedResult1);
        verify(mocks.get(1)).never().equals(result1);

        doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                Object mock = invocation.getMock();
                if (mockName2.equals(mockUtil.getMockName(mock).toString())) {
                    return mock;
                }
                return null;
            }
        }).when(mockUtil).filterCandidate(mocks, field, fieldInstance2);

        Object expectedResult2 = mocks.get(1);
        Object result2 = filter.filterCandidate(mocks, field, fieldInstance2);

        verify(mockUtil).filterCandidate(mocks, field, fieldInstance2);
        verify(mocks.get(0)).never().equals(result2);
        verify(mocks.get(1)).equals(expectedResult2);
    }

    @Test
    public void testFilterCandidate_DifferentNamesSameType() {
        String mockName1 = "mock1";
        String mockName2 = "mock2";
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mock(MockCandidateFilter.class));

        doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                Object mock = invocation.getMock();
                if (mockName1.equals(mockUtil.getMockName(mock).toString())) {
                    return mock;
                }
                return null;
            }
        }).when(mockUtil).filterCandidate(mocks, field, fieldInstance);

        Object expectedResult1 = mocks.get(0);
        Object result1 = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mockUtil).filterCandidate(mocks, field, fieldInstance);
        verify(mocks.get(0)).equals(expectedResult1);
        verify(mocks.get(1)).never().equals(result1);

        doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                Object mock = invocation.getMock();
                if (mockName2.equals(mockUtil.getMockName(mock).toString())) {
                    return mock;
                }
                return null;
            }
        }).when(mockUtil).filterCandidate(mocks, field, fieldInstance);

        Object expectedResult2 = mocks.get(1);
        Object result2 = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mockUtil).filterCandidate(mocks, field, fieldInstance);
        verify(mocks.get(0)).never().equals(result2);
        verify(mocks.get(1)).equals(expectedResult2);
    }

    @Test
    public void testFilterCandidate_SameNameSameTypeDifferentValues() {
        String mockName = "mock1";
        String differentValue1 = "mock1_differentValue1";
        String differentValue2 = "mock1_differentValue2";
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mock(MockCandidateFilter.class));

        doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                Object mock = invocation.getMock();
                if (mockName.equals(mockUtil.getMockName(mock).toString())) {
                    return mock;
                }
                return null;
            }
        }).when(mockUtil).filterCandidate(mocks, field, fieldInstance);

        Object expectedResult = mocks.get(0);
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mockUtil).filterCandidate(mocks, field, fieldInstance);
        verify(mocks.get(0)).equals(expectedResult);
        verify(mocks.get(1)).never().equals(result);

        doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                Object mock = invocation.getMock();
                if (mockName.equals(mockUtil.getMockName(mock).toString())) {
                    Mockito.when(mock.toString()).thenReturn(differentValue1);
                    return mock;
                }
                return null;
            }
        }).when(mockUtil).filterCandidate(mocks, field, fieldInstance);

        Object resultWithDifferentValue = mocks.get(0);
        Object expectedResultWithDifferentValue = mocks.get(1);
        Object resultWithDifferentValue2 = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mockUtil).filterCandidate(mocks, field, fieldInstance);
        verify(mocks.get(0)).toString().equals(differentValue1);
        verify(mocks.get(1)).equals(expectedResultWithDifferentValue);
        verify(mocks.get(1)).never().equals(resultWithDifferentValue2);
        verify(resultWithDifferentValue2).equals(expectedResultWithDifferentValue);
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = Collections.emptyList();

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mock(MockCandidateFilter.class));

        doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                Object mock = invocation.getMock();
                if (mockName.equals(mockUtil.getMockName(mock).toString())) {
                    return mock;
                }
                return null;
            }
        }).when(mockUtil).filterCandidate(mocks, field, fieldInstance);

        verify(mockUtil, never()).filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullField() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field nullField = null;

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mock(MockCandidateFilter.class));

        try {
            filter.filterCandidate(mocks, nullField, fieldInstance);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object nullFieldInstance = null;

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mock(MockCandidateFilter.class));

        try {
            filter.filterCandidate(mocks, field, nullFieldInstance);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}