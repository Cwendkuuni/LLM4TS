package test.org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock;

 @Before
 public void setUp() {
 mock = mock(Object.class);
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testAnswerForNonMockableType() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericDeclaration returnType = MockUtil.getReturnType(invocation);
 Type genericType = returnType.getTypeParameters()[0];
 Class<?> nonMockableClass = (Class<?>) genericType.getRawType();

 assertEquals(ReturnsDeepStubs.delegate().returnValueFor(nonMockableClass), returnsDeepStubs.answer(invocation));
 }

 @Test
 public void testAnswerForMockableType() throws Throwable {
 Object nestedMock = mock(Object.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class, ArgumentMatchers.eq(nestedMock));
 GenericDeclaration returnType = MockUtil.getReturnType(invocation);
 Type genericType = returnType.getTypeParameters()[0];
 Class<?> mockableClass = (Class<?>) genericType.getRawType();

 assertEquals(nestedMock, returnsDeepStubs.answer(invocation));

 verify(nestedMock).toString();
 }

 @Test
 public void testDeepStubbing() throws Throwable {
 Object containerMock = mock(Object.class);
 Object nestedMock = mock(Object.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class, ArgumentMatchers.eq(containerMock));
 GenericDeclaration returnType = MockUtil.getReturnType(invocation);
 Type genericType = returnType.getTypeParameters()[0];
 Class<?> mockableClass = (Class<?>) genericType.getRawType();

 Mockito.when(containerMock.deepStub(invocation, ArgumentMatchers.any(GenericMetadataSupport.class)))
 .thenReturn(nestedMock);

 Object result = returnsDeepStubs.answer(invocation);

 assertEquals(nestedMock, result);
 }

 @Test
 public void testDeepStubbingNestedGeneric() throws Throwable {
 Class<?> genericInterface = createNestedGenericInterface();
 Object genericMock = mock(genericInterface);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class, ArgumentMatchers.eq(genericMock));
 GenericDeclaration returnType = MockUtil.getReturnType(invocation);
 Type genericType = returnType.getTypeParameters()[0];
 Type nestedGenericType = genericType.getComponentType();
 Class<?> nestedGenericClass = (Class<?>) nestedGenericType.getRawType();

 Mockito.when(genericMock.deepStub(invocation, ArgumentMatchers.any(GenericMetadataSupport.class)))
 .thenReturn(mock(nestedGenericClass));

 Object result = returnsDeepStubs.answer(invocation);

 assertEquals(mock(nestedGenericClass), result);
 }

 @Test
 public void testDeepStubbingSubstitutingType() throws Throwable {
 Class<?> interfaceType = createNestedGenericInterface();
 Object genericMock = mock(interfaceType);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class, ArgumentMatchers.eq(genericMock));
 GenericDeclaration returnType = MockUtil.getReturnType(invocation);
 Type genericType = returnType.getTypeParameters()[0];
 Type nestedGenericType = genericType.getComponentType();
 Class<?> nestedGenericClass = (Class<?>) nestedGenericType.getRawType();

 Mockito.when(genericMock.deepStub(invocation, ArgumentMatchers.any(GenericMetadataSupport.class)))
 .thenReturn(mock(List.class));

 Object result = returnsDeepStubs.answer(invocation);

 assertEquals(mock(List.class), result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeepStubbingNonInvocationContainer() throws Throwable {
 Object containerMock = mock(Object.class);
 Object result = returnsDeepStubs.answer(containerMock);
 }

 private Class<?> createNestedGenericInterface() {
 return new Class<>() {
 @SuppressWarnings("unchecked")
 private static final Class<Object> TYPE = (Class<Object>) Class.forName("java.util.List");
 @SuppressWarnings("unchecked")
 @Override
 public Type getGenericSuperclass() {
 return (Type) Class.forName("java.util.Map")
 .getGenericInterfaces()[0];
 }
 @SuppressWarnings("unchecked")
 @Override
 public Type[] getGenericInterfaces() {
 return new Type[] { TYPE };
 }
 @SuppressWarnings("unchecked")
 @Override
 public Class<?>[] getInterfaces() {
 return new Class<?>[] { TYPE.getClass() };
 }
 @SuppressWarnings("unchecked")
 @Override
 public Class<?> getComponentType() {
 return Number.class;
 }
 };
 }
}