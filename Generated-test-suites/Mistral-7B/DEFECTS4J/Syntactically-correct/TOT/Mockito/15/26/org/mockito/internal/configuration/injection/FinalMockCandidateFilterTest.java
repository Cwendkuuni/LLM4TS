package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Exactly;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.mock.MockComponent;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object mockComponent;
    private Object mockDependency;
    private Object mockDependency1;
    private Object mockDependency2;
    private Field field;

    @Rule
    public MockComponent mockComponentRule = new MockComponent();

    @Before
    public void setup() {
        mockComponent = mockComponentRule.getMock();
        mockDependency = Mockito.mock(Object.class);
        mockDependency1 = Mockito.mock(Object.class);
        mockDependency2 = Mockito.mock(Object.class);
        field = Mockito.field(mockComponent, "fieldName"); // replace with actual field name

        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithSingleMockAndSuccessfulInjection() {
        Collection<Object> mocks = Arrays.asList(mockDependency);
        assumeTrue(mocks.contains(mockDependency)); // ensure there is a single mock

        try {
            FieldSetter fieldSetter = new FieldSetter(mockComponent, field);
            fieldSetter.set(mockDependency);
        } catch (Exception e) {
            throw new RuntimeException("Setup failed", e);
        }

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, mockComponent);
        assertNotNull(injecter);
        verify(mockDependency, times(1)).then(invocation -> true);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndFailure() {
        Collection<Object> mocks = Arrays.asList(mockDependency1, mockDependency2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, mockComponent);
        assertNull(injecter);
    }

    @Test
    public void testFilterCandidateWithMockDependencyException() {
        Collection<Object> mocks = Arrays.asList(mockDependency);

        try {
            FieldSetter fieldSetter = new FieldSetter(mockComponent, field);
            fieldSetter.set(mockDependency, new Object() {}); // throw an arbitrary exception
        } catch (Exception e) {
            // intentionally left blank
        }

        assertThrows(MockitoException.class, () -> filter.filterCandidate(mocks, field, mockComponent));
    }

    @Test
    public void testFilterCandidateWithMockDependencyThatAnswerFalse() {
        when(mockDependency.thenAnswer(invocation -> false)).thenAnswer(invocation -> false);

        Collection<Object> mocks = Arrays.asList(mockDependency);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, mockComponent);
        assertNull(injecter);
        verify(mockDependency, times(1)).thenAnswer(invocation -> false);
    }

    @Test
    public void testFilterCandidateWithMockDependencyThatAnswerNull() {
        when(mockDependency.thenAnswer(invocation -> null)).thenAnswer(invocation -> null);

        Collection<Object> mocks = Arrays.asList(mockDependency);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, mockComponent);
        assertNull(injecter);
        verify(mockDependency, times(1)).thenAnswer(invocation -> null);
    }
}