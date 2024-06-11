import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.MockUtil;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateMock() {
        Class<String> stringClass = String.class;
        MockSettings settings = new MockSettings();
        T mock = mockUtil.createMock(stringClass, settings);
        verify(mock).toString(); // Basic functionality check
    }
}