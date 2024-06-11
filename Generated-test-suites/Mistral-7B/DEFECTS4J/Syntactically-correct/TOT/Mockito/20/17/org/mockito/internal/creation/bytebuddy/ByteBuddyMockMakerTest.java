import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InternalMockHandler;
import org.mockito.Mockito;
import org.mockito.invocation.MockHandler;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerMockitoHelper;
import org.mockito.plugins.MockMakerSettings;
import org.mockito.plugins.MockMakerUtil;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    @Rule
    public MockMaker mockMaker = new MockMakerSettings().withMockMaker(byteBuddyMockMaker = new ByteBuddyMockMaker());

    @Before
    public void setUp() {
        MockMakerMockitoHelper.initMocks(this);
    }

    @Test
    public void testCreateMockWithValidSettingsAndHandler() {
        Class<?> mockedType = Mockito.mock(String.class);
        MockHandler handler = mock(MockHandler.class);
        T tMock = byteBuddyMockMaker.createMock(new MockCreationSettings<T>() {{
            setTypeToMock(mockedType);
            setHandler(handler);
        }}, handler);
        assertNotNull(tMock);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithInvalidSerializableMode() {
        Class<?> mockedType = Mockito.mock(String.class);
        MockHandler handler = mock(MockHandler.class);
        byteBuddyMockMaker.createMock(new MockCreationSettings<T>() {{
            setTypeToMock(mockedType);
            setHandler(handler);
            setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        }}, handler);
    }

    @Test
    public void testCreateMockThrowsClassCastException() {
        Class<?> mockedType = Mockito.mock(String.class);
        MockHandler handler = mock(MockHandler.class);
        T tMock = byteBuddyMockMaker.createMock(new MockCreationSettings<T>() {{
            setTypeToMock(Object.class);
            setHandler(handler);
        }}, handler);
        assertThrows(MockitoException.class, () -> {
            byteBuddyMockMaker.ensureMockIsAssignableToMockedType(new MockCreationSettings<>(), tMock);
        });
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(String.class);
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        Object mock = mock(String.class);
        MockHandler handler = mock(MockHandler.class);
        byteBuddyMockMaker.resetMock(mock, handler, new MockCreationSettings<>());
        MockHandler newHandler = byteBuddyMockMaker.getHandler(mock);
        assertEquals(handler, newHandler);
    }

    @Test
    public void testInitializeClassInstantiator() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class<?> classInstantiator = Whitebox.getInternalState(byteBuddyMockMaker, "classInstantiator");
        assertEquals(ClassInstantiator.class, classInstantiator);
    }

    @Test
    public void testCreateMockWithObjenesisMissing() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        assertThrows(IllegalStateException.class, () -> byteBuddyMockMaker.createMock(new MockCreationSettings<>(), mock(MockHandler.class)));
    }
}