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
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private Collection<Object> mocks;

 @Before
 public void setup() {
 mocks = new ArrayList<>();
 addMock("mock1", Mockito.mock(Object.class));
 addMock("mock2", Mockito.mock(Object.class));
 addMock("mock3", Mockito.mock(Object.class));
 filter = new NameBasedCandidateFilter(new NoOpMockCandidateFilter()) {};
 }

 @Test
 public void testFilterCandidate_whenFieldNameMatchesMockName_shouldReturnCorrespondingMock() throws Exception {
 Object fieldInstance = Mockito.mock(Object.class);
 Field field = mock(Field.class);
 when(field.getName()).thenReturn("mock1");

 Object result = filter.filterCandidate(mocks, field, fieldInstance);
 assertEquals(mocks.get(0), result);
 }

 @Test
 public void testFilterCandidate_whenMultipleMatches_shouldReturnCorrespondingMock() throws Exception {
 Object fieldInstance = Mockito.mock(Object.class);
 Field field = mock(Field.class);
 when(field.getName()).thenReturn("mock2");

 doAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 return filter.filterCandidate(mocks, field, fieldInstance);
 }
 }).when(filter).filterCandidate(mocks, field, fieldInstance);

 assertEquals(mocks.get(1), verify(filter).filterCandidate(argThat(isA(Collection.class)), argThat(isA(Field.class)), argThat(isA(Object.class))));
 }

 @Test(expected = IllegalStateException.class)
 public void testFilterCandidate_whenNoMatchingMock_shouldThrowException() throws Exception {
 Object fieldInstance = Mockito.mock(Object.class);
 Field field = mock(Field.class);
 when(field.getName()).thenReturn("mockX");

 filter.filterCandidate(mocks, field, fieldInstance);
 }

 @Test
 public void testFilterCandidate_whenNoNextFilter_shouldReturnNull() throws Exception {
 NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
 Object result = filter.filterCandidate(mocks, mock(Field.class), mock(Object.class));
 assertEquals(null, result);
 }

 private void addMock(String name, Object mock) {
 mocks.add(mock);
 mockUtil.injectName(mock, name);
 }
}

class NoOpMockCandidateFilter implements MockCandidateFilter {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 return null;
 }
}