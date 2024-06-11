package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ValueIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class StdValueInstantiatorTest {

 private StdValueInstantiator instantiator;
 private DeserializationContext context;
 private AnnotatedClass testClass;

 @Before
 public void setup() {
 context = mock(DeserializationContext.class);
 testClass = mock(AnnotatedClass.class);
 instantiator = new StdValueInstantiator(new DeserializationConfig(), testClass.getRawClass());
 }

 @Test
 public void testGetValueTypeDesc() {
 assertEquals("getClass() returns: " + instantiator.getValueTypeDesc(), instantiator.getValueTypeDesc(), instantiator.getValueClass().getName());
 }

 @Test
 public void testCanCreateFromString() throws Exception {
 instantiator.configureFromStringCreator(createMockAnnotatedMethod(instantiator.getClass(), "createFromString", String.class));
 assertEquals(true, instantiator.canCreateFromString());
 }

 @Test
 public void testCanCreateFromInt() throws Exception {
 instantiator.configureFromIntCreator(createMockAnnotatedMethod(instantiator.getClass(), "createFromInt", int.class));
 assertEquals(true, instantiator.canCreateFromInt());
 }

 @Test
 public void testCanCreateFromLong() throws Exception {
 instantiator.configureFromLongCreator(createMockAnnotatedMethod(instantiator.getClass(), "createFromLong", long.class));
 assertEquals(true, instantiator.canCreateFromLong());
 }

 @Test
 public void testCanCreateFromDouble() throws Exception {
 instantiator.configureFromDoubleCreator(createMockAnnotatedMethod(instantiator.getClass(), "createFromDouble", double.class));
 assertEquals(true, instantiator.canCreateFromDouble());
 }

 @Test
 public void testCanCreateFromBoolean() throws Exception {
 instantiator.configureFromBooleanCreator(createMockAnnotatedMethod(instantiator.getClass(), "createFromBoolean", boolean.class));
 assertEquals(true, instantiator.canCreateFromBoolean());
 }

 @Test
 public void testCanCreateUsingDefault() throws Exception {
 AnnotatedConstructor noArgConstructor = createMockAnnotatedConstructor(instantiator.getClass(), new Class[0]);
 instantiator.configureFromObjectSettings(null, null, null, null, null, null);
 instantiator.configureFromObjectSettings(null, noArgConstructor, null, null, null, null);
 assertEquals(true, instantiator.canCreateUsingDefault());
 }

 @Test
 public void testCanCreateUsingDelegate() throws Exception {
 JavaType delegateType = createMockJavaType();
 instantiator.configureFromObjectSettings(null, null, null, null, null, null);
 instantiator.configureFromDelegateSettings(delegateType, createMockAnnotatedConstructor(instantiator.getClass(), new Class[0]), null);
 assertEquals(true, instantiator.canCreateUsingDelegate());
 }

 @Test
 public void testCanCreateUsingArrayDelegate() throws Exception {
 JavaType arrayDelegateType = createMockJavaType();
 instantiator.configureFromObjectSettings(null, null, null, null, null, null);
 instantiator.configureFromArraySettings(createMockAnnotatedWithParams(instantiator.getClass(), "createUsingArrayDelegate"), arrayDelegateType, createMockSettableBeanProperties());
 assertEquals(true, instantiator.canCreateUsingArrayDelegate());
 }

 @Test
 public void testCanCreateFromObjectWith() throws Exception {
 SettableBeanProperty[] constructorArguments = createMockSettableBeanProperties();
 instantiator.configureFromObjectSettings(null, null, null, null, createMockAnnotatedWithParams(instantiator.getClass(), "createFromObjectWith"), constructorArguments);
 assertEquals(true, instantiator.canCreateFromObjectWith());
 }

 @Test
 public void testGetDelegateType() throws Exception {
 JavaType delegateType = createMockJavaType();
 instantiator.configureFromDelegateSettings(delegateType, createMockAnnotatedConstructor(instantiator.getClass(), new Class[0]), null);
 assertEquals(delegateType, instantiator.getDelegateType(context));
 }

 @Test
 public void testGetArrayDelegateType() throws Exception {
 JavaType arrayDelegateType = createMockJavaType();
 instantiator.configureFromArraySettings(createMockAnnotatedWithParams(instantiator.getClass(), "createUsingArrayDelegate"), arrayDelegateType, createMockSettableBeanProperties());
 assertEquals(arrayDelegateType, instantiator.getArrayDelegateType(context));
 }

 @Test
 public void testGetFromObjectArguments() throws Exception {
 SettableBeanProperty[] constructorArguments = createMockSettableBeanProperties();
 instantiator.configureFromObjectSettings(null, null, null, constructorArguments, null, null);
 assertEquals(Arrays.asList(constructorArguments), instantiator.getFromObjectArguments(context));
 }

 @Test
 public void testCreateUsingDefault() throws Exception {
 Object result = instantiator.createUsingDefault(context);
 assertNull(result);
 }

 @Test
 public void testCreateFromObjectWith() throws Exception {
 SettableBeanProperty[] constructorArguments = createMockSettableBeanProperties();
 Object[] constructorArgs = new Object[constructorArguments.length];
 for (int i = 0; i < constructorArguments.length; i++) {
 constructorArgs[i] = constructorArguments[i].getValue();
 }
 Object result = instantiator.createFromObjectWith(context, constructorArgs);
 assertNull(result);
 }

 @Test
 public void testCreateUsingDelegate() throws Exception {
 Object delegate = createMockObject();
 Object result = instantiator.createUsingDelegate(context, delegate);
 assertNull(result);
 }

 @Test
 public void testCreateUsingArrayDelegate() throws Exception {
 Object[] arrayDelegate = createMockObjectArray();
 Object result = instantiator.createUsingArrayDelegate(context, arrayDelegate);
 assertNull(result);
 }

 @Test
 public void testCreateFromString() throws Exception {
 String testString = "testString";
 Object result = instantiator.createFromString(context, testString);
 assertNull(result);
 }

 @Test
 public void testCreateFromInt() throws Exception {
 int testInt = 1;
 Object result = instantiator.createFromInt(context, testInt);
 assertNull(result);
 }

 @Test
 public void testCreateFromLong() throws Exception {
 long testLong = 1L;
 Object result = instantiator.createFromLong(context, testLong);
 assertNull(result);
 }

 @Test
 public void testCreateFromDouble() throws Exception {
 double testDouble = 1.0;
 Object result = instantiator.createFromDouble(context, testDouble);
 assertNull(result);
 }

 @Test
 public void testCreateFromBoolean() throws Exception {
 boolean testBoolean = true;
 Object result = instantiator.createFromBoolean(context, testBoolean);
 assertNull(result);
 }

 @Test
 public void testCreateFromIncompleteParameter() throws Exception {
 AnnotatedParameter incompleteParameter = createMockAnnotatedParameter();
 instantiator.configureIncompleteParameter(incompleteParameter);
 Object result = instantiator.createUsingDefault(context);
 assertNull(result);
 }

 private AnnotatedMethod createMockAnnotatedMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
 AnnotatedMethod method = mock(AnnotatedMethod.class);
 when(method.getName()).thenReturn(name);
 when(method.getParameterTypes()).thenReturn(parameterTypes);
 return method;
 }

 private AnnotatedConstructor createMockAnnotatedConstructor(Class<?> clazz, Class<?>[] parameterTypes) {
 AnnotatedConstructor constructor = mock(AnnotatedConstructor.class);
 when(constructor.getDeclaringClass()).thenReturn(clazz);
 when(constructor.getParameterTypes()).thenReturn(parameterTypes);
 return constructor;
 }

 private AnnotatedWithParams createMockAnnotatedWithParams(Class<?> clazz, String name) {
 AnnotatedWithParams annotatedWithParams = mock(AnnotatedWithParams.class);
 when(annotatedWithParams.getDeclaringClass()).thenReturn(clazz);
 when(annotatedWithParams.getMethodName()).thenReturn(name);
 return annotatedWithParams;
 }

 private SettableBeanProperty[] createMockSettableBeanProperties() {
 SettableBeanProperty[] properties = new SettableBeanProperty[1];
 properties[0] = mock(SettableBeanProperty.class);
 return properties;
 }

 private JavaType createMockJavaType() {
 JavaType javaType = mock(JavaType.class);
 return javaType;
 }

 private Object createMockObject() {
 Object object = mock(Object.class);
 return object;
 }

 private Object[] createMockObjectArray() {
 Object[] objectArray = new Object[1];
 objectArray[0] = mock(Object.class);
 return objectArray;
 }

 private AnnotatedParameter createMockAnnotatedParameter() {
 AnnotatedParameter annotatedParameter = mock(AnnotatedParameter.class);
 return annotatedParameter;
 }
}