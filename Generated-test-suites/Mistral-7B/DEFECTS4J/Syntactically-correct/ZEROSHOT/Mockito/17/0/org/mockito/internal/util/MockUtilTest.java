import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockitoConstants;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.mockito2.stub.Stub;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class MockUtilTest {

 private MockUtil mockUtil;
 private ClassImposterizer classImposterizer;
 private MockSettingsImpl mockSettings;
 private MockHandler<Object> mockHandler;

 @Before
 public void setUp() {
 mockUtil = new MockUtil();
 classImposterizer = Mockito.spy(ClassImposterizer.class);
 mockSettings = new MockSettingsImpl();
 mockHandler = Mockito.mock(MockHandler.class);
 }

 @Test
 public void testCreateMock() throws Exception {
 // Test valid usage
 Class<Object> classToMock = Object.class;
 Object spiedInstance = new Object();
 mockSettings.setExtraInterfaces(new Class[0]);
 Object mock = mockUtil.createMock(classToMock, mockSettings);
 Assert.assertNotNull(mock);
 Assert.assertFalse(mockUtil.isMock(spiedInstance));
 Assert.assertTrue(mockUtil.isMock(mock));

 // Test invalid usage - null class
 Assert.assertNull(mockUtil.createMock(null, mockSettings));

 // Test invalid usage - null settings
 Assert.assertNull(mockUtil.createMock(classToMock, null));

 // Test invalid usage - invalid class
 Class<String> invalidClass = String.class;
 assertThrows(IllegalArgumentException.class, () -> mockUtil.createMock(invalidClass, mockSettings));

 // Test extra interfaces
 Class<Printable> printable = Printable.class;
 mockSettings.setExtraInterfaces(new Class[]{printable});
 Object printableMock = mockUtil.createMock(Object.class, mockSettings);
 Mockito.verify(classImposterizer).imposterise(ArgumentMatchers.any(MethodInterceptorFilter.class), ArgumentMatchers.eq(Object.class), ArgumentMatchers.array(printable));

 // Test spied instance
 Object spiedInstance1 = new Object();
 Object mock1 = mockUtil.createMock(Object.class, mockSettings);
 mockSettings.setSpiedInstance(spiedInstance1);
 Object mock2 = mockUtil.createMock(Object.class, mockSettings);
 new LenientCopyTool().copyToMock(spiedInstance1, mock1);
 Assert.assertEquals(spiedInstance1, mock1);
 Assert.assertNotEquals(spiedInstance1, mock2);
 }

 @Test
 public void testResetMock() {
 Object mock = Mockito.mock(Object.class);
 mockUtil.resetMock(mock);
 Mockito.verify(mockHandler, Mockito.times(1)).reset();
 }

 @Test
 public void testGetMockHandler() {
 Object mock = Mockito.mock(Object.class);
 Mockito.when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
 Assert.assertEquals(mockHandler, mockUtil.getMockHandler(mock));
 }

 @Test
 public void testIsMock() {
 Assert.assertFalse(mockUtil.isMock(null));
 Assert.assertFalse(mockUtil.isMock(new Object()));
 Object mock = Mockito.mock(Object.class);
 Assert.assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockitoMock() {
 Object mock = Mockito.mock(Object.class);
 Assert.assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testGetInterceptor() {
 Object mock = Mockito.mock(Object.class);
 Mockito.when(mockUtil.getInterceptor(mock)).thenReturn(new InvocationHandler() {
 @Override
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 Stub stub = Stub.returnDefault();
 Proxy.setInvocationHandler(mock, stub);
 return null;
 }
 });
 Object interceptor = mockUtil.getInterceptor(mock);
 Assert.assertEquals(interceptor, mockUtil.getInterceptor(mock));
 }

 @Test
 public void testGetMockName() {
 Object mock = Mockito.mock(Object.class);
 Mockito.when(mockUtil.getMockName(mock)).thenReturn(MockitoConstants.MOCK_NAME_PREFIX + "123");
 Assert.assertEquals("123", mockUtil.getMockName(mock).getName());
 }
}