import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.creation.MethodInterceptor;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.exceptions.misusing.NotAMockException;
import static org.junit.Assert.*;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockUtil mockUtilNoCreationValidator;
 private ClassImposterizer classImposterizer;
 private LenientCopyTool lenientCopyTool;
 private CreationValidator creationValidator;
 private Stubber stubber;
 private Object spiedInstance;
 private MockHandler mockHandler;
 private MethodInterceptor interceptor;
 private Factory mock;
 private MockName mockName;

 @Before
 public void setup() {
 mockUtil = new MockUtil(creationValidator = new CreationValidator());
 mockUtilNoCreationValidator = new MockUtil();
 classImposterizer = Mockito.spy(ClassImposterizer.class);
 lenientCopyTool = Mockito.spy(LenientCopyTool.class);
 stubber = Mockito.mock(Stubber.class);
 spiedInstance = new Object();
 mockHandler = Mockito.mock(MockHandler.class);
 interceptor = Mockito.mock(MethodInterceptor.class);
 mock = Mockito.mock(Factory.class);
 mockName = Mockito.mock(MockName.class);
 }

 @Test
 public void testCreateMock() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
 settings.initiateMockName(stringClass);
 settings.setExtraInterfaces(new Class<?>[0]);
 settings.setSpiedInstance(spiedInstance);

 Mockito.when(classImposterizer.imposterise(ArgumentMatchers.any(MethodInterceptorFilter.class), stringClass, ArgumentMatchers.array(Class.class))).thenReturn(mock);
 Mockito.when(lenientCopyTool.copyToMock(spiedInstance, ArgumentMatchers.isA(Object.class))).thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 // Copy logic
 return null;
 }
 });
 Mockito.when(mockHandler.getMockSettings()).thenReturn(settings);
 Mockito.when(mockHandler.getMockName()).thenReturn(mockName);

 when(mockUtil.createMock(stringClass, settings)).thenReturn(mock);

 assertEquals(mock, mockUtil.createMock(stringClass, settings));
 assertEquals(String.class, mock.getClass());
 assertEquals(settings, mockUtil.getMockHandler(mock).getMockSettings());
 assertEquals(mockHandler, mockUtil.getMockHandler(mock));
 assertEquals(mockName, mockUtil.getMockName(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullClass() {
 mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullSettings() {
 mockUtil.createMock(String.class, null);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullSpiedInstance() {
 mockUtil.createMock(String.class, new MockSettingsImpl());
 }

 @Test
 public void testResetMock() {
 when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
 resetMock(mock);

 verify(mockHandler, times(1)).setCallback(0, ArgumentMatchers.isA(MethodInterceptorFilter.class));
 }

 @Test(expected = NotAMockException.class)
 public void testResetMock_NullMock() {
 resetMock(null);
 }

 @Test
 public void testGetMockHandler() {
 Object mock = Mockito.mock(Object.class);
 when(mockUtil.isMockitoMock(mock)).thenReturn(true);
 when(mockUtil.getInterceptor(mock)).thenReturn(interceptor);

 assertEquals(mockHandler, mockUtil.getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_NullMock() {
 getMockHandler(null);
 }

 @Test
 public void testIsMock() {
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMock_NullMock() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testGetInterceptor() {
 when(mockUtil.isMockitoMock(mock)).thenReturn(true);
 when(mockUtil.getFactory(mock)).thenReturn(mock);
 when(mock.getCallback(0)).thenReturn(interceptor);

 assertEquals(interceptor, mockUtil.getInterceptor(mock));
 }

 @Test
 public void testGetInterceptor_NullMock() {
 assertEquals(null, mockUtil.getInterceptor(null));
 }

 @Test
 public void testGetMockName() {
 when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
 when(mockHandler.getMockName()).thenReturn(mockName);

 assertEquals(mockName, mockUtil.getMockName(mock));
 }

 @Test
 public void testGetMockName_NullMock() {
 assertEquals(null, mockUtil.getMockName(null));
 }

 @Test
 public void testCreateMock_WithExtraInterfaces() {
 Class<String> stringClass = String.class;
 Class<PrintStream> printStreamClass = PrintStream.class;
 MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
 settings.initiateMockName(stringClass);
 settings.setExtraInterfaces(new Class<?>[]{printStreamClass});
 settings.setSpiedInstance(spiedInstance);

 when(classImposterizer.imposterise(ArgumentMatchers.any(MethodInterceptorFilter.class), stringClass, ArgumentMatchers.array(Class.class))).thenReturn(mock);
 when(lenientCopyTool.copyToMock(spiedInstance, ArgumentMatchers.isA(Object.class))).thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 // Copy logic
 return null;
 }
 });
 Mockito.when(mockHandler.getMockSettings()).thenReturn(settings);
 Mockito.when(mockHandler.getMockName()).thenReturn(mockName);

 when(mockUtil.createMock(stringClass, settings)).thenReturn(mock);

 assertEquals(mock, mockUtil.createMock(stringClass, settings));
 assertEquals(String.class, mock.getClass());
 assertEquals(settings, mockUtil.getMockHandler(mock).getMockSettings());
 assertEquals(mockHandler, mockUtil.getMockHandler(mock));
 assertEquals(mockName, mockUtil.getMockName(mock));
 assertEquals(printStreamClass, Whitebox.getInternalState(mock, "extraInterfaces")[0]);
 }
}