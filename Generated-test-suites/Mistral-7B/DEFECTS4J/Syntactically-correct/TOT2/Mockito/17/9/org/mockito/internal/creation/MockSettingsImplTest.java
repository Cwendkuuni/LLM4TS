import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.Reporter;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings serializableMockSettings = mockSettings.serializable();
        assertEquals(MockSettings.class, serializableMockSettings.getClass());
        assertEquals(true, serializableMockSettings.isSerializable());
    }

    @Test(expected = IllegalStateException.class)
    public void testExtraInterfacesNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testExtraInterfacesNotInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        try {
            mockSettings.extraInterfaces(new Object() {});
            fail("Expected Reporter.extraInterfacesAcceptsOnlyInterfaces to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("extraInterfaces accepts only interfaces", e.getMessage());
        }
    }

    @Test
    public void testEmptyExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings emptyExtraInterfacesMockSettings = mockSettings.extraInterfaces();
        assertEquals(0, emptyExtraInterfacesMockSettings.getExtraInterfaces().length);
    }

    // Test cases for other methods following the same pattern

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "testName";
        MockSettings nameMockSettings = mockSettings.name(name);
        assertEquals(name, nameMockSettings.getMockName().getName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {String.class, Integer.class};
        MockSettings extraInterfacesMockSettings = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, extraInterfacesMockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings spiedInstanceMockSettings = mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, spiedInstanceMockSettings.getSpiedInstance());
    }

    @Test
    public void testName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "testName";
        MockSettings nameMockSettings = mockSettings.name(name);
        assertEquals(name, nameMockSettings.getMockName().getName());
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings spiedInstanceMockSettings = mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, spiedInstanceMockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = answer -> null;
        MockSettings defaultAnswerMockSettings = mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, defaultAnswerMockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings serializableMockSettings = mockSettings.serializable();
        assertTrue(serializableMockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class classToMock = MockSettingsImpl.class;
        mockSettings.initiateMockName(classToMock);
        assertNotNull(mockSettings.getMockName());
    }

    // Test cases for edge cases and exception handling following the same pattern

    @Test(expected = IllegalArgumentException.class)
    public void testNameNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.name(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSpiedInstanceNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings nullSpiedInstanceMockSettings = mockSettings.spiedInstance(null);
        assertNull(nullSpiedInstanceMockSettings.getSpiedInstance());
    }

    @Test(expected = NullPointerException.class)
    public void testDefaultAnswerNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings nullDefaultAnswerMockSettings = mockSettings.defaultAnswer(null);
        assertNull(nullDefaultAnswerMockSettings.getDefaultAnswer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClassToMockNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.initiateMockName(null);
    }
}