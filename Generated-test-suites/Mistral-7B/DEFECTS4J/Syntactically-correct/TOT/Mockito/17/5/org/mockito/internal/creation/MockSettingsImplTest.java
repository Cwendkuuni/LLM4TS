// Test for serializable method
import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.MockSettingsImpl;

public class MockSettingsImplTest {

  @Test
  public void testSerializable() {
    MockSettings settings = new MockSettingsImpl().serializable();
    assertTrue(settings.isSerializable());
  }

  @Test(expected = IllegalStateException.class)
  public void testSerializableWithNullExtraInterfaces() {
    MockSettings settings = new MockSettingsImpl();
    settings.serializable(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSerializableWithNonInterfaceClass() {
    MockSettings settings = new MockSettingsImpl();
    settings.serializable(String.class);
  }

  @Test
  public void testSerializableWithMultipleInterfaces() {
    MockSettings settings = new MockSettingsImpl();
    Class<?>[] interfaces = new Class[] { Serializable.class, Runnable.class };
    settings.serializable(interfaces);
    assertTrue(settings.isSerializable());
  }
}

