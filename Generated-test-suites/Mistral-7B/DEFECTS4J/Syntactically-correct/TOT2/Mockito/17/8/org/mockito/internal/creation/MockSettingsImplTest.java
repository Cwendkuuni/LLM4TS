import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettings settings = Mockito.spy(new MockSettingsImpl());
        settings.serializable();
        verify(settings).extraInterfaces(java.io.Serializable.class);
    }

    // Edge case: Testing with an empty array
    @Test(expected = IllegalStateException.class)
    public void testSerializableWithEmptyExtraInterfaces() {
        MockSettings settings = Mockito.spy(new MockSettingsImpl());
        settings.serializable();
        settings.extraInterfaces(new Class[0]);
    }

    // Exception handling: Testing with null extraInterfaces
    @Test(expected = IllegalArgumentException.class)
    public void testSerializableWithNullExtraInterfaces() {
        MockSettings settings = Mockito.spy(new MockSettingsImpl());
        settings.serializable();
        settings.extraInterfaces(null);
    }

    // Testing with a non-serializable interface
    @Test(expected = IllegalArgumentException.class)
    public void testSerializableWithNonInterface() {
        MockSettings settings = Mockito.spy(new MockSettingsImpl());
        settings.serializable();
        settings.extraInterfaces(String.class);
    }
}