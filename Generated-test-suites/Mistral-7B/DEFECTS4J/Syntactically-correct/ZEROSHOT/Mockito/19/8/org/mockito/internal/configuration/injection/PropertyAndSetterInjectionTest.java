package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.*;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.*;
import org.mockito.junit.MockitoJUnit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;
import static org.mockito.junit.MockitoJUnit.mock;
import static org.mockito.junit.MockitoJUnit.spy;
import static org.mockito.internal.util.reflection.Fields.annotatedWith;
import static org.mockito.internal.util.reflection.Fields.of;
import static org.mockito.internal.util.reflection.Fields.withName;
import static org.mockito.internal.util.reflection.Fields.withType;
import static org.mockito.internal.util.reflection.Invocation.invoke;
import static org.mockito.internal.util.reflection.Invocation.setField;
import static org.mockito.internal.util.reflection.Invocation.getField;
import static org.mockito.junit.MockitoJUnit.verify;
import static org.mockito.junit.MockitoJUnit.verifyNoMoreInteractions;
import static org.mockito.junit.MockitoJUnit.verifyZeroInteractions;

public class PropertyAndSetterInjectionTest {

 private static final Class<PropertyAndSetterInjection> CLASS_UNDER_TEST = PropertyAndSetterInjection.class;
 private static final Object INSTANCE_UNDER_TEST;
 private static final Set<Object> MOCK_CANDIDATES = newMockSafeHashSet(3);
 private static final Object MOCK_CANDIDATE_1 = mock(MockitoCandidate.class);
 private static final Object MOCK_CANDIDATE_2 = mock(MockitoCandidate.class);
 private static final Object MOCK_CANDIDATE_3 = mock(MockitoCandidate.class);
 private static final Object INJECT_MOCKS_FIELD_OWNER = mock(Object.class);
 private static final Field INJECT_MOCKS_FIELD = of(InjectMocks.class, INJECT_MOCKS_FIELD_OWNER, "injectMocksField");
 private static final Field mockCandidateFilterField = of(PropertyAndSetterInjection.class, PropertyAndSetterInjection.class, "mockCandidateFilter");
 private static final Field notFinalOrStaticFilterField = of(PropertyAndSetterInjection.class, PropertyAndSetterInjection.class, "notFinalOrStatic");
 private PropertyAndSetterInjection propertyAndSetterInjection;

