package org.mockito.internal.configuration.injection.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Object mock1;
 private Object mock2;
 private Object fieldInstance;
 private Field field;

 @Before
 public void setup() throws Exception {
 mock1 = new MockObject();
 mock2 = new MockObject();
 fieldInstance = new MockObject();
 field = new Field("testField", String.class, fieldInstance.getClass());
 }

 @Test
 public void testSingleMock() throws Exception {
 filter = new FinalMockCandidateFilter();
 List<Object> mocks = Arrays.asList(mock1);
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(injecter);
 injecter.thenInject();
 new FieldSetter(fieldInstance, field).set(mock1);
 }

 @Test
 public void testMultipleMocks() throws Exception {
 filter = new FinalMockCandidateFilter();
 List<Object> mocks = Arrays.asList(mock1, mock2);
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertInstanceOf(OngoingInjecter.class, injecter);
 assertFalse(injecter.thenInject());
 }

 @Test(expected = MockitoException.class)
 public void testExceptionThrown() throws Exception {
 filter = new FinalMockCandidateFilter();
 List<Object> mocks = Arrays.asList(mock1);
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(injecter);
 new FieldSetter(fieldInstance, field).set(mock1);
 injecter.thenInject();
 }

 @Test
 public void testEmptyMocks() throws Exception {
 filter = new FinalMockCandidateFilter();
 List<Object> mocks = Collections.emptyList();
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertInstanceOf(OngoingInjecter.class, injecter);
 assertFalse(injecter.thenInject());
 }
}

class MockObject {
}