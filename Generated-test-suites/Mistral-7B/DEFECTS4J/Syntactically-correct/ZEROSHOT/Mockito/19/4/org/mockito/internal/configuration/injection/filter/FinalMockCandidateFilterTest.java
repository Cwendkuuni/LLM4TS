package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.MockitoAnnotationUtils;
import org.mockito.internal.util.reflection.ReflectionUtils;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Object fieldInstance;
 private Object mock1;
 private Object mock2;
 private Field field;
 private OngoingInjecter injector;
 private Stubber stubber;

 @Rule
 public Answer answer = new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 };

 @Before
 public void setUp() {
 filter = new FinalMockCandidateFilter();
 fieldInstance = mock(Object.class);
 mock1 = mock(Object.class);
 mock2 = mock(Object.class);
 field = ReflectionUtils.findField(fieldInstance.getClass(), "fieldName"); // replace "fieldName" with the actual name of the field
 injector = mock(OngoingInjecter.class);
 stubber = doAnswer(answer).stub(mock1);
 }

 @Test
 public void testFilterCandidate_singleMock_propertySet() throws IllegalAccessException {
 // given
 List mocks = Collections.singletonList(mock1);
 BeanPropertySetter propertySetter = new BeanPropertySetter(fieldInstance, field);
 propertySetter.set(mock1);
 doAnswer(answer).when(propertySetter).set(mock1);

 // when
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // then
 assertEquals(injector, result);
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_singleMock_propertySet_failure() throws IllegalAccessException {
 // given
 List mocks = Collections.singletonList(mock1);
 BeanPropertySetter propertySetter = new BeanPropertySetter(fieldInstance, field);
 propertySetter.set(mock1);
 doThrow(new RuntimeException("test")).when(propertySetter).set(mock1);

 // when
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // then
 verify(propertySetter).set(mock1);
 verify(new Reporter()).cannotInjectDependency(field, mock1, new RuntimeException("test"));
 }

 @Test
 public void testFilterCandidate_singleMock_fieldSet() throws IllegalAccessException {
 // given
 List mocks = Collections.singletonList(mock1);
 FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
 fieldSetter.set(mock1);
 doAnswer(answer).when(fieldSetter).set(mock1);

 // when
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // then
 verifyNoInteractions(propertySetter);
 verify(fieldSetter).set(mock1);
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_singleMock_fieldSet_failure() throws IllegalAccessException {
 // given
 List mocks = Collections.singletonList(mock1);
 FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
 fieldSetter.set(mock1);
 doThrow(new RuntimeException("test")).when(fieldSetter).set(mock1);

 // when
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // then
 verify(fieldSetter).set(mock1);
 verify(new Reporter()).cannotInjectDependency(field, mock1, new RuntimeException("test"));
 }

 @Test
 public void testFilterCandidate_multipleMocks() {
 // given
 List<Object> mocks = Collections.singletonList(mock1);
 List<Object> mocks2 = Collections.singletonList(mock2);
 mocks.addAll(mocks2);

 // when
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // then
 assertEquals(new OngoingInjecter() {
 public Object thenInject() {
 return null;
 }
 }, result);
 }
}