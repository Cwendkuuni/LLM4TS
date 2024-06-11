import org.junit.Test;
import org.mockito.Mockito;

public class MockitoCoreTest {

  @Test
  public void testMock() {
    // Given
    Class<String> classToMock = String.class;
    MockSettings mockSettings = new MockSettingsImpl();

    // When
    String mock = MockitoCore.mock(classToMock, mockSettings);

    // Then
    assert mock != null;
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMockWithNullClass() {
    // Given
    Class<String> classToMock = null;
    MockSettings mockSettings = new MockSettingsImpl();

    // When & Then
    MockitoCore.mock(classToMock, mockSettings);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMockWithNullMockSettings() {
    // Given
    Class<String> classToMock = String.class;
    MockSettings mockSettings = null;

    // When & Then
    MockitoCore.mock(classToMock, mockSettings);
  }
}