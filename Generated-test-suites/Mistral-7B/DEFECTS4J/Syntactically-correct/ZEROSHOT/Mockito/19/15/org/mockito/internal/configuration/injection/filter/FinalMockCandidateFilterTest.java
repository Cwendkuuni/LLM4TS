package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.OngoingStubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Reporter reporter;
 private Collection<Object> mocks;
 private Field field;
 private Object fieldInstance;

 @Before
 public void setUp() {
 filter = new FinalMockCandidateFilter();
 reporter = mock(Reporter.class);
 mocks = new ArrayList<>();
 field = mock(Field.class);
 fieldInstance = mock(Object.class);
 }

 @Test
 public void testFilterCandidate_singleMock() {
 Object mock = mock(Object.class);
 mocks.add(mock);
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(injecter);
 }

 @Test(expected = AssertionError.class)
 public void testFilterCandidate_noMock() {
 OngoingInjecter injecter = filter.filterCandidate(new ArrayList<>(), field, fieldInstance);
 }

 @Test
 public void testFilterCandidate_propertySetterSuccess() {
 OngoingStubber stubber = Whitebox.getInternalState(mock(MockitoConfiguration.class), OngoingStubber.class);
 stubber.stub(field).toReturn(fieldInstance);
 mocks.add(spy(mock(Object.class)));
 Whitebox.setInternalState(mocks.get(0), OngoingInjecter.class, stubber);
 injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(injecter);
 }

 @Test(expected = AssertionError.class)
 public void testFilterCandidate_propertySetterFailure() {
 mocks.add(spy(mock(Object.class)));
 injecter = filter.filterCandidate(mocks, field, fieldInstance);
 Whitebox.getInternalState(mocks.get(0), Reporter.class).reportError(reporter, new RuntimeException());
 }

 @Test
 public void testFilterCandidate_fieldAccessSuccess() {
 Whitebox.setInternalState(fieldInstance, field.getName(), mock(Object.class));
 mocks.add(spy(mock(Object.class)));
 injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(injecter);
 }

 @Test(expected = AssertionError.class)
 public void testFilterCandidate_fieldAccessFailure() {
 mocks.add(spy(mock(Object.class)));
 injecter = filter.filterCandidate(mocks, field, fieldInstance);
 Whitebox.getInternalState(mocks.get(0), Reporter.class).reportError(reporter, new RuntimeException());
 }
}