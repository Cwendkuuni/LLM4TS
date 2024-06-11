import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.internal.util.MockUtil;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class<Mockito> mockitoClass = Mockito.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(mockitoClass, settings);
        assertEquals(mockitoClass, mock.getClass());
    }
}