import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter nextFilter;
    private MockUtil mockUtil;

    @Captor
    private ArgumentCaptor<Collection<Object>> collectionArgumentCaptor;

    @Before
    public void setUp() {
        mockUtil = mock(MockUtil.class);
        nextFilter = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testFilterCandidate_SingleMock_ReturnsNextFilter() {
        // Given
        Collection<Object> mocks = Arrays.asList(new Object());
        Field field = new Field("testField", Integer.TYPE, null);
        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
        assertEquals(nextFilter, result.filter());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_ReturnsMatchingMock() {
        // Given
        Collection<Object> mocks = Arrays.asList(new Object("mock1"), new Object("mock2"), new Object("mock3"));
        Field field = new Field("testField", String.class, null);
        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Object mockWithMatchingName = new Object("mock1");
        verify(nextFilter).filterCandidate(collectionArgumentCaptor.capture(mocks), field, fieldInstance);
        assertEquals(mockWithMatchingName, result.filter());
        assertEquals(Arrays.asList(mockWithMatchingName), collectionArgumentCaptor.getValue());
    }

    @Test
    public void testFilterCandidate_SingleMock_ShouldNotInvokedNextFilter() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field("testField", Integer.TYPE, null);
        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(nextFilter, never()).filterCandidate(mocks, field, fieldInstance);
        assertNull(result.filter());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullCollection_ThrowsException() {
        // Given
        Collection<Object> mocks = null;
        Field field = new Field("testField", String.class, null);
        Object fieldInstance = new Object();

        // When
        filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        // Exception should be thrown
    }

    @Test
    public void testFilterCandidate_MultipleMocks_ReturnsMatchingMock_WhenNextFilterReturnsNull() {
        // Given
        Collection<Object> mocks = Arrays.asList(new Object("mock1"), new Object("mock2"), new Object("mock3"));
        Field field = new Field("testField", String.class, null);
        Object fieldInstance = new Object();

        when(nextFilter.filterCandidate(mocks, field, fieldInstance)).thenReturn(null);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Object mockWithMatchingName = new Object("mock1");
        verify(nextFilter, never()).filterCandidate(mocks, field, fieldInstance);
        assertEquals(mockWithMatchingName, result.filter());
    }
}