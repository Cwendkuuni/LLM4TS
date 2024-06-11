import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.CreationValidator;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {
    @Test
    public void createMockTest() {
        MockUtil mockUtil = new MockUtil();

        // Test with valid arguments
        Class<String> stringClass = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        String mock = mockUtil.createMock(stringClass, settings);
        verify(mock, times(1)).mockMethod();

        // Test with null classToMock
        Class<Object> nullClass = null;
        try {
            mockUtil.createMock(nullClass, settings);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        // Test with null settings
        MockSettingsImpl nullSettings = null;
        try {
            mockUtil.createMock(stringClass, nullSettings);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected
        }

        // Test with unsupported mock type
        Class<Object> unsupportedClass = new Object() {};
        try {
            mockUtil.createMock(unsupportedClass, settings);
            fail("Expected UnsupportedMockTypeException");
        } catch (UnsupportedMockTypeException e) {
            // Expected
        }
    }
}