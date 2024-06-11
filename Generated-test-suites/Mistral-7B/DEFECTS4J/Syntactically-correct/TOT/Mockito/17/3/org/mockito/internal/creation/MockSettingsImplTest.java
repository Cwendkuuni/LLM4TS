import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.annotations.BeforeMethod;
import org.mockito.internal.creation.MockSettingsImpl;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockSettingsImplTest {
    private MockSettingsImpl mockSettings;

    @BeforeMethod
    public void setup() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializableMethod() {
        // Given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[]{java.io.Serializable.class};
        mockSettings.serializable();

        // When
        boolean result = mockSettings.isSerializable();

        // Then
        verify(mockSettings).extraInterfaces(extraInterfaces);
        verify(mockSettings).isSerializable();
        assertTrue(result);
    }

    @Test
    public void testExtraInterfacesMethod() {
        // Given
        Class<?>[] extraInterfaces = new Class[0];

        // When
        mockSettings.extraInterfaces(extraInterfaces);

        // Then
        assertNull(mockSettings.getExtraInterfaces());

        // Test with valid interfaces
        Class<?>[] validInterfaces = new Class[]{Mockito.class};
        mockSettings.extraInterfaces(validInterfaces);
        assertArrayEquals(validInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesWithNullParameter() {
        // Given
        mockSettings.extraInterfaces(null);

        // Then
        // Assertion error should be thrown due to the Reporter class reporting an error
    }

    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesWithNonInterfaceParameter() {
        // Given
        Class<?> nonInterfaceClass = String.class;
        mockSettings.extraInterfaces(nonInterfaceClass);

        // Then
        // Assertion error should be thrown due to the Reporter class reporting an error
    }

    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesWithMultipleInvalidInterfaces() {
        // Given
        Class<?>[] extraInterfaces = new Class[]{Mockito.class, String.class};
        mockSettings.extraInterfaces(extraInterfaces);

        // Then
        // Assertion errors should be thrown due to the Reporter class reporting errors for both the Mockito class and the String class
    }

    @Test
    public void testDefaultAnswerMethod() {
        // Given
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);

        // When
        mockSettings.defaultAnswer(defaultAnswer);

        // Then
        verify(mockSettings).getDefaultAnswer();
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testNameMethod() {
        // Given
        String name = "MockitoTest";
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        // When
        mockSettings.name(name);

        // Then
        assertEquals(name, mockSettings.getMockName().getName());
    }

    @Test
    public void testSpiedInstanceMethod() {
        // Given
        Object spiedInstance = new Object();
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        // When
        mockSettings.spiedInstance(spiedInstance);

        // Then
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }
}