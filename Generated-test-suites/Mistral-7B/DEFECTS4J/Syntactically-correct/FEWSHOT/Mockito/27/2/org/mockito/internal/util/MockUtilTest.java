package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.lang.reflect.Proxy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockCreationSettingsBuilder;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {

 private MockCreationValidator creationValidator;
 private MockUtil mockUtil;
 private ClassImposterizer classImposterizer;
 private InvocationNotifierHandler invocationNotifierHandler;
 private MethodInterceptorFilter methodInterceptorFilter;
 private MockHandler mockHandler;
 private MockSettingsImpl mockSettings;

 @Before
 public void setup() {
 creationValidator = mock(MockCreationValidator.class);
 mockUtil = new MockUtil(creationValidator);
 classImposterizer = mock(ClassImposterizer.class);
 invocationNotifierHandler = new InvocationNotifierHandler(mockHandler, mockSettings);
 methodInterceptorFilter = new MethodInterceptorFilter(invocationNotifierHandler, mockSettings);
 mockHandler = mock(MockHandler.class);
 mockSettings = new MockSettingsImpl();
 when(creationValidator.validateType(any(Class.class))).thenReturn(null);
 when(classImposterizer.imposterise(any(MethodInterceptorFilter.class), any(Class.class), any(Class[].class))).thenReturn(mock(Object.class));
 when(new LenientCopyTool().copyToMock(any(), any())).thenReturn(mock(Object.class));
 }

 @Test
 public void testCreateMock() {
 Class<?> clazzToMock = Mockito.mock(String.class);
 Object mock = mockUtil.createMock(clazzToMock, mockSettings);
 assertNotNull(mock);
 assertSame(clazzToMock, mockUtil.getClassToMockFromMock(mock));
 verify(creationValidator, times(3)).validateType(clazzToMock);
 verifyNoMoreInteractions(creationValidator);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, mockSettings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 Class<?> clazzToMock = Object.class;
 when(creationValidator.validateType(clazzToMock)).thenThrow(new NotAMockException("Invalid class"));
 mockUtil.createMock(clazzToMock, mockSettings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithExtraInterfaces() {
 Class<?> extraInterface = Integer.class;
 mockSettings.setExtraInterfaces(new Class<?>[]{extraInterface});
 Class<?> clazzToMock = Mockito.mock(String.class);
 when(creationValidator.validateType(clazzToMock)).thenReturn(null);
 when(creationValidator.validateExtraInterfaces(clazzToMock, eq(new Class<?>[]{extraInterface})))
 .thenThrow(new NotAMockException("Invalid extra interface"));
 mockUtil.createMock(clazzToMock, mockSettings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSpiedInstance() {
 mockUtil.createMock(Object.class, null);
 }

 @Test
 public void testCreateMockWithSpiedInstance() {
 Object spiedInstance = mock(String.class);
 mockSettings.setSpiedInstance(spiedInstance);
 Object mock = mockUtil.createMock(Object.class, mockSettings);
 assertNotNull(mock);
 assertSame(spiedInstance, mockUtil.getSpiedInstanceFromMock(mock));
 verify(classImposterizer).imposterise(any(MethodInterceptorFilter.class), eq(Object.class), any(Class[].class));
 verify(new LenientCopyTool()).copyToMock(spiedInstance, mock);
 }

 @Test
 public void testResetMock() {
 Object mock = mock(String.class);
 mockUtil.resetMock(mock);
 MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mock);
 assertNotNull(mockHandler);
 verify(mock).getClass();
 verify(mock).setCallback(anyInt(), any());
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockitoMock() {
 Object nonMockitoMock = Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{String.class}, new InvocationHandler() {
 @Override public Object invoke(Object proxy, Method method, Object[] args) {
 return null;
 }
 });
 mockUtil.getMockHandler(nonMockitoMock);
 }

 @Test
 public void testIsMock() {
 assertFalse(mockUtil.isMock(null));
 assertFalse(mockUtil.isMock(new Object()));
 assertFalse(mockUtil.isMock(new String()));
 Object mock = mock(String.class);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptor() {
 Object mock = mock(String.class);
 assertNull(mockUtil.getInterceptor(null));
 assertNull(mockUtil.getInterceptor(new Object()));
 assertNull(mockUtil.getInterceptor(new String()));
 assertNotNull(mockUtil.getInterceptor(mock));
 }

 @Test
 public void testGetMockName() {
 Object mock = mock(String.class);
 MockName mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 assertEquals("MockitoMock_" + mock.hashCode(), mockName.getName());
 }
}