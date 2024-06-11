package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.naming.NameTransformer;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Answer;
import org.mockito.stub.Answer2;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;
    private Field testField;
    private Object testFieldInstance;
    private Collection<Object> testMocks;

    @Before
    public void setUp() {
        testMocks = new ArrayList<>();
        testField = new Field("testField", String.class, null, null);
        testFieldInstance = mock(String.class);

        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_WhenInputCollectionIsEmpty_ThenReturnNullFromNextFilter() {
        when(nextMock.filterCandidate(testMocks, testField, testFieldInstance)).thenReturn(null);

        assertNull(filter.filterCandidate(new ArrayList<>(), testField, testFieldInstance));

        verifyZeroInteractions(nextMock);
    }

    @Test
    public void testFilterCandidate_WhenInputCollectionContainsMatchingType_ThenReturnMatchingMockFromNextFilter() {
        Object mockTypeMatch = mock(String[].class);
        testMocks.add(mockTypeMatch);

        when(nextMock.filterCandidate(testMocks, testField, testFieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) {
                return mock(OngoingInjecter.class);
            }
        });

        OngoingInjecter result = filter.filterCandidate(testMocks, testField, testFieldInstance);

        assertNotNull(result);
        verify(nextMock).filterCandidate(testMocks, testField, testFieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenInputCollectionContainsNonMatchingType_ThenReturnNullFromFilter() {
        Object nonMatchingMock = mock(Integer.class);
        testMocks.add(nonMatchingMock);

        when(nextMock.filterCandidate(testMocks, testField, testFieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) {
                return null;
            }
        });

        assertNull(filter.filterCandidate(testMocks, testField, testFieldInstance));

        verifyZeroInteractions(nextMock);
    }
}