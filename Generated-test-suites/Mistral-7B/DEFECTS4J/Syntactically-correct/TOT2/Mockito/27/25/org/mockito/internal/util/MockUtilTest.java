import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.misusing.NotAMockException;

import java.io.Serializable;

import static org.junit.Assert.*;

public class MockUtilTest {

    @Test
    public void testMockUtilCreation() {
        MockUtil mockUtil = new MockUtil();
        MockUtil mockUtilWithCreationValidator = new MockUtil(new MockCreationValidator());
    }

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        try {
            mockUtil.createMock(classToMock, settings);
            assertTrue(true);
        } catch (MockitoException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        String mock = mockUtil.createMock(String.class, settings);
        assertNotNull(mock);

        assertEquals("MockUtil created mock", mock.getClass().getSimpleName(), mock.getClass().getInterfaces()[0].getSimpleName());

        String input = "test";
        when(mock.length()).thenReturn(input.length());
        assertEquals(input.length(), mock.length());
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());

        mockUtil.resetMock(mock);
        assertFalse(mock.getClass().isInstance(mockUtil.getMockHandler(mock)));
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());

        MockHandlerInterface<String> handler = mockUtil.getMockHandler(mock);
        assertEquals("MockHandlerInterface", handler.getClass().getInterfaces()[0].getName(), MockHandlerInterface.class.getName());
    }

    @Test
    public void testIsMockitoMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());

        assertTrue(mockUtil.isMockitoMock(mock));
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());

        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testGetInterceptor() {
        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());

        assertNotNull(mockUtil.getInterceptor(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockNameNullMock() {
        MockUtil mockUtil = new MockUtil();
        MockHandlerInterface<Object> handler = mockUtil.getMockHandler(null);
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());

        MockName expectedMockName = new NamedMock("MockUtilTest_StringMock");
        MockName actualMockName = mockUtil.getMockName(mock);
        assertEquals(expectedMockName.getName(), actualMockName.getName());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockNullClass() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl();

        mockUtil.createMock(null, settings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockInterface() {
        MockUtil mockUtil = new MockUtil();
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        mockUtil.createMock(classToMock, settings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockImmutableClass() {
        MockUtil mockUtil = new MockUtil();
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        mockUtil.createMock(String.class, settings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockFinalClass() {
        MockUtil mockUtil = new MockUtil();
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        mockUtil.createMock(classToMock, settings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockStaticClass() {
        MockUtil mockUtil = new MockUtil();
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        mockUtil.createMock(classToMock, settings);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockNullMock() {
        MockUtil mockUtil = new MockUtil();
        MockHandlerInterface<Object> handler = mockUtil.getMockHandler(null);

        mockUtil.resetMock(null);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockNotFactoryInstance() {
        MockUtil mockUtil = new MockUtil();
        Object mock = new Object();

        mockUtil.resetMock(mock);
    }
}