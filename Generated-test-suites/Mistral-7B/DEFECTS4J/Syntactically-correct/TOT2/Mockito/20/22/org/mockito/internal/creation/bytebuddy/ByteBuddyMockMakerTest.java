import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.MockHandler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ByteBuddyMockMakerTest {

    @Test
    public void createMockTest() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<>(), handler);
        verify(handler).handle(Mockito.any());
        verifyNoMoreInteractions(handler);
    }

    @Test(expected = NullPointerException.class)
    public void createMockWithNullHandlerTest() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(new MockCreationSettings<>(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createMockWithInvalidTypeTest() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(new MockCreationSettings<>(String.class), handler);
        byteBuddyMockMaker.createMock(new MockCreationSettings<>(String[].class), handler);
    }

    @Test(expected = MockitoException.class)
    public void createMockWithSerializationExceptionTest() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(new MockCreationSettings<>(), handler);
        byteBuddyMockMaker.createMock(new MockCreationSettings<>(Serializable.class, SerializableMode.ACROSS_CLASSLOADERS), handler);
    }

    @Test(expected = InstantiationException.class)
    public void createMockWithInstantiationExceptionTest() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        try {
            byteBuddyMockMaker.createMock(new MockCreationSettings<>(String[].class), handler);
        } catch (MockitoException e) {
            // Do nothing
        }
        ByteBuddyMockMaker newByteBuddyMockMaker = new ByteBuddyMockMaker();
        Mockito.doAnswer(new Answer<T>() {
            @Override
            public T answer(InvocationOnMock invocation) throws Throwable {
                throw new InstantiationException();
            }
        }).when(newByteBuddyMockMaker).createMock(Mockito.any(), Mockito.any());

        Object mock = newByteBuddyMockMaker.createMock(new MockCreationSettings<>(String[].class), handler);
    }

    @Test(expected = ClassCastException.class)
    public void createMockWithClassCastExceptionTest() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(new MockCreationSettings<>(Integer[].class), handler);
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<>(String.class), handler);
    }

    @Test
    public void getHandlerTest() {
        Object mock = Mockito.mock(Object.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        ByteBuddyMockMaker.MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        assertNull(handler);
    }

    @Test
    public void resetMockTest() {
        Object mock = Mockito.mock(Object.class);
        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.resetMock(mock, handler, new MockCreationSettings<>());
        ByteBuddyMockMaker.MockHandler newHandler = byteBuddyMockMaker.getHandler(mock);
        assertEquals(handler, newHandler);
    }

    @Test
    public void describeClassTest() {
        Class<?> stringClass = String.class;
        Class<?> stringArrayClass = String[].class;
        Class<?> intClass = Integer.class;
        Class<?> intArrayClass = Integer[].class;
        Class<?> classLoader = ClassLoader.class;

        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        String expectedString = "'java.lang.String', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@5e2fa342'";
        String expectedStringArray = "'[Ljava.lang.String;', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@5e2fa342'";
        String expectedInteger = "'java.lang.Integer', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@5e2fa342'";
        String expectedIntegerArray = "'[I', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@5e2fa342'";
        String expectedClassLoader = "'java.lang.ClassLoader', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@5e2fa342'";

        assertEquals(expectedString, byteBuddyMockMaker.describeClass(stringClass));
        assertEquals(expectedStringArray, byteBuddyMockMaker.describeClass(stringArrayClass));
        assertEquals(expectedInteger, byteBuddyMockMaker.describeClass(intClass));
        assertEquals(expectedIntegerArray, byteBuddyMockMaker.describeClass(intArrayClass));
        assertEquals(expectedClassLoader, byteBuddyMockMaker.describeClass(classLoader));
    }

}