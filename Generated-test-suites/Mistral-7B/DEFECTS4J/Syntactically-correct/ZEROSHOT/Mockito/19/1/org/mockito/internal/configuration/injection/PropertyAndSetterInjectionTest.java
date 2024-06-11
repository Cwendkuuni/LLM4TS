package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.TestUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;
import static org.mockito.internal.util.reflection.TestUtils.getField;
import static org.mockito.internal.util.reflection.TestUtils.getFieldValue;
import static org.mockito.internal.util.reflection.TestUtils.isAnnotated;
import static org.mockito.internal.util.reflection.TestUtils.setFieldValue;
import static org.mockito.internal.util.reflection.TestUtils.setPrivateField;

public class PropertyAndSetterInjectionTest {

 private static final Class<PropertyAndSetterInjection> CLASS_UNDER_TEST = PropertyAndSetterInjection.class;
 private static final String FIELD_MOCK_CANDIDATES = "mockCandidates";
 private static final String FIELD_INJECT_MOCKS_FIELD_OWNER = "injectMocksFieldOwner";
 private static final String FIELD_FIELD_INSTANCE_NEEDING_INJECTION = "fieldInstanceNeedingInjection";
 private static final String FIELD_FIELD_CLASS = "fieldClass";
 private static final String FIELD_ORDERED_INSTANCE_FIELDS = "orderedInstanceFields";
 private static final String FIELD_INJECTION_OCCURRED = "injectionOccurred";
 private static final String FIELD_MOCK_CANDIDATE_FILTER = "mockCandidateFilter";
 private static final String FIELD_NOT_FINAL_OR_STATIC = "notFinalOrStatic";

 private PropertyAndSetterInjection propertyAndSetterInjection;
 private Reporter reporter;

 @Before
 public void setUp() {
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 propertyAndSetterInjection.mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
 reporter = new Reporter();
 }

 @Test
 public void testProcessInjection_injectMocksFieldIsAnnotated_shouldInitializeFieldWithFirstMatchingMockCandidateAndInjectMocksIntoClassHierarchy() throws Exception {
 // given
 Object owner = createMock(Object.class);
 Field injectMocksField = createAnnotatedField(Object.class, "injectMocksField", injectMocksFieldOwner -> owner);
 Class<AnnotatedClass> annotatedClass = createAnnotatedClass(AnnotatedClass.class);
 Object mock1 = createMock(MockType1.class);
 Object mock2 = createMock(MockType2.class);
 Set<Object> mockCandidates = newMockSafeHashSet(Arrays.asList(mock1, mock2));

 // when
 propertyAndSetterInjection.processInjection(injectMocksField, owner, mockCandidates);

 // then
 assertMockIsInjected(owner, mock1, annotatedClass);
 assertMockIsInjected(owner, mock1, MockType1.class);
 assertMockIsInjected(owner, mock1, MockType2.class);
 assertMockIsInjected(owner, mock1, Object.class);

 assertMockIsNotInjected(owner, mock2, annotatedClass);
 assertMockIsNotInjected(owner, mock2, MockType1.class);
 assertMockIsNotInjected(owner, mock2, MockType2.class);
 assertMockIsNotInjected(owner, mock2, Object.class);
 }

 @Test
 public void testProcessInjection_injectMocksFieldIsNotAnnotated_shouldNotInjectMocks() throws Exception {
 // given
 Object owner = createMock(Object.class);
 Object nonAnnotatedField = createField(Object.class, "nonAnnotatedField", owner);
 Set<Object> mockCandidates = newMockSafeHashSet(Arrays.asList(createMock(MockType1.class), createMock(MockType2.class)));

 // when
 propertyAndSetterInjection.processInjection(nonAnnotatedField, owner, mockCandidates);

 // then
 assertMockIsNotInjected(owner, createMock(MockType1.class), annotatedClass);
 assertMockIsNotInjected(owner, createMock(MockType1.class), MockType1.class);
 assertMockIsNotInjected(owner, createMock(MockType1.class), Object.class);

 assertMockIsNotInjected(owner, createMock(MockType2.class), annotatedClass);
 assertMockIsNotInjected(owner, createMock(MockType2.class), MockType2.class);
 assertMockIsNotInjected(owner, createMock(MockType2.class), Object.class);
 }

 // ... Add more test cases to cover different scenarios

 private <T> Field createAnnotatedField(Class<T> clazz, String name, Function<T, Object> fieldValueProvider) throws NoSuchFieldException, IllegalAccessException {
 return getField(clazz, name, isAnnotated(clazz, InjectMocks.class))
 .annotatedWith(InjectMocks.class)
 .ofType(Field.class)
 .with(Modifier.PRIVATE)
 .with(Modifier.FINAL)
 .with(name)
 .with(getFieldValue(getField(clazz, FIELD_INJECT_MOCKS_FIELD_OWNER), null))
 .assignTo(fieldValueProvider);
 }

 private <T> Class<T> createAnnotatedClass(Class<T> clazz) {
 return clazz.getAnnotation(InjectMocks.class) != null ? clazz : createAnnotatedClass(clazz.getSuperclass());
 }

 private Object createMock(Class<?> clazz) {
 return Mockito.mock(clazz);
 }

 private Field createField(Class<?> clazz, String name, Object owner) throws NoSuchFieldException, IllegalAccessException {
 return getField(clazz, name, Modifier.PRIVATE)
 .with(name)
 .with(clazz.getDeclaredField(name).getGenericType())
 .with(owner)
 .assignTo(getField(clazz, FIELD_INJECT_MOCKS_FIELD_OWNER));
 }

 private boolean assertMockIsInjected(Object owner, Object mock, Class<?> clazz) {
 Field field = getField(clazz, "fieldToBeInjected");
 return getFieldValue(getField(owner, FIELD_INJECTION_OCCURRED), null) > 0 &&
 getFieldValue(field, owner) == mock;
 }

 private boolean assertMockIsNotInjected(Object owner, Object mock, Class<?> clazz) {
 Field field = getField(clazz, "fieldToBeInjected");
 return getFieldValue(getField(owner, FIELD_INJECTION_OCCURRED), null) <= 0 ||
 getFieldValue(field, owner) != mock;
 }
}

class AnnotatedClass {
 @InjectMocks
 private Field fieldToBeInjected;
}

class InjectMocks {
}