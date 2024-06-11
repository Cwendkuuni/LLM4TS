import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InternalMockHandler;
import org.mockito.Mockito;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerMockitoHelper;
import org.mockito.plugins.MockMakerUtil;
import org.mockito.plugins.MockMakerUtil.ClassLoaderAwareMockMaker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ByteBuddyMockMakerTest {

    private MockMaker mockMaker;
    private InternalMockHandler mockHandler;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setUp() {
        mockHandler = mock(InternalMockHandler.class);
        mockMaker = new ByteBuddyMockMaker();
        classInstantiator = MockMakerUtil.getMockMaker(new ClassLoaderAwareMockMaker()).getInstance(ClassInstantiator.class);
        cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        when(mockMaker.getCachingMockBytecodeGenerator()).thenReturn(cachingMockBytecodeGenerator);
        when(mockMaker.initializeClassInstantiator()).thenReturn(classInstantiator);
    }

    @Test
    public void testCreateMockSuccess() {
        // Given
        Type typeToMock = String[].class;
        Class<? extends String[]> mockedProxyType = mockMaker.getMockType(typeToMock);
        ArgumentCaptor<MockHandler> handlerCaptor = ArgumentCaptor.forClass(MockHandler.class);

        // When
        String[] mock = mockMaker.createMock(new MockCreationSettings<>(typeToMock, null), handlerCaptor.capture());

        // Then
        verify(cachingMockBytecodeGenerator).get(eq(typeToMock), any());
        verify(classInstantiator).instantiate(mockedProxyType);
        verify(handlerCaptor.getValue()).setMockitoInterceptor(Mockito.any(MockMethodInterceptor.class));
        assertSame(mockHandler, handlerCaptor.getValue());
        assertEquals(String[].class, mock.getClass());
        assertNull(mockMaker.getHandler(mock));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockSerializationAcrossClassloaders() {
        // Given
        SerializableMode serializableMode = SerializableMode.ACROSS_CLASSLOADERS;
        MockCreationSettings<String[]> settings = new MockCreationSettings<>(String[].class, serializableMode, null, null);

        // When
        mockMaker.createMock(settings, mockHandler);

        // Then
        // A MockitoException should be thrown
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMockInstantiationException() {
        // Given
        Type typeToMock = String[].class;
        Class<? extends String[]> mockedProxyType = mock(Class.class);
        when(cachingMockBytecodeGenerator.get(eq(typeToMock), any()))
                .thenThrow(new InstantiationException("Unable to create mock instance of type 'String[]'"));

        // When & Then
        mockMaker.createMock(new MockCreationSettings<>(typeToMock, null), mockHandler);
    }

    @Test
    public void testGetHandler() {
        // Given
        String[] mock = mockMaker.createMock(new MockCreationSettings<>(String[].class, null), mockHandler);

        // When
        MockHandler handler = mockMaker.getHandler(mock);

        // Then
        assertSame(mockHandler, handler);
    }

    @Test
    public void testResetMock() {
        // Given
        String[] mock = mockMaker.createMock(new MockCreationSettings<>(String[].class, null), mockHandler);
        ArgumentCaptor<MockHandler> newHandlerCaptor = ArgumentCaptor.forClass(MockHandler.class);

        // When
        mockMaker.resetMock(mock, Mockito.mock(MockHandler.class), new MockCreationSettings<>(String[].class, null));

        // Then
        verify(mockHandler).setMockitoInterceptor(newHandlerCaptor.capture());
        assertSame(mockHandler, mockMaker.getHandler(mock));
        assertSame(newHandlerCaptor.getValue(), mockMaker.getHandler(mock));
    }

    @Test
    public void testDescribeClass() {
        // Given
        Class<String> stringClass = String.class;
        String[] stringArray = new String[0];
        Object nullObject = null;

        // When
        String stringClassDescription = ByteBuddyMockMaker.describeClass(stringClass);
        String stringArrayDescription = ByteBuddyMockMaker.describeClass(stringArray);
        String nullObjectDescription = ByteBuddyMockMaker.describeClass(nullObject);

        // Then
        assertEquals("'java.lang.String', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4a182'", stringClassDescription);
        assertEquals("'[Ljava.lang.String;', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4a182'", stringArrayDescription);
        assertEquals("null", nullObjectDescription);
    }
}