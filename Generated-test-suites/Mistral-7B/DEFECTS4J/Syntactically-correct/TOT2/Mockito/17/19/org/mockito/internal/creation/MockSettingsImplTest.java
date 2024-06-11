import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNotNull(mockSettings.serializable());
    }

    @Test
    public void testExtraInterfacesWithValidInput() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[] { java.io.Serializable.class };
        mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[] { java.io.Serializable.class };
        mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertNull(mockSettings.getSpiedInstance());
    }

    @Test
    public void testName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.name("MockName");
        assertEquals("MockName", mockSettings.getName());
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object instance = new Object();
        mockSettings.spiedInstance(instance);
        assertEquals(instance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> answer = mock(Answer.class);
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.defaultAnswer(answer);
        assertEquals(answer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        assertFalse(mockSettings.isSerializable());

        MockSettingsImpl mockSettingsWithSerializable = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[] { java.io.Serializable.class };
        mockSettingsWithSerializable.extraInterfaces(extraInterfaces);
        assertTrue(mockSettingsWithSerializable.isSerializable());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInitiateMockNameWithNullClassToMock() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.initiateMockName(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testExtraInterfacesWithNullInput() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testExtraInterfacesWithEmptyArray() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(new Class[] {});
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSerializableWithNonSerializableInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[] { Integer.class };
        mockSettings.serializable();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testExtraInterfacesWithNonInterfaceClass() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[] { Integer.class };
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSpiedInstanceWithNullInput() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.spiedInstance(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNameWithNullInput() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.name(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testDefaultAnswerWithNullAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> nullAnswer = null;
        mockSettings.defaultAnswer(nullAnswer);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testDefaultAnswerWithNonAnswerObject() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object nonAnswerObject = new Object();
        mockSettings.defaultAnswer(nonAnswerObject);
    }
}