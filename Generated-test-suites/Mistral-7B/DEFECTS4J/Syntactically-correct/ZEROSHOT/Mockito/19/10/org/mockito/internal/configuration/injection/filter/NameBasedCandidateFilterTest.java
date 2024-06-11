package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;

 @Before
 public void setup() {
 nextMock = Mockito.mock(MockCandidateFilter.class);
 filter = new NameBasedCandidateFilter(nextMock);
 }

 @Test
 public void testFilterCandidate_oneMock_noConflict() {
 Collection<Object> mocks = Arrays.asList(Mockito.mock(String.class));
 Field field = MockUtil.getField(String.class, "value");
 Object fieldInstance = "test";

 Object result = filter.filterCandidate(mocks, field, fieldInstance);
 assertEquals(mocks.get(0), result);
 }

 @Test
 public void testFilterCandidate_oneMock_conflict() {
 Collection<Object> mocks = Arrays.asList(Mockito.mock(String.class), Mockito.mock(String.class));
 Field field = MockUtil.getField(String.class, "value");
 Object fieldInstance = "test";

 Object result = filter.filterCandidate(mocks, field, fieldInstance);
 assertEquals(mocks.get(1), result);
 }

 @Test
 public void testFilterCandidate_multipleMocks_noConflict() {
 Collection<Object> mocks = Arrays.asList(Mockito.mock(String.class), Mockito.mock(Integer.class));
 Field field = MockUtil.getField(String.class, "value");
 Object fieldInstance = "testString";

 Object result = filter.filterCandidate(mocks, field, fieldInstance);
 assertEquals(mocks.get(0), result);
 }

 @Test
 public void testFilterCandidate_multipleMocks_conflict() {
 Collection<Object> mocks = Arrays.asList(Mockito.mock(String.class), Mockito.mock(String.class));
 Field field = MockUtil.getField(String.class, "value");
 Object fieldInstance = "testString";

 Object result = filter.filterCandidate(mocks, field, fieldInstance);
 assertEquals(mocks.get(1), result);
 }

 @Test
 public void testFilterCandidate_nextFilter_noConflict() {
 Collection<Object> mocks = Arrays.asList(Mockito.mock(String.class), Mockito.mock(Integer.class));
 Field field = MockUtil.getField(String.class, "value");
 Object fieldInstance = "testString";

 Mockito.when(nextMock.filterCandidate(Mockito.anyCollection(), Mockito.eq(field), Mockito.anyObject())).thenReturn(Mockito.mock(OngoingInjecter.class));

 Object result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNull(result);
 }

 @Test
 public void testFilterCandidate_nextFilter_conflict() {
 Collection<Object> mocks = Arrays.asList(Mockito.mock(String.class), Mockito.mock(String.class));
 Field field = MockUtil.getField(String.class, "value");
 Object fieldInstance = "testString";

 Mockito.when(nextMock.filterCandidate(Mockito.anyCollection(), Mockito.eq(field), Mockito.anyObject())).thenReturn(Mockito.mock(OngoingInjecter.class));

 Object result = filter.filterCandidate(mocks, field, fieldInstance);
 assertEquals(mocks.get(1), result);
 }
}