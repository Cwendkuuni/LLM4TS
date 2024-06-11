import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Object mock1;
 private Object mock2;
 private Field field;

 @Before
 public void setup() {
 mocks = new ArrayList<>();
 mock1 = Mockito.mock(String.class);
 mock2 = Mockito.mock(String.class);
 fieldInstance = new Object();
 field = mock(Field.class);
 try {
 Field.setAccessible(field);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }

 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testFilterCandidate_singleMock_injectsThroughPropertySetter() {
 mocks.add(mock1);
 doAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 field.set(fieldInstance, mock1);
 return null;
 }
 }).when(field).set(fieldInstance, any());

 OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertTrue(actualInjecter instanceof OngoingInjecter);
 actualInjecter.thenInject();
 verify(mock1).equals(fieldInstance);
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_singleMock_throwsExceptionIfPropertySetterFails() {
 mocks.clear();
 mocks.add(mock1);
 doThrow(new RuntimeException("Test exception")).when(field).set(fieldInstance, any());

 OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertTrue(actualInjecter instanceof OngoingInjecter);
 actualInjecter.thenInject();
 fail("Exception should have been thrown");
 }

 @Test
 public void testFilterCandidate_multipleMocks_returnsFalse() {
 mocks.clear();
 mocks.add(mock1);
 mocks.add(mock2);

 OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertFalse(actualInjecter instanceof OngoingInjecter);
 }

 @Test
 public void testFilterCandidate_multipleMocks_fieldAccessFails_returnsFalse() {
 mocks.clear();
 mocks.add(mock1);
 mocks.add(mock2);
 doAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 try {
 Field.set(fieldInstance, invocation.getArguments()[0]);
 fail("Should not be reached");
 } catch (IllegalAccessException e) {
 return null;
 }
 return null;
 }
 }).when(field).set(fieldInstance, any());

 OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertFalse(actualInjecter instanceof OngoingInjecter);
 }
}