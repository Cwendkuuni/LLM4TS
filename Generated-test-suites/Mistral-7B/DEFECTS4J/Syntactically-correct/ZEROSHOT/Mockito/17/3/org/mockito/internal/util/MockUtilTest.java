import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.CreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.powermock.reflect.WhiteboxImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class MockUtilTest {

 private MockUtil mockUtil;
 private CreationValidator creationValidatorMock;
 private Class<Object> mockClass;
 private Object spiedInstance;
 private Object mock;
 private MockHandler<Object> mockHandler;
 private MethodInterceptorFilter filter;
 private LenientCopyTool lenientCopyTool;

 @Before
 public void setUp() {
 creationValidatorMock = mock(CreationValidator.class);
 mockUtil = new MockUtil(creationValidatorMock);
 mockClass = Mockito.mock(Class.class);
 mockHandler = mock(MockHandler.class);
 lenientCopyTool = mock(LenientCopyTool.class);
 }

 @Test
 public void testCreateMock() throws Exception {
 assertNull(mock);

 when(creationValidatorMock.validateType(mockClass)).thenReturn(null);
 when(creationValidatorMock.validateExtraInterfaces(mockClass, anyArray())).thenReturn(new Class[0]);
 when(creationValidatorMock.validateMockedType(mockClass, spiedInstance)).thenReturn(null);

 mockUtil.createMock(mockClass, new MockSettingsImpl());

 verify(creationValidatorMock).validateType(mockClass);
 verify(creationValidatorMock).validateExtraInterfaces(mockClass, new Class[0]);
 verify(creationValidatorMock).validateMockedType(mockClass, spiedInstance);

 assertEquals(mockClass, WhiteboxImpl.getInternalState(mock));
 assertNotNull(getInterceptor(mock));

 lenientCopyTool.copyToMock(spiedInstance, mock);
 verify(lenientCopyTool).copyToMock(eq(spiedInstance), eq(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 assertNull(mockUtil.createMock(null, new MockSettingsImpl()));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 when(creationValidatorMock.validateType(mockClass)).thenThrow(new RuntimeException());
 mockUtil.createMock(mockClass, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithExtraInterfaces() {
 Class<Object> extraInterface = Mockito.mock(Class.class);
 when(creationValidatorMock.validateExtraInterfaces(mockClass, new Class[]{extraInterface})).thenThrow(new RuntimeException());
 mockUtil.createMock(mockClass, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithSpiedInstance() {
 when(creationValidatorMock.validateMockedType(mockClass, spiedInstance)).thenThrow(new RuntimeException());
 mockUtil.createMock(mockClass, new MockSettingsImpl());
 }

 @Test
 public void testResetMock() throws Exception {
 when(getInterceptor(mock).getHandler()).thenReturn(mockHandler);
 when(mockHandler.getMockName()).thenReturn("mockName");

 mockUtil.resetMock(mock);

 verify(mockHandler, times(1)).reset();
 assertEquals("mockName", getMockName(mock));
 }

 @Test
 public void testGetMockHandler() throws Exception {
 assertNull(mockUtil.getMockHandler(null));
 assertNull(mockUtil.getMockHandler(new Object()));

 Object mock = mock(Object.class);
 when(getInterceptor(mock).getHandler()).thenReturn(mockHandler);

 assertEquals(mockHandler, mockUtil.getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockitoMock() throws Exception {
 Object nonMockitoMock = new Object();
 when(getInterceptor(nonMockitoMock) == null).thenReturn(true);
 mockUtil.getMockHandler(nonMockitoMock);
 }

 @Test
 public void testIsMock() throws Exception {
 assertFalse(mockUtil.isMock(null));
 assertFalse(mockUtil.isMock(new Object()));

 Object mock = mock(Object.class);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptor() throws Exception {
 assertNull(mockUtil.getInterceptor(null));
 assertNull(mockUtil.getInterceptor(new Object()));

 Object mock = mock(Object.class);
 MethodInterceptorFilter interceptor = mock(MethodInterceptorFilter.class);
 when(getInterceptor(mock)).thenReturn(interceptor);

 assertEquals(interceptor, mockUtil.getInterceptor(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNonMockitoMock() throws Exception {
 Object nonMockitoMock = new Object();
 when(getInterceptor(nonMockitoMock) == null).thenReturn(true);
 mockUtil.getInterceptor(nonMockitoMock);
 }

 @Test
 public void testGetMockName() throws Exception {
 mock = mock(Object.class);
 mockUtil.resetMock(mock);

 assertNull(mockUtil.getMockName(mock));

 String mockName = "mockName";
 when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl().initiateMockName(mockClass).setMockName(mockName));

 assertEquals(mockName, mockUtil.getMockName(mock));
 }

 @Test
 public void testCreateMockWithExtraInterfacesAndSpiedInstance() throws Exception {
 assertNull(mock);

 when(creationValidatorMock.validateType(mockClass)).thenReturn(null);
 when(creationValidatorMock.validateExtraInterfaces(mockClass, anyArray())).thenReturn(new Class[]{Mockito.mock(Class.class)});
 when(creationValidatorMock.validateMockedType(mockClass, spiedInstance)).thenReturn(null);

 mockUtil.createMock(mockClass, new MockSettingsImpl().setSpiedInstance(spiedInstance));

 verify(creationValidatorMock).validateType(mockClass);
 verify(creationValidatorMock).validateExtraInterfaces(mockClass, new Class[]{Mockito.mock(Class.class)});
 verify(creationValidatorMock).validateMockedType(mockClass, spiedInstance);

 assertEquals(mockClass, WhiteboxImpl.getInternalState(mock));
 assertNotNull(getInterceptor(mock));

 lenientCopyTool.copyToMock(spiedInstance, mock);
 verify(lenientCopyTool).copyToMock(eq(spiedInstance), eq(mock));
 }
}