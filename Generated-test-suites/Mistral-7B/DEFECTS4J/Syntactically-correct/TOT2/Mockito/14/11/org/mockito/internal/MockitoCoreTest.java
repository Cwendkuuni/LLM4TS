import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockSettings;

public class MockitoCoreTest {

    @Test
    public void testMockCreation() {
        Class<String> stringClass = String.class;
        MockSettings mockSettings = new MockSettings();
        String mock = Mockito.mock(stringClass, mockSettings);
        // assert mock is not null
        // assert mock is an instance of String.class
        // test method calls on the mock
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullClassPassedToMock() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.mock(null, new MockSettings());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullMockSettingsPassedToMock() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.mock(String.class, null);
    }
}