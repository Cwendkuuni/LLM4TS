import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockSettingsImpl;
import org.mockito.internal.creation.MockSettingsImplExtensions;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;

import static org.junit.Assert.assertNotNull;

public class MockUtilTest {
    private MockUtil mockUtil;
    private MockSettingsImpl settings;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
        settings = new MockSettingsImplExtensions().createSettings();
    }

    @Test
    public void testCreateMock() {
        Class<String> stringClass = String.class;
        Object mock = mockUtil.createMock(stringClass, settings);
        assertNotNull(mock);
    }
}