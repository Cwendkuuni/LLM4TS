package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.lang.reflect.Proxy;
import java.util.Arrays;

import static java.util.Arrays.asList;
import static java.util.Arrays.forAll;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;

public class MockUtilTest {

 private final MockCreationValidator creationValidator = mock(MockCreationValidator.class);
 private final MockSettingsImpl settings = new MockSettingsImpl();

 @Test
 public void testCreateMockWithValidArguments() {
 MockUtil mockUtil = new MockUtil();

 Class<String> classToMock = String.class;
 Class<?>[] extraInterfaces = new Class[0];

 MockSettingsImpl settingsWithName = new MockSettingsImpl();
 settingsWithName.initiateMockName(classToMock);
 settingsWithName.setMockName("someName");
 settingsWithName.setExtraInterfaces(extraInterfaces);

 T mock = mockUtil.createMock(classToMock, settingsWithName);

 Assert.assertNotNull(mock);
 Assert.assertEquals(classToMock.getName(), mock.getClass().getName());
 Assert.assertSame(settingsWithName, mockUtil.getMockHandler(mock).getMockSettings());
 }

 @Test
 public void testCreateMockWithValidArgumentsAndExtraInterfaces() {
 MockUtil mockUtil = new MockUtil();

 Class<String> classToMock = String.class;
 Class<?>[] extraInterfaces = new Class[]{Serializable.class};

 MockSettingsImpl settingsWithNameAndExtraInterfaces = new MockSettingsImpl();
 settingsWithNameAndExtraInterfaces.initiateMockName(classToMock);
 settingsWithNameAndExtraInterfaces.setMockName("someName");
 settingsWithNameAndExtraInterfaces.setExtraInterfaces(extraInterfaces);

 T mock = mockUtil.createMock(classToMock, settingsWithNameAndExtraInterfaces);

 Assert.assertNotNull(mock);
 Assert.assertEquals(classToMock.getName(), mock.getClass().getName());
 Assert.assertEquals(asList(Serializable.class), Arrays.asList(mock.getClass().getInterfaces()));
 Assert.assertSame(settingsWithNameAndExtraInterfaces, mockUtil.getMockHandler(mock).getMockSettings());
 }

 @Test
 public void testCreateMockWithNullClassToMock() {
 MockUtil mockUtil = new MockUtil();

 Assert.assertThrows(IllegalArgumentException.class, () -> mockUtil.createMock(null, settings));
 }

 @Test
 public void testCreateMockWithInvalidClassToMock() {
 MockUtil mockUtil = new MockUtil();

 Class<Integer> classToMock = Integer.class;

 Assert.assertThrows(IllegalArgumentException.class, () -> mockUtil.createMock(classToMock, settings));
 }

 @Test
 public void testCreateMockWithNullMockSettings() {
 MockUtil mockUtil = new MockUtil();

 Assert.assertThrows(NullPointerException.class, () -> mockUtil.createMock(String.class, null));
 }

 @Test
 public void testCreateMockWithEmptyExtraInterfacesArray() {
 MockUtil mockUtil = new MockUtil();

 Class<String> classToMock = String.class;

 MockSettingsImpl settingsWithEmptyExtraInterfaces = new MockSettingsImpl();
 settingsWithEmptyExtraInterfaces.initiateMockName(classToMock);
 settingsWithEmptyExtraInterfaces.setMockName("someName");
 settingsWithEmptyExtraInterfaces.setExtraInterfaces(new Class<?>[0]);

 T mock = mockUtil.createMock(classToMock, settingsWithEmptyExtraInterfaces);

 Assert.assertNotNull(mock);
 Assert.assertEquals(classToMock.getName(), mock.getClass().getName());
 Assert.assertSame(settingsWithEmptyExtraInterfaces, mockUtil.getMockHandler(mock).getMockSettings());
 }

 @Test
 public void testCreateMockWithValidSettingsAndSpiedInstance() {
 MockUtil mockUtil = new MockUtil();

 Class<String> classToMock = String.class;
 Object spiedInstance = "mockito test";

 MockSettingsImpl settingsWithSpiedInstance = new MockSettingsImpl();
 settingsWithSpiedInstance.initiateMockName(classToMock);
 settingsWithSpiedInstance.setMockName("someName");
 settingsWithSpiedInstance.setSpiedInstance(spiedInstance);

 T mock = mockUtil.createMock(classToMock, settingsWithSpiedInstance);

 Assert.assertNotNull(mock);
 Assert.assertEquals(classToMock.getName(), mock.getClass().getName());
 Assert.assertEquals(spiedInstance, mockUtil.getMockHandler(mock).getMockSettings().getSpiedInstance());
 }

