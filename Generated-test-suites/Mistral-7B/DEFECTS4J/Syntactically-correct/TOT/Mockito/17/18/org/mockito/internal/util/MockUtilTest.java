import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Answer;
import org.mockito.stub.Stub;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

 private MockUtil mockUtil;
 private ClassImposterizer classImposterizer;
 private Factory mockFactory;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mockUtil = new MockUtil();
 classImposterizer = Mockito.mock(ClassImposterizer.class);
 mockFactory = Mockito.spy(Factory.class);
 }

 @Test
 public void testCreateMockWithValidArguments() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(new String("Hello World"));

 when(classImposterizer.imposterise(ArgumentMatchers.any(MethodInterceptorFilter.class),
 ArgumentMatchers.eq(stringClass), ArgumentMatchers.anyArray()))
 .thenReturn(Mockito.mock(String.class));

 when(mockFactory.getCallback(0)).thenReturn(Mockito.mock(MethodInterceptorFilter.class));

 T mock = mockUtil.createMock(stringClass, settings);

 assertEquals("Hello World", mock.toString());
 assertNotNull(mock.getClass().getInterfaces());
 verify(classImposterizer).imposterise(ArgumentMatchers.any(MethodInterceptorFilter.class),
 ArgumentMatchers.eq(stringClass), ArgumentMatchers.anyArray());
 verify(mockFactory).getCallback(0);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 Class<Object> objectClass = Object.class;
 mockUtil.createMock(objectClass, new MockSettingsImpl());
 }

 @Test
 public void testResetMock() {
 T mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 mockUtil.resetMock(mock);
 assertNull(mockUtil.getMockHandler(mock));
 }

 @Test
 public void testGetMockHandler() {
 T mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 Object handler = mockUtil.getMockHandler(mock);
 assertNotNull(handler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockitoMock() {
 Object nonMockitoMock = new Object();
 mockUtil.getMockHandler(nonMockitoMock);
 }

 @Test
 public void testIsMock() {
 Object mockitoMock = mockUtil.createMock(String.class, new MockSettingsImpl());
 assertTrue(mockUtil.isMock(mockitoMock));
 }

 @Test
 public void testIsMockWithNullMock() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testGetInterceptor() {
 T mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNonMockitoMock() {
 Object nonMockitoMock = new Object();
 mockUtil.getInterceptor(nonMockitoMock);
 }

 @Test
 public void testGetMockName() {
 T mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 MockName mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 assertEquals("MockUtil_String_", mockName.getName());
 }

 @Test
 public void testCreateMockWithValidExtraInterfaces() {
 Class<String> stringClass = String.class;
 Class<PrintWriter> printWriterClass = PrintWriter.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[]{printWriterClass});
 settings.setSpiedInstance(new String("Hello World"));

 when(classImposterizer.imposterise(ArgumentMatchers.any(MethodInterceptorFilter.class),
 ArgumentMatchers.eq(stringClass), ArgumentMatchers.anyArray()))
 .thenReturn(Mockito.mock(String.class, printWriterClass));

 when(mockFactory.getCallback(0)).thenReturn(Mockito.mock(MethodInterceptorFilter.class));

 T mock = mockUtil.createMock(stringClass, settings);

 assertEquals("Hello World", mock.toString());
 assertNotNull(mock.getClass().getInterfaces());
 assertTrue(mock.getClass().isInterface());
 assertTrue(mock.getClass().getInterfaces()[0].equals(PrintWriter.class));
 verify(classImposterizer).imposterise(ArgumentMatchers.any(MethodInterceptorFilter.class),
 ArgumentMatchers.eq(stringClass), ArgumentMatchers.anyArray());
 verify(mockFactory).getCallback(0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithInvalidExtraInterfaces() {
 Class<String> stringClass = String.class;
 Class<PrintWriter> printWriterClass = PrintWriter.class;
 Class<Integer> integerClass = Integer.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[]{printWriterClass, integerClass});
 settings.setSpiedInstance(new String("Hello World"));

 when(classImposterizer.imposterise(ArgumentMatchers.any(MethodInterceptorFilter.class),
 ArgumentMatchers.eq(stringClass), ArgumentMatchers.anyArray()))
 .thenReturn(Mockito.mock(String.class));

 when(mockFactory.getCallback(0)).thenReturn(Mockito.mock(MethodInterceptorFilter.class));

 mockUtil.createMock(stringClass, settings);
 }
}