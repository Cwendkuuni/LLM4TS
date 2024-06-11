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
import org.mockito.Mockito;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class MockUtilTest {

 private MockUtil mockUtil;

 @Test
 public void testCreateMock() {
 MockSettingsImpl settings = new MockSettingsImpl();
 Class<String> classToMock = String.class;
 String expectedMockName = "mockString";
 settings.initiateMockName(classToMock);
 settings.setName(expectedMockName);
 Object spiedInstance = "spiedInstance";

 mockUtil = new MockUtil();
 T mock = mockUtil.createMock(classToMock, settings);
 assertEquals(expectedMockName, mockUtil.getMockName(mock));
 assertNotNull(mock);
 assertEquals(spiedInstance, settings.getSpiedInstance());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClassToMock() {
 mockUtil = new MockUtil();
 mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidType() {
 mockUtil = new MockUtil();
 Class<Integer> classToMock = Integer.class;
 mockUtil.createMock(classToMock, new MockSettingsImpl());
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<String> classToMock = String.class;
 Class<Serializable> serializable = Serializable.class;
 Class<String>[] extraInterfaces = {serializable};
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(extraInterfaces);
 Object spiedInstance = "spiedInstance";

 mockUtil = new MockUtil();
 T mock = mockUtil.createMock(classToMock, settings);
 assertNotNull(mock);
 assertArrayEquals(new Class[]{serializable, String.class}, mock.getClass().getInterfaces());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithMockedTypeAndSpiedInstance() {
 Class<String> classToMock = String.class;
 Object spiedInstance = "spiedInstance";
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);

 mockUtil = new MockUtil();
 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithMockedTypeNullSpiedInstance() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(null);

 mockUtil = new MockUtil();
 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testResetMock() {
 Class<String> classToMock = String.class;
 Object spiedInstance = "spiedInstance";
 String expectedMockName = "mockString";
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.initiateMockName(classToMock);
 settings.setName(expectedMockName);
 settings.setSpiedInstance(spiedInstance);

 Factory mock = mock(Factory.class);
 when(mock.getCallback(0)).thenReturn(mock(MethodInterceptorFilter.class));
 when(mock.getHandler()).thenReturn(mock(MockHandler.class));

 Object mockObject = mockUtil.createMock(classToMock, settings);
 assertEquals(expectedMockName, mockUtil.getMockName(mockObject));
 assertEquals(spiedInstance, settings.getSpiedInstance());

 mockUtil.resetMock(mockObject);
 assertNotNull(mockUtil.getInterceptor(mockObject));
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNullMock() {
 mockUtil.resetMock(null);
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNonMockObject() {
 Object object = new Object();
 mockUtil.resetMock(object);
 }

 @Test
 public void testIsMock() {
 Factory mockFactory = mock(Factory.class);
 when(mockFactory.getHandler()).thenReturn(mock(MockHandler.class));
 when(mockFactory.getCallback(0)).thenReturn(mock(Callback.class));
 when(mockFactory.isMock()).thenReturn(true);
 Object mockObject = mockUtil.createMock(Object.class, new MockSettingsImpl());
 assertTrue(mockUtil.isMock(mockObject));
 }

 @Test
 public void testIsMockFalse() {
 Object object = new Object();
 assertFalse(mockUtil.isMock(object));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockObject() {
 Object object = new Object();
 mockUtil.getMockHandler(object);
 }

 @Test
 public void testGetMockHandlerWithMockObject() {
 Factory mockFactory = mock(Factory.class);
 when(mockFactory.getHandler()).thenReturn(mock(MockHandler.class));
 when(mockFactory.getCallback(0)).thenReturn(mock(Callback.class));
 when(mockFactory.isMock()).thenReturn(true);
 Object mockObject = mockUtil.createMock(Object.class, new MockSettingsImpl());
 MockHandlerInterface mockHandler = mockUtil.getMockHandler(mockObject);
 assertNotNull(mockHandler);
 }

 @Test
 public void testGetInterceptorWithMockObject() {
 Factory mockFactory = mock(Factory.class);
 when(mockFactory.getHandler()).thenReturn(mock(MockHandler.class));
 when(mockFactory.getCallback(0)).thenReturn(mock(Callback.class));
 when(mockFactory.isMock()).thenReturn(true);
 Object mockObject = mockUtil.createMock(Object.class, new MockSettingsImpl());
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mockObject);
 assertNotNull(interceptor);
 }

 @Test
 public void testGetInterceptorWithNonMockObject() {
 Object object = new Object();
 assertNull(mockUtil.getInterceptor(object));
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 }

 @Test
 public void testGetMockNameWithMockObject() {
 Factory mockFactory = mock(Factory.class);
 when(mockFactory.getHandler()).thenReturn(mock(MockHandler.class));
 when(mockFactory.getCallback(0)).thenReturn(mock(Callback.class));
 when(mockFactory.isMock()).thenReturn(true);
 Object mockObject = mockUtil.createMock(Object.class, new MockSettingsImpl());
 MockName mockName = mockUtil.getMockName(mockObject);
 assertNotNull(mockName);
 }

 @Test
 public void testGetMockNameWithNullMock() {
 mockUtil.getMockName(null);
 }
}