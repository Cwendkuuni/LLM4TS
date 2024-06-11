import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.mockito.exceptions.Reporter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockSettingsImplTest {

    @Test
    public void testSerializableMethod() {
        // Given
        MockSettings mockSettings = new MockSettingsImpl();

        // When
        MockSettings serializableSettings = mockSettings.serializable();

        // Then
        verify(mockSettings).extraInterfaces(java.io.Serializable.class);
    }

    @Test
    public void testExtraInterfacesMethod_EmptyArray() {
        // Given
        MockSettings mockSettings = mock(MockSettingsImpl.class);

        // When
        mockSettings.extraInterfaces(new Class[0]);

        // Then
        verify(mockSettings, Mockito.never()).extraInterfacesRequiresAtLeastOneInterface();
    }

    @Test
    public void testExtraInterfacesMethod_NullInterface() {
        // Given
        MockSettings mockSettings = new MockSettingsImpl();

        // When & Then
        mockSettings.extraInterfaces(null);

        new Reporter().expectLastCall().extraInterfacesDoesNotAcceptNullParameters();
    }

    @Test
    public void testExtraInterfacesMethod_NonInterfaceClass() {
        // Given
        MockSettings mockSettings = new MockSettingsImpl();

        // When & Then
        mockSettings.extraInterfaces(String.class);

        new Reporter().expectLastCall().extraInterfacesAcceptsOnlyInterfaces(String.class);
    }

    @Test
    public void testDefaultAnswerMethod_NullAnswer() {
        // Given
        MockSettings mockSettings = new MockSettingsImpl();

        // When & Then
        mockSettings.defaultAnswer(null);

        new Reporter().expectLastCall().defaultAnswerRequiresNonnullArgument();
    }

    @Test
    public void testGetSpiedInstanceMethod_NullInstance() {
        // Given
        MockSettings mockSettings = new MockSettingsImpl();

        // When
        Object nullInstance = null;
        MockSettings settingsWithNullInstance = mockSettings.spiedInstance(nullInstance);

        // Then
        assert null == settingsWithNullInstance.getSpiedInstance();
    }

    @Test
    public void testNameMethod_NullName() {
        // Given
        MockSettings mockSettings = new MockSettingsImpl();

        // When
        String nullName = null;
        MockSettings settingsWithNullName = mockSettings.name(nullName);

        // Then
        assert null == settingsWithNullName.getName();
    }

    @Test
    public void testInitiateMockNameMethod_NullClassToMock() {
        // Given
        MockSettings mockSettings = new MockSettingsImpl();

        // When
        Class<?> nullClassToMock = null;
        MockSettings settings = mockSettings;

        try {
            settings.initiateMockName(nullClassToMock);
        } catch (Exception e) {
            // Expected exception
        }

        new Reporter().expectLastCall().initiateMockNameRequiresNonnullClassArgument();
    }

    @Test
    public void testIsSerializableMethod_NullExtraInterfaces() {
        // Given
        MockSettings mockSettings = new MockSettingsImpl();

        // When
        MockSettings settings = mockSettings;

        // Then
        assertFalse(settings.isSerializable());
    }

    @Test
    public void testIsSerializableMethod_EmptyExtraInterfaces() {
        // Given
        MockSettings mockSettings = new MockSettingsImpl();

        // When
        MockSettings settings = mockSettings.serializable();

        // Then
        assertTrue(settings.isSerializable());
    }

    @Test
    public void testIsSerializableMethod_ExtraInterfacesWithSerializable() {
        // Given
        MockSettings mockSettings = new MockSettingsImpl();

        // When
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        MockSettings settings = mockSettings.extraInterfaces(extraInterfaces);

        // Then
        assertTrue(settings.isSerializable());
    }

    @Test
    public void testIsSerializableMethod_ExtraInterfacesWithoutSerializable() {
        // Given
        MockSettings mockSettings = new MockSettingsImpl();

        // When
        Class<?>[] extraInterfaces = {String.class};
        MockSettings settings = mockSettings.extraInterfaces(extraInterfaces);

        // Then
        assertFalse(settings.isSerializable());
    }
}