import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.CreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@PrepareForTest(MockUtil.class)
public class MockUtilTest {

 private CreationValidator creationValidator;
 private MockUtil mockUtil;
 private InvocationHandler mockHandler;
 private Object mockInstance;
 private Object spiedInstance;

 @Before
 public void setup() {
 creationValidator = mock(CreationValidator.class);
 mockUtil = new MockUtil(creationValidator);
 mockHandler = Mockito.mock(InvocationHandler.class);
 }

 @Test
 public void testCreateMockWithValidArguments() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl(classToMock, null);
 Object mock = mockUtil.createMock(classToMock, settings);
 assertNotNull(mock);
 Whitebox.invokeMethod(mock, "toString");
 }

 @Test(expected = AssertionError.class)
 public void testCreateMockWithInvalidType() {
 Class<Integer> classToMock = Integer.class;
 MockSettingsImpl settings = new MockSettingsImpl(classToMock, null);
 mockUtil.createMock(Object.class, settings);
 }

 @Test(expected = AssertionError.class)
 public void testCreateMockWithNullClassToMock() {
 MockSettingsImpl settings = new MockSettingsImpl(null, null);
 mockUtil.createMock(Mockito.mock(Class.class), settings);
 }

 @Test(expected = AssertionError.class)
 public void testCreateMockWithNullExtraInterfaces() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl(classToMock, null);
 mockUtil.createMock(classToMock, null);
 }

 @Test
 public void testCreateMockWithSpiedInstance() {
 Class<String> classToMock = String.class;
 String spiedInstance = "test";
 MockSettingsImpl settings = new MockSettingsImpl(classToMock, spiedInstance);
 Object mock = mockUtil.createMock(classToMock, settings);
 assertNotNull(mock);
 assertEquals(spiedInstance, Whitebox.getInternalState(mock, "spiedInstance"));
 }

 @Test
 public void testResetMock() {
 mockInstance = mockUtil.createMock(String.class, null);
 mockUtil.resetMock(mockInstance);
 assertNull(mockUtil.getInterceptor(mockInstance));
 }

 @Test
 public void testGetMockHandler() {
 mockInstance = mockUtil.createMock(String.class, null);
 MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mockInstance);
 assertNotNull(mockHandler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockitoMock() {
 Object nonMockitoMock = PowerMockito.mock(String.class);
 mockUtil.getMockHandler(nonMockitoMock);
 }

 @Test
 public void testIsMockitoMock() {
 mockInstance = mockUtil.createMock(String.class, null);
 assertTrue(mockUtil.isMockitoMock(mockInstance));
 }

 @Test
 public void testIsMockitoMockWithNullMock() {
 assertFalse(mockUtil.isMockitoMock(null));
 }

 @Test
 public void testIsMockitoMockWithNonMockitoMock() {
 Object nonMockitoMock = PowerMockito.mock(String.class);
 assertFalse(mockUtil.isMockitoMock(nonMockitoMock));
 }

 @Test
 public void testGetInterceptor() {
 mockInstance = mockUtil.createMock(String.class, null);
 MethodInterceptorFilter methodInterceptorFilter = mockUtil.getInterceptor(mockInstance);
 assertNotNull(methodInterceptorFilter);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNonMockitoMock() {
 Object nonMockitoMock = PowerMockito.mock(String.class);
 mockUtil.getInterceptor(nonMockitoMock);
 }

 @Test
 public void testGetMockName() {
 mockInstance = mockUtil.createMock(String.class, null);
 MockName mockName = mockUtil.getMockName(mockInstance);
 assertNotNull(mockName);
 assertEquals("Mockito stub for: String", mockName.getMockName());
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNullMock() {
 mockUtil.getMockName(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNonMockitoMock() {
 Object nonMockitoMock = PowerMockito.mock(String.class);
 mockUtil.getMockName(nonMockitoMock);
 }
}