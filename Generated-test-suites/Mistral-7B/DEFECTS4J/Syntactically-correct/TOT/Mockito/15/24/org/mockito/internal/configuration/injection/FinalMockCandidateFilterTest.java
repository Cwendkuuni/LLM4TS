package org.mockito.internal.configuration.injection.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class FinalMockCandidateFilterTest {

 private List<Object> mocks;
 private Field field;
 private Object fieldInstance;

 @Before
 public void setup() {
 mocks = new ArrayList<>();
 field = Mockito.mock(Field.class);
 fieldInstance = new Object();
 }

 @Test
 public void testSingleMock() {
 Object mock = Mockito.mock(String.class);
 mocks.add(mock);
 FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

 assertFalse(injecter.thenInject()); // test that no injection happened when there is only one mock
 }

 @Test
 public void testMultipleMocksAndSuccessfulInjection() {
 Object mock1 = Mockito.mock(String.class);
 Object mock2 = Mockito.mock(String.class);
 mocks.add(mock1);
 mocks.add(mock2);
 FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

 // Test successful injection
 Mockito.when(mock1.getClass()).thenReturn(String.class);
 Mockito.when(mock1.equals(mock2)).thenReturn(false);
 injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertTrue(injecter instanceof OngoingInjecter);
 try {
 new FieldSetter(fieldInstance, field).set(mock1);
 } catch (Exception e) {
 throw new RuntimeException("Unexpected exception", e);
 }
 }

 @Test(expected = MockitoException.class)
 public void testMultipleMocksAndFailedInjection() {
 Object mock1 = Mockito.mock(String.class);
 Object mock2 = Mockito.mock(String.class);
 mocks.add(mock1);
 mocks.add(mock2);
 FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

 // Test failed injection
 Mockito.when(mock1.getClass()).thenReturn(Integer.TYPE);
 injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertFalse(injecter.thenInject());
 assertThrows(MockitoException.class, () -> new FieldSetter(fieldInstance, field).set(mock1));
 }

 @Test(expected = MockitoException.class)
 public void testMultipleMocksAndFailedInjectionWithSameClass() {
 Object mock1 = Mockito.mock(String.class);
 Object mock2 = Mockito.mock(String.class);
 mocks.add(mock1);
 mocks.add(mock2);
 FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

 // Test failed injection with mocks of the same class
 Mockito.when(mock1.getClass()).thenReturn(String.class);
 Mockito.when(mock1.equals(mock2)).thenReturn(true);
 injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertFalse(injecter.thenInject());
 assertThrows(MockitoException.class, () -> new FieldSetter(fieldInstance, field).set(mock1));
 }
}