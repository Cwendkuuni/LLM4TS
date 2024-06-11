package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl.MockName;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.lang.reflect.Proxy;

public class MockUtilTest {

 private MockUtil mockUtil;

 @Before
 public void setup() {
 mockUtil = new MockUtil();
 }

 @Test
 public void testCreateMock() {
 // Test creating a mock with no extra interfaces or spied instance
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String mock = mockUtil.createMock(stringClass, settings);
 assertNotNull(mock);
 assertInstanceOf(String.class, mock);

 // Test creating a mock with extra interfaces
 Class<String> stringClassWithInterface = String.class;
 Class<PrintStream> printStreamClass = System.out.getClass();
 MockSettingsImpl settingsWithInterface = new MockSettingsImpl();
 settingsWithInterface.setExtraInterfaces(new Class[] { printStreamClass });
 String mockWithInterface = mockUtil.createMock(stringClassWithInterface, settingsWithInterface);
 assertNotNull(mockWithInterface);
 assertInstanceOf(String.class, mockWithInterface);
 assertInstanceOf(PrintStream.class, mockWithInterface);

 // Test creating a mock with a spied instance
 String spiedString = "mock string";
 MockSettingsImpl settingsWithSpiedInstance = new MockSettingsImpl();
 settingsWithSpiedInstance.setSpiedInstance(spiedString);
 String mockWithSpiedInstance = mockUtil.createMock(stringClass, settingsWithSpiedInstance);
 assertNotNull(mockWithSpiedInstance);
 assertInstanceOf(String.class, mockWithSpiedInstance);
 assertEquals(spiedString, mockWithSpiedInstance);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 mockUtil.createMock(Object[].class, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSettings() {
 mockUtil.createMock(String.class, null);
 }

 @Test
 public void testResetMock() {
 String mockString = mock(String.class);
 mockUtil.resetMock(mockString);
 verifyNoMoreInteractions(mockString);
 }

 @Test
 public void testGetMockHandler() {
 String mockString = mock(String.class);
 MockHandler<String> mockHandler = mockUtil.getMockHandler(mockString);
 assertNotNull(mockHandler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test
 public void testIsMockitoMock() {
 String mockString = mock(String.class);
 assertTrue(mockUtil.isMockitoMock(mockString));
 }

 @Test
 public void testIsMockWithNullMock() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testIsMockWithNonMockitoMock() {
 Object nonMockitoMock = proxy(Mockito.mock(String.class));
 assertFalse(mockUtil.isMockitoMock(nonMockitoMock));
 }

 @Test
 public void testGetInterceptorWithMock() {
 String mockString = mock(String.class);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mockString);
 assertNotNull(interceptor);
 }

 @Test
 public void testGetInterceptorWithNonMockitoMock() {
 Object nonMockitoMock = proxy(Mockito.mock(String.class));
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(nonMockitoMock);
 assertNull(interceptor);
 }

 @Test
 public void testGetMockNameWithMock() {
 String mockString = mock(String.class);
 MockName mockName = mockUtil.getMockName(mockString);
 assertNotNull(mockName);
 assertEquals("mockString", mockName.getMockName());
 }

 @Test
 public void testGetMockNameWithNullMock() {
 assertNull(mockUtil.getMockName(null));
 }
}