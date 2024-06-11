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

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;
 private Object fieldInstance;
 private Collection<Object> mocks;

 @Before
 public void setup() {
 mocks = new ArrayList<>();
 nextMock = mock(MockCandidateFilter.class);
 filter = new NameBasedCandidateFilter(nextMock);
 fieldInstance = spy(new Object());
 }

 @Test
 public void testFilterCandidate_whenMocksHaveSameNameAsField_thenReturnCorrectMock() throws NoSuchFieldException {
 Object mock = mock(Object.class);
 mocks.add(mock);
 Field field = fieldInstance.getClass().getDeclaredField("fieldName");
 field.setAccessible(true);

 when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 return mock;
 }
 });

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(result);
 verify(nextMock).filterCandidate(mocks, field, fieldInstance);
 }

 @Test
 public void testFilterCandidate_whenMultipleMocksHaveSameNameAsField_thenReturnCorrectMock() throws NoSuchFieldException {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mocks.add(mock1);
 mocks.add(mock2);
 Field field = fieldInstance.getClass().getDeclaredField("fieldName");
 field.setAccessible(true);

 when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 return mock2;
 }
 });

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(result);
 verify(nextMock).filterCandidate(mocks, field, fieldInstance);
 }

 @Test
 public void testFilterCandidate_whenNoMocksHaveSameNameAsField_thenReturnNextFilterResult() throws NoSuchFieldException {
 Field field = fieldInstance.getClass().getDeclaredField("notMatchingFieldName");
 field.setAccessible(true);

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(result);
 verify(nextMock).filterCandidate(mocks, field, fieldInstance);
 }
}