import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import java.io.Serializable;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl settings = Mockito.spy(MockSettingsImpl.class);
        settings.serializable();
        Mockito.verify(settings).extraInterfaces(Serializable.class);
    }

    @Test(expected = RuntimeException.class)
    public void testSerializableWithNonSerializableClass() {
        MockSettingsImpl settings = Mockito.spy(MockSettingsImpl.class);
        settings.extraInterfaces(Object.class);
        settings.serializable();
    }

    @Test
    public void testSerializableWithEmptyArray() {
        MockSettingsImpl settings = Mockito.spy(MockSettingsImpl.class);
        settings.serializable((Class<?>[]) null);
        Mockito.verifyNoInteractions(settings);
    }

    @Test
    public void testSerializableReturnsSameInstance() {
        MockSettingsImpl settings = Mockito.spy(MockSettingsImpl.class);
        settings.serializable();
        settings.serializable();
        Mockito.verify(settings).extraInterfaces(Mockito.eq(MockSettingsImpl.class.getClassLoader().getResource("MockName.class").getType()));
    }
}