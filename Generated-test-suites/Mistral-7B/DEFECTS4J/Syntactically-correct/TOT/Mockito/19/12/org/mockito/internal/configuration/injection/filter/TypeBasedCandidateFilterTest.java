
package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.MockCandidateFilter.InjectedField;
import org.mockito.internal.util.collections.Arrays;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

public class TypeBasedCandidateFilterTest {

    TypeBasedCandidateFilter filter;
    MockCandidateFilter nextMock;

    @Before
    public void setup() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_ReturnsExpectedMock_WhenInputMockMatchesType() {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(String.class), mock(String.class));
        Field field = new Field("testField", String.class);
        Object fieldInstance = "someValue";

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertThat(result.getMock(), instanceOf(String.class));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_ThrowsExpectedException_WhenInputFieldHasNullValue() {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(String.class), mock(String.class));
        Field field = new Field(null, String.class);
        Object fieldInstance = "someValue";

        // Act & Assert
        assertFalse("Expected NullPointerException, but no exception was thrown", filter.filterCandidate(mocks, field, fieldInstance) instanceof OngoingInjecter);
        assertThrows(NullPointerException.class, () -> filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_ReturnsNextFilterResult_WhenInputMockDoesNotMatchType() {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(String.class), mock(Integer.class));
        Field field = new Field("testField", Integer.class);
        Object fieldInstance = 1;

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNull(result.getMock());
        assertEquals(nextMock, filter.next);
    }

    @Test
    public void testFilterCandidate_ReturnsNextFilterResult_WhenInputMockMatchesFieldButDoesNotMatchType() {
        // Arrange
        Collection<Object> mocks = Arrays.asList(mock(String.class), mock(String.class));
        Field field = new Field("testField", Object.class);
        Object fieldInstance = new Object() {};

        when(nextMock.filterCandidate(eq(Arrays.asList(mock(String.class), mock(String.class))), eq(field), eq(fieldInstance)))
                .thenReturn(new OngoingInjecter(mock(InjectedField.class), mock(Field.class), null));

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(result);
        assertEquals(nextMock, filter.next);
    }
}
