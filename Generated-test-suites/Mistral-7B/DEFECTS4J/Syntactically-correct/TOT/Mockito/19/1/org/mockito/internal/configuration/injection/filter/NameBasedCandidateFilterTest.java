package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;
 private Object fieldInstance;

 @Before
 public void setUp() {
 filter = new NameBasedCandidateFilter(nextMock = mock(MockCandidateFilter.class));
 fieldInstance = mock(Object.class);
 }

 @Test
 public void testFilterCandidateWithOneMockAndMatchingName() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object.class, "mockName"));

 Field field = Mockito.spy(new Field("testField") {
 @Override
 public String getName() {
 return "mockName";
 }
 });

 when(nextMock.filterCandidate(ArgumentMatchers.eq(mocks), ArgumentMatchers.eq(field), ArgumentMatchers.eq(fieldInstance)))
 .thenAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 Object mock = invocation.getArguments()[0];
 verify(mock).getName();
 return mock;
 }
 });

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 verify(result).injectInto(fieldInstance, ArgumentMatchers.eq(field));
 }

 @Test
 public void testFilterCandidateWithOneMockAndDifferentName() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object.class, "differentMockName"));

 Field field = Mockito.spy(new Field("testField") {
 @Override
 public String getName() {
 return "mockName";
 }
 });

 when(nextMock.filterCandidate(ArgumentMatchers.eq(mocks), ArgumentMatchers.eq(field), ArgumentMatchers.eq(fieldInstance)))
 .thenReturn(null);

 verifyNoInteractions(mocks.get(0));
 }

 @Test
 public void testFilterCandidateWithMultipleMocksAndMatchingName() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object.class, "mockName1"));
 mocks.add(mock(Object.class, "mockName1"));

 Field field = Mockito.spy(new Field("testField") {
 @Override
 public String getName() {
 return "mockName1";
 }
 });

 when(nextMock.filterCandidate(ArgumentMatchers.eq(mocks), ArgumentMatchers.eq(field), ArgumentMatchers.eq(fieldInstance)))
 .thenReturn(mocks.get(1));

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 verify(result).injectInto(fieldInstance, ArgumentMatchers.eq(field));
 }

 @Test
 public void testFilterCandidateWithMultipleMocksAndDifferentNames() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object.class, "mockName1"));
 mocks.add(mock(Object.class, "mockName2"));

 Field field = Mockito.spy(new Field("testField") {
 @Override
 public String getName() {
 return "mockName1";
 }
 });

 OngoingInjecter result1 = filter.filterCandidate(Collections.singletonList(mocks.get(0)), field, fieldInstance);
 verify(result1).injectInto(fieldInstance, ArgumentMatchers.eq(field));

 OngoingInjecter result2 = filter.filterCandidate(mocks, field, fieldInstance);
 verifyNoInteractions(mocks.get(0));
 verify(result2).injectInto(fieldInstance, ArgumentMatchers.eq(field));
 }
}