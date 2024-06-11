package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockCreationValidator;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;
import java.lang.reflect.Proxy;

public class MockUtilTest {

 private MockUtil util;
 private MockCreationValidator creationValidatorMock;

 @Before
 public void setUp() {
 creationValidatorMock = mock(MockCreationValidator.class);
 util = new MockUtil(creationValidatorMock);
 }

 @Test
 public void testCreateMock() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object spiedInstance = spy(classToMock);

 util.createMock(classToMock, settings);

 assertNotNull(settings.getMockName());
 assertNotNull(settings.getHandler());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClassToMock() {
 util.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClassToMock() {
 Class<String> classToMock = String.class;
 util.createMock(classToMock, new MockSettingsImpl());
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[] {Serializable.class});
 Object spiedInstance = spy(classToMock);

 util.createMock(classToMock, settings);

 assertNotNull(settings.getMockName());
 assertNotNull(settings.getHandler());
 verify(creationValidatorMock).validateType(classToMock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidExtraInterface() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[] {Integer.class});
 Object spiedInstance = spy(classToMock);

 util.createMock(classToMock, settings);
 }

 @Test
 public void testCreateMockWithSpiedInstance() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object spiedInstance = spy(classToMock);
 settings.setSpiedInstance(spiedInstance);

 Object mock = util.createMock(classToMock, settings);

 assertNotNull(mock);
 new LenientCopyTool().copyToMock(spiedInstance, mock);
 }

 @Test
 public void testCreateMockWithoutSpiedInstance() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();

 Object mock = util.createMock(classToMock, settings);

 assertNotNull(mock);
 verifyNoInteractions(settings.getHandler());
 }

 @Test
 public void testResetMock() {
 Object mock = mock(Object.class);
 util.resetMock(mock);

 MockHandlerInterface mockHandler = util.getMockHandler(mock);
 verify(mockHandler).reset();
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNullMock() {
 util.resetMock(null);
 }

 @Test
 public void testGetMockHandler() {
 Object mock = mock(Object.class);
 Object handler = util.getMockHandler(mock);

 assertNotNull(handler);
 assertNotNull(handler.getMockSettings());
 assertNotNull(handler.getMockName());
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 util.getMockHandler(null);
 }

 @Test
 public void testIsMockitoMock() {
 Object mock = mock(Object.class);
 assertTrue(util.isMockitoMock(mock));
 }

 @Test
 public void testIsMockitoMockWithNullMock() {
 assertFalse(util.isMockitoMock(null));
 }

 @Test
 public void testIsMockitoMockWithNonMockitoMock() {
 Object mock = new Object() {};
 assertFalse(util.isMockitoMock(mock));
 }

 @Test
 public void testIsMock() {
 Object mock = mock(Object.class);
 assertTrue(util.isMock(mock));
 }

 @Test
 public void testIsMockWithNullMock() {
 assertFalse(util.isMock(null));
 }

 @Test
 public void testGetInterceptor() {
 Object mock = mock(Factory.class);
 Factory factoryMock = (Factory) Proxy.getInvocationHandler(mock);
 MethodInterceptorFilter methodInterceptorFilter = new MethodInterceptorFilter(new InvocationNotifierHandler<>(new MockHandler<>(new MockSettingsImpl())), new MockSettingsImpl());
 Proxy.setInvocationHandler(mock, methodInterceptorFilter);

 MethodInterceptorFilter interceptorFilter = util.getInterceptor(mock);
 assertEquals(methodInterceptorFilter, interceptorFilter);
 }

 @Test
 public void testGetInterceptorWithNullMock() {
 assertNull(util.getInterceptor(null));
 }

 @Test
 public void testGetMockNameWithNullMock() {
 assertNull(util.getMockName(null));
 }

}