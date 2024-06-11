package org.mockito.internal.util.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMockException;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.ArrayUtils;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;
import java.lang.reflect.Proxy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockSettingsImpl settings;
 private Class<TestClass> testClass;

 @Before
 public void setUp() {
 mockUtil = new MockUtil(new MockCreationValidator());
 settings = new MockSettingsImpl();
 testClass = new TestClass();
 }

 @Test
 public void testCreateMockHappyPath() {
 when(mockUtil.createMock(testClass, settings)).thenReturn(mock(TestClass.class));
 TestClass mock = mockUtil.createMock(testClass, settings);
 assertEquals(mock, mock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 settings.setExtraInterfaces(new Class[0]);
 mockUtil.createMock(Object.class, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidSpiedInstance() {
 settings.setSpiedInstance(new Object());
 mockUtil.createMock(TestClass.class, settings);
 }

 @Test
 public void testCreateMockWithSerializableTrue() {
 settings.setSerializable(true);
 settings.setExtraInterfaces(new Class[]{Serializable.class});
 TestClass mock = mockUtil.createMock(TestClass.class, settings);
 assertNotNull(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidExtraInterface() {
 settings.setExtraInterfaces(new Class[]{String.class});
 mockUtil.createMock(TestClass.class, settings);
 }

 @Test
 public void testResetMock() {
 TestClass mock = mock(TestClass.class);
 when(mockUtil.getMockHandler(mock)).thenReturn(mock(MockHandler.class));
 mockUtil.resetMock(mock);
 verify(mockUtil, times(1)).getMockHandler(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNullMock() {
 mockUtil.resetMock(null);
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNonMockitoMock() {
 TestClass mock = new TestClass();
 mockUtil.resetMock(mock);
 }

 @Test
 public void testGetMockHandler() {
 TestClass mock = mock(TestClass.class);
 when(mockUtil.getInterceptor(mock)).thenReturn(mock(MethodInterceptorFilter.class));
 MockHandlerInterface mockHandler = mockUtil.getMockHandler(mock);
 assertEquals(mockHandler, mockHandler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockitoMock() {
 TestClass mock = new TestClass();
 mockUtil.getMockHandler(mock);
 }

 @Test
 public void testIsMockitoMock() {
 TestClass mock = mock(TestClass.class);
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMockitoMockWithNullMock() {
 assertFalse(mockUtil.isMockitoMock(null));
 }

 @Test
 public void testIsMockitoMockWithNonMockitoMock() {
 TestClass mock = new TestClass();
 assertFalse(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMock() {
 TestClass mock = mock(TestClass.class);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockWithNullMock() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testGetInterceptor() {
 TestClass mock = mock(TestClass.class);
 when(mockUtil.getInterceptor(mock)).thenReturn(mock(MethodInterceptorFilter.class));
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertEquals(interceptor, interceptor);
 }

 @Test
 public void testGetInterceptorWithNullMock() {
 assertNull(mockUtil.getInterceptor(null));
 }

 @Test
 public void testGetInterceptorWithNonMockitoMock() {
 TestClass mock = new TestClass();
 assertNull(mockUtil.getInterceptor(mock));
 }

 @Test
 public void testGetMockName() {
 TestClass mock = mock(TestClass.class);
 settings.setMockName("TestClassMock");
 when(mockUtil.getMockHandler(mock)).thenReturn(mock(MockHandler.class));
 when(mockUtil.getMockHandler(mock).getMockSettings()).thenReturn(settings);
 String mockName = mockUtil.getMockName(mock);
 assertEquals("TestClassMock", mockName);
 }

 @Test
 public void testGetMockNameWithNullMock() {
 assertNull(mockUtil.getMockName(null));
 }

 @Test
 public void testGetMockNameWithNonMockitoMock() {
 TestClass mock = new TestClass();
 assertNull(mockUtil.getMockName(mock));
 }

 private static class TestClass implements Serializable {
 }

}