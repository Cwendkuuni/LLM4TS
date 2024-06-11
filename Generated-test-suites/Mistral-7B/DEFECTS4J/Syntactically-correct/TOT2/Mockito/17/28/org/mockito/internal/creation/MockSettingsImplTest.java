import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.MockSettingsImpl;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.serializable();
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testExtraInterfacesNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(null);
        fail("Expected IllegalStateException not thrown");
    }

    @Test(expected = IllegalStateException.class)
    public void testExtraInterfacesAcceptsOnlyInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(new Object(){});
    }

    // Test cases for other methods, edge cases and exceptions omitted for brevity
}