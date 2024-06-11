package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.annotations.InitMocks;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class TypeBasedCandidateFilterTest {
  @Mock private Collection<Object> mocks;
  @Mock private Field field;
  @Mock private Object fieldInstance;
  @InjectMocks private TypeBasedCandidateFilter filter;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testFilterCandidate_WhenMockTypeMatches_ThenReturnFilteredMock() {
    // Given
    when(mocks.contains(any())).thenReturn(true);
    when(field.getType()).thenReturn(String.class);

    // When
    Object filteredMock = filter.filterCandidate(mocks, field, fieldInstance);

    // Then
    verifyNoMoreInteractions(mocks);
    assertEquals(fieldInstance, filteredMock);
  }

  @Test
  public void testFilterCandidate_WhenNoMockTypeMatches_ThenReturnNull() {
    // Given
    when(mocks.isEmpty()).thenReturn(true);

    // When
    Object filteredMock = filter.filterCandidate(mocks, field, fieldInstance);

    // Then
    verifyNoMoreInteractions(mocks);
    assertNull(filteredMock);
  }

  @Test
  public void testFilterCandidate_WhenMultipleMockTypeMatches_ThenReturnFirstOne() {
    // Given
    when(mocks.size()).thenReturn(3);
    when(mocks.get(0)).thenReturn(new String("mock1"));
    when(mocks.get(1)).thenReturn(new Integer(1));
    when(mocks.get(2)).thenReturn(new String("mock3"));
    when(field.getType()).thenReturn(String.class);

    // When
    Object filteredMock = filter.filterCandidate(mocks, field, fieldInstance);

    // Then
    verifyNoMoreInteractions(mocks);
    assertEquals("mock1", filteredMock);
  }

  @Test
  public void testFilterCandidate_WhenNullMockType_ThenReturnNull() {
    // Given
    when(field.getType()).thenReturn(null);

    // When
    Object filteredMock = filter.filterCandidate(mocks, field, fieldInstance);

    // Then
    verifyNoMoreInteractions(mocks);
    assertNull(filteredMock);
  }

  @Test
  public void testFilterCandidate_WhenMockTypeDoesntMatchAndNextFilterIsPresent_ThenInvokeNextFilter() {
    // Given
    MockCandidateFilter nextFilter = Mockito.mock(MockCandidateFilter.class);
    when(mocks.contains(any())).thenReturn(false);
    TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextFilter);

    // When
    Object filteredMock = filter.filterCandidate(mocks, field, fieldInstance);

    // Then
    verify(nextFilter).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    assertNull(filteredMock);
  }

  @Test
  public void testFilterCandidate_WhenFieldTypeIsEmpty_ThenReturnNull() {
    // Given
    when(field.getType()).thenReturn(null);

    // When
    Object filteredMock = filter.filterCandidate(mocks, field, fieldInstance);

    // Then
    assertNull(filteredMock);
  }
}