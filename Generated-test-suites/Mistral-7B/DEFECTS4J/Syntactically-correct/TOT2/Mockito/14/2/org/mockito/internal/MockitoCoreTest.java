import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {
    @Test
    public void testMock() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = Mockito.mock(mockitoCoreClass, mockSettings);
        assertNotNull(mockitoCore);
    }
}