 @Before
 public void setUp() {
 INSTANCE_UNDER_TEST = new PropertyAndSetterInjection();
 propertyAndSetterInjection = spy(INSTANCE_UNDER_TEST);

 MOCK_CANDIDATES.add(MOCK_CANDIDATE_1);
 MOCK_CANDIDATES.add(MOCK_CANDIDATE_2);
 MOCK_CANDIDATES.add(MOCK_CANDIDATE_3);

 invokeStatic(mockCandidateFilterField, propertyAndSetterInjection, new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter())));
 invokeStatic(notFinalOrStaticFilterField, propertyAndSetterInjection, new ListUtil.Filter<Field>() {
 @Override
 public boolean isOut(Field object) {
 return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
 }
 });
 }

 @Test
 public void testProcessInjection() {
 Object injectMocksFieldInitializationReport = invoke(propertyAndSetterInjection, "initializeInjectMocksField", INJECT_MOCKS_FIELD, INJECT_MOCKS_FIELD_OWNER);
 verify(propertyAndSetterInjection).initializeInjectMocksField(INJECT_MOCKS_FIELD, INJECT_MOCKS_FIELD_OWNER);
 verify(INJECT_MOCKS_FIELD_OWNER).getClass();
 verifyZeroInteractions(MOCK_CANDIDATE_1, MOCK_CANDIDATE_2, MOCK_CANDIDATE_3);

 // test with no injection
 assertFalse(invoke(propertyAndSetterInjection, "processInjection", INJECT_MOCKS_FIELD, INJECT_MOCKS_FIELD_OWNER, MOCK_CANDIDATES));
 verifyZeroInteractions(MOCK_CANDIDATE_1, MOCK_CANDIDATE_2, MOCK_CANDIDATE_3);

 // test with one injection by type
 MOCK_CANDIDATE_1 = mock(AnnotatedWithInjectMocks.class);
 MOCK_CANDIDATES.clear();
 MOCK_CANDIDATES.add(MOCK_CANDIDATE_1);

 assertTrue(invoke(propertyAndSetterInjection, "processInjection", INJECT_MOCKS_FIELD, INJECT_MOCKS_FIELD_OWNER, MOCK_CANDIDATES));
 verify(MOCK_CANDIDATE_1).isOut(any());
 verify(MOCK_CANDIDATE_2).isOut(any());
 verify(MOCK_CANDIDATE_3).isOut(any());
 verifyNoMoreInteractions(MOCK_CANDIDATE_1, MOCK_CANDIDATE_2, MOCK_CANDIDATE_3);

 // test with one injection by name
 AnnotatedWithName annotatedWithName = mock(AnnotatedWithName.class);
 annotatedWithName.getName().when(annotatedWithName).toString().thenReturn("injectedFieldByName");
 MOCK_CANDIDATE_2 = annotatedWithName;
 MOCK_CANDIDATES.clear();
 MOCK_CANDIDATES.add(MOCK_CANDIDATE_2);

 assertTrue(invoke(propertyAndSetterInjection, "processInjection", INJECT_MOCKS_FIELD, INJECT_MOCKS_FIELD_OWNER, MOCK_CANDIDATES));
 verify(MOCK_CANDIDATE_1).isOut(any());
 verify(MOCK_CANDIDATE_2).isOut(any());
 verify(MOCK_CANDIDATE_3).isOut(any());
 verify(MOCK_CANDIDATE_2).setName(eq("injectedFieldByName"));
 verifyNoMoreInteractions(MOCK_CANDIDATE_1, MOCK_CANDIDATE_2, MOCK_CANDIDATE_3);

 // test with multiple injections by name
 AnnotatedWithName annotatedWithName1 = mock(AnnotatedWithName.class);
 annotatedWithName1.getName().when(annotatedWithName1).toString().thenReturn("injectedFieldByName1");
 MOCK_CANDIDATE_1 = annotatedWithName1;

 AnnotatedWithName annotatedWithName2 = mock(AnnotatedWithName.class);
 annotatedWithName2.getName().when(annotatedWithName2).toString().thenReturn("injectedFieldByName2");
 MOCK_CANDIDATE_3 = annotatedWithName2;

 assertTrue(invoke(propertyAndSetterInjection, "processInjection", INJECT_MOCKS_FIELD, INJECT_MOCKS_FIELD_OWNER, MOCK_CANDIDATES));
 verify(MOCK_CANDIDATE_1).isOut(any());
 verify(MOCK_CANDIDATE_2).isOut(any());
 verify(MOCK_CANDIDATE_3).isOut(any());
 verify(MOCK_CANDIDATE_1).setName(eq("injectedFieldByName1"));
 verify(MOCK_CANDIDATE_3).setName(eq("injectedFieldByName2"));
 verifyNoMoreInteractions(MOCK_CANDIDATE_1, MOCK_CANDIDATE_2, MOCK_CANDIDATE_3);

 // test with multiple injections by type and name
 AnnotatedWithTypeAndName annotatedWithTypeAndName1 = mock(AnnotatedWithTypeAndName.class);
 annotatedWithTypeAndName1.getName().when(annotatedWithTypeAndName1).toString().thenReturn("injectedFieldByName1");
 annotatedWithTypeAndName1.getType().when(annotatedWithTypeAndName1).getType().thenReturn(String.class);
 MOCK_CANDIDATE_1 = annotatedWithTypeAndName1;

 AnnotatedWithTypeAndName annotatedWithTypeAndName2 = mock(AnnotatedWithTypeAndName.class);
 annotatedWithTypeAndName2.getName().when(annotatedWithTypeAndName2).toString().thenReturn("injectedFieldByName2");
 annotatedWithTypeAndName2.getType().when(annotatedWithTypeAndName2).getType().thenReturn(Integer.class);
 MOCK_CANDIDATE_2 = annotatedWithTypeAndName2;

 assertTrue(invoke(propertyAndSetterInjection, "processInjection", INJECT_MOCKS_FIELD, INJECT_MOCKS_FIELD_OWNER, MOCK_CANDIDATES));
 verify(MOCK_CANDIDATE_1).isOut(any());
 verify(MOCK_CANDIDATE_2).isOut(any());
 verify(MOCK_CANDIDATE_3).isOut(any());
 verify(MOCK_CANDIDATE_1).setName(eq("injectedFieldByName1"));
 verify(MOCK_CANDIDATE_2).setName(eq("injectedFieldByName2"));
 verify(MOCK_CANDIDATE_2).getType().isEqualTo(Integer.class);
 verifyNoMoreInteractions(MOCK_CANDIDATE_1, MOCK_CANDIDATE_2, MOCK_CANDIDATE_3);
 }

 private static Object invokeStatic(Field field, Object instance, String methodName, Object... arguments) {
 try {
 return invoke(field, field.get(instance), methodName, arguments);
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 private static Object invoke(Field field, Object instance, String methodName, Object... arguments) {
 try {
 return invoke(field.getType().getMethod(methodName), instance, arguments);
 } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
 throw new RuntimeException(e);
 }
 }
}

class AnnotatedWithInjectMocks {
 @InjectMocks
 private Object field;
}

class AnnotatedWithName {
 @InjectMocks
 @Name("injectedFieldByName")
 private Object field;
}

class AnnotatedWithTypeAndName {
 @InjectMocks
 @Name("injectedFieldByName")
 @Type(String.class)
 private Object field;
}

class MockitoCandidate {
}