import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import static org.mockito.Mockito.RETURNS_DEFAULTS;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        CreationValidator creationValidator = new CreationValidator();
        MockUtil mockUtil = new MockUtil(creationValidator);
        Class<MockitoTestClass> classToMock = MockitoTestClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        MockUtil mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
    }
}

class MockitoTestClass {
    //dummy class for testing
}