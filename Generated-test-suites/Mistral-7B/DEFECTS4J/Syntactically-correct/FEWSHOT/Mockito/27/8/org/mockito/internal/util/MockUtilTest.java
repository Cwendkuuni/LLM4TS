package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class MockUtilTest {

 private MockUtil mockUtil;

 @Test
 public void testCreateMock() {
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[]{Serializable.class});
 settings.setSpiedInstance(new Object());
 mockUtil = new MockUtil(new MockCreationValidator());

 Class<Object> classToMock = Object.class;
 Object mock = mockUtil.createMock(classToMock, settings);
 assertNotNull(mock);
 assertNotNull(mockUtil.getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockInvalidType() {
 mockUtil = new MockUtil();
 Class<String> classToMock = String.class;
 mockUtil.createMock(classToMock, null);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockInvalidExtraInterfaces() {
 mockUtil = new MockUtil();
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[0]);
 settings.setSpiedInstance(new Object());
 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockInvalidSpiedInstance() {
 mockUtil = new MockUtil();
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[]{Serializable.class});
 settings.setSpiedInstance(null);
 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockInvalidMockType() {
 mockUtil = new MockUtil();
 Object mock = mock(Object.class);
 mockUtil.createMock(mock.getClass(), null);
 }

 @Test
 public void testResetMock() {
 Object mock = mock(Object.class);
 mockUtil = new MockUtil();
 mockUtil.resetMock(mock);
 verify(mock, never()).methodCall();
 }

 @Test
 public void testGetMockHandler() {
 Object mock = mock(Object.class);
 mockUtil = new MockUtil();
 assertEquals(mockUtil.getMockHandler(mock), mockitoMock(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerInvalidMock() {
 Object mock = new Object();
 mockUtil = new MockUtil();
 assertNull(mockUtil.getMockHandler(mock));
 }

 @Test
 public void testIsMock() {
 Object mock = mock(Object.class);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockFalse() {
 Object mock = new Object();
 assertFalse(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptor() {
 Object mock = mock(Object.class);
 Factory factory = mockitoMock(mock);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertEquals(interceptor, factory.getCallback(0));
 }

 @Test
 public void testGetInterceptorNull() {
 Object mock = new Object();
 Factory factory = mockitoMock(mock);
 assertNull(mockUtil.getInterceptor(mock));
 }

 @Test
 public void testGetMockName() {
 Object mock = mock(Object.class);
 MockHandler<Object> mockHandler = mockitoMock(mock);
 MockName mockName = mockUtil.getMockName(mock);
 assertEquals(mockHandler.getMockSettings().getMockName(), mockName);
 }

 @Test
 public void testGetMockNameNull() {
 Object mock = new Object();
 assertNull(mockUtil.getMockName(mock));
 }
}