package org.mockito.internal.configuration.injection.filter;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class NameBasedCandidateFilterTest {
    @Test
    public void testConstructor() {
        MockCandidateFilter mockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mockFilter);
        // Check if next filter is set correctly
        assertEquals(mockFilter, filter.next);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullNext() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullMockFilter() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        MockCandidateFilter mockFilter = mock(MockCandidateFilter.class);
        Object mock = mock(Object.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mockFilter);

        Collection<Object> mocksCollection = new ArrayList<>();
        mocksCollection.add(mock);

        List<Object> result = filter.filterCandidate(mocksCollection, mockUtil.createField("testField"), fieldInstance);

        assertEquals(mock, result.get(0));
    }

    @Test
    public void testFilterCandidateWithEmptyMocksCollection() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mock(MockCandidateFilter.class));

        List<Object> result = filter.filterCandidate(new ArrayList<>(), mockUtil.createField("testField"), mock(Object.class));

        assertTrue(result.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullMockCollection() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mock(MockCandidateFilter.class));

        List<Object> result = filter.filterCandidate(null, mockUtil.createField("testField"), mock(Object.class));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullField() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mock(MockCandidateFilter.class));

        List<Object> result = filter.filterCandidate(new ArrayList<>(), null, mock(Object.class));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mock(MockCandidateFilter.class));

        List<Object> result = filter.filterCandidate(new ArrayList<>(), mockUtil.createField("testField"), null);
    }

    // ... Add test cases for other methods

    @Test
    public void testFilterCandidateWithMultipleMocksAndSameName() {
        MockCandidateFilter mockFilter1 = mock(MockCandidateFilter.class);
        MockCandidateFilter mockFilter2 = mock(MockCandidateFilter.class);

        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mockFilter1);

        Collection<Object> mocksCollection = new ArrayList<>();
        mocksCollection.add(mock1);
        mocksCollection.add(mock2);

        List<Object> result = filter.filterCandidate(mocksCollection, mockUtil.createField("testField"), fieldInstance);

        assertEquals(mock1, result.get(0));
    }

    // ... Add test cases for other methods

    @Test
    public void testFilterCandidateWithMultipleMocksAndDifferentNames() {
        MockCandidateFilter mockFilter1 = mock(MockCandidateFilter.class);
        MockCandidateFilter mockFilter2 = mock(MockCandidateFilter.class);

        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mockFilter1);

        Collection<Object> mocksCollection = new ArrayList<>();
        mocksCollection.add(mock1);
        mocksCollection.add(mock2);

        List<Object> result = filter.filterCandidate(mocksCollection, mockUtil.createField("field1"), fieldInstance);

        assertEquals(mock1, result.get(0));

        result = filter.filterCandidate(mocksCollection, mockUtil.createField("field2"), fieldInstance);

        assertEquals(mock2, result.get(0));
    }
}