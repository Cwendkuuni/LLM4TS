package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Test;
import org.mockito.internal.MockCreationValidator;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.lang.reflect.Proxy;

public class MockUtilTest {

 private MockCreationValidator creationValidator;
 private MockSettingsImpl settings;

 @Before
 public void setUp() {
 creationValidator = new MockCreationValidator();
 settings = new MockSettingsImpl();
 }

 @Test
 public void testCreateMock() {
 // Test creating a mock for a simple class
 Class<DummyClass> dummyClass = DummyClass.class;
 T mock = MockUtil.createMock(dummyClass, settings);
 assertNotNull(mock);
 assertInstanceOf(DummyClass.class, mock);

 // Test creating a mock for a class with interfaces
 Class<DummyInterface> dummyInterface = DummyInterface.class;
 Class<?>[] extraInterfaces = new Class[]{DummyOtherInterface.class};
 settings.setExtraInterfaces(extraInterfaces);
 T mockWithInterfaces = MockUtil.createMock(dummyClass, settings);
 assertNotNull(mockWithInterfaces);
 assertInstanceOf(DummyClass.class, mockWithInterfaces);
 assertInstanceOf(DummyOtherInterface.class, mockWithInterfaces);

 // Test creating a mock for a serializable class
 DummySerializable dummySerializable = spy(new DummySerializable());
 settings.setSerializable(true);
 T mockSerializable = MockUtil.createMock(DummySerializable.class, settings);
 assertNotNull(mockSerializable);
 assertInstanceOf(DummySerializable.class, mockSerializable);

 // Test creating a mock for a spied instance
 DummyClass spiedInstance = new DummyClass();
 T mockWithSpiedInstance = MockUtil.createMock(DummyClass.class, settings);
 assertNotNull(mockWithSpiedInstance);
 assertInstanceOf(DummyClass.class, mockWithSpiedInstance);
 LenientCopyTool.copyToMock(spiedInstance, mockWithSpiedInstance);
 }

 @Test(expected = NotAMockException.class)
 public void testInvalidType() {
 MockUtil mockUtil = new MockUtil();
 Class<String> stringClass = String.class;
 mockUtil.createMock(stringClass, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testInvalidExtraInterfaces() {
 MockUtil mockUtil = new MockUtil();
 Class<DummyClass> dummyClass = DummyClass.class;
 Class<Number> numberClass = Number.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[]{numberClass});
 mockUtil.createMock(dummyClass, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testMockedTypeWithSpiedInstance() {
 MockUtil mockUtil = new MockUtil();
 Class<String> stringClass = String.class;
 String spiedInstance = "test";
 mockUtil.createMock(stringClass, settings);
 settings.setSpiedInstance(spiedInstance);
 mockUtil.createMock(stringClass, settings);
 }

 @Test
 public void testResetMock() {
 DummyClass mock = mock(DummyClass.class);
 MockUtil mockUtil = new MockUtil();
 mockUtil.resetMock(mock);
 assertNull(getInterceptor(mock));
 }

 @Test
 public void testGetInterceptor() {
 DummyClass dummyClass = mock(DummyClass.class);
 Factory factory = (Factory) Proxy.getInvocationHandler(dummyClass);
 MethodInterceptorFilter interceptor = MockUtil.getInterceptor(dummyClass);
 assertEquals(interceptor, MockUtil.getInterceptor(factory));
 }

 @Test
 public void testIsMock() {
 Object dummyObject = mock(DummyClass.class);
 assertFalse(MockUtil.isMock(dummyObject));
 DummyClass mock = mock(DummyClass.class);
 assertTrue(MockUtil.isMock(mock));
 }

 @Test
 public void testGetMockName() {
 DummyClass mock = mock(DummyClass.class);
 String mockName = MockUtil.getMockName(mock);
 assertNotNull(mockName);
 assertEquals("DummyClass", mockName.getMockName());
 }

 private MethodInterceptorFilter getInterceptor(Object mock) {
 return MockUtil.getInterceptor((Factory) mock);
 }
}

class DummyClass implements Serializable {
}

interface DummyInterface {
}

interface DummyOtherInterface {
}

class DummySerializable implements Serializable {
}