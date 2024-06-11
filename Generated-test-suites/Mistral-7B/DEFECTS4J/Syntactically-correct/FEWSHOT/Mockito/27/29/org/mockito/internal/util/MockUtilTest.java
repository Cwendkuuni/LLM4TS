package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.MockCreationValidator;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito. jelassi.factory.Factory;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MockUtilTest {

 private MockCreationValidator mockCreationValidator = mock(MockCreationValidator.class);
 private MockUtil mockUtil = new MockUtil(mockCreationValidator);

 @Test
 public void testCreateMock() {
 Class<?> clazz = Mockito.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object mock = mockUtil.createMock(clazz, settings);
 assertNotNull(mock);
 assertInstanceOf(clazz, mock);
 }

 @Test(expected = AssertionError.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = AssertionError.class)
 public void testCreateMockWithInvalidClass() {
 Class<?> clazz = Integer.TYPE;
 MockSettingsImpl settings = new MockSettingsImpl();
 mockUtil.createMock(clazz, settings);
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<?> clazz = Mockito.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class<?>[]{Serializable.class, RuntimeException.class});
 Object mock = mockUtil.createMock(clazz, settings);
 assertInstanceOf(clazz, mock);
 assertInstanceOf(Serializable.class, mock);
 assertInstanceOf(RuntimeException.class, mock);
 }

 @Test(expected = AssertionError.class)
 public void testCreateMockWithInvalidExtraInterface() {
 Class<?> clazz = Mockito.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class<?>[]{Integer.TYPE});
 mockUtil.createMock(clazz, settings);
 }

 @Test
 public void testCreateMockWithNullSpiedInstance() {
 Class<?> clazz = Mockito.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object mock = mockUtil.createMock(clazz, settings);
 assertNotNull(mock);
 assertNull(settings.getSpiedInstance());
 }

 @Test
 public void testCreateMockWithSpiedInstance() {
 Class<?> clazz = Mockito.class;
 Object spiedInstance = mock(String.class);
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);
 Object mock = mockUtil.createMock(clazz, settings);
 assertInstanceOf(clazz, mock);
 assertSame(spiedInstance, settings.getSpiedInstance());
 }

 @Test(expected = AssertionError.class)
 public void testCreateMockWithInvalidSpiedInstanceType() {
 Class<?> clazz = Mockito.class;
 Object spiedInstance = new Object();
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);
 mockUtil.createMock(clazz, settings);
 }

 @Test
 public void testResetMock() {
 Object mock = mock(String.class);
 mockUtil.resetMock(mock);
 verifyNoMoreInteractions(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResetMockWithNullMock() {
 mockUtil.resetMock(null);
 }

 @Test
 public void testGetMockHandler() {
 Object mock = mock(String.class);
 MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mock);
 assertNotNull(mockHandler);
 assertInstanceOf(MockHandler.class, mockHandler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockitoMock() {
 Object mock = new Object();
 mockUtil.getMockHandler(mock);
 }

 @Test
 public void testIsMock() {
 Object mock = mock(String.class);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockWithNullMock() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testGetInterceptor() {
 Object mock = mock(String.class);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test
 public void testGetInterceptorWithNullMock() {
 assertNull(mockUtil.getInterceptor(null));
 }

 @Test
 public void testGetMockName() {
 Object mock = mock(String.class);
 String mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 }

 @Test
 public void testGetMockNameWithNullMock() {
 assertNull(mockUtil.getMockName(null));
 }

 @Test
 public void testCreateMockWithCustomName() {
 Class<?> clazz = Mockito.class;
 String customName = "MyMockitoMock";
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setMockName(customName);
 Object mock = mockUtil.createMock(clazz, settings);
 assertInstanceOf(clazz, mock);
 assertEquals(customName, mockUtil.getMockName(mock).getName());
 }

 @Test
 public void testCreateProxyMock() {
 Class<?> interfaceClass = String[].class;
 Object mock = Proxy.newProxyInstance(Mockito.class.getClassLoader(), new Class<?>[]{interfaceClass}, new MethodInterceptorFilter(mockCreationValidator));
 assertInstanceOf(interfaceClass, mock);
 }

 @Test
 public void testCreateProxyMockWithCustomName() {
 Class<?> interfaceClass = String[].class;
 String customName = "MyProxyMockitoMock";
 Object mock = Proxy.newProxyInstance(Mockito.class.getClassLoader(), new Class<?>[]{interfaceClass}, new MethodInterceptorFilter(mockCreationValidator, customName));
 assertInstanceOf(interfaceClass, mock);
 assertEquals(customName, mockUtil.getMockName(mock).getName());
 }

 @Test
 public void testCreateProxyMockWithExtraInterfaces() {
 Class<?> interfaceClass = String[].class;
 Class<?> extraInterfaceClass = RuntimeException.class;
 Object mock = Proxy.newProxyInstance(Mockito.class.getClassLoader(), new Class<?>[]{interfaceClass, extraInterfaceClass}, new MethodInterceptorFilter(mockCreationValidator));
 assertInstanceOf(interfaceClass, mock);
 assertInstanceOf(extraInterfaceClass, mock);
 }
}