import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.internal.validation.CreationValidator;
import org.mockito.internal.validation.creation.MethodInterceptor;
import org.mockito.internal.validation.creation.MethodInterceptorFilter;
import org.mockito.internal.validation.creation.MockSettingsImpl;
import org.mockito.internal.validation.creation.jmock.ClassImposterizer;
import org.mockito.internal.validation.util.MockHandler;
import org.mockito.internal.validation.util.MockHandlerInterface;
import org.mockito.internal.validation.util.ReflectionUtils;
import org.mockito.internal.util.reflection.Whitebox2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MockUtilTest {

 private MockUtil mockUtil;
 private CreationValidator creationValidator;
 private ClassImposterizer classImposterizer;
 private LenientCopyTool lenientCopyTool;
 private MockSettingsImpl mockSettings;
 private MockHandler<Object> mockHandler;
 private MethodInterceptorFilter methodInterceptorFilter;

 @Before
 public void setUp() {
 creationValidator = Mockito.spy(new CreationValidator());
 mockUtil = new MockUtil(creationValidator);
 classImposterizer = Mockito.spy(ClassImposterizer.INSTANCE);
 lenientCopyTool = Mockito.spy(new LenientCopyTool());
 mockSettings = Mockito.spy(new MockSettingsImpl());
 mockHandler = Mockito.spy(new MockHandler<Object>(mockSettings));
 methodInterceptorFilter = Mockito.spy(new MethodInterceptorFilter(mockHandler, withSettings().defaultAnswer(Mockito.RETURNS_DEFAULTS)));
 }

 @Test
 public void testCreateMockWithNullClass() {
 assertNull(mockUtil.createMock(null, mockSettings));
 verify(creationValidator).validateType(any(Class.class));
 }

 @Test
 public void testCreateMockWithNullSettings() {
 assertNull(mockUtil.createMock(Mockito.mock(String.class), null));
 verify(creationValidator).validateType(Mockito.eq(String.class));
 }

 @Test
 public void testCreateMockWithValidArguments() throws Exception {
 Object mock = mockUtil.createMock(Mockito.mock(String.class), mockSettings);
 assertNotNull(mock);
 assertSame(String.class, mock.getClass());
 assertSame(mockHandler, Whitebox.getInternalState(mock, "handler"));
 assertSame(methodInterceptorFilter, Whitebox.getInternalState(mock, "interceptor"));
 verify(creationValidator).validateType(Mockito.eq(String.class));
 verify(creationValidator).validateExtraInterfaces(Mockito.eq(String.class), Mockito.eq(new Class[0]));
 verifyZeroInteractions(classImposterizer, lenientCopyTool);
 }

 @Test
 public void testCreateMockWithExtraInterfaces() throws Exception {
 Class<?> extraInterface = Mockito.mock(Interface.class);
 mockSettings.setExtraInterfaces(new Class[]{extraInterface});
 Object mock = mockUtil.createMock(Mockito.mock(String.class), mockSettings);
 assertNotNull(mock);
 assertSame(String.class, mock.getClass());
 assertSame(mockHandler, Whitebox.getInternalState(mock, "handler"));
 assertSame(methodInterceptorFilter, Whitebox.getInternalState(mock, "interceptor"));
 verify(creationValidator).validateType(Mockito.eq(String.class));
 verify(creationValidator).validateExtraInterfaces(Mockito.eq(String.class), Mockito.eq(new Class[]{extraInterface}));
 verify(classImposterizer).imposterise(eq(methodInterceptorFilter), Mockito.eq(String.class), Mockito.eq(new Class[]{extraInterface}));
 }

 @Test
 public void testCreateMockWithSpiedInstance() throws Exception {
 Object spiedInstance = mock(String.class);
 mockSettings.setSpiedInstance(spiedInstance);
 Object mock = mockUtil.createMock(Mockito.mock(String.class), mockSettings);
 assertNotNull(mock);
 assertSame(String.class, mock.getClass());
 assertSame(mockHandler, Whitebox.getInternalState(mock, "handler"));
 assertSame(methodInterceptorFilter, Whitebox.getInternalState(mock, "interceptor"));
 verify(creationValidator).validateType(Mockito.eq(String.class));
 verify(creationValidator).validateExtraInterfaces(Mockito.eq(String.class), Mockito.eq(new Class[0]));
 verifyZeroInteractions(classImposterizer, lenientCopyTool);
 lenientCopyTool.copyToMock(spiedInstance, mock);
 verify(lenientCopyTool).copyToMock(eq(spiedInstance), eq(mock));
 }

 @Test
 public void testResetMock() throws Exception {
 Object mock = mockUtil.createMock(Mockito.mock(String.class), mockSettings);
 MethodInterceptorFilter oldFilter = (MethodInterceptorFilter) Whitebox.getInternalState(mock, "interceptor");
 MockHandler<Object> oldMockHandler = oldFilter.getMockHandler();
 resetMock(mock);
 MethodInterceptorFilter newFilter = (MethodInterceptorFilter) Whitebox.getInternalState(mock, "interceptor");
 MockHandler<Object> newMockHandler = newFilter.getMockHandler();
 assertNotSame(oldMockHandler, newMockHandler);
 }

 @Test
 public void testGetMockHandler() throws Exception {
 Object mock = mockUtil.createMock(Mockito.mock(String.class), mockSettings);
 MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);
 assertNotNull(mockHandler);
 assertEquals(mockHandler.getMockName(), mockSettings.getMockName());
 }

 @Test
 public void testIsMockitoMock() throws Exception {
 Object mock = mockUtil.createMock(Mockito.mock(String.class), mockSettings);
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMock() throws Exception {
 Object mock = mockUtil.createMock(Mockito.mock(String.class), mockSettings);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptor() throws Exception {
 Object mock = mockUtil.createMock(Mockito.mock(String.class), mockSettings);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test
 public void testGetMockName() throws Exception {
 Object mock = mockUtil.createMock(Mockito.mock(String.class), mockSettings);
 String mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 assertEquals(mockName, mockSettings.getMockName());
 }

 interface Interface {}

 static class Factory {
 Callback getCallback(int index) {
 throw new UnsupportedOperationException();
 }
 }

 static class InvocationHandler implements Callback {
 public Object invoke(Object proxy, Method method, Object[] args, InvocationOnMock invocation) throws Throwable {
 throw new UnsupportedOperationException();
 }
 }

 static class Callback {
 }
}