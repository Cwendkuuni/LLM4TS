import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.creation.CreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.mockito2.Mockito;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import java.lang.reflect.Proxy;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MockUtilTest {

 private MockUtil mockUtil;
 private CreationValidator creationValidatorMock;
 private MockSettingsImpl settingsMock;
 private ClassMockHandler classMockHandlerMock;
 private MethodInterceptorFilter methodInterceptorFilterMock;
 private Factory factoryMock;
 private Object spiedInstance;

 @Before
 public void setup() {
 creationValidatorMock = Mockito.mock(CreationValidator.class);
 settingsMock = new MockSettingsImpl();
 mockUtil = new MockUtil(creationValidatorMock);
 classMockHandlerMock = Mockito.mock(MockHandler.class);
 methodInterceptorFilterMock = new MethodInterceptorFilter(classMockHandlerMock, settingsMock);
 factoryMock = Mockito.mock(Factory.class);
 spiedInstance = Mockito.mock(Object.class);
 settingsMock.setSpiedInstance(spiedInstance);
 }

 @Test
 public void testCreateMockWithValidParameters() {
 Class<Object> classToMock = Object.class;
 Object mock = mockUtil.createMock(classToMock, settingsMock);

 assertNotNull(mock);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 Class<Object> nullClass = null;
 mockUtil.createMock(nullClass, settingsMock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 Class<String> invalidClass = String.class;
 mockUtil.createMock(invalidClass, settingsMock);
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<Object> classToMock = Object.class;
 Class<?>[] extraInterfaces = {Integer.class};
 settingsMock.setExtraInterfaces(extraInterfaces);
 Object mock = mockUtil.createMock(classToMock, settingsMock);

 assertNotNull(mock);
 assertTrue(mockUtil.isMock(mock));
 PowerMockito.verifyNewCallable(()-> Proxy.getInterfaces(mock)[0]).thenAnswer(new Answer<Class<?>>() {
 @Override
 public Class<?> answer() throws Throwable {
 return Integer.class;
 }
 });
 }

 @Test
 public void testResetMock() {
 Object mock = Mockito.mock(Object.class);
 mockUtil.resetMock(mock);
 MethodInterceptorFilter newFilter = Mockito.mock(MethodInterceptorFilter.class);
 factoryMock.setCallback(0, newFilter);
 PowerMockito.verifyNew(MethodInterceptorFilter.class).withArguments(classMockHandlerMock, ArgumentMatchers.notNull());
 }

 @Test
 public void testGetMockHandler() {
 Object mock = Mockito.mock(Object.class);
 assertNull(mockUtil.getMockHandler(null));
 assertNotNull(mockUtil.getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithInvalidMock() {
 Object invalidMock = new Object();
 mockUtil.getMockHandler(invalidMock);
 }

 @Test
 public void testIsMockitoMock() {
 Object mock = Mockito.mock(Object.class);
 assertFalse(mockUtil.isMock(null));
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMock() {
 assertFalse(mockUtil.isMock(null));
 assertTrue(mockUtil.isMock(Mockito.mock(Object.class)));
 }

 @Test
 public void testGetInterceptor() {
 Object mock = Mockito.mock(Object.class);
 assertNull(mockUtil.getInterceptor(null));
 assertNotNull(mockUtil.getInterceptor(mock));
 }

 @Test
 public void testGetMockName() {
 Object mock = Mockito.mock(Object.class);
 String expectedName = "mock";
 settingsMock.setMockName(expectedName);
 assertEquals(expectedName, mockUtil.getMockName(mock));
 }

 @Test
 public void testCreateMockWithDefaultSettings() {
 Class<Object> classToMock = Object.class;
 Object mock = mockUtil.new MockUtil().createMock(classToMock);
 assertNotNull(mock);
 assertTrue(mockUtil.isMock(mock));
 }
}