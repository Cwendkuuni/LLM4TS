import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Capture;
import org.mockito.invocation.MockHandler;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.MockCreationSettings;
import org.mockito.internal.creation.instance.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerMockHandler;
import org.mockito.invocation.MockMethodInterceptor;
import org.mockito.internal.util.StringJoiner;
import org.mockito.exceptions.base.MockitoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker byteBuddyMockMaker;
 private ClassInstantiator classInstantiatorSpy;
 private CachingMockBytecodeGenerator cachingMockBytecodeGeneratorSpy;
 private MockHandler mockHandlerSpy;

 @Before
 public void setup() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 classInstantiatorSpy = spy(byteBuddyMockMaker.classInstantiator);
 cachingMockBytecodeGeneratorSpy = spy(byteBuddyMockMaker.cachingMockBytecodeGenerator);
 mockHandlerSpy = mock(MockHandler.class);
 }

 @Test
 public void testCreateMockSuccessScenario() {
 MockCreationSettings settings = new MockCreationSettings<Object>() {{
 setTypeToMock(String.class);
 setExtraInterfaces(String[].class);
 setSerializableMode(SerializableMode.DEFAULT);
 setHandler(mockHandlerSpy);
 }};
 Object mock = byteBuddyMockMaker.createMock(settings, mockHandlerSpy);
 ArgumentCaptor<MockCreationSettings<Object>> creationSettingsCaptor = ArgumentCaptor.forClass(MockCreationSettings.class);
 verify(classInstantiatorSpy).instantiate(eq(String.class));
 verify(cachingMockBytecodeGeneratorSpy).get(eq(String.class), eq(new Class[0]));
 verify(mockHandlerSpy).handle(argThat(new ArgumentCaptor<Object>() {{
 return MockMethodInterceptor.class.isAssignableFrom(getClass());
 }}));
 verify(mockHandlerSpy).handle(eq(mock));
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockSerializationAcrossClassloadersErrorScenario() {
 MockCreationSettings settings = new MockCreationSettings<Object>() {{
 setTypeToMock(String.class);
 setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
 setHandler(mockHandlerSpy);
 }};
 byteBuddyMockMaker.createMock(settings, mockHandlerSpy);
 }

 @Test
 public void testGetHandlerSuccessScenario() {
 Object mock = byteBuddyMockMaker.createMock(mockCreationSettings(), mockHandlerSpy);
 verify(mockHandlerSpy).getMockHandler();
 }

 @Test
 public void testGetHandlerNullScenario() {
 Object mock = byteBuddyMockMaker.createMock(mockCreationSettings(), null);
 verifyZeroInteractions(mockHandlerSpy);
 }

 @Test
 public void testResetMockSuccessScenario() {
 Object mock = byteBuddyMockMaker.createMock(mockCreationSettings(), mockHandlerSpy);
 MockHandler newHandler = new MockMakerMockHandler();
 byteBuddyMockMaker.resetMock(mock, newHandler, mockCreationSettings());
 ArgumentCaptor<MockHandler> newHandlerCaptor = ArgumentCaptor.forClass(MockHandler.class);
 verify(mockHandlerSpy).setMockitoInterceptor(newHandlerCaptor.capture());
 verify(newHandlerCaptor.getValue()).setMockitoInterceptor(mockHandlerSpy);
 }

 @Test
 public void testResetMockNullNewHandlerScenario() {
 Object mock = byteBuddyMockMaker.createMock(mockCreationSettings(), mockHandlerSpy);
 byteBuddyMockMaker.resetMock(mock, null, mockCreationSettings());
 verifyZeroInteractions(mockHandlerSpy);
 }

 @Test
 public void testInitializeClassInstantiatorSuccessScenario() {
 Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
 Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
 ClassInstantiator classInstantiator = byteBuddyMockMaker.initializeClassInstantiator();
 verify(objenesisClassLoader).newInstance();
 verify(classInstantiator).setUseClassCache(true);
 }

 @Test(expected = IllegalStateException.class)
 public void testInitializeClassInstantiatorFailureScenario() {
 doThrow(new IllegalStateException("Mockito could not create mock: Objenesis is missing on the classpath.")).when(classInstantiatorSpy).instantiate(eq(String.class));
 ByteBuddyMockMaker byteBuddyMockMakerSpy = spy(byteBuddyMockMaker);
 byteBuddyMockMakerSpy.initializeClassInstantiator();
 }

 @Test
 public void testAsInternalMockHandlerSuccessScenario() {
 MockHandler handler = mock(MockHandler.class);
 InternalMockHandler internalMockHandler = byteBuddyMockMaker.asInternalMockHandler(handler);
 verify(handler).getMockHandler();
 }

 @Test(expected = MockitoException.class)
 public void testAsInternalMockHandlerInvalidHandlerScenario() {
 MockHandler handler = mock(Object.class);
 byteBuddyMockMaker.asInternalMockHandler(handler);
 }
}