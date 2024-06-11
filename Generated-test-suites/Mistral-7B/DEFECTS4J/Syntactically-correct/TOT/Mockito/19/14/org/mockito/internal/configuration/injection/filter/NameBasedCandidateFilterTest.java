package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private Collection<Object> mocks;
 private Field field;
 private Object fieldInstance;

 @Before
 public void setUp() {
 mocks = new ArrayList<>();
 field = MockUtil.getField(NameBasedCandidateFilter.class, "next");
 filter = new NameBasedCandidateFilter(null);
 fieldInstance = mock(Object.class);
 }

 @Test
 public void testFilterCandidateWithUniqueMock() {
 Object mock = mock(Object.class);
 Mockito.doAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 return mock;
 }
 }).when(filter).filterCandidate(mocks, field, fieldInstance);
 assertEquals(mock, filter.filterCandidate(mocks, field, fieldInstance).getMock());
 }

 @Test
 public void testFilterCandidateWithMultipleMocksWithMatchingNames() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mocks.add(mock1);
 mocks.add(mock2);
 Mockito.doAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 return mock1;
 }
 }).when(filter).filterCandidate(mocks, field, fieldInstance);
 verify(mock1).answer(Mockito.any());
 }

 @Test
 public void testFilterCandidateWithMultipleMocksWithNonMatchingNames() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mocks.add(mock1);
 mocks.add(mock2);
 Mockito.doAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 return mock2;
 }
 }).when(filter).filterCandidate(mocks, field, fieldInstance);
 verify(mock2).answer(Mockito.any());
 }

 @Test
 public void testFilterCandidateWithNullNext() {
 filter = new NameBasedCandidateFilter(null);
 Mockito.doAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 }).when(filter).filterCandidate(mocks, field, fieldInstance);
 assertEquals(null, filter.filterCandidate(mocks, field, fieldInstance));
 }

 @Test
 public void testFilterCandidateWithEmptyMocksCollection() {
 filter = new NameBasedCandidateFilter(null);
 Mockito.doAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException("No mocks found");
 }
 }).when(filter).filterCandidate(Collections.emptyList(), field, fieldInstance);
 assertEquals(null, filter.filterCandidate(Collections.emptyList(), field, fieldInstance));
 }
}