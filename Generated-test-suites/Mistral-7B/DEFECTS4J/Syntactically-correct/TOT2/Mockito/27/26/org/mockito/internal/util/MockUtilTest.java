import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class<String> stringClass = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(stringClass, settings);
        verify(mock).toString(); // Verifying a basic method call on the created mock
    }
}