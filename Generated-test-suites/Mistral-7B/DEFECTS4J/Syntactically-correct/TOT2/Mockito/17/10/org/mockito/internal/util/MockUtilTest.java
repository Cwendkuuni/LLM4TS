import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.creation.MockSettingsImpl;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        Class<String> stringClass = String.class;
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(stringClass, mockSettings);
        verify(mock, times(1)).toString();
    }
}