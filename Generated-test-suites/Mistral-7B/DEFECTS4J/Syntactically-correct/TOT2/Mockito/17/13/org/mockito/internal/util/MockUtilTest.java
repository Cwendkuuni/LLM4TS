import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.CreationValidator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class<String> classToMock = String.class;
        CreationValidator creationValidator = mock(CreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);

        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[0]);
        Object spiedInstance = new Object();

        T mock = mockUtil.createMock(classToMock, settings);
        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(classToMock, spiedInstance);

        // Add edge cases and exception handling tests here
    }
}