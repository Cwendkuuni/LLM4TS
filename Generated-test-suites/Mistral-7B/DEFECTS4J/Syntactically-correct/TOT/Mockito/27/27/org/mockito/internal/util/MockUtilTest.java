package org.mockito.internal.util.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MockUtilTest {

 private MockUtil mockUtil;
 private Object spiedInstance;
 private Class<MyClass> clazzToMock;

 @Before
 public void setUp() {
 mockUtil = new MockUtil(new MockCreationValidator());
 spiedInstance = new Object() {};
 clazzToMock = MyClass.class;
 }

 @Test
 public void testCreateMockWithValidArguments() {
 MyClass mock = mockUtil.createMock(clazzToMock, new MockSettingsImpl());
 assertEquals(MyClass.class, mock.getClass());
 assertSame(spiedInstance, mockUtil.getMockHandler(mock).getMockName().getMock());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClassToMock() {
 mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidType() {
 mockUtil.createMock(String.class, new MockSettingsImpl());
 }

 @Test
 public void testCreateMockWithSerializableSetting() {
 MySerializableClass mock = mockUtil.createMock(MySerializableClass.class, new MockSettingsImpl());
 assertEquals(MySerializableClass.class, mock.getClass());
 assertSame(spiedInstance, mockUtil.getMockHandler(mock).getMockName().getMock());
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 MyExtraInterface mock = mockUtil.createMock(MyClassWithExtraInterfaces.class, new MockSettingsImpl(), MyExtraInterface.class);
 assertEquals(MyClassWithExtraInterfaces.class, mock.getClass());
 verify(mock, times(1)).myExtraMethod();
 }

 @Test
 public void testCreateMockWithSpiedInstance() {
 MyClass mock = mockUtil.createMock(clazzToMock, new MockSettingsImpl(), spiedInstance);
 assertEquals(spiedInstance, mockUtil.getMockHandler(mock).getMockName().getMock());
 }

 @Test
 public void testResetMock() {
 MyClass mock = mockUtil.createMock(clazzToMock, new MockSettingsImpl());
 mockUtil.resetMock(mock);
 assertNull(mockUtil.getInterceptor(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNullMock() {
 mockUtil.resetMock(null);
 }

 @Test
 public void testGetMockHandler() {
 MyClass mock = mockUtil.createMock(clazzToMock, new MockSettingsImpl());
 assertSame(mockUtil.getMockHandler(mock), mockUtil.getMockHandler(Proxy.getProxy(mock)));
 }

 @Test
 public void testIsMock() {
 assertTrue(mockUtil.isMock(mockUtil.createMock(MyClass.class, new MockSettingsImpl())));
 assertFalse(mockUtil.isMock(new Object()));
 }

 @Test
 public void testGetInterceptor() {
 MyClass mock = mockUtil.createMock(clazzToMock, new MockSettingsImpl());
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test
 public void testGetMockName() {
 MyClass mock = mockUtil.createMock(clazzToMock, new MockSettingsImpl());
 String mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 assertEquals("MockUtilTest$MockMyClass", mockName.getMockName());
 }

 @Test
 public void testGetMockNameWithSpiedInstance() {
 MyClass mock = mockUtil.createMock(clazzToMock, new MockSettingsImpl(), spiedInstance);
 String mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 assertEquals("MockUtilTest$MockMyClass_37188312", mockName.getMockName());
 }

 @Test
 public void testCreateMockWithSerializableTrueAndExtraInterfaces() {
 MyClassWithExtraInterfacesAndSerializable mock = mockUtil.createMock(MyClassWithExtraInterfacesAndSerializable.class, new MockSettingsImpl(), MyExtraInterface.class, true);
 assertEquals(MyClassWithExtraInterfacesAndSerializable.class, mock.getClass());
 verify(mock, times(1)).myExtraMethod();
 assertTrue(mock instanceof Serializable);
 }

 @Test
 public void testCreateMockWithMultipleExtraInterfaces() {
 MyClassWithMultipleExtraInterfaces mock = mockUtil.createMock(MyClassWithMultipleExtraInterfaces.class, new MockSettingsImpl(), MyExtraInterface1.class, MyExtraInterface2.class);
 assertEquals(MyClassWithMultipleExtraInterfaces.class, mock.getClass());
 verify(mock, times(1)).myMethod1();
 verify(mock, times(1)).myMethod2();
 }

 @Test
 public void testCreateMockWithMethodCallsInterception() {
 MyClassWithMethodCallsInterception mock = mockUtil.createMock(MyClassWithMethodCallsInterception.class, new MockSettingsImpl());
 when(mock.myMethod()).thenReturn("Mockito Mocked Method");
 assertEquals("Mockito Mocked Method", mock.myMethod());
 }

 @Test
 public void testCreateMockWithMethodCallsInterceptionWithArguments() {
 MyClassWithMethodCallsInterception mock = mockUtil.createMock(MyClassWithMethodCallsInterception.class, new MockSettingsImpl());
 when(mock.myMethod(ArgumentMatchers.anyString())).thenReturn("Mockito Mocked Method with Argument");
 assertEquals("Mockito Mocked Method with Argument", mock.myMethod("Test Argument"));
 }

 @Test
 public void testCreateMockWithDefaultValues() {
 MyClassWithDefaultValues mock = mockUtil.createMock(MyClassWithDefaultValues.class, new MockSettingsImpl());
 assertEquals(1, mock.myIntField);
 assertEquals("Default Value", mock.myStringField);
 }

 @Test
 public void testCreateMockWithCustomValues() {
 MyClassWithCustomValues mock = mockUtil.createMock(MyClassWithCustomValues.class, new MockSettingsImpl(), "Custom Value");
 assertEquals("Custom Value", mock.myStringField);
 }

 @Test
 public void testCreateMockWithCustomValuesAndExtraInterfaces() {
 MyClassWithCustomValuesAndExtraInterfaces mock = mockUtil.createMock(MyClassWithCustomValuesAndExtraInterfaces.class, new MockSettingsImpl(), "Custom Value", MyExtraInterface.class);
 assertEquals("Custom Value", mock.myStringField);
 verify(mock, times(1)).myExtraMethod();
 }

 @Test
 public void testCreateMockWithCustomValuesAndMethodCallsInterception() {
 MyClassWithCustomValuesAndMethodCallsInterception mock = mockUtil.createMock(MyClassWithCustomValuesAndMethodCallsInterception.class, new MockSettingsImpl(), "Custom Value");
 when(mock.myMethod()).thenReturn(42);
 assertEquals(42, mock.myMethod());
 }

 @Test
 public void testCreateMockWithCustomValuesAndMethodCallsInterceptionWithArguments() {
 MyClassWithCustomValuesAndMethodCallsInterception mock = mockUtil.createMock(MyClassWithCustomValuesAndMethodCallsInterception.class, new MockSettingsImpl(), "Custom Value");
 when(mock.myMethod(ArgumentMatchers.anyString())).thenReturn("Custom Value " + ArgumentMatchers.anyString());
 assertEquals("Custom Value Test Argument", mock.myMethod("Test Argument"));
 }
}