 @Test
 public void testCreateMockWithValidSettingsAndExtraInterfacesAndSpiedInstance() {
 MockUtil mockUtil = new MockUtil();

 Class<String> classToMock = String.class;
 Object spiedInstance = "mockito test";
 Class<?>[] extraInterfaces = new Class[]{Serializable.class};

 MockSettingsImpl settingsWithSpiedInstanceAndExtraInterfaces = new MockSettingsImpl();
 settingsWithSpiedInstanceAndExtraInterfaces.initiateMockName(classToMock);
 settingsWithSpiedInstanceAndExtraInterfaces.setMockName("someName");
 settingsWithSpiedInstanceAndExtraInterfaces.setExtraInterfaces(extraInterfaces);
 settingsWithSpiedInstanceAndExtraInterfaces.setSpiedInstance(spiedInstance);

 T mock = mockUtil.createMock(classToMock, settingsWithSpiedInstanceAndExtraInterfaces);

 Assert.assertNotNull(mock);
 Assert.assertEquals(classToMock.getName(), mock.getClass().getName());
 Assert.assertEquals(asList(Serializable.class), Arrays.asList(mock.getClass().getInterfaces()));
 Assert.assertEquals(spiedInstance, mockUtil.getMockHandler(mock).getMockSettings().getSpiedInstance());
 }

 @Test
 public void testResetMock() {
 MockUtil mockUtil = new MockUtil();

 Object mock = Mockito.mock(String.class);
 resetMock(mock);

 MockHandlerInterface<String> newMockHandler = mockUtil.getMockHandler(mock);
 Assert.assertNotNull(newMockHandler);
 }

 @Test
 public void testGetMockHandler() {
 MockUtil mockUtil = new MockUtil();

 Object mock = Mockito.mock(String.class);
 MockHandlerInterface<String> oldMockHandler = mockUtil.getMockHandler(mock);

 Assert.assertNotNull(oldMockHandler);
 }

 @Test
 public void testGetMockHandlerWithNullMock() {
 MockUtil mockUtil = new MockUtil();

 Assert.assertNull(mockUtil.getMockHandler(null));
 }

 @Test
 public void testIsMockitoMockWithMockitoMock() {
 MockUtil mockUtil = new MockUtil();

 Object mockitoMock = mock(String.class);

 Assert.assertTrue(mockUtil.isMockitoMock(mockitoMock));
 }

 @Test
 public void testIsMockitoMockWithNotAMock() {
 MockUtil mockUtil = new MockUtil();

 Object notAMock = new Object();

 Assert.assertFalse(mockUtil.isMockitoMock(notAMock));
 }

 @Test
 public void testIsMockitoMockWithNullObject() {
 MockUtil mockUtil = new MockUtil();

 Assert.assertFalse(mockUtil.isMockitoMock(null));
 }

 @Test
 public void testIsMockWithMockitoMock() {
 MockUtil mockUtil = new MockUtil();

 Object mockitoMock = Mockito.mock(String.class);

 Assert.assertTrue(mockUtil.isMock(mockitoMock));
 }

 @Test
 public void testIsMockWithNotAMock() {
 MockUtil mockUtil = new MockUtil();

 Object notAMock = new Object();

 Assert.assertFalse(mockUtil.isMock(notAMock));
 }

 @Test
 public void testIsMockWithNullObject() {
 MockUtil mockUtil = new MockUtil();

 Assert.assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testGetInterceptorWithMockitoMock() {
 MockUtil mockUtil = new MockUtil();

 Object mockitoMock = Mockito.mock(String.class);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mockitoMock);

 Assert.assertNotNull(interceptor);
 }

 @Test
 public void testGetInterceptorWithNullObject() {
 MockUtil mockUtil = new MockUtil();

 Assert.assertNull(mockUtil.getInterceptor(null));
 }

 @Test
 public void testGetMockNameWithMockitoMock() {
 MockUtil mockUtil = new MockUtil();

 Object mockitoMock = Mockito.mock(String.class);

 MockName mockName = mockUtil.getMockName(mockitoMock);

 Assert.assertNotNull(mockName);
 Assert.assertEquals("someName", mockName.getName());
 }

 @Test
 public void testGetMockNameWithNullObject() {
 MockUtil mockUtil = new MockUtil();

 Assert.assertNull(mockUtil.getMockName(null));
 }
}