import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.creation.CreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.WhiteboxReflection;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockUtilTest {

 private MockUtil mockUtil;
 private CreationValidator creationValidator;
 private MockHandler<Object> mockHandler;
 private Object mock;

 @Before
 public void setUp() {
 creationValidator = MockitoAnnotations.spy(new CreationValidator());
 mockUtil = new MockUtil(creationValidator);
 }

 @Test
 public void testCreateMock() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setName("MockName");
 Object spiedInstance = new Object();

 mockUtil.createMock(classToMock, settings);
 assertNotNull(mock);
 verify(creationValidator, Mockito.atLeastOnce()).validateType(ArgumentMatchers.eq(classToMock));
 verify(creationValidator, Mockito.atLeastOnce()).validateExtraInterfaces(ArgumentMatchers.eq(classToMock), ArgumentMatchers.any());
 verify(creationValidator, Mockito.atLeastOnce()).validateMockedType(ArgumentMatchers.eq(classToMock), ArgumentMatchers.eq(spiedInstance));

 MockHandler<Object> mockHandler = mockUtil.getMockHandler(mock);
 assertNotNull(mockHandler);
 assertEquals("MockName", mockHandler.getMockSettings().getMockName());
 }

 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test
 public void testCreateMockWithEmptyExtraInterfaces() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 settings.setExtraInterfaces(new Class[0]);

 mockUtil.createMock(classToMock, settings);
 assertNotNull(mock);
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<Object> classToMock = Object.class;
 Class<?>[] extraInterfaces = {String.class};
 MockSettingsImpl settings = new MockSettingsImpl();
 Object spiedInstance = new Object();
 settings.setExtraInterfaces(extraInterfaces);
 settings.setName("MockName");

 mockUtil.createMock(classToMock, settings);
 assertNotNull(mock);
 MockHandler<Object> mockHandler = mockUtil.getMockHandler(mock);
 assertNotNull(mockHandler);
 assertEquals("MockName", mockHandler.getMockSettings().getMockName());

 Whitebox.setInternalState(mockHandler, "handler", new MockHandler<String>(settings));
 Object mockWithExtraInterface = mockUtil.getInterceptor(mock).getHandler();
 assertNotNull(mockWithExtraInterface);
 assertInstanceOf(MockHandler.class, mockWithExtraInterface);
 assertSame(mockHandler, mockWithExtraInterface);
 }

 @Test
 public void testResetMock() {
 mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
 mockUtil.resetMock(mock);

 MethodInterceptorFilter filter = mockUtil.getInterceptor(mock);
 assertNotNull(filter);
 assertNull(filter.getHandler());
 }

 @Test
 public void testGetMockHandler() {
 mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
 Object spiedInstance = new Object();
 new LenientCopyTool().copyToMock(spiedInstance, mock);

 MockHandler<Object> mockHandler = mockUtil.getMockHandler(mock);
 assertNotNull(mockHandler);
 assertSame(spiedInstance, mockHandler.getMockSettings().getSpiedInstance());
 }

 @Test
 public void testIsMockitoMock() {
 mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMock() {
 mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptor() {
 mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
 Object interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test
 public void testGetMockName() {
 mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
 String mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 }

 @Test
 public void testPowerMockitoMockUtil() throws Exception {
 PowerMockito.mockStatic(MockUtil.class);
 mock = PowerMockito.mock(Object.class);

 Object mockHandler = WhiteboxReflection.getInternalState(mock, "handler");
 assertNotNull(mockHandler);
 assertInstanceOf(MockHandler.class, mockHandler);
 }

 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testGetInterceptorOnNonMockitoMock() {
 mock = new Object();
 assertNull(mockUtil.getInterceptor(mock));
 }

 @Test
 public void testLenientCopyTool() {
 Object source = new Object();
 Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
 new LenientCopyTool().copyToMock(source, mock);

 verify(mock, Mockito.atLeastOnce()).answer(Mockito.eq(source.toString()), Mockito.eq(ArgumentMatchers.anyInt()), Mockito.eq(ArgumentMatchers.any()));
 }
}