import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.serializable();

        Class<?>[] expectedInterfaces = new Class[] {java.io.Serializable.class};
        MockSettingsImpl settingsWithSerializable = new MockSettingsImpl().extraInterfaces(expectedInterfaces);
        verify(settingsWithSerializable).extraInterfaces(expectedInterfaces);
    }
}