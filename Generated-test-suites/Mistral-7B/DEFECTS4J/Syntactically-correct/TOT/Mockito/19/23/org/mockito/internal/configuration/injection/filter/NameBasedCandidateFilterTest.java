package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.InvocationHandler;
import org.mockito.MockUtility;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Field field;

 @Before
 public void setUp() {
 mocks = new ArrayList<>();
 fieldInstance = mock(Object.class);
 field = MockUtil.getAccessibleField(NameBasedCandidateFilter.class, "next");
 filter = new NameBasedCandidateFilter(new DummyFilter()) {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 // Test method body
 return super.filterCandidate(mocks, field, fieldInstance);
 }
 };
 }

 @Test
 public void testFilterCandidate_singleMockWithSameName_returnsThatMock() {
 Object singleMock = mock(SomeClass.class);
 mocks.add(singleMock);
 String fieldName = "fieldName";
 Field testField = MockUtil.getAccessibleField(SomeClass.class, fieldName);

 when(field.getName()).thenReturn(fieldName);
 when(mockUtil.getMockName(singleMock)).thenReturn(fieldName);
 InvocationHandler handler = Mockito.mock(InvocationHandler.class);
 Answer answer = Mockito.answers.returns(handler);
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(singleMock, result.getMock());
 assertNotNull(result.getInjector());
 }

 @Test
 public void testFilterCandidate_singleMockWithDifferentName_nextFilterIsCalled() {
 Object singleMock1 = mock(SomeClass1.class);
 Object singleMock2 = mock(SomeClass2.class);
 mocks.add(singleMock1);
 mocks.add(singleMock2);
 String fieldName = "fieldName";
 Field testField = MockUtil.getAccessibleField(SomeClass1.class, fieldName);

 when(field.getName()).thenReturn(fieldName);
 when(mockUtil.getMockName(singleMock1)).thenReturn(fieldName);
 when(mockUtil.getMockName(singleMock2)).thenReturn("differentName");

 InvocationHandler handler = Mockito.mock(InvocationHandler.class);
 Answer answer = Mockito.answers.returns(handler);
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(singleMock1, result.getMock());
 assertNotNull(result.getInjector());

 verify(field).getName();
 verify(mockUtil).getMockName(singleMock2);
 verifyNoMoreInteractions(mockUtil);
 }

 @Test
 public void testFilterCandidate_conflictingNames_nextFilterIsCalled() {
 Object mock1 = mock(SomeClass1.class);
 Object mock2 = mock(SomeClass1.class);
 mocks.add(mock1);
 mocks.add(mock2);
 String fieldName = "fieldName";
 Field testField1 = MockUtil.getAccessibleField(SomeClass1.class, fieldName);
 Field testField2 = MockUtil.getAccessibleField(SomeClass1.class, fieldName);

 when(field.getName()).thenReturn(fieldName);
 when(mockUtil.getMockName(mock1)).thenReturn(fieldName);
 when(mockUtil.getMockName(mock2)).thenReturn(fieldName);

 InvocationHandler handler = Mockito.mock(InvocationHandler.class);
 Answer answer = Mockito.answers.returns(handler);
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mock1, result.getMock());
 assertNotNull(result.getInjector());

 verify(testField2).get(fieldInstance); // Tests whether correct field is picked
 }

 @Test
 public void testFilterCandidate_noMatchingMock_nextFilterIsCalled() {
 Object mock1 = mock(SomeClass1.class);
 Object mock2 = mock(SomeClass2.class);
 mocks.add(mock1);
 mocks.add(mock2);
 String fieldName = "differentFieldName";
 Field testField = MockUtil.getAccessibleField(SomeClass2.class, fieldName);

 when(field.getName()).thenReturn(fieldName);
 when(mockUtil.getMockName(mock1)).thenReturn("fieldName");
 when(mockUtil.getMockName(mock2)).thenReturn("differentName");

 InvocationHandler handler = Mockito.mock(InvocationHandler.class);
 Answer answer = Mockito.answers.returns(handler);
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 assertNull(result.getMock());
 assertNull(result.getInjector());

 verify(mock2).getClass();
 }

 private static class DummyFilter implements MockCandidateFilter {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 throw new UnsupportedOperationException();
 }
 }

 private static class SomeClass {
 private String fieldName;
 }

 private static class SomeClass1 {
 private String fieldName;
 }

 private static class SomeClass2 {
 private String fieldName;
 }
}