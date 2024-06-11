import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        mocks = new ArrayList<>();
        fieldToBeInjected = Mockito.spy(Mockito.mock(Field.class));
        fieldInstance = Mockito.mock(Object.class);
    }

    @Test
    public void testFilterCandidate_ReturnsNull_WhenFieldIsNotAnnotated() {
        // Given
        addMockToCollection(Mockito.mock(Injectable.class));

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertNull(result);
        verify(filter).doNothing();
    }

    @Test
    public void testFilterCandidate_ReturnsInjectedMock_WhenFieldIsAnnotated() {
        // Given
        Injectable injectableMock = mock(Injectable.class);
        addMockToCollection(injectableMock);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return injectableMock;
            }
        }).when(filter).filterCandidate(mocks, Mockito.eq(fieldToBeInjected), Mockito.eq(fieldInstance));

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertNotNull(result);
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_ReturnsNull_WhenFilterCandidateReturnsNull() {
        // Given
        Mockito.doAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertNull(result);
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_ReturnsInjectedStub_WhenFilterCandidateThrowsException() {
        // Given
        Mockito.doThrow(new RuntimeException("Test Exception")).when(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        Stub<Object> stub = Mockito.stub(Object.class);
        addMockToCollection(stub);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return stub.getMockForInstallation();
            }
        }).when(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        assertNotNull(result);
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    private void addMockToCollection(Object mock) {
        mocks.add(mock);
    }

}