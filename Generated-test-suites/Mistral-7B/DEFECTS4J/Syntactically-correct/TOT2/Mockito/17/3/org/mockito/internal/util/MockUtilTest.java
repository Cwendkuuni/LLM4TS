import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<String> classToMock = String.class;
        String mock = new MockUtil().createMock(classToMock, settings);
        verify(mock).toString();
    }
}