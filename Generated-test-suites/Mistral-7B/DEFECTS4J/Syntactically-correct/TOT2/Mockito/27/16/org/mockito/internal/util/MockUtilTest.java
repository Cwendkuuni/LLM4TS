import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.exceptions.misusing.NotAMockException;

import java.lang.reflect.Type;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.invoke;
import static org.junit.Assert.*;

public class MockUtilTest {
    MockUtil mockUtil;
    MockSettingsImpl settings;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
        settings = new MockSettingsImpl();
    }

    @Test
    public void testCreateMock() {
        Class<String> classToMock = String.class;
        T mock = mockUtil.createMock(classToMock, settings);
        assertEquals(classToMock, mock.getClass());
    }

    @Test (expected = NotAMockException.class)
    public void testIncorrectClassPassedToCreateMock() {
        // Test with an invalid class
        mockUtil.createMock(Integer.TYPE, settings);
    }

    @Test (expected = RuntimeException.class)
    public void testExtraInterfacesNotImplementingValidInterface() {
        // Test with an interface that doesn't exist
        settings.setExtraInterfaces(new Class[]{UnknownInterface.class});
        mockUtil.createMock(String.class, settings);
    }

    @Test (expected = RuntimeException.class)
    public void testSpiedInstanceIsNotNullButNotSerializable() {
        // Test with a non-serializable object
        settings.setSpiedInstance(new Object());
        mockUtil.createMock(String.class, settings);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSpiedInstanceIsNull() {
        // Test with a null object
        settings.setSpiedInstance(null);
        mockUtil.createMock(String.class, settings);
    }

    @Test (expected = NotAMockException.class)
    public void testClassToMockIsAnInterface() {
        // Test with an interface
        mockUtil.createMock(Object.class, settings);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testClassToMockIsAnAbstractClass() {
        // Test with an abstract class
        mockUtil.createMock(AbstractClass.class, settings);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testClassToMockIsAFinalClass() {
        // Test with a final class
        mockUtil.createMock(FinalClass.class, settings);
    }

    @Test
    public void testResetMock() {
        T mock = mockUtil.createMock(String.class, settings);
        mockUtil.resetMock(mock);
        MockHandlerInterface<T> handler = mockUtil.getMockHandler(mock);
        assertNull(handler.getInvocationCount());
    }

    @Test
    public void testIsMock() {
        T mock = mockUtil.createMock(String.class, settings);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testGetMockName() {
        T mock = mockUtil.createMock(String.class, settings);
        MockName name = mockUtil.getMockName(mock);
        assertEquals("MockUtil_String_#", name.getName());
    }
}