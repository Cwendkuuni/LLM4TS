import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.serializable;
import static org.mockito.Mockito.verify;

public class MockSettingsImplTest {
  @Test
  public void testSerializable() {
    MockSettings mockSettings = Mockito.mock(MockSettings.class);
    MockSettings serializableMockSettings = mockSettings.serializable();
    verify(mockSettings).serializable();
    Class<?>[] expectedSerializableInterfaces = new Class<?>[]{java.io.Serializable.class};
    Class<?>[] actualSerializableInterfaces = serializableMockSettings.getExtraInterfaces();
    assertArrayEquals(expectedSerializableInterfaces, actualSerializableInterfaces);
  }
}