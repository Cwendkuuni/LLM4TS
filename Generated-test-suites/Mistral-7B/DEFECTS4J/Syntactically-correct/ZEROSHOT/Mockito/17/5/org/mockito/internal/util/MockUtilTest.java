import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.mockito2.mock.MockitoMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Proxy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockSettingsImpl settings;
 private Object spiedInstance;

 @Before
 public void setup() {
 mockUtil = new MockUtil();
 settings = new MockSettingsImpl();
 spiedInstance = new Object() {};
 }

 @Test
 public void testCreateMock() {
 Class<?> clazz = Object.class;
 Object mock = mockUtil.createMock(clazz, settings);
 assertNotNull(mock);
 verify(mockUtil, times(1)).validateType(clazz);
 verify(mockUtil, times(1)).validateExtraInterfaces(clazz, (Class[]) eq(new Class[0]));
 verify(mockUtil, times(1)).validateMockedType(clazz, spiedInstance);
 verifyNoMoreInteractions(mockUtil);

 Type mockType = mock.getClass();
 assertTrue(Proxy.isProxyClass(mockType));
 assertFalse(Mockito.isMock(mock));
 assertEquals(clazz, Proxy.getProxyClass(mockType).getInterfaces()[0]);
 assertEquals(settings.getMockName(mock), mockUtil.getMockName(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, settings);
 fail("Should throw NotAMockException with message: Argument should not be null!");
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSpiedInstance() {
 mockUtil.createMock(Object.class, null);
 fail("Should throw NotAMockException with message: Argument should not be null!");
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<?> clazz = Object.class;
 Class<?> extraInterface = Mockito.class;
 settings.setExtraInterfaces(new Class[] { extraInterface });
 Object mock = mockUtil.createMock(clazz, settings);
 assertNotNull(mock);
 verify(mockUtil, times(1)).validateType(clazz);
 verify(mockUtil, times(1)).validateExtraInterfaces(clazz, (Class[]) eq(new Class[] { extraInterface }));
 verifyNoMoreInteractions(mockUtil);

 Type mockType = mock.getClass();
 assertTrue(Proxy.isProxyClass(mockType));
 assertFalse(Mockito.isMock(mock));
 assertEquals(clazz, Proxy.getProxyClass(mockType).getInterfaces()[0]);
 assertEquals(clazz.getName() + " $", mockUtil.getMockName(mock).getName());
 }

 @Test
 public void testResetMock() {
 Object mock = mockUtil.createMock(Object.class, settings);
 mockUtil.resetMock(mock);
 MockHandlerInterface mockHandler = mockUtil.getMockHandler(mock);
 assertNotNull(mockHandler);
 Answer answer = mockHandler.getAnswers().get(0);
 assertEquals(Answer.RETURN_DEFAULTS, answer);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 fail("Should throw NotAMockException with message: Argument should be a mock, but is null!");
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithMockitoMockButNotMockitoMock2() {
 @MockitoMock Object mockitoMock = mock(Object.class);
 MockHandlerInterface mockHandler = mockUtil.getMockHandler(mockitoMock);
 fail("Should throw NotAMockException with message: Argument should be a mock, but is: " +
 MockitoMock.class.getName());
 }

 @Test
 public void testIsMock() {
 assertTrue(mockUtil.isMock(mockUtil.createMock(Object.class, settings)));
 }

 @Test
 public void testIsMockWithNull() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testIsMockWithNonMockitoMock() {
 Object nonMockitoMock = new Object() {};
 assertFalse(mockUtil.isMock(nonMockitoMock));
 }

 @Test
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 fail("Should throw NotAMockException with message: Argument should be a mock, but is null!");
 }

 @Test
 public void testGetInterceptorWithMockitoMockButNotMockitoMock2() {
 Object mockitoMock = mockUtil.createMock(Object.class, settings);
 assertNull(mockUtil.getInterceptor(mockitoMock));
 }

 @Test
 public void testGetMockNameWithNullMock() {
 mockUtil.getMockName(null);
 fail("Should throw NotAMockException with message: Argument should be a mock, but is null!");
 }

 @Test
 public void testGetMockNameWithMockitoMock() {
 Object mockitoMock = mockUtil.createMock(Object.class, settings);
 String mockName = mockUtil.getMockName(mockitoMock);
 assertNotNull(mockName);
 assertEquals(mockName.getName(), "mockitoMock" + System.identityHashCode(mockitoMock));
 }

 @Test
 public void testLenientCopyTool() {
 Object spiedInstance = new Object() {};
 Object mock = mockUtil.createMock(Object.class, settings);
 LenientCopyTool lenientCopyTool = new LenientCopyTool();
 lenientCopyTool.copyToMock(spiedInstance, mock);
 assertEquals(spiedInstance.hashCode(), mock.hashCode());
 }
}