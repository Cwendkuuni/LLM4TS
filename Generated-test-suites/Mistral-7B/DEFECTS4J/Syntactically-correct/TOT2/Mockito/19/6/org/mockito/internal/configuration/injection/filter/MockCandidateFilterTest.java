import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InternalVerifier;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.junit.MockitoJUnit;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;
    private OngoingStubbing<Object> ongoingStubbing;

    @Before
    public void setUp() {
        filter = mock(MockCandidateFilter.class);
        mocks = ExtendedCollections.newArrayList();
        fieldToBeInjected = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate() {
        ongoingStubbing = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithEmptyMocksCollection() {
        filter = mock(MockCandidateFilter.class);
        mocks = ExtendedCollections.newArrayList();
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(mock(OngoingStubbing.class));

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verifyNoMoreInteractions(filter);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullMocks() {
        filter = mock(MockCandidateFilter.class);
        mocks = null;
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenThrow(new NullPointerException());

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldToBeInjected() {
        filter = mock(MockCandidateFilter.class);
        fieldToBeInjected = null;
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenThrow(new NullPointerException());

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        filter = mock(MockCandidateFilter.class);
        fieldInstance = null;
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenThrow(new NullPointerException());

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithMultipleInstancesOfSameFieldToBeInjected() {
        filter = mock(MockCandidateFilter.class);
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        ArgumentCaptor<Field> fieldCaptor = ArgumentCaptor.forClass(Field.class);

        when(filter.filterCandidate(mocks, fieldCaptor.capture(), fieldInstance)).thenReturn(mock(OngoingStubbing.class));

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        InternalVerifier verifier = new InternalVerifier();
        verifier.verifyThat(fieldCaptor.getAllValues())
                .hasSize(1);
    }

    @Test
    public void testFilterCandidate_VerifyOngoingStubbing() {
        filter = mock(MockCandidateFilter.class);
        mocks = ExtendedCollections.newArrayList();
        fieldToBeInjected = mock(Field.class);
        fieldInstance = mock(Object.class);
        ongoingStubbing = mock(OngoingStubbing.class);

        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(ongoingStubbing);
        when(ongoingStubbing.thenReturn(mock(Object.class))).thenReturn(mock(Object.class));

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(ongoingStubbing).thenReturn(mock(Object.class));
    }

    @Test
    public void testFilterCandidate_VerifyZeroInteractions() {
        filter = mock(MockCandidateFilter.class);
        mocks = ExtendedCollections.newArrayList();
        fieldToBeInjected = mock(Field.class);
        fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verifyZeroInteractions(filter);
    }

}