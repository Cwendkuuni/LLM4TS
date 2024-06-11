package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Field field;
 private Object candidateMock;
 private Stubber stubber;
 private Reporter reporter;
 private BeanPropertySetter propertySetterSpy;
 private FieldSetter fieldSetterSpy;

 @Before
 public void setup() {
 mocks = new ArrayList<>();
 fieldInstance = mock(Object.class);
 field = mock(Field.class);
 filter = spy(new FinalMockCandidateFilter());
 reporter = mock(Reporter.class);
 propertySetterSpy = spy(new BeanPropertySetter(fieldInstance, field));
 fieldSetterSpy = spy(new FieldSetter(fieldInstance, field));
 stubber = mock(Stubber.class);
 doAnswer(invocation -> {
 throw new RuntimeException("Test exception");
 }).when(propertySetterSpy).set(any(), any());
 doAnswer(invocation -> {
 FieldType.write(fieldInstance, field, candidateMock);
 return candidateMock;
 }).when(fieldSetterSpy).set(any(), any());
 }

 @Test
 public void testFilterCandidateWithOneMock() {
 candidateMock = mock(Object.class);
 mocks.add(candidateMock);
 OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
 actualInjecter.thenInject();

 verify(propertySetterSpy).set(fieldInstance, candidateMock);
 verify(fieldSetterSpy).set(any(), any()).never();
 }

 @Test(expected = RuntimeException.class)
 public void testFilterCandidateWithOneMockAndFieldAccessFailed() {
 candidateMock = mock(Object.class);
 mocks.add(candidateMock);
 doAnswer(invocation -> {
 throw new RuntimeException("Test exception");
 }).when(fieldSetterSpy).set(any(), any());
 OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
 actualInjecter.thenInject();

 verify(propertySetterSpy).set(fieldInstance, candidateMock);
 verify(reporter).cannotInjectDependency(field, candidateMock, any());
 }

 @Test
 public void testFilterCandidateWithMultipleMocks() {
 candidateMock = mock(Object.class);
 mocks.add(candidateMock);
 mocks.add(mock(Object.class));
 OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);
 actualInjecter.thenInject();

 verify(propertySetterSpy, never()).set(any(), any());
 verify(fieldSetterSpy, never()).set(any(), any());
 }
}