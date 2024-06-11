package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.naming.Name;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.MockUtil.mock;
import static org.mockito.internal.util.naming.Name.method;
import static org.mockito.internal.util.naming.Name.field;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;
 private MockUtil mockUtil;

 @Before
 public void setUp() {
 mockUtil = mock(MockUtil.class);
 nextMock = mock(MockCandidateFilter.class);
 filter = new NameBasedCandidateFilter(nextMock);
 }

 @Test
 public void testFilterCandidate_singleMock_fieldMatchesName() {
 // Given
 Object mock = mock("mockName");
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock);
 Field field = field(NameBasedCandidateFilter.class, "fieldInstance");
 Object fieldInstance = new Object();

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mock, result.getMock());
 }

 @Test
 public void testFilterCandidate_singleMock_fieldDoesNotMatchName() {
 // Given
 Object mock = mock("mockNameDifferentFromFieldName");
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock);
 Field field = field(NameBasedCandidateFilter.class, "fieldInstance");
 Object fieldInstance = new Object();

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(null, result);
 }

 @Test
 public void testFilterCandidate_multipleMocks_fieldMatchesName() {
 // Given
 Object mock1 = mock("mockName1");
 Object mock2 = mock("mockName1");
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 Field field = field(NameBasedCandidateFilter.class, "fieldInstance");
 Object fieldInstance = new Object();

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mock1, result.getMock());
 }

 @Test
 public void testFilterCandidate_multipleMocks_nextFilterIsCalled() {
 // Given
 Object mock1 = mock("mockName1");
 Object mock2 = mock("mockName2");
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 Field field = field(NameBasedCandidateFilter.class, "fieldInstance");
 Object fieldInstance = new Object();

 // When
 nextMock.expects(once()).method("filterCandidate")
 .withCollection(contains(mock2))
 .with(eq(field))
 .with(eq(fieldInstance));

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(null, result);
 }

 @Test
 public void testFilterCandidate_multipleMocks_fieldMatchesNameInSecondMock() {
 // Given
 Object mock1 = mock("mockName1");
 Object mock2 = mock("mockName1");
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 Field field = field(NameBasedCandidateFilter.class, "fieldInstance");
 Object fieldInstance = new Object();

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mock2, result.getMock());
 }
}