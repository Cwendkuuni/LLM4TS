package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

 private MockCreationValidator creationValidator;
 private MockUtil mockUtil;
 private Stubber stubber;
 private Class<String> stringClass;
 private Object spiedInstance;
 private MockSettingsImpl settings;

 @Before
 public void setup() {
 creationValidator = Mockito.mock(MockCreationValidator.class);
 mockUtil = new MockUtil(creationValidator);
 stubber = Mockito.stub(Stubber.class);
 stringClass = String.class;
 spiedInstance = "mockito mock";
 settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);
 }

 @Test
 public void testCreateMock() {
 // Test creating a mock with no extra interfaces
 Object mock1 = mockUtil.createMock(stringClass, settings);
 assertNotNull(mock1);

 // Test creating a mock with extra interfaces
 Class<?>[] extraInterfaces = new Class[]{Serializable.class};
 settings.setExtraInterfaces(extraInterfaces);
 Object mock2 = mockUtil.createMock(stringClass, settings);
 assertNotNull(mock2);

 // Test creating a mock with a spied instance
 Object spiedMock = spy(mock1);
 settings.setSpiedInstance(spiedMock);
 Object mock3 = mockUtil.createMock(stringClass, settings);
 assertNotNull(mock3);

 // Test that the spied instance is copied to the mock
 verify(spiedMock).toString();
 }

 @Test
 public void testResetMock() {
 Object mock = mockUtil.createMock(stringClass, settings);
 mockUtil.resetMock(mock);
 verify(mock, Mockito.never()).toString();
 }

 @Test
 public void testGetMockHandler() {
 Object mock = mockUtil.createMock(stringClass, settings);
 MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);
 assertNotNull(mockHandler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test
 public void testIsMockitoMock() {
 Object mock = mockUtil.createMock(stringClass, settings);
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testIsMockitoMockWithNullMock() {
 mockUtil.isMockitoMock(null);
 }

 @Test
 public void testIsMockWithNullMock() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testIsMock() {
 Object mock = mockUtil.createMock(stringClass, settings);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptorWithMock() {
 Object mock = mockUtil.createMock(stringClass, settings);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test(expected = NullPointerException.class)
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 }

 @Test
 public void testGetMockName() {
 Object mock = mockUtil.createMock(stringClass, settings);
 String mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 }
}