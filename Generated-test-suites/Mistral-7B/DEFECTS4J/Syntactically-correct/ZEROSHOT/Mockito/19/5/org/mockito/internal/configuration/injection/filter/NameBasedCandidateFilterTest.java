package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.internal.stubbing.answers.Answers;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;
 private Object fieldInstance;
 private Collection<Object> mocks;

 @Rule
 public Answer answer = Answers.callOriginal();

 @Before
 public void setUp() {
 nextMock = mock(MockCandidateFilter.class);
 filter = new NameBasedCandidateFilter(nextMock);
 fieldInstance = mock(Object.class);
 mocks = new ArrayList<>();
 mocks.add(mock(Object.class, "mock1"));
 mocks.add(mock(Object.class, "mock2"));
 mocks.add(mock(Object.class, "mock3"));
 }

 @Test
 public void testFilterCandidate_MultipleMocks_SameName_ReturnsCorrectMock() throws IllegalAccessException, InvocationTargetException {
 Object mockToBeReturned = mocks.get(1);
 when(nextMock.filterCandidate(mocks, anyField(), fieldInstance)).thenAnswer(Answers.delegateTo(filter));

 Field field = getField("mockFieldName");
 Object returnedMock = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mockToBeReturned, returnedMock);
 verify(nextMock).filterCandidate(argThat(contains(mockToBeReturned)), eq(field), eq(fieldInstance));
 }

 @Test
 public void testFilterCandidate_MultipleMocks_DifferentNames_NextFilterIsCalled() throws IllegalAccessException {
 Object notMatchedMock = mocks.get(2);
 when(nextMock.filterCandidate(mocks, anyField(), fieldInstance)).thenAnswer(Answers.delegateTo(filter));

 Field field = getField("mockFieldName2");
 Object returnedMock = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(notMatchedMock, returnedMock);
 verifyNoInteractions(nextMock);
 }

 @Test
 public void testFilterCandidate_SingleMock_NoConflict_NextFilterIsCalled() throws IllegalAccessException {
 when(nextMock.filterCandidate(mocks, anyField(), fieldInstance)).thenAnswer(Answers.delegateTo(filter));

 Field field = getField("mockFieldName3");
 Object returnedMock = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mocks.get(0), returnedMock);
 verify(nextMock).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
 }

 private void verifyNoInteractions(MockCandidateFilter nextMock) {
 verify(nextMock, never()).filterCandidate(anyCollection(), any(Field.class), any(Object.class));
 }

 private boolean contains(Object mock) {
 for (Object object : mocks) {
 if (object == mock) {
 return true;
 }
 }
 return false;
 }

 private Field getField(String fieldName) throws IllegalAccessException, InvocationTargetException {
 Field field = fieldInstance.getClass().getDeclaredField(fieldName);
 field.setAccessible(true);
 return field;
 }
}