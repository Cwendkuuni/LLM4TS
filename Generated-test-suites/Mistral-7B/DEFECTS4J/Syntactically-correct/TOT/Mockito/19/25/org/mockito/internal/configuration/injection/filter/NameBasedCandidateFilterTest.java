package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private Object mock1;
 private Object mock2;
 private Object fieldInstance;

 @Before
 public void setup() {
 mock1 = Mockito.mock(String.class);
 mock2 = Mockito.mock(String.class);
 fieldInstance = new Object();
 filter = new NameBasedCandidateFilter(new DummyMockCandidateFilter()) {};
 }

 @Test
 public void testFilterCandidate_sameName_oneMock() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, new Field("nameField", String.class, fieldInstance), fieldInstance);

 // Then
 assertEquals(mock1, result.getMock());
 }

 @Test
 public void testFilterCandidate_sameName_multipleMocks() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, new Field("nameField", String.class, fieldInstance), fieldInstance);

 // Then
 assertEquals(mock1, result.getMock());
 }

 @Test
 public void testFilterCandidate_differentName() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, new Field("anotherNameField", String.class, fieldInstance), fieldInstance);

 // Then
 assertEquals(mock2, result.getMock());
 }

 @Test
 public void testFilterCandidate_conflictingNames() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);

 Field conflictingField = new Field("nameField", String.class, fieldInstance);
 // When
 OngoingInjecter result = filter.filterCandidate(mocks, conflictingField, fieldInstance);

 // Then
 assertEquals(mock1, result.getMock());
 }

 @Test
 public void testFilterCandidate_noMatches() {
 // Given
 Collection<Object> mocks = new ArrayList<>();

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, new Field("nameField", String.class, fieldInstance), fieldInstance);

 // Then
 assertNull(result);
 }

 private static class DummyMockCandidateFilter implements MockCandidateFilter {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 throw new UnsupportedOperationException();
 }
 }
}